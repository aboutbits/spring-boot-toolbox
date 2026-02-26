package it.aboutbits.springboot.toolbox.swagger;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

// JsonProperty on boolean fields is required or it will interpret the name as a getter and remove the "is".
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@NullMarked
public class SwaggerMeta {
    private @Nullable String originalTypeFqn = null;

    @JsonProperty("isIdentity")
    private @Nullable Boolean isIdentity = null;

    @JsonProperty("isCustomType")
    private @Nullable Boolean isCustomType = null;

    @JsonProperty("isNestedStructure")
    private @Nullable Boolean isNestedStructure = null;

    @JsonProperty("isMap")
    private @Nullable Boolean isMap = null;

    private @Nullable String mapKeyTypeFqn = null;

    @JsonProperty("isNullable")
    private @Nullable Boolean isNullable = null;
}
