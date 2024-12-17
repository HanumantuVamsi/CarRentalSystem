package com.cts.carrentalsystem.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
	private String username;
	private String email;
	private String password;

	public UserDto(String email, String password) {
		this.email = email;
		this.password = password;
	}

}
