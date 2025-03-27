package it.aboutbits.springboot.toolbox.swagger;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SwaggerMeta {
    private String originalTypeFqn;
    @JsonProperty("isIdentity")
    private boolean isIdentity = false;
    @JsonProperty("isNestedStructure")
    private boolean isNestedStructure = false;
    @JsonProperty("isMap")
    private boolean isMap = false;
    private String mapKeyTypeFqn;
}
