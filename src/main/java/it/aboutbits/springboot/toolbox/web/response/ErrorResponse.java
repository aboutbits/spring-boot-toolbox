package it.aboutbits.springboot.toolbox.web.response;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Map;

@NullMarked
public record ErrorResponse(
        @Nullable String message,
        @Nullable Map<String, List<String>> errors
) {

}
