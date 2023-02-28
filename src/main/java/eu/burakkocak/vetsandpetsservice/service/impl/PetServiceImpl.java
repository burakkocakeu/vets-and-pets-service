package eu.burakkocak.vetsandpetsservice.service.impl;

import eu.burakkocak.vetsandpetsservice.api.dto.ApiPetRequest;
import eu.burakkocak.vetsandpetsservice.api.dto.ApiPetResponse;
import eu.burakkocak.vetsandpetsservice.api.dto.ApiPetStatisticResponse;
import eu.burakkocak.vetsandpetsservice.api.dto.ApiUserResponse;
import eu.burakkocak.vetsandpetsservice.auth.enums.Role;
import eu.burakkocak.vetsandpetsservice.core.ContextHolder;
import eu.burakkocak.vetsandpetsservice.data.model.Account;
import eu.burakkocak.vetsandpetsservice.data.model.Pet;
import eu.burakkocak.vetsandpetsservice.data.repository.AccountRepository;
import eu.burakkocak.vetsandpetsservice.data.repository.PetRepository;
import eu.burakkocak.vetsandpetsservice.enums.PetType;
import eu.burakkocak.vetsandpetsservice.exception.ErrorCode;
import eu.burakkocak.vetsandpetsservice.exception.ServiceException;
import eu.burakkocak.vetsandpetsservice.mapper.ContextMapper;
import eu.burakkocak.vetsandpetsservice.mapper.ServiceMapper;
import eu.burakkocak.vetsandpetsservice.service.PetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
@RequiredArgsConstructor
public class PetServiceImpl implements PetService {
    private final AccountRepository accountRepository;
    private final PetRepository petRepository;
    private final ContextMapper contextMapper;
    private final ServiceMapper serviceMapper;

    @Override
    public Page<ApiPetResponse> getPetList(Pageable pageable) {
        // Returns user's pets only
        if (Role.USER.equals(ContextHolder.get().getRole())) {
            String username = ContextHolder.get().getUsername();
            List<Pet> pets = new ArrayList<>(accountRepository.findByUsername(username)
                    .orElseThrow(() -> new ServiceException(ErrorCode.USER_NOT_FOUND, List.of(username)))
                    .getPets());

            int startIndex = pageable.getPageNumber() * pageable.getPageSize();
            int endIndex = Math.min(startIndex + pageable.getPageSize(), pets.size());

            pets = pets.subList(startIndex, endIndex);
            return new PageImpl<>(serviceMapper.map(new HashSet<>(pets)), pageable, pets.size());
        }
        // Returns all pets for 'ADMIN' user
        return new PageImpl<>(serviceMapper.map(petRepository.findAll(pageable).toSet()), pageable, petRepository.count());
    }

    @Override
    public void addPetForUserAccount(ApiPetRequest request) {
        Account account = getAccountToAddOrUpdatePet(request);

        account.getPets().add(Pet.builder()
                .name(request.getName())
                .type(request.getType())
                .account(account).build());
        accountRepository.save(account);
    }

    @Override
    public void updatePetOnUserAccount(ApiPetRequest request) {
        Account account = getAccountToAddOrUpdatePet(request);

        Pet maybePet = account.getPets().stream()
                .filter(pet -> pet.getId().equals(request.getId()))
                .findFirst()
                .orElseThrow(() -> new ServiceException(ErrorCode.PET_NOT_FOUND, List.of(request.getId())));
        serviceMapper.map(request, maybePet);
        accountRepository.save(account);
    }

    private Account getAccountToAddOrUpdatePet(ApiPetRequest request) {
        Account account;
        // Admin user can add/update pet for any user by userId -> request.getUserId()
        if (Role.ADMIN.equals(ContextHolder.get().getRole()) && Objects.nonNull(request.getUserId())) {
            account = accountRepository.findById(request.getUserId())
                    .orElseThrow(() -> new ServiceException(ErrorCode.USER_NOT_FOUND, List.of(request.getUserId())));
        }
        // Regular user cannot provide userId in the request
        else if (Objects.nonNull(request.getUserId())) {
            throw new ServiceException(ErrorCode.UNAUTHORIZED_REQUEST);
        }
        // The current User is adding/updating pet for its account
        else {
            String username = ContextHolder.get().getUsername();
            account = accountRepository.findByUsername(username)
                    .orElse(contextMapper.map(ContextHolder.get()));
        }
        return account;
    }

    @Override
    public void removePetById(UUID petId) {
        String username = ContextHolder.get().getUsername();
        Account account = accountRepository.findByUsername(username)
                .orElse(contextMapper.map(ContextHolder.get()));
        Pet maybePet = account.getPets().stream()
                .filter(pet -> pet.getId().equals(petId))
                .findFirst()
                .orElseThrow(() -> new ServiceException(ErrorCode.PET_NOT_FOUND));
        maybePet.setDeleted(true);
        accountRepository.save(account);
    }

    @Override
    public List<ApiPetStatisticResponse> getPetsByTypes(List<PetType> petTypes) {
        HashMap<PetType, AtomicInteger> petMap = new HashMap<>();
        if (petTypes == null || petTypes.size() == 0)
            petTypes = List.of(PetType.values());
        petRepository.findAllByTypeIn(petTypes)
                .forEach(pet -> {
                    if (Objects.isNull(petMap.get(pet.getType())))
                        petMap.put(pet.getType(), new AtomicInteger(1));
                    else
                        petMap.get(pet.getType()).getAndIncrement();
                });
        List<ApiPetStatisticResponse> response = new ArrayList<>();
        petMap.forEach((petType, atomicInteger) -> response.add(ApiPetStatisticResponse.builder().type(petType).total(atomicInteger.get()).build()));
        return response;
    }

    @Override
    public List<ApiUserResponse> getLatestUsers(Long latestUserNumber) {
        PageRequest of = PageRequest.of(0, latestUserNumber.shortValue());
        return serviceMapper.map(accountRepository.findAllByOrderByCreatedAtDesc(of).toList());
    }
}
