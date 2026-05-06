package it.aboutbits.springboot.toolbox.autoconfiguration.persistence.impl.jpa;

import it.aboutbits.springboot.toolbox.type.identity.Identified;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullUnmarked;

@Entity
@Getter
@Setter
@Table(name = "direct_enum_id_test_model")
@NullUnmarked
public class DirectEnumIdTestModel implements Identified<@NonNull DirectEnumEntityId> {
    @Id
    @Enumerated(EnumType.STRING)
    private DirectEnumEntityId id;
}
