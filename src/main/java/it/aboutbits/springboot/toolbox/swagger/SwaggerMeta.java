package it.aboutbits.springboot.toolbox.swagger;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.jspecify.annotations.NullUnmarked;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@NullUnmarked
public class SwaggerMeta {
    private String originalTypeFqn = null;
    @JsonProperty("isIdentity")
    private Boolean isIdentity = null;
    @JsonProperty("isCustomType")
    private Boolean isCustomType = null;
    @JsonProperty("isNestedStructure")
    private Boolean isNestedStructure = null;
    @JsonProperty("isMap")
    private Boolean isMap = null;
    private String mapKeyTypeFqn = null;
}
