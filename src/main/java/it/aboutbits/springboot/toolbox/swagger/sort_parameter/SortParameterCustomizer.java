package it.aboutbits.springboot.toolbox.swagger.sort_parameter;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.StringSchema;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.customizers.OpenApiCustomizer;

@RequiredArgsConstructor
public class SortParameterCustomizer implements OpenApiCustomizer {
    private final Class<?> sortParameterSortFieldClass;

    @Override
    public void customise(OpenAPI openApi) {
        if (openApi.getPaths() != null) {
            for (var path : openApi.getPaths().entrySet()) {
                var pathItemOperations = path.getValue().readOperations();
                if (pathItemOperations == null) {
                    continue;
                }

                for (var operation : pathItemOperations) {
                    if (operation.getParameters() == null) {
                        continue;
                    }

                    for (var parameter : operation.getParameters()) {
                        if (parameter.getSchema() == null || parameter.getSchema().get$ref() == null) {
                            continue;
                        }

                        if (parameter.getSchema().get$ref().endsWith(".SortParameter")) {
                            parameter.required(false);
                            parameter.description(
                                    """
                                    Defines the sort order and if left empty the implementation specific default order will be used.<br>
                                    Also supports multiple order fields by specifying the order query parameter multiple times.<br>
                                    The format is as follows:<br><br>
                                    <code>\\<property\\>[:direction][:nullHandling]</code><br>
                                    <ul>
                                      <li>
                                        <code>property</code> is the field name you want to sort on the response Object<br>
                                      </li><br>
                                      <li>
                                        <code>direction</code> is the optional sort direction (case insensitive)
                                        <ul>
                                          <br>
                                          <li>
                                            <code>asc</code> (default)
                                          </li><br>
                                          <li>
                                            <code>desc</code>
                                          </li>
                                        </ul>
                                      </li><br>
                                      <li>
                                        <code>nullHandling</code> is the optional null handling strategy (case insensitive)<br>
                                        <ul>
                                          <br>
                                          <li>
                                            <code>native</code> (default)
                                          </li><br>
                                          <li>
                                            <code>first</code>
                                          </li><br>
                                          <li>
                                            <code>last</code>
                                          </li>
                                         </ul>
                                      </li>
                                    </ul>
                                    Examples:
                                    <ul>
                                      <li>
                                        <code>typeFamily</code> (uses the default sort of asc and native null handling)
                                      </li><br>
                                      <li>
                                        <code>typeFamily:desc</code> (sorts desc and uses the default native null handling)
                                      </li><br>
                                      <li>
                                        <code>typeFamily:asc:last</code> (sorts asc and uses the null handling strategy last)
                                      </li><br>
                                    </ul
                                    """
                            );
                            var itemSchema = new StringSchema()._default("property:asc:last");
                            itemSchema.setDescription(
                                    "{\"originalTypeFqn\": \"%s\"}".formatted(sortParameterSortFieldClass.getName())
                            );

                            parameter.setSchema(new ArraySchema().items(
                                    itemSchema
                            ));
                        }
                    }
                }
            }
        }
    }
}
