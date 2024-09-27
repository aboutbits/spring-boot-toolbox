package it.aboutbits.springboot.toolbox.web.response;

import lombok.NonNull;

public record ItemResponse<T>(@NonNull T data) {
}
