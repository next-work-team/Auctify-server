package org.example.auctify.service.auction;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.example.auctify.dto.Goods.*;
import org.example.auctify.dto.bid.BidHistoryResponseDTO;
import org.example.auctify.dto.bid.BidSummaryDTO;
import org.example.auctify.entity.Goods.GoodsEntity;
import org.example.auctify.entity.Goods.GoodsImageEntity;
import org.example.auctify.entity.bidHistory.BidHistoryEntity;
import org.example.auctify.entity.like.LikeEntity;
import org.example.auctify.entity.payment.PaymentEntity;
import org.example.auctify.entity.review.ReviewEntity;
import org.example.auctify.entity.user.UserEntity;
import org.example.auctify.repository.bidHistory.BidHistoryRepository;
import org.example.auctify.repository.goods.GoodsImageRepository;
import org.example.auctify.repository.goods.GoodsRepository;
import org.example.auctify.repository.like.LikeRepository;
import org.example.auctify.repository.payment.PaymentRepository;
import org.example.auctify.repository.review.ReviewRepository;
import org.example.auctify.repository.user.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class GoodsService {
    private final GoodsRepository goodsRepository;
    private final UserRepository userRepository;
    private final GoodsImageRepository goodsImageRepository;
    private final BidHistoryRepository bidHistoryRepository;
    private final LikeRepository likeRepository;
    private final ReviewRepository reviewRepository;
    private final PaymentRepository paymentRepository;




    public GoodsResponseDTO searchGoodsId(long goodsId) {
        GoodsEntity goodsEntity = goodsRepository.findById(goodsId).orElseThrow(() ->
                new IllegalArgumentException("Goods not found with ID: " + goodsId));
        return GoodsResponseDTO
                .builder()
                .goodsId(goodsEntity.getGoodsId())
                .goodsName(goodsEntity.getGoodsName())
                .goodsDescription(goodsEntity.getGoodsDescription())
                .buyNowPrice(goodsEntity.getBuyNowPrice())
                .goodsProcessStatus(goodsEntity.getGoodsProcessStatus())
                .goodsStatus(goodsEntity.getGoodsStatus())
                .minimumBidAmount(goodsEntity.getMinimumBidAmount())
                .actionEndTime(goodsEntity.getActionEndTime())
                .userId(goodsEntity.getUser().getUserId())
                .category(goodsEntity.getCategory())
                .currentBidPrice(goodsEntity.getCurrentBidPrice())
                .imageUrls(goodsEntity.getImageUrls())
                .build();
    }


    public GoodsResponseDTO createActionsGoods(Long userId, GoodsRequestDTO goodsRequestDTO) {

        // 1. User 조회
        UserEntity user = userRepository.findById(userId).orElseThrow(() ->
                new IllegalArgumentException("User not found with ID: " + userId));

        // 2. Goods 엔티티 생성 및 저장
        GoodsEntity goodsEntity = GoodsEntity.builder()
                .goodsName(goodsRequestDTO.getGoodsName())
                .goodsDescription(goodsRequestDTO.getGoodsDescription())
                .buyNowPrice(goodsRequestDTO.getBuyNowPrice())
                .goodsStatus(GoodsStatus.valueOf(goodsRequestDTO.getGoodsProcessStatus()))
                .goodsProcessStatus(GoodsProcessStatus.valueOf(goodsRequestDTO.getGoodsStatus()))
                .minimumBidAmount(goodsRequestDTO.getMinimumBidAmount())
                .actionEndTime(goodsRequestDTO.getActionEndTime())
                .user(user)
                .category(goodsRequestDTO.getCategory())
                .build();

        goodsEntity = goodsRepository.save(goodsEntity); // 상품을 먼저 저장해 ID를 얻어야 함

        // 3. 이미지 처리 및 저장 (Presigned URL 사용하므로 URL리스트만 넘겨받은 상태.)
        List<GoodsImageEntity> goodsImages = new ArrayList<>();

        if (goodsRequestDTO.getImage() != null && !goodsRequestDTO.getImage().isEmpty()) {
            for (String imageUrl : goodsRequestDTO.getImage()) {
                GoodsImageEntity imageEntity = GoodsImageEntity.builder()
                        .goods(goodsEntity)
                        .imageSrc(imageUrl)
                        .build();
                goodsImages.add(imageEntity);
            }
            goodsImageRepository.saveAll(goodsImages);
        }

        // Goods 엔티티에 이미지 설정
        goodsEntity.getImage().addAll(goodsImages);

        // 4. 응답 DTO 생성
        return GoodsResponseDTO
                .builder()
                .goodsId(goodsEntity.getGoodsId())
                .goodsName(goodsEntity.getGoodsName())
                .goodsDescription(goodsEntity.getGoodsDescription())
                .buyNowPrice(goodsEntity.getBuyNowPrice())
                .goodsProcessStatus(goodsEntity.getGoodsProcessStatus())
                .goodsStatus(goodsEntity.getGoodsStatus())
                .minimumBidAmount(goodsEntity.getMinimumBidAmount())
                .actionEndTime(goodsEntity.getActionEndTime())
                .userId(goodsEntity.getUser().getUserId())
                .category(goodsEntity.getCategory())
                .currentBidPrice(goodsEntity.getCurrentBidPrice())
                .imageUrls(goodsEntity.getImageUrls())
                .build();
    }

    public BidHistoryResponseDTO bidAuctionGoods(Long userId, BidRequestDTO bidRequestDTO) {

        // 1. User 조회
        UserEntity user = userRepository.findById(userId).orElseThrow(() ->
                new IllegalArgumentException("User not found with ID: " + userId));

        // 2. 입찰한 Goods 조회
        GoodsEntity goods = goodsRepository.findGoodsImageBidHistoryByGoodsId(bidRequestDTO.getGoodsId()).orElseThrow(() ->
                new IllegalArgumentException("Goods not found with ID: " + bidRequestDTO.getGoodsId()));

        // 만약에
        BidHistoryEntity bidHistory = BidHistoryEntity.builder()
                .bidStatus(false)
                .cancelFlag(false)
                .bidPrice(bidRequestDTO.getBidPrice())
                .user(user)
                .goods(goods)
                .payment(null) // 결제는 나중에 하게된다
                .build();
        bidHistory = bidHistoryRepository.save(bidHistory); // 먼저 저장
        user.getBidHistories().add(bidHistory);
        goods.getBidHistories().add(bidHistory);


        return BidHistoryResponseDTO.builder()
                .bidHistoryId(bidHistory.getBidHistoryId())
                .goodsId(goods.getGoodsId())
                .goodsName(goods.getGoodsName())
                .cancelFlag(bidHistory.getCancelFlag())
                .goodsProcessStatus(goods.getGoodsProcessStatus())
                .imageUrls(goods.getFirstImage())
                .bidPrice(bidHistory.getBidPrice())
                .bidMaxPrice(goods.getMaxBidPrice())
                .build();
    }

    //입찰에 대한 취소 기록은 유지
    public BidHistoryResponseDTO cancelBidAuctions(Long userId, Long bidHistoryId) {
        // 1. User 조회
        UserEntity user = userRepository.findById(userId).orElseThrow(() ->
                new IllegalArgumentException("User not found with ID: " + userId));

        // 2. 입찰내역 조회
        BidHistoryEntity bidHistory = bidHistoryRepository.findById(bidHistoryId).orElseThrow(() ->
                new IllegalArgumentException("Bid not found with ID: " + bidHistoryId));

        // 3. Goods 조회
        GoodsEntity goods = goodsRepository.findGoodsImageBidHistoryByGoodsId(bidHistory.getGoods().getGoodsId()).orElseThrow(() ->
                new IllegalArgumentException("Goods not found with ID: " + bidHistory.getGoods().getGoodsId()));

        // 3. cancel상태로 바꿈
        bidHistory.onChangeCancelFlag(true);

        return BidHistoryResponseDTO.builder()
                .bidHistoryId(bidHistory.getBidHistoryId())
                .goodsId(goods.getGoodsId())
                .goodsName(goods.getGoodsName())
                .cancelFlag(bidHistory.getCancelFlag())
                .goodsProcessStatus(goods.getGoodsProcessStatus())
                .imageUrls(goods.getFirstImage())
                .bidPrice(bidHistory.getBidPrice())
                .bidMaxPrice(goods.getMaxBidPrice())
                .build();
    }

    public Page<GoodsResponseSummaryDTO> searchGoods(
            String category,
            Double priceRangeLow,
            Double priceRangeHigh,
            String goodsStatus,
            String goodsProcessStatus,
            String goodsName,
            String sort,
            Pageable pageable) {

        Page<GoodsEntity> goodsEntityPage = goodsRepository.searchGoods(category, priceRangeLow, priceRangeHigh, goodsStatus, goodsProcessStatus, goodsName, sort, pageable);


        return goodsEntityPage.map(goodsEntity -> {

            // 가장 높은 현재 입찰가
            Long highestBidPrice = goodsEntity.getMaxBidPrice();

            return GoodsResponseSummaryDTO.builder()
                    .goodsId(goodsEntity.getGoodsId())
                    .goodsName(goodsEntity.getGoodsName())
                    .goodsProcessStatus(goodsEntity.getGoodsProcessStatus())
                    .currentBidPrice(highestBidPrice)
                    .imageUrls(goodsEntity.getFirstImage())
                    .endTime(goodsEntity.getActionEndTime())
                    .goodsStatus(goodsEntity.getGoodsStatus())
                    .category(goodsEntity.getCategory())
                    .currentBidCount((long) goodsEntity.getBidHistories().size())
                    .build();
        });
    }

    public List<BidSummaryDTO> getBidHistorySummary(Long goodsId, Long size) {
        Pageable pageable = PageRequest.of(0, size.intValue()); // size만큼 limit
        return goodsRepository.findTopBidHistoryByGoodsId(goodsId, pageable);
    }


    public ReviewDetailResponseDTO createReview(Long userId, ReviewRequestDTO reviewRequestDTO) {

        // 1. User 조회
        UserEntity writer = userRepository.findById(userId).orElseThrow(() ->
                new IllegalArgumentException("User not found with ID: " + userId));

        UserEntity receiver = userRepository.findById(reviewRequestDTO.getReceiver()).orElseThrow(() ->
                new IllegalArgumentException("User not found with ID: " + reviewRequestDTO.getReceiver()));

        PaymentEntity payment = paymentRepository.findById(reviewRequestDTO.getPaymentId()).orElseThrow(() ->
                new IllegalArgumentException("Payment not found with ID:" + reviewRequestDTO.getPaymentId()));

        //2. 해당 상품에 작성자로 리뷰를 작성한 유저가 있는지 체크한다.
        // 리뷰는 1번 작성 가능하기 때문
        boolean flag  = reviewRepository.findByWriterUser_UserIdAndGoods_GoodsId(userId, reviewRequestDTO.getGoodsId()).isPresent();
        if (!flag) {
            throw new RuntimeException("좋아요 엔티티가 없어야 합니다. 이미 좋아요 존재");
        }

        //3. Goods 조회
        GoodsEntity goods = goodsRepository.findById(reviewRequestDTO.getGoodsId()).orElseThrow(() ->
                new IllegalArgumentException("Goods not found with ID: " + reviewRequestDTO.getGoodsId()));

        //3. 리뷰 생성
        ReviewEntity.builder()
                .content(reviewRequestDTO.getContent())
                .temperature(reviewRequestDTO.getTemperature())
                .receiverUser(receiver)
                .writerUser(writer)
                .payment(payment)
                .goods(goods)
                .build();

        return null;
    }

    public void createLike(Long userId, Long goodsId) {

        // 1. User 조회
        UserEntity user = userRepository.findById(userId).orElseThrow(() ->
                new IllegalArgumentException("User not found with ID: " + userId));

        // 2. Goods 조회
        GoodsEntity goods = goodsRepository.findById(goodsId).orElseThrow(() ->
                new IllegalArgumentException("Goods not found with ID: " + goodsId));

        // 이미 좋아요 있으면 에러.
        boolean flag = likeRepository.findByUser_UserIdAndGoods_GoodsId(userId, goodsId).isPresent();
        if (!flag) {
            throw new RuntimeException("좋아요 엔티티가 없어야 합니다. 이미 좋아요 존재");
        }

        LikeEntity.builder()
                .goods(goods)
                .user(user)
                .build();
        return;
    }

    public void cancelLike(Long userId, Long goodsId) {
        LikeEntity like = likeRepository.findByUser_UserIdAndGoods_GoodsId(userId, goodsId).orElseThrow(() ->
                new IllegalArgumentException("잘못된 요청 좋아요 내역이 없는데 좋아요 취소 요청을 함. \n Like Entity Empty UserId: " + userId + "  goodsId : " + goodsId));
        likeRepository.delete(like);
        return;
    }





}
