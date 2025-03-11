package org.example.auctify.dto.social;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.example.auctify.dto.user.Role;

@Getter
@Setter
@JsonFormat(shape = JsonFormat.Shape.STRING)
public class UserDTO {

    private String  oauthId;
    private Role role;
    private String name;

}
