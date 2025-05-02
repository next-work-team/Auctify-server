package org.example.auctify.service.user;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.example.auctify.dto.Goods.GoodsResponseSummaryDTO;
import org.example.auctify.dto.Goods.ReviewDetailResponseDTO;
import org.example.auctify.dto.bid.BidHistoryResponseDTO;
import org.example.auctify.dto.like.LikeGoodsResponseDTO;
import org.example.auctify.dto.user.*;
import org.example.auctify.entity.Goods.GoodsEntity;
import org.example.auctify.entity.Goods.GoodsImageEntity;
import org.example.auctify.entity.bidHistory.BidHistoryEntity;
import org.example.auctify.entity.like.LikeEntity;
import org.example.auctify.entity.review.ReviewEntity;
import org.example.auctify.entity.user.AddressEntity;
import org.example.auctify.entity.user.UserEntity;
import org.example.auctify.repository.bidHistory.BidHistoryRepository;
import org.example.auctify.repository.goods.GoodsRepository;
import org.example.auctify.repository.like.LikeRepository;
import org.example.auctify.repository.review.ReviewRepository;
import org.example.auctify.repository.user.AddressRepository;
import org.example.auctify.repository.user.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class UserService {

    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final GoodsRepository goodsRepository;
    private final BidHistoryRepository bidHistoryRepository;
    private final LikeRepository likeRepository;
    private final AddressRepository addressRepository;


    public UserInfoResponseDTO getProfile(Long userId) {
        // 그냥 UserEntity를 가져와서 ReviewEntity에 접근하면 N +1 문제가 발생한다 이를 Fetch조인으로 예방함
            UserEntity user = userRepository.findByIdWithReceivedReviews(userId)
                    .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));
        List<ReviewEntity> reviewList = user.getReceivedReviews();
        // 유저의 매너온도 기본값 0.0

        // 평균 온도를 가져오는 메서드
        Double temperature = user.getAverageTemperature();


        return UserInfoResponseDTO.builder()
                .userId(user.getUserId())
                .nickName(user.getNickName())
                .profileImage(user.getImage())
                .birthdate(user.getBirthday() != null ? user.getBirthday().toString() : null)
                .mannerTemperature(temperature)
                .build();
    }

    public UserChangedProfileResponseDTO changeProfile(Long userId, UserInfoRequestDTO userInfoRequestDTO) {

        UserEntity user = userRepository.findByIdWithAddresses(userId).orElseThrow(()
                -> new EntityNotFoundException("User not found with ID: " + userId));

        user.onChangeProfileImage(userInfoRequestDTO.getProfileImage());
        user.onChangeNickname(userInfoRequestDTO.getNickname());
        user.onChangeBirthday(userInfoRequestDTO.getBirthdate());
        user.onChangeEmail(userInfoRequestDTO.getEmail());


        System.out.println(userInfoRequestDTO.getProfileImage());
        System.out.println(userInfoRequestDTO.getNickname());
        System.out.println(userInfoRequestDTO.getBirthdate());
        System.out.println(userInfoRequestDTO.getEmail());


        AddressDTO addressDTO = userInfoRequestDTO.getAddressDTO();

        if(addressDTO != null && addressDTO.getAddressId() != null && addressDTO.getAddressId() != 0)
            user.onChangeDefaultAddress(userInfoRequestDTO.getAddressDTO().getAddressId());

        log.info("트랜잭션 활성 여부: {}", TransactionSynchronizationManager.isActualTransactionActive());


        return UserChangedProfileResponseDTO.builder()
                .userId(user.getUserId())
                .nickName(user.getNickName())
                .profileImage(user.getImage())
                .email(user.getEmail())
                .birthdate(user.getBirthday() != null ? user.getBirthday().toString() : null)
                .addressDTO(addressDTO != null && addressDTO.getAddressId() != null && addressDTO.getAddressId() != 0 ?
                        AddressDTO.changeDTO(user.getDefaultAddress()) : null)
                .build();
    }

    public Page<ReviewDetailResponseDTO> getReviewDetailList(Long userId, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);
        UserEntity user = userRepository.findById(userId).orElseThrow(() ->
                new IllegalArgumentException("User not found with ID: " + userId)
        );

        Page<ReviewEntity> reviewEntityPage = reviewRepository.findReviewsByReceiverUser(user, pageable);

        return reviewEntityPage.map(reviewEntity -> {

            return ReviewDetailResponseDTO.builder()
                    .userId(reviewEntity.getReceiverUser().getUserId())
                    .registerNickname(reviewEntity.getGoods().getUser().getNickName())
                    .bidderNickName(reviewEntity.getPayment().getUser().getNickName())
                    .registerId(reviewEntity.getGoods().getUser().getUserId())
                    .bidderId(reviewEntity.getPayment().getUser().getUserId())
                    .content(reviewEntity.getContent())
                    .goodsId(reviewEntity.getGoods().getGoodsId())
                    .goodsImage(reviewEntity.getGoods().getFirstImage())
                    .temperature(reviewEntity.getTemperature())
                    .build();
        });
    }

    public Page<GoodsResponseSummaryDTO> getMyGoodsList(Long userId, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        UserEntity user = userRepository.findById(userId).orElseThrow(() ->
                new IllegalArgumentException("User not found with ID: " + userId)
        );

        Page<GoodsEntity> goodsEntityPage = goodsRepository.findGoodsByUserId(user.getUserId(),pageable);

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

    //자신의 입찰 내역 리스트를 보내줌
    public Page<BidHistoryResponseDTO> getMyBidHistory(Long userId, String category, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        UserEntity user = userRepository.findById(userId).orElseThrow(() ->
                new IllegalArgumentException("User not found with ID: " + userId)
        );

        Page<BidHistoryEntity> bidHistoryEntityPage =
                bidHistoryRepository.searchMyBid(user.getUserId(), category, pageable);

        return bidHistoryEntityPage.map(bidHistoryEntity -> {
            return BidHistoryResponseDTO.builder()
                    .bidHistoryId(bidHistoryEntity.getBidHistoryId())
                    .goodsId(bidHistoryEntity.getGoods().getGoodsId())
                    .goodsName(bidHistoryEntity.getGoods().getGoodsName())
                    .cancelFlag(bidHistoryEntity.getCancelFlag())
                    .goodsProcessStatus(bidHistoryEntity.getGoods().getGoodsProcessStatus())
                    .imageUrls(bidHistoryEntity.getGoods().getFirstImage())
                    .bidPrice(bidHistoryEntity.getBidPrice())
                    .bidMaxPrice(bidHistoryEntity.getGoods().getMaxBidPrice())
                    .build();
        });
    }

    //자신이 좋아요한 상품의 리스트를 보여줌
    public Page<LikeGoodsResponseDTO> getLikeGoods(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        UserEntity user = userRepository.findById(userId).orElseThrow(() ->
                new IllegalArgumentException("User not found with ID: " + userId)
        );

        Page<LikeEntity> likeEntityPage =
                likeRepository.findLikeGoodsByUserId(user.getUserId(), pageable);

        return likeEntityPage.map(likeEntity -> {

            return LikeGoodsResponseDTO.builder()
                    .likeId(likeEntity.getLikeId())
                    .goodsId(likeEntity.getGoods().getGoodsId())
                    .goodsName(likeEntity.getGoods().getGoodsName())
                    .imageUrls(likeEntity.getGoods().getFirstImage())
                    .endTime(likeEntity.getGoods().getActionEndTime())
                    .goodsProcessStatus(likeEntity.getGoods().getGoodsProcessStatus())
                    .goodsStatus(likeEntity.getGoods().getGoodsStatus())
                    .category(likeEntity.getGoods().getCategory())
                    .currentBidCount((long) likeEntity.getGoods().getBidHistories().size())
                    .build();
        });

    }

    public List<AddressDTO> getAddressList(Long userId) {
        UserEntity user = userRepository.findById(userId).orElseThrow(() ->
                new IllegalArgumentException("User not found with ID: " + userId)
        );
        return addressRepository.findByUser_UserId(user.getUserId())
                .stream()  //  리스트를 스트림으로 변환
                .map(AddressDTO::changeDTO)  //  AddressDTO로 변환
                .toList();
    }
    public AddressDTO addAddress(Long userId, AddressRequestDTO addressRequestDTO) {

        // 1️⃣ 유저 검증
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        // 3️⃣ AddressEntity 생성 및 저장
        AddressEntity newAddress = AddressEntity.builder()
                .addr(addressRequestDTO.getAddr())
                .addrDetail(addressRequestDTO.getAddrDetail())
                .zipCode(addressRequestDTO.getZipCode())
                .defaultAddress(false)
                .user(user)  // 🔹 연관관계 설정
                .build();

        AddressEntity address = addressRepository.save(newAddress);

        // 4️⃣ DTO 변환 후 반환
        return AddressDTO.changeDTO(address);
    }


    public AddressDTO changDefaultAddress(Long userId, AddressDTO defaultAddressDTO) {
        UserEntity user = userRepository.findByIdWithAddresses(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
        List<AddressEntity> addressList = user.getAddress();
        Optional<AddressEntity> existingAddress = addressList.stream()
                .filter(address -> address.getAddressId().equals(defaultAddressDTO.getAddressId()))
                .findAny();
        if (existingAddress.isPresent()) {
            // 이미 주소가 존재할 때 처리 정상적으로 처리
            user.onChangeDefaultAddress(defaultAddressDTO.getAddressId());
            return defaultAddressDTO;
        } else {
            // 주소가 존재하지 않을 때 처리 에러 발생
            throw new RuntimeException("올바르지 않은 주소입니다.");
        }
    }

}
