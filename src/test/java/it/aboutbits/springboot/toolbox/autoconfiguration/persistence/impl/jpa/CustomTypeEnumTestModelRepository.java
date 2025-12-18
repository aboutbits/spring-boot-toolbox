package it.aboutbits.springboot.toolbox.autoconfiguration.persistence.impl.jpa;

import org.jspecify.annotations.NullMarked;
import org.springframework.data.jpa.repository.JpaRepository;

@NullMarked
public interface CustomTypeEnumTestModelRepository extends JpaRepository<CustomTypeEnumTestModel, CustomTypeEnumTestModel.ID> {
}
