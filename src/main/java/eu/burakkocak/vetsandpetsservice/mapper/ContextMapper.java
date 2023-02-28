package eu.burakkocak.vetsandpetsservice.mapper;

import eu.burakkocak.vetsandpetsservice.core.Context;
import eu.burakkocak.vetsandpetsservice.data.model.Account;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ContextMapper {
    Context map(Account principal);
    Account map(Context context);
}
