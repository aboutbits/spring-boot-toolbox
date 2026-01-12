package it.aboutbits.springboot.toolbox.autoconfiguration.persistence;

import org.hibernate.type.descriptor.java.JavaType;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface AutoRegisteredJavaType<T> extends JavaType<T> {
}
