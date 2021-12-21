package com.oga.userservice.controller;

import com.oga.userservice.Entity.UserEntity;
import com.oga.userservice.Utile.JwtUtil;
import com.oga.userservice.dto.UserTokenDto;
import com.oga.userservice.security.auth.AuthenticationRequest;
import com.oga.userservice.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class AuthenticateController {

    @Autowired
    private final UserService userService;
    @Autowired
    private final JwtUtil jwtUtil;

    @Autowired
    private final AuthenticationManager authenticationManager;

    public AuthenticateController(UserService userService, JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }


    @PostMapping("/authenticate")
    public ResponseEntity<UserTokenDto> generateToken(@RequestBody AuthenticationRequest request) throws Exception{


            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(),request.getPassword()));


        UserEntity user = userService.getUserByUserName(request.getUsername());

        System.out.println(user);
        return new ResponseEntity(new UserTokenDto(jwtUtil.generateToken(request.getUsername()),user),HttpStatus.OK)  ;
    }
}
