package eu.burakkocak.vetsandpetsservice.api.dto;

import eu.burakkocak.vetsandpetsservice.enums.PetType;
import lombok.Data;

import java.util.UUID;

@Data
public class ApiPetResponse {
    private UUID id;
    private String name;
    private PetType type;
    private String owner;
    private UUID ownerId;
}
