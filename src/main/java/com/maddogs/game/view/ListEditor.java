package com.maddogs.game.view;


import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

import java.util.ArrayList;

class ListEditor extends CustomField<ArrayList<String>> {
    VerticalLayout fields = new VerticalLayout();
    Button add = new Button("Add Answer", VaadinIcon.CHECK.create(), this::addItem);

    public ListEditor(String caption) {
        add(fields, add);
    }

    public void refresh() {
        remove(fields, add);
        fields = new VerticalLayout();
        add(fields, add);
        setListAsValue();
        refreshTextFields();
    }

    void setListAsValue() {
        ArrayList<String> list = getValue();
        if (list == null)
            list = new ArrayList<>();
        setValue(list);
    }

    void refreshTextFields() {
        for (String value : getValue()) {
            final TextField tf = new TextField();
            tf.setValue(value);
            tf.addValueChangeListener(valueChange -> {
                textFieldListenerMethod(tf);
            });
            fields.add(tf);
        }
    }

    void addItem(ClickEvent event) {
        setListAsValue();

        final TextField tf = new TextField();
        tf.addValueChangeListener(valueChange -> {
            textFieldListenerMethod(tf);
        });
        fields.add(tf);
    }

    void textFieldListenerMethod(TextField tf) {
        ArrayList<String> newList = getValue();
        newList.add(tf.getValue());
        setValue(newList);
    }

    @Override
    public ArrayList<String> getValue() {
        return super.getValue();
    }

    @Override
    protected ArrayList<String> generateModelValue() {
        return getValue();
    }

    @Override
    protected void setPresentationValue(ArrayList<String> strings) {

    }
}
