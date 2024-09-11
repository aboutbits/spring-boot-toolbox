package it.aboutbits.springboot.toolbox.persistence;

import java.time.OffsetDateTime;

public interface ChangeAware {
    OffsetDateTime getCreatedAt();

    OffsetDateTime getUpdatedAt();

    void setCreatedAt(OffsetDateTime createdAt);

    void setUpdatedAt(OffsetDateTime updatedAt);
}
