package it.aboutbits.springboot.toolbox.autoconfiguration.persistence.impl.jpa;

import it.aboutbits.springboot.toolbox.type.EmailAddress;
import it.aboutbits.springboot.toolbox.type.Iban;
import it.aboutbits.springboot.toolbox.type.ScaledBigDecimal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomTypeTestModelRepository extends JpaRepository<CustomTypeTestModel, CustomTypeTestModel.ID> {
    Optional<CustomTypeTestModel> findByEmail(EmailAddress emailAddress);

    Optional<CustomTypeTestModel> findByIban(Iban iban);

    Optional<CustomTypeTestModel> findByAccountBalance(ScaledBigDecimal accountBalance);

    Optional<CustomTypeTestModel> findByReferencedId(ReferencedTestModel.ID otherId);
}
