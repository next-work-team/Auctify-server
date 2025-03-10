package org.example.auctify.service.user;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.example.auctify.config.security.CustomUserDetails;
import org.example.auctify.entity.user.UserEntity;
import org.example.auctify.repository.user.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;


/*
 *   worker : 조영흔
 *   work : 로그인시 회원을 조회하고 찾아서 CustomUserDetails에 넘겨줌.
 *   date : 2025/03/09
 * */

@Component
@Log4j2
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity findUser = userRepository.findByOauthId(username);
        log.info("user : " + findUser);

        // 여기서 DB에서 사용자 정보를 가져와야 함
        if (findUser == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return new CustomUserDetails(findUser);
    }
}