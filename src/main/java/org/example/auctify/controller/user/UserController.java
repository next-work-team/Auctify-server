package org.example.auctify.controller.user;

import org.example.auctify.dto.Goods.AuctionGoodsDTO;
import org.example.auctify.dto.Goods.FeedbackDTO;
import org.example.auctify.dto.Goods.GoodsResponseDTO;
import org.example.auctify.dto.user.MannerTemperatureDTO;
import org.example.auctify.dto.user.ProfileDTO;
import org.example.auctify.dto.user.UserInfoDTO;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

public class UserController implements UserControllerDocs{
    @Override
    @GetMapping("/user")
    public ResponseEntity<UserInfoDTO> getMyInfo(UserDetails userDetails) {
        return null;
    }

    @Override
    @PutMapping("/user")
    public ResponseEntity<UserInfoDTO> changeProfile(
            UserInfoDTO userInfoDTO,
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
    public ResponseEntity<Page<AuctionGoodsDTO>> getAuctifyInfo(
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
    @GetMapping(value = "/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<?> subscribe(UserDetails userDetails, String lastEventId) {
        return null;
    }
}
