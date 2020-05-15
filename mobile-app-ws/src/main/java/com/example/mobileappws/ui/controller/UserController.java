package com.example.mobileappws.ui.controller;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.mobileappws.model.request.UserDetailsRequestModel;
import com.example.mobileappws.model.response.AddressRest;
import com.example.mobileappws.model.response.OperationStatusModel;
import com.example.mobileappws.model.response.RequestOperationStatus;
import com.example.mobileappws.model.response.UserRest;
import com.example.mobileappws.service.AddressService;
import com.example.mobileappws.service.UserService;
import com.example.mobileappws.shared.Roles;
import com.example.mobileappws.shared.dto.AddressDTO;
import com.example.mobileappws.shared.dto.UserDto;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;


@RestController
@RequestMapping("users")
//@CrossOrigin(origins= {"http://localhost:8083","http://localhost:8084"})
public class UserController {
	@Autowired
	private UserService userService;
	
	@Autowired
	private AddressService addressService;
	
	@PostAuthorize("hasRole('ADMIN') or returnObject.userId == principal.userId")
	@ApiOperation(value="The Get User Details Web Services Endpoint",
			notes = "${userController.GetUser.ApiOperation.Notes}")
	@ApiImplicitParams({
		@ApiImplicitParam(name="authorization", value = "Bearer JWT Token",paramType="header")
	})
	@GetMapping(path="/{id}",
			produces = {MediaType.APPLICATION_XML_VALUE,MediaType.APPLICATION_JSON_VALUE })
	public UserRest getMapping(@PathVariable String id) {
		
		UserRest returnValue = new UserRest();
		UserDto userDto = userService.getUserByUserId(id);
		BeanUtils.copyProperties(userDto, returnValue);
		return returnValue;
	}
	
	@ApiImplicitParams({
		@ApiImplicitParam(name="authorization", value = "Bearer JWT Token",paramType="header")
	})
	@PostMapping(
			consumes= {MediaType.APPLICATION_XML_VALUE,MediaType.APPLICATION_JSON_VALUE},
			produces = {MediaType.APPLICATION_XML_VALUE,MediaType.APPLICATION_JSON_VALUE})
	public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) throws Exception {
				
		if(userDetails.getFirstName().isEmpty()) throw new NullPointerException("The object is null");
//		UserDto userDto = new UserDto();
//		BeanUtils.copyProperties(userDetails, userDto);
		ModelMapper modelMapper = new ModelMapper();
		UserDto userDto = modelMapper.map(userDetails, UserDto.class);
//		userDto.setRoles(new HashSet<>(Arrays.asList(Roles.ROLE_USER.name())));
		
		UserDto createdUser = new UserDto();
		createdUser = userService.createUser(userDto);	
		UserRest returnValue = modelMapper.map(createdUser, UserRest.class);
//		BeanUtils.copyProperties(createdUser, returnValue);
		return returnValue;
	}

	@ApiImplicitParams({
		@ApiImplicitParam(name="authorization", value = "Bearer JWT Token",paramType="header")
	})
	@PutMapping(path="/{id}",
			consumes= {MediaType.APPLICATION_XML_VALUE,MediaType.APPLICATION_JSON_VALUE},
			produces = {MediaType.APPLICATION_XML_VALUE,MediaType.APPLICATION_JSON_VALUE}
			)
	public UserRest updateUser(@PathVariable String id, @RequestBody UserDetailsRequestModel userDetails) {
		UserRest returnValue = new UserRest();
		UserDto userDto = new UserDto();
		BeanUtils.copyProperties(userDetails, userDto);
		UserDto updatedUser = new UserDto();
		updatedUser = userService.updateUser(id,userDto);	
		BeanUtils.copyProperties(updatedUser, returnValue);
		return returnValue;
	}
	
	@ApiImplicitParams({
		@ApiImplicitParam(name="authorization", value = "Bearer JWT Token",paramType="header")
	})
//    @PreAuthorize("hasAuthority('DELETE_AUTHORITY')")
 	@PreAuthorize("hasRole('ROLE_ADMIN')or #id == principal.userId")
//	@Secured("ROLE_ADMIN")
	@DeleteMapping(path="/{id}",
			produces = {MediaType.APPLICATION_XML_VALUE,MediaType.APPLICATION_JSON_VALUE}
			)
	public OperationStatusModel deleteUser(@PathVariable String id) {
		
		OperationStatusModel returnValue = new OperationStatusModel();
		returnValue.setOperationName(RequestOperationName.DELETE.name());
		userService.deleteUser(id);
		returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
		return returnValue;
	}
	
	@ApiImplicitParams({
		@ApiImplicitParam(name="authorization", value = "Bearer JWT Token",paramType="header")
	})
	@GetMapping(
			produces = {MediaType.APPLICATION_XML_VALUE,MediaType.APPLICATION_JSON_VALUE}
			)
	public List<UserRest> getUsers(@RequestParam(value="page",defaultValue="1")int page,
			@RequestParam(value="limit",defaultValue="5")int limit){
		
		List<UserRest> returnValue = new ArrayList<UserRest>();
		List<UserDto> users = userService.getUsers(page,limit);
		for(UserDto userDto : users) {
			UserRest userModel = new UserRest();
			BeanUtils.copyProperties(userDto, userModel);
			returnValue.add(userModel);
		}
		
		return returnValue;
	}
	
	@ApiImplicitParams({
		@ApiImplicitParam(name="authorization", value = "Bearer JWT Token",paramType="header")
	})
	@GetMapping(path="/{id}/addresses",
			produces = {MediaType.APPLICATION_XML_VALUE,MediaType.APPLICATION_JSON_VALUE })
	public List<AddressRest> getUserAddress(@PathVariable String id) {
		List<AddressRest> returnValue = new ArrayList<>();
		List<AddressDTO> addressesDTO = addressService.getAddresses(id);
		if(addressesDTO != null && !addressesDTO.isEmpty()) {
			Type listType = new TypeToken<List<AddressRest>>() {}.getType();
			returnValue = new ModelMapper().map(addressesDTO, listType);
		}
		return returnValue;
	}
	
}
