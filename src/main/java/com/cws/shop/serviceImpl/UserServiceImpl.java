package com.cws.shop.serviceImpl;

import org.springframework.stereotype.Service;


import com.cws.shop.dto.response.UserDto;
import com.cws.shop.model.User;
import com.cws.shop.model.UserStatus;
import com.cws.shop.repository.OrderRepository;
import com.cws.shop.repository.UserRepository;
import com.cws.shop.service.UserService;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService{
	private final UserRepository userRepository;
	
	
	private final OrderRepository orderRepository;

	

	public UserServiceImpl(UserRepository userRepository, OrderRepository orderRepository) {
		this.userRepository = userRepository;
		this.orderRepository = orderRepository;
	}

	@Override
	public UserDto  activeUser(Long id) {
		User user = getUserEntity(id);
		
		user.setStatus(UserStatus.ACTIVE);
		
		User updatedUser = userRepository.save(user);
		
		return mapToDto(updatedUser);
		
	}

	@Override
	public UserDto suspendUser(Long id) {
		User user = getUserEntity(id);
		
		user.setStatus(UserStatus.SUSPENDED);
		
		User updatedUser = userRepository.save(user);
		
		return mapToDto(updatedUser);
		
	}

	@Override
	public UserDto blockUser(Long id) {
		// TODO Auto-generated method stub
		User user = getUserEntity(id);
		
		user.setStatus(UserStatus.BLOCKED);
		
		User updatedUser = userRepository.save(user);
		
		return mapToDto(updatedUser);
		
	}

	@Override
	public void deleteUser(Long id) {
		User user = getUserEntity(id);
		
		//user.setDeleted(true);
		
		user.setStatus(UserStatus.DELETED);
		
		userRepository.save(user);
		
	}
	
	private User getUserEntity(Long id) {

        return userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("User not found with id: " + id));
    }
	
	private UserDto mapToDto(User user) {
		long totalOrders =
	            orderRepository.countByUserId(user.getId());

        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getMobileNumber(),
                user.getRole(),
                user.getStatus(),
                user.getUpdatedAt(),
                totalOrders
               
        );
    } 
	
	//kajal added
	
	 	@Override
	    public List<UserDto> searchUsers(String keyword) {

	        List<User> users = userRepository.searchUsers(keyword);

	        return users.stream()
	                .map(this::mapToDto)
	                .collect(Collectors.toList());
	    }
	
	 	//pratik added 28-05-26
//	 	@Override
//		public List<UserDto> getAllUsers() {
//		    return userRepository.findByDeletedFalse()
//		            .stream()
//		            .map(this::mapToDto)
//		            .toList();
//		}
	 	
	 	@Override
	 	public List<UserDto> getAllUsers() {
	 	    return userRepository.findAll()
	 	            .stream()
	 	            .map(this::mapToDto)
	 	            .toList();
	 	}
	 	
	 	//pratik added 29-05-26
	 	@Override
	 	public List<UserDto> filterUsers(String status) {

	 	    UserStatus userStatus = UserStatus.valueOf(status);

	 	    return userRepository
	 	            //.findByStatusAndDeletedFalse(userStatus)
	 	    		.findByStatus(userStatus)
	 	            .stream()
	 	            .map(this::mapToDto)
	 	            .toList();
	 	}
     
    
}
