package eu.burakkocak.vetsandpetsservice.data.repository;

import eu.burakkocak.vetsandpetsservice.data.model.Pet;
import eu.burakkocak.vetsandpetsservice.enums.PetType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PetRepository extends JpaRepository<Pet, UUID> {
    List<Pet> findAllByTypeIn(List<PetType> petTypes);
}
