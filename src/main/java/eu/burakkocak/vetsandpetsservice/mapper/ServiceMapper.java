package eu.burakkocak.vetsandpetsservice.mapper;

import eu.burakkocak.vetsandpetsservice.api.dto.ApiPetRequest;
import eu.burakkocak.vetsandpetsservice.api.dto.ApiPetResponse;
import eu.burakkocak.vetsandpetsservice.api.dto.ApiUserRequest;
import eu.burakkocak.vetsandpetsservice.api.dto.ApiUserResponse;
import eu.burakkocak.vetsandpetsservice.data.model.Account;
import eu.burakkocak.vetsandpetsservice.data.model.Pet;
import org.mapstruct.*;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy  = NullValuePropertyMappingStrategy.IGNORE)
public interface ServiceMapper {
    List<ApiPetResponse> map(Set<Pet> pets);

    @Mapping(target = "ownerId", source = "account.id")
    @Mapping(target = "owner", source = "account.username")
    ApiPetResponse map(Pet pet);

    List<ApiUserResponse> map(List<Account> accounts);

    @IterableMapping(qualifiedByName = "mapAccounts")
    List<ApiUserResponse> mapOnlyAccounts(List<Account> accounts);

    @Named("mapAccounts")
    @Mapping(target = "pets", ignore = true)
    ApiUserResponse map(Account account);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "username", ignore = true)
    void map(ApiUserRequest request, @MappingTarget Account account);

    @Mapping(target = "id", ignore = true)
    void map(ApiPetRequest request, @MappingTarget Pet pet);
}
