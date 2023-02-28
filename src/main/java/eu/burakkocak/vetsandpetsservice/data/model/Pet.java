package eu.burakkocak.vetsandpetsservice.data.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import eu.burakkocak.vetsandpetsservice.enums.PetType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Where(clause = "deleted = false")
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;
    private String name;
    @Enumerated(EnumType.STRING)
    private PetType type;
    @Builder.Default
    private boolean deleted = false;
    @JsonIgnore
    @EqualsAndHashCode.Exclude // To get rid of StackOverflowError
    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @Override
    public String toString() {
        return "Pet{" +
                "name='" + name + '\'' +
                '}';
    }
}
