package com.appsdeveloperblog.app.ws.ui.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;

import com.appsdeveloperblog.app.ws.exceptions.UserServiceException;
import com.appsdeveloperblog.app.ws.service.UserService;
import com.appsdeveloperblog.app.ws.shared.dto.UserDto;
import com.appsdeveloperblog.app.ws.ui.model.request.RequestOperationStatus;
import com.appsdeveloperblog.app.ws.ui.model.request.UserDetailsRequestModel;
import com.appsdeveloperblog.app.ws.ui.model.response.ErrorMessages;
import com.appsdeveloperblog.app.ws.ui.model.response.OperationStatusModel;
import com.appsdeveloperblog.app.ws.ui.model.response.RequestOperationName;
import com.appsdeveloperblog.app.ws.ui.model.response.UserRest;

import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("users") //http://localhost:8080/users
public class UserController {

	@Autowired
	UserService userService;
	
	@GetMapping(path="/{id}", 
			produces= { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE }) //API always defaults to first mediatype
	public UserRest getUser(@PathVariable String id) {
		UserRest returnValue = new UserRest();
		
		UserDto userDto = userService.getUserByUserId(id);
		
		//our 2 properties won't have the same details, but any details that match will be copied. useDto contains password, emailverification etc
		BeanUtils.copyProperties(userDto, returnValue);
		
		
		
		return returnValue;
	}
	
	@PostMapping (
			consumes = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
			produces= { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE }
			) //API always defaults to first mediatype
	public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) throws Exception {
		UserRest returnValue = new UserRest();
		
		if(userDetails.getFirstName().isEmpty()) 
			throw new NullPointerException("The Object is null");
		
		//Prepares SQL statement
		UserDto userDto = new UserDto();
		
		//Copies Request Details into Data Object
		BeanUtils.copyProperties(userDetails, userDto);
		
		//Returns modified Data Object after SQL stored
		UserDto createdUser = userService.createUser(userDto);
		
		//Copies from Data Object to pass as correct Response
		BeanUtils.copyProperties(createdUser, returnValue);
		
		//Returns Response
		return returnValue;
	}
	
	@PutMapping (path="/{id}",
			consumes = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
			produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE }
			) //API always defaults to first media type
	public UserRest updateUser(@PathVariable String id, @RequestBody UserDetailsRequestModel userDetails) {
		
		UserRest returnValue = new UserRest();
		
		//Prepares SQL statement
		UserDto userDto = new UserDto();
		
		//Copies Request Details into Data Object
		BeanUtils.copyProperties(userDetails, userDto);
		
		//Returns modified Data Object after SQL stored
		UserDto updateUser = userService.updateUser(id, userDto);
		
		//Copies from Data Object to pass as correct Response
		BeanUtils.copyProperties(updateUser, returnValue);
		
		//Returns Response
		return returnValue;
	}
	
	@DeleteMapping(path="/{id}", produces= {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
	public OperationStatusModel deleteUser(@PathVariable String id) {

		OperationStatusModel returnValue = new OperationStatusModel();
		returnValue.setOperationName(RequestOperationName.DELETE.name());
		
		userService.deleteUser(id);
		
		
		returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
		return returnValue;
	}
	
	@GetMapping
	public List<UserRest> getUsers() {
		List<UserRest> returnValue = new ArrayList<>();
		
		return returnValue;
	}
}
