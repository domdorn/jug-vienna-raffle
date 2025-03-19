package com.vaadin.demo.application.views.spinwheel.component;

import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.react.ReactAdapterComponent;

import java.util.List;

@NpmPackage(value = "react-spin-wheel", version = "0.0.7")
@JsModule("./components/react-spin-wheel/react-spin-wheel.tsx")
@Tag("react-spin-wheel")
public class ReactSpinWheel extends ReactAdapterComponent implements HasSize {

    public void setItems(List<String> itemList) {
        setState("items", itemList);
    }

    public void addOnFinishSpin() {
        getElement().addEventListener("onFinishSpin", event -> {
            System.out.println(event.getEventData());
        }).addEventData("event.detail");
    }

}
