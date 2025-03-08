package org.example.auctify.controller.user;

import org.example.auctify.dto.user.ProfileDTO;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;

public class UserController implements UserControllerDocs{
    @Override
    @GetMapping("/user")
    public ResponseEntity<?> getMyInfo(UserDetails userDetails) {
        return null;
    }

    @Override
    @PutMapping("/user")
    public ResponseEntity<?> changeProfile(ProfileDTO profile, BindingResult bindingResult, UserDetails userDetails) {
        return null;
    }

    @Override
    @GetMapping("/TotalFeedback")
    public ResponseEntity<?> getListTotalFeedback(UserDetails userDetails) {
        return null;
    }

    @Override
    @GetMapping("/MannerTemperature")
    public ResponseEntity<?> getMannerTemperature(UserDetails userDetails) {
        return null;
    }

    @Override
    @GetMapping("/myBid")
    public ResponseEntity<?> getMyBid(Long myBidId, int size, UserDetails userDetails) {
        return null;
    }

    @Override
    @GetMapping("/myAuctify")
    public ResponseEntity<?> getAuctifyInfo(Long auctifyId, int size, UserDetails userDetails) {
        return null;
    }

    @Override
    @GetMapping("/myLiked")
    public ResponseEntity<?> getInfiniteScrollPosts(Long likedAuctifyId, int size, UserDetails userDetails) {
        return null;
    }

    @Override
    @GetMapping(value = "/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<?> subscribe(UserDetails userDetails, String lastEventId) {
        return null;
    }
}
