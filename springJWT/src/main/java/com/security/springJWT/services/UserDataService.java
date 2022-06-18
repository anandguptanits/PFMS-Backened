package com.security.springJWT.services;

import com.security.springJWT.repository.UserRepository;
import com.security.springJWT.user.UserData;
import org.springframework.beans.InvalidPropertyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.transaction.Transactional;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Transactional
public class UserDataService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        UserData user = userRepository.findByUserEmail(userName);
        return new User(user.getUserEmail(),user.getPassword(),new ArrayList<>());
    }

//    public List<UserData> getAll(){
//        return userRepository.findAll();
//    }

    public ResponseEntity<Map<String, String>> signUp(UserData user){
        Map<String,String> responseMap=new HashMap<>();
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/users/signup").toUriString());

        //email regex
        String patternEmail = ".+(\\.[A-z]+)?@[a-z]+\\..{2,3}(\\.[a-z]{2,3})?";
        String username=user.getUserEmail();
        Matcher matcherEmail = Pattern.compile(patternEmail).matcher(username);

        //password regex
        String patternPassword="^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{8,}$";
        String password=user.getPassword();
        Matcher matcherPassword = Pattern.compile(patternPassword).matcher(password);

        //full name regex
        String fullName=user.getFullName();


        if(!matcherEmail.matches()){
            responseMap.put("response","This email is not valid, make sure you enter email in correct format");
            return ResponseEntity.badRequest().body(responseMap);
        }
        else if(!matcherPassword.matches())
        {
            responseMap.put("response","Password atleast be of 8 characters, And must contain One Uppercase letter, One Lowercase character and a number");
            return ResponseEntity.badRequest().body(responseMap);
        }else if(fullName.length()<2 || fullName.length()>30)
        {
            responseMap.put("response","Full name should have minimum 2 character and maximum 30 character");
            return ResponseEntity.badRequest().body(responseMap);
        }
        else if(userRepository.existsByUserEmail(user.getUserEmail()))
        {
            responseMap.put("response","User already registered");
            return ResponseEntity.badRequest().body(responseMap);
        }
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
        responseMap.put("response","successfully registered");
        return ResponseEntity.created(uri).body(responseMap);
    }

    public String validateCredentials(String email,String password) throws Exception {
        try{
            UserData user = userRepository.findByUserEmail(email);
            if(!checkPassword(password, user.getPassword())){
                throw new Exception("Invalid Credentials");
            }
            return user.getFullName();
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    public boolean checkPassword(String password,String encodedPassword){
        return passwordEncoder.matches(password,encodedPassword);
    }
}
