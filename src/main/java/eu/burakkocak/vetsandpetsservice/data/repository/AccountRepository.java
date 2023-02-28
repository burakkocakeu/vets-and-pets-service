package eu.burakkocak.vetsandpetsservice.data.repository;

import eu.burakkocak.vetsandpetsservice.data.model.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {
    Optional<Account> findByUsername(String username);

    Page<Account> findAllByFullNameContainingIgnoreCaseOrderByFullNameDesc(String fullName, Pageable pageable);

    Page<Account> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
