package com.cts.carrentalsystem.dtos;

import com.cts.carrentalsystem.enums.UserRole;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
	
	@NotBlank(message = "Username is mandatory")
	private String username;
	
	@NotBlank(message = "Email is mandatory") 
	@Email(message = "Email should be valid")
	private String email;
	
	@NotBlank(message = "Password is mandatory") 
	@Size(min = 6, message = "Password must be at least 6 characters long")
	private String password;
   
	public UserDto(String email, String password) {
		this.email = email;
		this.password = password;
	}
	
    private UserRole role;

}
