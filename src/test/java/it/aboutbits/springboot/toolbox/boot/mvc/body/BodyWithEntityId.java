package it.aboutbits.springboot.toolbox.boot.mvc.body;

import it.aboutbits.springboot.toolbox.boot.persistence.impl.jpa.CustomTypeTestModel;

public record BodyWithEntityId(
        CustomTypeTestModel.ID entityId
) {
}
