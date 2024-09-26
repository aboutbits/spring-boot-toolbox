package it.aboutbits.springboot.toolbox.persistence.transformer.impl.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

public interface QueryTransformerTestModelRepository extends JpaRepository<QueryTransformerTestModel, QueryTransformerTestModel.ID> {
}
