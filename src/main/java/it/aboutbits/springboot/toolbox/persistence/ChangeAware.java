package it.aboutbits.springboot.toolbox.persistence;

import org.jspecify.annotations.NullMarked;

import java.time.OffsetDateTime;

@NullMarked
public interface ChangeAware {
    OffsetDateTime getCreatedAt();

    OffsetDateTime getUpdatedAt();

    void setCreatedAt(OffsetDateTime createdAt);

    void setUpdatedAt(OffsetDateTime updatedAt);
}
