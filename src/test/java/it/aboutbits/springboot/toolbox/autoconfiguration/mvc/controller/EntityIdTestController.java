package it.aboutbits.springboot.toolbox.autoconfiguration.mvc.controller;

import it.aboutbits.springboot.toolbox.autoconfiguration.mvc.body.BodyWithEntityId;
import it.aboutbits.springboot.toolbox.autoconfiguration.mvc.body.BodyWithEnumEntityId;
import it.aboutbits.springboot.toolbox.autoconfiguration.persistence.impl.jpa.CustomTypeEnumTestModel;
import it.aboutbits.springboot.toolbox.autoconfiguration.persistence.impl.jpa.CustomTypeTestModel;
import org.jspecify.annotations.NullMarked;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test/entity-id")
@NullMarked
public class EntityIdTestController {
    @GetMapping("/CustomTypeTestModel.ID/as-path-variable/{value}")
    public CustomTypeTestModel.ID customTypeTestModelIdAsPathVariable(@PathVariable CustomTypeTestModel.ID value) {
        return value;
    }

    @GetMapping("/CustomTypeTestModel.ID/as-request-parameter")
    public CustomTypeTestModel.ID customTypeTestModelIdAsRequestParameter(@RequestParam CustomTypeTestModel.ID value) {
        return value;
    }

    @PostMapping("/CustomTypeTestModel.ID/as-body")
    public BodyWithEntityId customTypeTestModelIdAsBody(@RequestBody BodyWithEntityId value) {
        return value;
    }

    @GetMapping("/CustomTypeEnumTestModel.ID/as-path-variable/{value}")
    public CustomTypeEnumTestModel.ID customTypeEnumTestModelIdAsPathVariable(@PathVariable CustomTypeEnumTestModel.ID value) {
        return value;
    }

    @GetMapping("/CustomTypeEnumTestModel.ID/as-request-parameter")
    public CustomTypeEnumTestModel.ID customTypeEnumTestModelIdAsRequestParameter(@RequestParam CustomTypeEnumTestModel.ID value) {
        return value;
    }

    @PostMapping("/CustomTypeEnumTestModel.ID/as-body")
    public BodyWithEnumEntityId customTypeEnumTestModelIdAsBody(@RequestBody BodyWithEnumEntityId value) {
        return value;
    }
}
