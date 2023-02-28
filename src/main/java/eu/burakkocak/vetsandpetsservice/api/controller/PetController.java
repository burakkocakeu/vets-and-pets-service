package eu.burakkocak.vetsandpetsservice.api.controller;

import eu.burakkocak.vetsandpetsservice.aop.TrackExecutionTime;
import eu.burakkocak.vetsandpetsservice.api.dto.ApiPetRequest;
import eu.burakkocak.vetsandpetsservice.api.dto.ApiPetResponse;
import eu.burakkocak.vetsandpetsservice.service.PetService;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Pet API Endpoints should hasAnyRole('USER', 'ADMIN')
 */
@RestController
@RequestMapping("/user/pets")
@RequiredArgsConstructor
public class PetController {
    private final PetService petService;

    /**
     * Gets existing Pets for the User #('USER') or All Users #('ADMIN')
     * @return
     */
    @GetMapping
    @TrackExecutionTime
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Page<ApiPetResponse>> getPets(Pageable pageable) {
        return ResponseEntity.ok(petService.getPetList(pageable));
    }

    /**
     * Adds a new Pet for the User #('USER') or any User by UserId #('ADMIN')
     * @param request
     */
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public void addPet(@Valid @RequestBody ApiPetRequest request) {
        petService.addPetForUserAccount(request);
    }

    /**
     * Updates an existing Pet by PetId
     * @param request
     */
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public void updatePet(@Valid @RequestBody ApiPetRequest request) {
        if (Objects.isNull(request.getId())) {
            throw new ValidationException();
        }
        petService.updatePetOnUserAccount(request);
    }

    /**
     * Deletes any Pet by PetId
     * @param petId
     */
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public void removePetById(@PathVariable("id") UUID petId) {
        petService.removePetById(petId);
    }
}
