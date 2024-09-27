package it.aboutbits.springboot.toolbox.mvc.response;

import lombok.NonNull;

public record ItemResponse<T>(@NonNull T data) {
}
