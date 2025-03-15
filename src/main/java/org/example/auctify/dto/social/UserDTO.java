package org.example.auctify.dto.social;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.example.auctify.dto.user.Role;

@Getter
@Setter
@ToString
@JsonFormat(shape = JsonFormat.Shape.STRING)
public class UserDTO {
    private Long userId;
    private String  oauthId;
    private Role role;
    private String name;

}
