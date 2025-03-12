package org.example.auctify.controller.user;

import org.example.auctify.dto.Goods.AuctionResponseGoodsDTO;
import org.example.auctify.dto.Goods.FeedbackDTO;
import org.example.auctify.dto.Goods.GoodsResponseDTO;
import org.example.auctify.dto.user.MannerTemperatureDTO;
import org.example.auctify.dto.user.UserInfoRequestDTO;
import org.example.auctify.dto.user.UserInfoResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user") //유저 컨트럴러
public class UserController implements UserControllerDocs{
    @Override
    @GetMapping("/")
    public ResponseEntity<UserInfoResponseDTO> getMyProfile(UserDetails userDetails) {
        return null;
    }

    @Override
    @GetMapping("/{userId}")
    public ResponseEntity<UserInfoResponseDTO> getProfile(
            @PathVariable long userId) {
        return null;
    }



    @Override
    @PutMapping("/")
    public ResponseEntity<UserInfoResponseDTO> changeProfile(
            UserInfoRequestDTO userInfoDTO,
            BindingResult bindingResult,
            UserDetails userDetails) {
        return null;
    }

    @Override
    @GetMapping("/TotalFeedback")
    public ResponseEntity<Page<FeedbackDTO>> getListTotalFeedback(
            int page,
            int size,
            UserDetails userDetails){
        return null;
    }

    @Override
    @GetMapping("/MannerTemperature")
    public ResponseEntity<Page<MannerTemperatureDTO>>getMannerTemperature(
            int page,
            int size,
            UserDetails userDetails
    ) {
        return null;
    }

    @Override
    @GetMapping("/myBid")
    public ResponseEntity<Page<GoodsResponseDTO>> getMyBid(
            int page,
            int size,
            UserDetails userDetails) {
        return null;
    }

    @Override
    @GetMapping("/myAuctify")
    public ResponseEntity<Page<AuctionResponseGoodsDTO>> getAuctifyInfo(
            int page,
            int size,
            UserDetails userDetails) {
        return null;
    }

    @Override
    @GetMapping("/myLiked")
    public ResponseEntity<Page<GoodsResponseDTO>> getInfiniteScrollPosts(
            int page,
            int size,
            UserDetails userDetails) {
        return null;
    }

    @Override
    @GetMapping("/address")
    public ResponseEntity<Page<FeedbackDTO>> getListAddress(UserDetails userDetails) {
        return null;
    }

    @Override
    @GetMapping(value = "/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<?> subscribe(UserDetails userDetails, String lastEventId) {
        return null;
    }
}
