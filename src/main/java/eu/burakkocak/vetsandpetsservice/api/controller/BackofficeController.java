package eu.burakkocak.vetsandpetsservice.api.controller;

import eu.burakkocak.vetsandpetsservice.aop.TrackExecutionTime;
import eu.burakkocak.vetsandpetsservice.api.dto.ApiPetStatisticResponse;
import eu.burakkocak.vetsandpetsservice.api.dto.ApiUserRequest;
import eu.burakkocak.vetsandpetsservice.api.dto.ApiUserResponse;
import eu.burakkocak.vetsandpetsservice.enums.PetType;
import eu.burakkocak.vetsandpetsservice.service.AccountService;
import eu.burakkocak.vetsandpetsservice.service.PetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Backoffice API Endpoints should hasRole('ADMIN')
 */
@RestController
@RequestMapping("/backoffice/user")
@RequiredArgsConstructor
public class BackofficeController {
    private final AccountService accountService;
    private final PetService petService;

    /**
     * Backoffice API to list all existing Users
     * @param pageable
     * @return
     */
    @GetMapping
    @TrackExecutionTime
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<ApiUserResponse>> getUsers(@RequestParam(name = "pets", required = false) boolean returnPets,
                                                          @RequestParam(name = "search", required = false) String byFullNameLike,
                                                          @RequestParam(name = "all", required = false) boolean allUsers,
                                                          Pageable pageable) {
        return ResponseEntity.ok(accountService.getUserList(returnPets, byFullNameLike, allUsers, pageable));
    }

    /**
     * Backoffice API to update any User by UserId
     * @param request
     */
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public void updateUser(@Valid @RequestBody ApiUserRequest request) {
        accountService.updateUser(request);
    }

    /**
     * Deletes any User by UserId
     * @param userId
     */
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void removeUserById(@PathVariable("id") UUID userId) {
        accountService.removeUserById(userId);
    }

    /**
     * Backoffice API to list Pet Types and Total Counts of all Users
     * @param petTypes
     * @return
     */
    @GetMapping("/pet-statistics")
    @TrackExecutionTime
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ApiPetStatisticResponse>> getPetsByTypes(
            @RequestParam(name = "types", required = false) List<PetType> petTypes) {
        return ResponseEntity.ok(petService.getPetsByTypes(petTypes));
    }

    /**
     * Backoffice API to get Total Counts of Users
     * @return
     */
    @GetMapping("/user-statistics")
    @TrackExecutionTime
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ApiUserResponse>> getLatestUsers(@RequestParam Long latestUserNumber) {
        return ResponseEntity.ok(petService.getLatestUsers(latestUserNumber));
    }
}
