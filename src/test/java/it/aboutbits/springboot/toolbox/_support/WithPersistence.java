package it.aboutbits.springboot.toolbox._support;

import it.aboutbits.springboot.toolbox._support.persistence.WithPostgres;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithPostgres
public @interface WithPersistence {

}
