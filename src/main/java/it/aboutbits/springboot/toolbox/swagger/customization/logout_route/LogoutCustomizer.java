package it.aboutbits.springboot.toolbox.swagger.customization.logout_route;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.responses.ApiResponses;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.customizers.OpenApiCustomizer;

@RequiredArgsConstructor
public class LogoutCustomizer implements OpenApiCustomizer {
    @NonNull
    private final String logoutUrl;
    @NonNull
    private final String tag;

    @Override
    public void customise(OpenAPI openApi) {
        var operation = new Operation();
        operation.addTagsItem(tag);
        operation.summary("Logout the current user");
        operation.operationId("logout");
        operation.responses(new ApiResponses());

        var pathItem = new PathItem();
        pathItem.setPost(operation);

        openApi.getPaths().addPathItem(logoutUrl, pathItem);
    }
}
