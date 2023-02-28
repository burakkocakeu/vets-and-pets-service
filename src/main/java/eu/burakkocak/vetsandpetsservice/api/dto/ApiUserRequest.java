package eu.burakkocak.vetsandpetsservice.api.dto;

import eu.burakkocak.vetsandpetsservice.auth.enums.Role;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class ApiUserRequest {
    @NotNull
    private UUID id;
    private String fullName;
    private String email;
    private Role role;
}
