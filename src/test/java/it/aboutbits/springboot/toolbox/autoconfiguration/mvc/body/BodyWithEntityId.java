package it.aboutbits.springboot.toolbox.autoconfiguration.mvc.body;

import it.aboutbits.springboot.toolbox.autoconfiguration.persistence.impl.jpa.CustomTypeTestModel;

public record BodyWithEntityId(
        CustomTypeTestModel.ID entityId
) {
}
