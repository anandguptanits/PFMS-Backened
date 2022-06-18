package com.security.springJWT.controller;

import com.security.springJWT.services.UserDataService;
import com.security.springJWT.user.AuthenticationRequest;
import com.security.springJWT.user.UserData;
import com.security.springJWT.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("*")
public class Controller {

    @Autowired
    private UserDataService userDataService;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @RequestMapping("/hello")
    public String hello(){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return "Hello "+userDetails.getUsername();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/signin")
    public ResponseEntity<Map<String,String>> generateToken(@RequestBody AuthenticationRequest authRequest) throws Exception {
        Map<String,String> response = new HashMap<>();
        try {
            String fullName = userDataService.validateCredentials(authRequest.getUserEmail(), authRequest.getPassword());
            String token =  jwtUtil.generateToken(authRequest.getUserEmail());
            response.put("userEmail",authRequest.getUserEmail());
            response.put("fullName",fullName);
            response.put("access-token",token);
            return ResponseEntity.accepted().body(response);
        }
        catch (Exception e){
            response.put("message",e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @RequestMapping(method = RequestMethod.POST,value = "/signup")
    public ResponseEntity<Map<String, String>> signUp(@RequestBody UserData user) {
        return userDataService.signUp(user);
    }

//    @RequestMapping("/getall")
//    public List<UserData> getAll(){
//        return userDataService.getAll();
//    }

}
