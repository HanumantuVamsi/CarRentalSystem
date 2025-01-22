 package com.cts.carrentalsystem.model;

import java.util.List;

import com.cts.carrentalsystem.enums.UserRole;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="users" , uniqueConstraints = {
		@UniqueConstraint(columnNames= {"email"})
})
public class Users {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(name="username" ,nullable = false)
	private String username;
	
	@Column(name = "email", nullable = false)
	private String email;
	
	@Column(name = "password", nullable=false)
	private String password;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private UserRole role;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true) 
	private List<Review> reviews;
	
	
}
