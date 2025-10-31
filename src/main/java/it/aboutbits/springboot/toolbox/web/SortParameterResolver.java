package it.aboutbits.springboot.toolbox.web;

import it.aboutbits.springboot.toolbox.parameter.SortParameter;
import lombok.NonNull;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.ArrayList;
import java.util.Objects;

public class SortParameterResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return SortParameter.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(
            @NonNull MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) throws IllegalArgumentException {
        var sorts = webRequest.getParameterValues(
                Objects.requireNonNull(parameter.getParameterName())
        );

        // Allow empty order list
        if (sorts == null) {
            return SortParameter.unsorted();
        }

        var sortFields = new ArrayList<SortParameter.SortField>(sorts.length);
        for (var order : sorts) {
            var parts = order.split(":");
            if (parts.length == 1) {
                sortFields.add(new SortParameter.SortField(
                        parts[0],
                        org.springframework.data.domain.Sort.DEFAULT_DIRECTION,
                        org.springframework.data.domain.Sort.NullHandling.NATIVE
                ));
            } else if (parts.length == 2) {
                sortFields.add(new SortParameter.SortField(
                        parts[0],
                        org.springframework.data.domain.Sort.Direction.fromString(parts[1]),
                        org.springframework.data.domain.Sort.NullHandling.NATIVE
                ));
            } else if (parts.length == 3) {
                sortFields.add(new SortParameter.SortField(
                        parts[0],
                        org.springframework.data.domain.Sort.Direction.fromString(parts[1]),
                        nullHandlingFromString(parts[2])
                ));
            } else {
                throw new IllegalArgumentException(
                        "Invalid sort order, only a maximum of two semicolons are allowed in format <property>[:asc|desc][:native|first|last] -> Your input " + order
                );
            }
        }

        return new SortParameter<>(sortFields);
    }

    private org.springframework.data.domain.Sort.NullHandling nullHandlingFromString(@NonNull String value) {
        return switch (value.toLowerCase()) {
            case "first":
                yield org.springframework.data.domain.Sort.NullHandling.NULLS_FIRST;
            case "last":
                yield org.springframework.data.domain.Sort.NullHandling.NULLS_LAST;
            case "native":
                yield org.springframework.data.domain.Sort.NullHandling.NATIVE;
            default:
                throw new IllegalArgumentException(
                        "Only native, first or last are allowed as null handling strategy -> Your input " + value
                );
        };
    }
}
