package com.security.springJWT;

import com.security.springJWT.repository.UserRepository;
import com.security.springJWT.services.UserDataService;
import com.security.springJWT.user.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class UserDataServiceTests {

	@MockBean
	UserRepository userRepository;

	@Autowired
	UserDataService userDataService;

	@MockBean
	PasswordEncoder passwordEncoder;

	@Test
	public void signUpTest(){
		UserData user = new UserData(Long.parseLong("1"),"abc@gmail.com","abc123","Password123","male");
		Mockito.doReturn(Boolean.FALSE).when(userRepository).existsByUserEmail("abc@gmail.com");
		Mockito.doReturn(user).when(userRepository).save(user);
		Mockito.doReturn("$Password123$").when(passwordEncoder).encode("$Password123");
		Assertions.assertEquals(201,userDataService.signUp(user).getStatusCodeValue());
	}

	@Test
	public void validateCredentialsTest() throws Exception {
		UserData user = new UserData(Long.parseLong("1"),"abc@gmail.com","abc123","Password123","male");
		Mockito.doReturn(user).when(userRepository).findByUserEmail("abc@gmail.com");
		Mockito.doReturn(Boolean.TRUE).when(passwordEncoder).matches("Password123","Password123");
		Assertions.assertEquals("abc123",userDataService.validateCredentials("abc@gmail.com","Password123"));
	}

	@Test
	public void validateCredentialTestForWrongCredentials() throws Exception {
		UserData user = new UserData(Long.parseLong("1"),"abc@gmail.com","abc123","Password123","male");
		Mockito.doReturn(user).when(userRepository).findByUserEmail("abc@gmail.com");
		Mockito.doReturn(Boolean.FALSE).when(passwordEncoder).matches("Password123","Password123");
		try {
			userDataService.validateCredentials("abc@gmail.com", "Password123");
		}catch (Exception e){
			Assertions.assertTrue(Boolean.TRUE);
			return;
		}
		Assertions.fail();
//		Assertions.assertFalse(Boolean.TRUE);
	}

	@Test void signUpTestForInvalidPassword(){
		UserData user = new UserData(Long.parseLong("1"),"abc@gmail.com","abc123","Pass","male");
		Mockito.doReturn(Boolean.FALSE).when(userRepository).existsByUserEmail("abc@gmail.com");
		Mockito.doReturn(user).when(userRepository).save(user);
		Mockito.doReturn("Pass").when(passwordEncoder).encode("$Password123");
		Assertions.assertEquals(400,userDataService.signUp(user).getStatusCodeValue());
	}

	@Test
	void signUpTestForInvalidFullName(){
		UserData user = new UserData(Long.parseLong("1"),"abc@gmail.com","a","Password123","male");
		Mockito.doReturn(Boolean.FALSE).when(userRepository).existsByUserEmail("abc@gmail.com");
		Mockito.doReturn(user).when(userRepository).save(user);
		Mockito.doReturn("$Password123$").when(passwordEncoder).encode("$Password123");
		Assertions.assertEquals(400,userDataService.signUp(user).getStatusCodeValue());
	}

	@Test
	void signUpTestForInvalidEMail(){
		UserData user = new UserData(Long.parseLong("1"),"abcgmail.com","abc123","Password123","male");
		Mockito.doReturn(Boolean.FALSE).when(userRepository).existsByUserEmail("abc@gmail.com");
		Mockito.doReturn(user).when(userRepository).save(user);
		Mockito.doReturn("$Password123$").when(passwordEncoder).encode("$Password123");
		Assertions.assertEquals(400,userDataService.signUp(user).getStatusCodeValue());
	}

	@Test
	void signUpTestIfEMailIsAlreadyBeingUsed(){
		UserData user = new UserData(Long.parseLong("1"),"abc@gmail.com","abc123","Password123","male");
		Mockito.doReturn(Boolean.TRUE).when(userRepository).existsByUserEmail("abc@gmail.com");
		Mockito.doReturn(user).when(userRepository).save(user);
		Mockito.doReturn("$Password123$").when(passwordEncoder).encode("$Password123");
		Assertions.assertEquals(400,userDataService.signUp(user).getStatusCodeValue());
	}

}
