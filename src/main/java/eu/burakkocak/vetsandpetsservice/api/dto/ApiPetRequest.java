package eu.burakkocak.vetsandpetsservice.api.dto;

import eu.burakkocak.vetsandpetsservice.enums.PetType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class ApiPetRequest {
    private UUID userId;
    private UUID id;
    @NotEmpty
    private String name;
    @NotNull
    private PetType type;
}
