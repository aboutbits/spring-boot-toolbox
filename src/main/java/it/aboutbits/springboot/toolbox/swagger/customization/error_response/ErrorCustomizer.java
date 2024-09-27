package it.aboutbits.springboot.toolbox.swagger.customization.error_response;

import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import it.aboutbits.springboot.toolbox.mvc.response.ErrorResponse;
import org.springdoc.core.customizers.OpenApiCustomizer;

import java.util.Map;

public class ErrorCustomizer implements OpenApiCustomizer {
    @Override
    public void customise(OpenAPI openApi) {
        openApi.getComponents()
                .getSchemas()
                .putAll(
                        ModelConverters.getInstance().read(ErrorResponse.class)
                );

        var errorResponseSchema = openApi.getComponents().getSchemas().get("ErrorResponse");
        @SuppressWarnings("unchecked")
        Map<String, Schema<?>> props = errorResponseSchema.getProperties();
        for (var prop : props.values()) {
            prop.nullable(true);
        }

        openApi.getPaths()
                .values()
                .forEach(
                        pathItem -> pathItem.readOperations()
                                .forEach(
                                        operation -> {
                                            ApiResponses apiResponses = operation.getResponses();
                                            apiResponses.addApiResponse(
                                                    "400",
                                                    createApiResponse(
                                                            "Bad Request",
                                                            errorResponseSchema
                                                    )
                                            );
                                            apiResponses.addApiResponse(
                                                    "404",
                                                    createApiResponse(
                                                            "Not Found",
                                                            errorResponseSchema
                                                    )
                                            );
                                        }
                                )
                );
    }

    private ApiResponse createApiResponse(String message, Schema<?> schema) {
        var mediaType = new MediaType();
        mediaType.schema(schema);
        return new ApiResponse().description(message)
                .content(new Content().addMediaType(
                        org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
                        mediaType
                ));
    }
}
