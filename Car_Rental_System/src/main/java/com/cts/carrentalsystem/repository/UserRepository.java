package com.cts.carrentalsystem.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cts.carrentalsystem.enums.UserRole;
import com.cts.carrentalsystem.model.Users;

public interface UserRepository extends JpaRepository<Users,Long > {

     Optional<Users> findByEmail(String email);

	 Optional<Users> findByRole(UserRole role);

	

	
}
