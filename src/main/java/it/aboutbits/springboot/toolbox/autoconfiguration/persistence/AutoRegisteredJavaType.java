package it.aboutbits.springboot.toolbox.autoconfiguration.persistence;

import org.hibernate.type.descriptor.java.JavaType;

public interface AutoRegisteredJavaType<T> extends JavaType<T> {
}
