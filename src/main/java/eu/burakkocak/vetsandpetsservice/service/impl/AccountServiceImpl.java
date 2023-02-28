package eu.burakkocak.vetsandpetsservice.service.impl;

import eu.burakkocak.vetsandpetsservice.api.dto.ApiUserRequest;
import eu.burakkocak.vetsandpetsservice.api.dto.ApiUserResponse;
import eu.burakkocak.vetsandpetsservice.core.ContextHolder;
import eu.burakkocak.vetsandpetsservice.data.model.Account;
import eu.burakkocak.vetsandpetsservice.data.model.Pet;
import eu.burakkocak.vetsandpetsservice.data.repository.AccountRepository;
import eu.burakkocak.vetsandpetsservice.exception.ErrorCode;
import eu.burakkocak.vetsandpetsservice.exception.ServiceException;
import eu.burakkocak.vetsandpetsservice.mapper.ServiceMapper;
import eu.burakkocak.vetsandpetsservice.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final ServiceMapper serviceMapper;

    @Override
    public Page<ApiUserResponse> getUserList(boolean returnPets, String byFullNameLike, boolean allUsers, Pageable pageable) {
        List<Account> userAccounts = getUserAccounts(byFullNameLike, allUsers, pageable);
        List<ApiUserResponse> apiUserResponses = getApiUserResponses(userAccounts, returnPets);
        return new PageImpl<>(apiUserResponses);
    }

    private List<Account> getUserAccounts(String byFullNameLike, boolean allUsers, Pageable pageable) {
        if (allUsers) {
            return accountRepository.findAll();
        } else if (Objects.nonNull(byFullNameLike)) {
            return accountRepository.findAllByFullNameContainingIgnoreCaseOrderByFullNameDesc(byFullNameLike, pageable).toList();
        } else {
            return accountRepository.findAll(pageable).toList();
        }
    }

    private List<ApiUserResponse> getApiUserResponses(List<Account> userAccounts, boolean returnPets) {
        return (returnPets) ? serviceMapper.map(userAccounts) : serviceMapper.mapOnlyAccounts(userAccounts);
    }

    @Override
    public void updateUser(ApiUserRequest request) {
        Account account = accountRepository.findById(request.getId())
                .orElseThrow(() -> new ServiceException(ErrorCode.USER_NOT_FOUND, List.of(request.getId())));
        serviceMapper.map(request, account);
        accountRepository.save(account);
    }

    @Override
    public void removeUserById(UUID userId) {
        Account account = accountRepository.findById(userId)
                .orElseThrow(() -> new ServiceException(ErrorCode.PET_NOT_FOUND));
        account.setDeleted(true);
        account.getPets().stream().forEach(pet -> pet.setDeleted(true));
        accountRepository.save(account);
    }
}
