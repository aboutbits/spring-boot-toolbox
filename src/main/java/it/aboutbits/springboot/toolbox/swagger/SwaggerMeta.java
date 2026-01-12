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
    @Nullable
    private String originalTypeFqn = null;

    @Nullable
    @JsonProperty("isIdentity")
    private Boolean isIdentity = null;

    @Nullable
    @JsonProperty("isCustomType")
    private Boolean isCustomType = null;

    @Nullable
    @JsonProperty("isNestedStructure")
    private Boolean isNestedStructure = null;

    @Nullable
    @JsonProperty("isMap")
    private Boolean isMap = null;

    @Nullable
    private String mapKeyTypeFqn = null;
}
