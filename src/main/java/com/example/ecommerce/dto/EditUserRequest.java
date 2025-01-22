package com.example.ecommerce.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EditUserRequest {
	
	private String username;
	private String role;
}
