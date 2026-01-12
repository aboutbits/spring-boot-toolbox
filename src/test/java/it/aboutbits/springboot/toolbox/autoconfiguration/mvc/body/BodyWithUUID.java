package it.aboutbits.springboot.toolbox.autoconfiguration.mvc.body;

import org.jspecify.annotations.NullMarked;

import java.util.UUID;

@NullMarked
public record BodyWithUUID(
        UUID uuid
) {
}
