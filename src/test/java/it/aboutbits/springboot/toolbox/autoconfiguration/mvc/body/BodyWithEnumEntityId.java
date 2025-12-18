package it.aboutbits.springboot.toolbox.autoconfiguration.mvc.body;

import it.aboutbits.springboot.toolbox.autoconfiguration.persistence.impl.jpa.CustomTypeEnumTestModel;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record BodyWithEnumEntityId(
        CustomTypeEnumTestModel.ID enumEntityId
) {
}
