package it.aboutbits.springboot.toolbox.swagger.customization.authorization_docs;

import io.swagger.v3.oas.models.Operation;
import it.aboutbits.springboot.toolbox.swagger.annotations.SwaggerScopedAuth;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.method.HandlerMethod;

import java.util.ArrayList;
import java.util.Optional;

@Slf4j
public class AuthorizationDescriptor implements OperationCustomizer {
    @Override
    public Operation customize(Operation operation, HandlerMethod handlerMethod) {
        try {
            var additionalDescription = new ArrayList<String>();

            var maybeAnnotation = Optional.ofNullable(handlerMethod.getMethodAnnotation(PreAuthorize.class));
            if (maybeAnnotation.isPresent()) {
                var annotation = maybeAnnotation.get();
                additionalDescription.add("<b>Authorization:</b> " + annotation.value());
            }

            var maybeAnnotation2 = Optional.ofNullable(handlerMethod.getMethodAnnotation(SwaggerScopedAuth.class));
            if (maybeAnnotation2.isPresent()) {
                var annotation = maybeAnnotation2.get();
                additionalDescription.add("<b>Scoped Authorization:</b> " + annotation.value());
            }

            if (!additionalDescription.isEmpty()) {

                var currentDescription = Optional.ofNullable(operation.getDescription());

                var description = String.join("<br />", additionalDescription);
                if (currentDescription.isPresent()) {
                    description = "<p>" + description + "</p>" + currentDescription.get();
                }

                operation.description(
                        description
                );
            }
        } catch (Exception e) {
            log.error("Error when creating swagger documentation for authorities.", e);
        }
        return operation;
    }
}
