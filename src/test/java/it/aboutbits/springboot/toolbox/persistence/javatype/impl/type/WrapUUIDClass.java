package it.aboutbits.springboot.toolbox.persistence.javatype.impl.type;

import it.aboutbits.springboot.toolbox.type.CustomType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.jspecify.annotations.NullMarked;

import java.util.UUID;

@Getter
@Accessors(fluent = true)
@EqualsAndHashCode
@RequiredArgsConstructor
@NullMarked
public class WrapUUIDClass implements CustomType<UUID> {
    private final UUID value;
}
