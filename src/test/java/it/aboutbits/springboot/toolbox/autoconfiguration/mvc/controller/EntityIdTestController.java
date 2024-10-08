package it.aboutbits.springboot.toolbox.autoconfiguration.mvc.controller;

import it.aboutbits.springboot.toolbox.autoconfiguration.mvc.body.BodyWithEntityId;
import it.aboutbits.springboot.toolbox.autoconfiguration.persistence.impl.jpa.CustomTypeTestModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test/entity-id")
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
}
