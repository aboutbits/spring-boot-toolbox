package it.aboutbits.springboot.toolbox.boot.mvc.controller;

import it.aboutbits.springboot.toolbox.boot.mvc.body.BodyWithEmailAddress;
import it.aboutbits.springboot.toolbox.boot.mvc.body.BodyWithIban;
import it.aboutbits.springboot.toolbox.boot.mvc.body.BodyWithScaledBigDecimal;
import it.aboutbits.springboot.toolbox.type.EmailAddress;
import it.aboutbits.springboot.toolbox.type.Iban;
import it.aboutbits.springboot.toolbox.type.ScaledBigDecimal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test/type")
public class CustomTypeTestController {
    @GetMapping("/EmailAddress/as-path-variable/{value}")
    public EmailAddress emailAddressAsPathVariable(@PathVariable EmailAddress value) {
        return value;
    }

    @GetMapping("/EmailAddress/as-request-parameter")
    public EmailAddress emailAddressAsRequestParameter(@RequestParam EmailAddress value) {
        return value;
    }

    @PostMapping("/EmailAddress/as-body")
    public BodyWithEmailAddress emailAddressAsBody(@RequestBody BodyWithEmailAddress value) {
        return value;
    }

    @GetMapping("/Iban/as-path-variable/{value}")
    public Iban ibanAsPathVariable(@PathVariable Iban value) {
        return value;
    }

    @GetMapping("/Iban/as-request-parameter")
    public Iban ibanAsRequestParameter(@RequestParam Iban value) {
        return value;
    }

    @PostMapping("/Iban/as-body")
    public BodyWithIban ibanAsBody(@RequestBody BodyWithIban value) {
        return value;
    }

    @GetMapping("/ScaledBigDecimal/as-path-variable/{value}")
    public ScaledBigDecimal scaledBigDecimalAsPathVariable(@PathVariable ScaledBigDecimal value) {
        return value;
    }

    @GetMapping("/ScaledBigDecimal/as-request-parameter")
    public ScaledBigDecimal scaledBigDecimalAsRequestParameter(@RequestParam ScaledBigDecimal value) {
        return value;
    }

    @PostMapping("/ScaledBigDecimal/as-body")
    public BodyWithScaledBigDecimal scaledBigDecimalAsBody(@RequestBody BodyWithScaledBigDecimal value) {
        return value;
    }
}
