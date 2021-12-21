package com.oga.userservice.security.auth;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest  implements Serializable {

    private static final long serialVersionUID = 5926468583005150707L;


    private String username;
    private String password;
}
