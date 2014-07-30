package org.vaadin.teemu.clara.inflater.handler;

import com.vaadin.ui.Component;
import java.util.Map;

public interface AttributeHandler {

    enum Phase {

        BEFORE_ATTACH, AFTER_ATTACH
    };

    /**
     * Returns the namespace of attributes this AttributeHandler is
     * interested in.
     *
     * @return
     */
    String getNamespace();

    /**
     * Returns the Phase when this handler want to run.
     *
     * @return
     */
    Phase getPhase();

    /**
     * Assigns the given attributes to the given {@link Component}.
     *
     * @param component
     * @param attributes
     */
    void assignAttributes(Component component, Map<String, String> attributes);

}
