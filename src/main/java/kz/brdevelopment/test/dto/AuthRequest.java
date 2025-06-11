package kz.brdevelopment.test.dto;

import lombok.Data;

@Data
public class AuthRequest {
    private String name;
    private String username;
    private String password;
}
