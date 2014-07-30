package org.vaadin.teemu.clara.inflater.handler;

import static org.vaadin.teemu.clara.util.ReflectionUtils.findMethods;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.vaadin.teemu.clara.inflater.filter.AttributeFilter;
import org.vaadin.teemu.clara.inflater.parser.AttributeParser;
import org.vaadin.teemu.clara.util.AnyClassOrPrimitive;

import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;

public class LayoutAttributeHandler extends DefaultAttributeHandler {

    private static final String LAYOUT_ATTRIBUTE_NAMESPACE = "urn:vaadin:parent";

    public LayoutAttributeHandler(List<AttributeFilter> attributeFilters) {
        super(attributeFilters);
    }

    @Override
    public String getNamespace() {
        return LAYOUT_ATTRIBUTE_NAMESPACE;
    }

    @Override
    public Phase getPhase() {
        return Phase.AFTER_ATTACH;
    }

    @Override
    public void assignAttributes(Component component,
            Map<String, String> attributes) {
        if (attributes.isEmpty()) {
            return;
        }

        if (!(component.getParent() instanceof ComponentContainer)) {
            throw new IllegalStateException(
                    "The given Component must be attached to a ComponentContainer.");
        }

        ComponentContainer container = (ComponentContainer) component
                .getParent();
        try {
            for (Map.Entry<String, String> attribute : attributes.entrySet()) {
                Method writeMethod = getWriteMethod(attribute.getKey(),
                        container.getClass());
                if (writeMethod != null) {
                    AttributeParser parser = getParserFor(writeMethod
                            .getParameterTypes()[1]);
                    if (parser != null) {
                        invokeWithAttributeFilters(writeMethod, container,
                                component, parser.getValueAs(
                                        attribute.getValue(),
                                        writeMethod.getParameterTypes()[1],
                                        component));
                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new AttributeHandlerException(e);
        } catch (InvocationTargetException e) {
            throw new AttributeHandlerException(e);
        }
    }

    @Override
    protected Method getWriteMethod(String propertyName,
            Class<? extends Component> layoutClass) {
        // We need the first parameter to be a Component, the other one can be
        // anything.
        Class<?>[] expectedParamTypes = { Component.class,
                AnyClassOrPrimitive.class };

        List<Method> layoutSetters = findMethods(layoutClass,
                getWriteMethodName(propertyName), expectedParamTypes);
        return getPreferredMethod(layoutSetters);

    }

}
