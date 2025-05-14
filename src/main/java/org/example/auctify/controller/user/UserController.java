package org.example.auctify.controller.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.example.auctify.dto.Goods.GoodsResponseSummaryDTO;
import org.example.auctify.dto.Goods.ReviewDetailResponseDTO;
import org.example.auctify.dto.bid.BidHistoryResponseDTO;
import org.example.auctify.dto.duplication.DuplicationDTO;
import org.example.auctify.dto.like.LikeGoodsResponseDTO;
import org.example.auctify.dto.response.ApiResponseDTO;
import org.example.auctify.dto.social.CustomOauth2User;
import org.example.auctify.dto.user.*;
import org.example.auctify.repository.user.UserRepository;
import org.example.auctify.service.user.UserService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.regex.Pattern;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user") //유저 컨트럴러
@Log4j2
@Validated
public class UserController implements UserControllerDocs{

    private final UserService userService;

    private final UserRepository userRepository;
    @Override
    @GetMapping("")
    public ResponseEntity<ApiResponseDTO<UserInfoResponseDTO>> getMyProfile(CustomOauth2User userDetails) {
        try {
            log.error("[LOG] 마이프로필: {}");

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
    @PutMapping("")
    public ResponseEntity<ApiResponseDTO<UserChangedProfileResponseDTO>> changeProfile(
            UserInfoRequestDTO userInfoDTO,
            BindingResult bindingResult,
            CustomOauth2User userDetails) {
        try {

            Long id =  userDetails.getUserId();

            long countByNickname = userRepository.countByNickNameExcludingUserId( userInfoDTO.getNickname(), id);
            long countByEmail= userRepository.countByEmailExcludingUserId(userInfoDTO.getEmail(), id);

            if (countByNickname > 0) {
                throw new Exception("닉네임이 중복됩니다.");
            }

            if (countByEmail > 0) {
                throw new Exception("이메일이 중복됩니다.");
            }


            UserChangedProfileResponseDTO userChangedProfileResponseDTO
                    = userService.changeProfile(id, userInfoDTO);
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
            String category,
            CustomOauth2User userDetails) {
        try {
            System.out.println(category);
            Long userId = userDetails.getUserId();
            Page<BidHistoryResponseDTO> myBidList = userService.getMyBidHistory(userId, category, page, size);
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
    @GetMapping("/check-email")
    public ResponseEntity<ApiResponseDTO<DuplicationDTO>> checkEmail(String email) {

        String emailRegexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

        // 이메일 형식 검사
        if (!Pattern.matches(emailRegexp, email)) {
            DuplicationDTO duplicationDTO = new DuplicationDTO("올바른 이메일 형식이 아닙니다.", false);
            return ResponseEntity
                    .ok((ApiResponseDTO.success(duplicationDTO)));
        }

        long countByEmail = userRepository.countByEmail(email);
        DuplicationDTO duplicationDTO;

        if (countByEmail == 0) {
            duplicationDTO = new DuplicationDTO("중복된 이메일이 없습니다.", true);
        } else {
            duplicationDTO = new DuplicationDTO("이메일이 이미 존재합니다.", false);
        }

        return ResponseEntity.ok(ApiResponseDTO.success(duplicationDTO));
    }

    

    @Override
    @GetMapping("/check-nickname")
    public ResponseEntity<ApiResponseDTO<DuplicationDTO>> checkNickname(String nickname) {

        String regexp = "^[a-zA-Z가-힣0-9]{2,20}$";

        // 정규표현식 일치 여부 체크
        if (!Pattern.matches(regexp, nickname)) {
            DuplicationDTO duplicationDTO = new DuplicationDTO("닉네임 형식이 올바르지 않습니다. (영문, 한글, 숫자 2~20자)", false);
            return ResponseEntity
                    .ok((ApiResponseDTO.success(duplicationDTO)));
        }

        long countByNickName = userRepository.countByNickName(nickname);

        DuplicationDTO duplicationDTO;

        if (countByNickName == 0) {
            duplicationDTO = new DuplicationDTO("중복된 닉네임이 없습니다.", true);
        } else {
            duplicationDTO = new DuplicationDTO("닉네임이 중복됩니다.", false);
        }

        return ResponseEntity.ok(ApiResponseDTO.success(duplicationDTO));
    }

    @Override
    @PutMapping("address")
    public ResponseEntity<ApiResponseDTO<AddressDTO>> changeDefaultAddress(AddressDTO defaultAddressDTO, CustomOauth2User userDetails) {

        try{
            Long userId = userDetails.getUserId();

            AddressDTO addressDTO = userService.changDefaultAddress(userId, defaultAddressDTO);

            return ResponseEntity.ok(ApiResponseDTO.success(addressDTO));
        }catch (Exception e){
            log.error("[LOG]: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponseDTO.error(400,e.getMessage()+"/ Create Address Error"));
        }
    }


}
