package it.aboutbits.springboot.toolbox.web;

import it.aboutbits.springboot.toolbox.type.identity.EntityId;
import org.springframework.lang.Nullable;

import java.beans.PropertyEditorSupport;

public final class EntityIdPropertyEditor extends PropertyEditorSupport {
    @SuppressWarnings("unchecked")
    @Override
    @Nullable
    public String getAsText() {
        var value = (EntityId<?>) getValue();

        return value == null ? null : value.toString();
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        EntityId<?> value = null;
        try {
            var theValue = Long.parseLong(text);
            value = new EntityId<Long>() {
                @Override
                public Long value() {
                    return theValue;
                }

                @Override
                public String toString() {
                    return String.valueOf(value());
                }
            };
        } catch (NumberFormatException e) {
            value = new EntityId<String>() {
                @Override
                public String value() {
                    return text;
                }

                @Override
                public String toString() {
                    return value();
                }
            };
        }

        setValue(
                value
        );
    }
}
