package org.example.auctify.controller.user;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.example.auctify.config.security.CustomUserDetails;
import org.example.auctify.dto.Goods.AuctionResponseGoodsDTO;
import org.example.auctify.dto.Goods.FeedbackDTO;
import org.example.auctify.dto.Goods.GoodsResponseDTO;
import org.example.auctify.dto.social.CustomOauth2User;
import org.example.auctify.dto.user.MannerTemperatureDTO;
import org.example.auctify.dto.user.UserInfoRequestDTO;
import org.example.auctify.dto.user.UserInfoResponseDTO;
import org.example.auctify.service.user.UserService;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user") //유저 컨트럴러
@Log4j2
public class UserController implements UserControllerDocs{

    private final UserService userService;
    @Override
    @GetMapping("/")
    public ResponseEntity<UserInfoResponseDTO> getMyProfile(CustomOauth2User userDetails) {
        try {
            userDetails.getName();
            log.info("[LOG] " + userDetails.getName());
            log.info("[LOG] " + userDetails.getOauthId());
            UserInfoResponseDTO  userProfile = userService.getProfile(userDetails.getUserId());
            return ResponseEntity.ok(userProfile);
        } catch (Exception e) {
            log.error("[LOG]"+ e.getMessage()) ;
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    @GetMapping("/{userId}")
    public ResponseEntity<UserInfoResponseDTO> getProfile(
            @PathVariable long userId) {
        return ResponseEntity.ok(null);
    }



    @Override
    @PutMapping("/")
    public ResponseEntity<UserInfoResponseDTO> changeProfile(
            UserInfoRequestDTO userInfoDTO,
            BindingResult bindingResult,
            CustomOauth2User userDetails) {
        return null;
    }

    @Override
    @GetMapping("/TotalFeedback")
    public ResponseEntity<Page<FeedbackDTO>> getListTotalFeedback(
            int page,
            int size,
            CustomOauth2User userDetails){
        return null;
    }

    @Override
    @GetMapping("/MannerTemperature")
    public ResponseEntity<Page<MannerTemperatureDTO>>getMannerTemperature(
            int page,
            int size,
            CustomOauth2User userDetails
    ) {
        return null;
    }

    @Override
    @GetMapping("/myBid")
    public ResponseEntity<Page<GoodsResponseDTO>> getMyBid(
            int page,
            int size,
            CustomOauth2User userDetails) {
        return null;
    }

    @Override
    @GetMapping("/myAuctify")
    public ResponseEntity<Page<AuctionResponseGoodsDTO>> getAuctifyInfo(
            int page,
            int size,
            CustomOauth2User userDetails) {
        return null;
    }

    @Override
    @GetMapping("/myLiked")
    public ResponseEntity<Page<GoodsResponseDTO>> getInfiniteScrollPosts(
            int page,
            int size,
            CustomOauth2User userDetails) {
        return null;
    }

    @Override
    @GetMapping("/address")
    public ResponseEntity<Page<FeedbackDTO>> getListAddress(CustomOauth2User userDetails) {
        return null;
    }

    @Override
    @GetMapping(value = "/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<?> subscribe(CustomOauth2User userDetails, String lastEventId) {
        return null;
    }
}
