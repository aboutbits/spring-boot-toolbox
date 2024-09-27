package it.aboutbits.springboot.toolbox.mvc.response;

import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Map;

public record ErrorResponse(
        @Nullable String message,
        @Nullable Map<String, List<String>> errors
) {

}