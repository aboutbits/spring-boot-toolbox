package it.aboutbits.springboot.toolbox.web.response;

import lombok.NonNull;

import java.util.List;

public record ListResponse<T>(@NonNull List<T> data) {
}
