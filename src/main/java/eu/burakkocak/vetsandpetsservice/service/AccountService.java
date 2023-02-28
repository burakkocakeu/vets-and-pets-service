package eu.burakkocak.vetsandpetsservice.service;

import eu.burakkocak.vetsandpetsservice.api.dto.ApiUserRequest;
import eu.burakkocak.vetsandpetsservice.api.dto.ApiUserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface AccountService {
    Page<ApiUserResponse> getUserList(boolean returnPets, String byFullNameLike, boolean allUsers, Pageable pageable);

    void updateUser(ApiUserRequest request);
    void removeUserById(UUID userId);
}
