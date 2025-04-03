package org.example.auctify.service.payment;

import io.swagger.v3.oas.annotations.servers.Server;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.example.auctify.dto.tossPayments.PayRequestDTO;
import org.example.auctify.dto.tossPayments.PaymentResponseDTO;
import org.example.auctify.dto.user.AddressDTO;
import org.example.auctify.entity.Goods.GoodsEntity;
import org.example.auctify.entity.bidHistory.BidHistoryEntity;
import org.example.auctify.entity.payment.PaymentEntity;
import org.example.auctify.entity.user.AddressEntity;
import org.example.auctify.entity.user.UserEntity;
import org.example.auctify.repository.bidHistory.BidHistoryRepository;
import org.example.auctify.repository.goods.GoodsRepository;
import org.example.auctify.repository.payment.PaymentRepository;
import org.example.auctify.repository.user.AddressRepository;
import org.example.auctify.repository.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class PaymentService {
    private GoodsRepository goodsRepository;
    private UserRepository userRepository;
    private BidHistoryRepository bidHistoryRepository;
    private PaymentRepository paymentRepository;
    private AddressRepository addressRepository;


    public PaymentResponseDTO saveConfirmedPayment(PayRequestDTO payRequestDTO){

        // 1. 요청시 넘어온 데이터 기반으로 Entity 를 조회
        GoodsEntity goodsEntity = goodsRepository.findById(payRequestDTO.getGoodsId()).orElseThrow(() ->
                new IllegalArgumentException("Goods not found with ID: " + payRequestDTO.getGoodsId()));

        UserEntity user = userRepository.findById(payRequestDTO.getUserId()).orElseThrow(() ->
                new IllegalArgumentException("User not found with ID: " + payRequestDTO.getUserId()));

        BidHistoryEntity bid = bidHistoryRepository.findById(payRequestDTO.getBidId()).orElseThrow(() ->
                new IllegalArgumentException("Bid not found with ID: " + payRequestDTO.getBidId()));

        AddressEntity address = addressRepository.findById(payRequestDTO.getAddressId()).orElseThrow(() ->
                new IllegalArgumentException("Address not found with ID: " + payRequestDTO.getAddressId()));

        // 2. Payment 엔티티 생성 및 저장
        PaymentEntity payment = PaymentEntity.builder()
                .bidHistory(bid)
                .type(payRequestDTO.getType())
                .user(user)
                .address(address)
                .amount(payRequestDTO.getAmount())
                .build();

        payment = paymentRepository.save(payment); // 상품을 먼저 저장해 ID를 얻어야 함

        return PaymentResponseDTO.builder()
                .paymentId(payment.getPaymentId())
                .goodsCategory(goodsEntity.getCategory())
                .type(payment.getType())
                .goodsName(goodsEntity.getGoodsName())
                .goodsStatus(goodsEntity.getGoodsStatus())
                .goodsImage(goodsEntity.getFirstImage())
                .amount(payment.getAmount())
                .goodsUserId(payment.getUser().getUserId())
                .addressDTO(AddressDTO.changeDTO(payment.getAddress()))
                .build();

    }
}
