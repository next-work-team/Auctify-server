package org.example.auctify.controller.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.example.auctify.dto.Goods.AuctionResponseGoodsDTO;
import org.example.auctify.dto.Goods.GoodsResponseSummaryDTO;
import org.example.auctify.dto.Goods.ReviewDetailResponseDTO;
import org.example.auctify.dto.Goods.GoodsResponseDTO;
import org.example.auctify.dto.bid.BidHistoryResponseDTO;
import org.example.auctify.dto.like.LikeGoodsResponseDTO;
import org.example.auctify.dto.response.ApiResponseDTO;
import org.example.auctify.dto.social.CustomOauth2User;
import org.example.auctify.dto.user.*;
import org.example.auctify.service.user.UserService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user") //유저 컨트럴러
@Log4j2
public class UserController implements UserControllerDocs{

    private final UserService userService;
    @Override
    @GetMapping("/")
    public ResponseEntity<ApiResponseDTO<UserInfoResponseDTO>> getMyProfile(CustomOauth2User userDetails) {
        try {
            UserInfoResponseDTO  userProfile = userService.getProfile(userDetails.getUserId());
            return ResponseEntity.ok(ApiResponseDTO.success(userProfile));
        } catch (Exception e) {
            log.error("[LOG] Internal server error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponseDTO.error(400, "Get My Profile"));
        }
    }

    @Override
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponseDTO<UserInfoResponseDTO>> getProfile(
            @PathVariable(required = true) long userId) {
        try {
            UserInfoResponseDTO userProfile = userService.getProfile(userId);
            return ResponseEntity.ok(ApiResponseDTO.success(userProfile));
        } catch (Exception e) {
            log.error("[LOG]: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponseDTO.error(400, "Get profile"));
        }
    }


    @Override
    @PutMapping("/")
    public ResponseEntity<ApiResponseDTO<UserChangedProfileResponseDTO>> changeProfile(
            UserInfoRequestDTO userInfoDTO,
            BindingResult bindingResult,
            CustomOauth2User userDetails) {

        try {
            UserChangedProfileResponseDTO userChangedProfileResponseDTO
                    = userService.changeProfile(userDetails.getUserId(), userInfoDTO);
            return ResponseEntity.ok(ApiResponseDTO.success(userChangedProfileResponseDTO));
        }catch (Exception e){
            log.error("[LOG]: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponseDTO.error(400,"Change Profile Error"));
        }
    }

    @Override
    @GetMapping("/TotalFeedback")
    public ResponseEntity<ApiResponseDTO<Page<ReviewDetailResponseDTO>>> getListTotalFeedback(
            int page,
            int size,
            CustomOauth2User userDetails){
        try {
            Long userId = userDetails.getUserId();
            Page<ReviewDetailResponseDTO> reviewDetailList = userService.getReviewDetailList(userId, page, size);
            return ResponseEntity.ok(ApiResponseDTO.success(reviewDetailList));
        } catch (Exception e) {
            log.error("[LOG]: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponseDTO.error(400,e.getMessage()+"/ List Total Feedback Error"));
        }
    }

//    @Override
//    @GetMapping("/MannerTemperature")
//    public ResponseEntity<ApiResponseDTO<Page<MannerTemperatureDTO>>> getMannerTemperature(
//            int page,
//            int size,
//            CustomOauth2User userDetails
//    ) {
//        try {
//            return null;
//        } catch (Exception e) {
//            return null;
//        }
//    }

    @Override
    @GetMapping("/myGoods")
    public ResponseEntity<ApiResponseDTO<Page<GoodsResponseSummaryDTO>>> getMyGoods(
            int page,
            int size,
            CustomOauth2User userDetails) {
        try {
            Long userId = userDetails.getUserId();
            Page<GoodsResponseSummaryDTO> myGoodsList = userService.getMyGoodsList(userId, page, size);
            return ResponseEntity.ok(ApiResponseDTO.success(myGoodsList));
        } catch (Exception e) {
            log.error("[LOG]: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponseDTO.error(400,e.getMessage()+"/ List My Auction Error"));
        }
    }

    @Override
    @GetMapping("/myBid")
    public ResponseEntity<ApiResponseDTO<Page<BidHistoryResponseDTO>>> getMyAuctifyGoods(
            int page,
            int size,
            CustomOauth2User userDetails) {
        try {
            Long userId = userDetails.getUserId();
            Page<BidHistoryResponseDTO> myBidList = userService.getMyBidHistory(userId, page, size);
            return  ResponseEntity.ok(ApiResponseDTO.success(myBidList));
        }catch(Exception e) {
            log.error("[LOG]: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponseDTO.error(400,e.getMessage()+"/ List My Bid Error"));
        }
    }

    @Override
    @GetMapping("/myLikeGoods")
    public ResponseEntity<ApiResponseDTO<Page<LikeGoodsResponseDTO>>> getMyLikeGoods(
            int page,
            int size,
            CustomOauth2User userDetails) {
        try {
            Long userId = userDetails.getUserId();
            Page<LikeGoodsResponseDTO> myLikedGoodsList = userService.getLikeGoods(userId, page, size);
            return  ResponseEntity.ok(ApiResponseDTO.success(myLikedGoodsList));
        }catch(Exception e) {
            log.error("[LOG]: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponseDTO.error(400,e.getMessage()+"/ List My Like Goods Error"));
        }
    }

    @Override
    @GetMapping("/address")
    public ResponseEntity<ApiResponseDTO<List<AddressDTO>>> getListAddress(CustomOauth2User userDetails) {
        try{
            Long userId = userDetails.getUserId();
            List<AddressDTO> addressList= userService.getAddressList(userId);

            return ResponseEntity.ok(ApiResponseDTO.success(addressList));
        }catch (Exception e){
            log.error("[LOG]: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponseDTO.error(400,e.getMessage()+"/ List Address Error"));
        }
    }

    @Override
    @PostMapping("/address")
    public ResponseEntity<ApiResponseDTO<AddressDTO>> postAddress(
            AddressRequestDTO addressRequestDTO,
            BindingResult bindingResult,
            CustomOauth2User userDetails){
        try{
            Long userId = userDetails.getUserId();

            AddressDTO addressDTO = userService.addAddress(userId,addressRequestDTO);

            return ResponseEntity.ok(ApiResponseDTO.success(addressDTO));
        }catch (Exception e){
            log.error("[LOG]: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponseDTO.error(400,e.getMessage()+"/ Create Address Error"));
        }
    }

    @Override
    @GetMapping(value = "/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<?> subscribe(CustomOauth2User userDetails, String lastEventId) {
        return null;
    }
}
