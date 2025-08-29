package it.aboutbits.springboot.toolbox.autoconfiguration.persistence.impl.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomTypeEnumTestModelRepository extends JpaRepository<CustomTypeEnumTestModel, CustomTypeEnumTestModel.ID> {
}
