package it.aboutbits.springboot.toolbox.persistence.transformer.impl.jpa;

import org.jspecify.annotations.NullMarked;
import org.springframework.data.jpa.repository.JpaRepository;

@NullMarked
public interface QueryTransformerTestModelRepository extends JpaRepository<QueryTransformerTestModel, QueryTransformerTestModel.ID> {
}
