package it.aboutbits.springboot.toolbox.support;

import it.aboutbits.springboot.toolbox.support.persistence.WithPostgres;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithPostgres
public @interface WithPersistence {

}
