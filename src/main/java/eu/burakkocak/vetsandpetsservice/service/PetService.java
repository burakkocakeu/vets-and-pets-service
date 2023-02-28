package eu.burakkocak.vetsandpetsservice.service;

import eu.burakkocak.vetsandpetsservice.api.dto.ApiPetRequest;
import eu.burakkocak.vetsandpetsservice.api.dto.ApiPetResponse;
import eu.burakkocak.vetsandpetsservice.api.dto.ApiPetStatisticResponse;
import eu.burakkocak.vetsandpetsservice.api.dto.ApiUserResponse;
import eu.burakkocak.vetsandpetsservice.enums.PetType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface PetService {
    Page<ApiPetResponse> getPetList(Pageable pageable);

    void addPetForUserAccount(ApiPetRequest request);

    void updatePetOnUserAccount(ApiPetRequest request);

    void removePetById(UUID petId);

    List<ApiPetStatisticResponse> getPetsByTypes(List<PetType> petTypes);

    List<ApiUserResponse> getLatestUsers(Long latestUserNumber);
}
