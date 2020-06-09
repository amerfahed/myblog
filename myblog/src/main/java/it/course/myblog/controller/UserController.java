package it.course.myblog.controller;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.course.myblog.entity.User;
import it.course.myblog.repository.UserRepository;
import it.course.myblog.payload.response.ApiResponseCustom;
import it.course.myblog.payload.response.UserResponse;

@RestController
public class UserController {
	
	@Autowired
	UserRepository userRepository;
	
	
	//GET	list - select * from user
	@GetMapping("/get-users")
	public ResponseEntity<ApiResponseCustom> getAllUsers(HttpServletRequest request){
		
		List<User> us =  userRepository.findAll();
		
		if(us.isEmpty())
			return new ResponseEntity<ApiResponseCustom>(new ApiResponseCustom(
					Instant.now(), 200, "OK", "User not found", request.getRequestURI()), HttpStatus.OK);
		
		List<UserResponse> urs = us.stream().map(UserResponse::createFromEntity).collect(Collectors.toList());
		return new ResponseEntity<ApiResponseCustom>(new ApiResponseCustom(
				Instant.now(), 200, "OK",urs, request.getRequestURI()), HttpStatus.OK);
	}
	
	@GetMapping("/get-user")
	public ResponseEntity<ApiResponseCustom> getSingleUser(@RequestParam Long id, HttpServletRequest request){
		Optional<User> u = userRepository.findById(id);
		
		
		if(u.isPresent()&& u.get().getEnabled()) {
			UserResponse ur = UserResponse.createFromEntity(u.get());
			return new ResponseEntity<ApiResponseCustom>(new ApiResponseCustom(
					Instant.now(), 200, "OK", ur, request.getRequestURI()), HttpStatus.OK);
		}
		
		return new ResponseEntity<ApiResponseCustom>(new ApiResponseCustom(
				Instant.now(), 200, "OK", "User not found or disabled", request.getRequestURI()), HttpStatus.OK);
	}
	/*//GET	single
	@GetMapping("/get-user")
	public User getSingleUser(@RequestParam Long id){
		
		return userRepository.findById(id).get();
		
	}*/
	
	//POST
	@PostMapping("/insert-user")
	public ResponseEntity<ApiResponseCustom>  insertUser(@Valid @RequestBody User user, HttpServletRequest request) {
		
		if(userRepository.existsByUsernameOrEmail(user.getUsername(), user.getEmail()))
				return new ResponseEntity<ApiResponseCustom>(new ApiResponseCustom(
						Instant.now(), 200, "OK", "Username or Email already in use", request.getRequestURI()), HttpStatus.OK );
		User u = new User();
		u.setEmail(user.getEmail());
		u.setUsername(user.getUsername());
		u.setPassword(user.getPassword());
		u.setEnabled(true);
		
		userRepository.save(u);
		return new ResponseEntity<ApiResponseCustom>(new ApiResponseCustom(
				Instant.now(), 200, "OK", "User succesfully created", request.getRequestURI()), HttpStatus.OK );
		
	}
	
	//UPDATE
	@PutMapping("/update-user")
	public ResponseEntity<ApiResponseCustom> updateUser(@Valid @RequestBody User user, HttpServletRequest request) {
		Optional<User> u = userRepository.findById(user.getId());
		//Optional<User> u = userRepository.findByEmail(user.getEmail());
		
		if(u.isPresent()) {
			u.get().setPassword(user.getPassword());
			u.get().setUsername(user.getUsername());
			u.get().setEnabled(user.getEnabled());
			u.get().setEmail(user.getEmail());
			userRepository.save(u.get());
			return new ResponseEntity<ApiResponseCustom>(new ApiResponseCustom(
					Instant.now(), 200, "OK", "User succesfully updated", request.getRequestURI()), HttpStatus.OK );
		} else {
			return new ResponseEntity<ApiResponseCustom>(new ApiResponseCustom(
				Instant.now(), 200, "OK", "User not found", request.getRequestURI()), HttpStatus.OK);
			
		}
	}
	
	//DELETE
	@PutMapping("/enable-disable-user/{id}")
	public ResponseEntity<ApiResponseCustom> enableDisableUser(@PathVariable Long id, HttpServletRequest request) {
		
		Optional<User> u = userRepository.findById(id);
		
		if(u.isPresent()) {
			u.get().setEnabled(!u.get().getEnabled());
			return new ResponseEntity<ApiResponseCustom>(new ApiResponseCustom(
					Instant.now(), 200, "OK", "User succesfully updated", request.getRequestURI()), HttpStatus.OK );
		} else {
			return new ResponseEntity<ApiResponseCustom>(new ApiResponseCustom(
					Instant.now(), 200, "OK", "User not found", request.getRequestURI()), HttpStatus.OK );
		}
		
	}
			

}
