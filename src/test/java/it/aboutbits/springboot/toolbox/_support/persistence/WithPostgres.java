package it.aboutbits.springboot.toolbox._support.persistence;

import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.ResourceAccessMode;
import org.junit.jupiter.api.parallel.ResourceLock;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@ExtendWith({PostgresTestcontainer.class})
@ResourceLock(value = "Database", mode = ResourceAccessMode.READ_WRITE)
@NullMarked
public @interface WithPostgres {
}
