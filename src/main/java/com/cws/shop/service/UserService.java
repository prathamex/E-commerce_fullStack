package com.cws.shop.service;

import com.cws.shop.dto.response.UserDto;
import java.util.List;

public interface UserService {
	UserDto activeUser(Long id);
	
	UserDto suspendUser(Long id);
	
	UserDto blockUser(Long id);
	
	void deleteUser(Long id);
	
	//kajal added
	List<UserDto> searchUsers(String keyword);
	
	//pratik added 28-05-26
	List<UserDto> getAllUsers();
	
	//pratik added 29-05-26
	List<UserDto> filterUsers(String status);
	
}
