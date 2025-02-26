package org.example.auctify.dto.social;


import lombok.Getter;
import lombok.Setter;
import org.example.auctify.dto.user.Role;

@Getter
@Setter
public class UserDTO {

    private String  oauthId;
    private Role role;
    private String name;

}
