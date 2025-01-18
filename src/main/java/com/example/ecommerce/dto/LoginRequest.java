package com.example.ecommerce.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginRequest {
	
	private String username;
	private String password;

}
