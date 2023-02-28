package eu.burakkocak.vetsandpetsservice.core;

import eu.burakkocak.vetsandpetsservice.auth.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Context {
    private String fullName;
    private String username;
    private String email;
    private String password;
    private Role role;
}
