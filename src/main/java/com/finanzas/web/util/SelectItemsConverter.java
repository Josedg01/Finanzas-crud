package com.finanzas.web.util;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import jakarta.faces.model.SelectItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Converter that works with <f:selectItems> where itemValue is the entity.
 * It finds the selected entity by comparing the String id with each select item.
 *
 * Requirement: entities must have a public getId(): Long method.
 */
@FacesConverter(value = "selectItemsConverter", managed = true)
public class SelectItemsConverter implements Converter<Object> {

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) return "";
        try {
            Object id = value.getClass().getMethod("getId").invoke(value);
            return id == null ? "" : String.valueOf(id);
        } catch (Exception e) {
            return "";
        }
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isBlank()) return null;

        List<SelectItem> items = new ArrayList<>();
        collectSelectItems(component, items);

        for (SelectItem it : items) {
            Object obj = it.getValue();
            if (obj == null) continue;
            try {
                Object id = obj.getClass().getMethod("getId").invoke(obj);
                if (id != null && value.equals(String.valueOf(id))) {
                    return obj;
                }
            } catch (Exception ignored) {
            }
        }
        return null;
    }

    private void collectSelectItems(UIComponent component, List<SelectItem> out) {
        for (UIComponent child : component.getChildren()) {
            Object val = child.getAttributes().get("value");
            if (val instanceof SelectItem) {
                out.add((SelectItem) val);
            } else if (val instanceof List) {
                // might be List<SelectItem>
                for (Object o : (List<?>) val) {
                    if (o instanceof SelectItem) out.add((SelectItem) o);
                }
            }
            collectSelectItems(child, out);
        }
    }
}
