package eu.burakkocak.vetsandpetsservice.api.dto;

import eu.burakkocak.vetsandpetsservice.auth.enums.Role;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
public class ApiUserResponse {
    private UUID id;
    private String fullName;
    private String username;
    private String email;
    private Role role;
    private Set<ApiPetResponse> pets = new HashSet<>();
}
