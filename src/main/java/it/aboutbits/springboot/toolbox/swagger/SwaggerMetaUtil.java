package it.aboutbits.springboot.toolbox.swagger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;

@Slf4j
public final class SwaggerMetaUtil {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private SwaggerMetaUtil() {
    }

    @SneakyThrows(JsonProcessingException.class)
    public static String setMapKeyTypeFqn(@Nullable String currentMeta, @NonNull String value) {
        var meta = getSwaggerMeta(currentMeta);
        meta.setMapKeyTypeFqn(value);

        return OBJECT_MAPPER.writeValueAsString(meta);
    }

    @SneakyThrows(JsonProcessingException.class)
    public static String setIsMap(@Nullable String currentMeta, boolean value) {
        var meta = getSwaggerMeta(currentMeta);
        meta.setIsMap(!value ? null : true);

        return OBJECT_MAPPER.writeValueAsString(meta);
    }

    @SneakyThrows(JsonProcessingException.class)
    public static String setOriginalTypeFqn(@Nullable String currentMeta, @NonNull String value) {
        var meta = getSwaggerMeta(currentMeta);
        meta.setOriginalTypeFqn(value);

        return OBJECT_MAPPER.writeValueAsString(meta);
    }

    @SneakyThrows(JsonProcessingException.class)
    public static String setIsIdentity(@Nullable String currentMeta, boolean value) {
        var meta = getSwaggerMeta(currentMeta);
        meta.setIsIdentity(!value ? null : true);

        return OBJECT_MAPPER.writeValueAsString(meta);
    }

    @SneakyThrows(JsonProcessingException.class)
    public static String setIsNestedStructure(@Nullable String currentMeta, boolean value) {
        var meta = getSwaggerMeta(currentMeta);
        meta.setIsNestedStructure(!value ? null : true);

        return OBJECT_MAPPER.writeValueAsString(meta);
    }

    private static SwaggerMeta getSwaggerMeta(String currentMeta) {
        var meta = new SwaggerMeta();
        if (currentMeta != null) {
            try {
                meta = OBJECT_MAPPER.readValue(currentMeta, SwaggerMeta.class);
            } catch (JsonProcessingException e) {
                log.warn("error processing meta for swagger: {}", currentMeta);
            }
        }
        return meta;
    }
}
