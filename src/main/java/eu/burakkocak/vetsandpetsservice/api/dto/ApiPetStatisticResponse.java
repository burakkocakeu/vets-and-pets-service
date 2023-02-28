package eu.burakkocak.vetsandpetsservice.api.dto;

import eu.burakkocak.vetsandpetsservice.enums.PetType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiPetStatisticResponse {
    private PetType type;
    private int total;
}
