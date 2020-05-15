package com.example.mobileappws.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.example.mobileappws.entity.UserEntity;
import com.example.mobileappws.repository.UserRepository;
import com.example.mobileappws.shared.dto.UserDto;



public class UserServiceImplTest {
	
	@Mock
	UserRepository userRepository;
	
	@InjectMocks
	UserServiceImpl userService;

	@BeforeEach
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testGetUser() {
		UserEntity userEntity = new UserEntity();
		userEntity.setId(1L);
		userEntity.setFirstName("demo");
		userEntity.setUserId("ygygyg54jdk");
		userEntity.setEncryptedPassword("hhjhsfys5534sj");

	when(userRepository.findUserByEmail(anyString())).thenReturn(userEntity);
	
	UserDto userDto = userService.getUser("test@test.com");
	assertNotNull(userDto);
	assertEquals("demo", userDto.getFirstName());
	
	}

	public void testGetUser_UsernameNotFoundException() {
		
		when(userRepository.findUserByEmail(anyString())).thenReturn(null);
		assertThrows(UsernameNotFoundException.class,
		
		()->{
			userService.getUser("test@test.com");
		}
		);
	}
	
}
