package org.vaadin.teemu.clara.demo;

import java.util.Date;

import com.vaadin.data.HasValue;
import org.vaadin.teemu.clara.binder.annotation.UiDataSource;
import org.vaadin.teemu.clara.binder.annotation.UiHandler;

import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.util.IndexedContainer;
import com.vaadin.v7.data.util.ObjectProperty;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification;

public class DemoController {

    @UiDataSource("date")
    public Property<Date> getDateProperty() {
        return new ObjectProperty<Date>(new Date());
    }

    @SuppressWarnings("unchecked")
    @UiDataSource("person-list")
    public Container getPersonContainer() {
        IndexedContainer container = new IndexedContainer();
        container.addContainerProperty("Name", String.class, "");
        container.addContainerProperty("Age", Integer.class, 0);

        Object itemId = container.addItem();
        container.getItem(itemId).getItemProperty("Name")
                .setValue("Teemu Pöntelin");
        container.getItem(itemId).getItemProperty("Age").setValue(32);

        itemId = container.addItem();
        container.getItem(itemId).getItemProperty("Name")
                .setValue("John Smith");
        container.getItem(itemId).getItemProperty("Age").setValue(35);
        return container;
    }

    @UiHandler("button")
    public void handleButtonClick(ClickEvent event) {
        Notification.show("Button \"button\" clicked");
    }

    @UiHandler("another-button")
    public void handleAnotherButtonClick(ClickEvent event) {
        Notification.show("Button \"another-button\" clicked");
    }

    @UiHandler("value-field")
    public void someValueChanged(HasValue.ValueChangeEvent<Double> event) {
        Notification.show("Value of \"value-field\" is now "
                + event.getValue());
    }

}
