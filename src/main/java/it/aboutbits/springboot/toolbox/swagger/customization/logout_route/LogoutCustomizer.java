package it.aboutbits.springboot.toolbox.swagger.customization.logout_route;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.customizers.OpenApiCustomizer;

@RequiredArgsConstructor
public class LogoutCustomizer implements OpenApiCustomizer {
    private final String logoutUrl;

    @Override
    public void customise(OpenAPI openApi) {
        var operation = new Operation();
        operation.addTagsItem("Authentication API");
        operation.summary("Logout the current user");
        operation.responses(new ApiResponses());

        var pathItem = new PathItem();
        pathItem.setPost(operation);

        openApi.getPaths().addPathItem(logoutUrl, pathItem);
    }
}
