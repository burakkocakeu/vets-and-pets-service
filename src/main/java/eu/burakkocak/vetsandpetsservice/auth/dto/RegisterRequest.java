package eu.burakkocak.vetsandpetsservice.auth.dto;

import eu.burakkocak.vetsandpetsservice.auth.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String fullName;
    private String username;
    private String email;
    private String password;
    private Role role = Role.USER;
}
