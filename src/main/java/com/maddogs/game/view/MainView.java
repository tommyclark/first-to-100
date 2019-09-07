package com.maddogs.game.view;

import com.maddogs.game.model.Question;
import com.maddogs.game.repository.QuestionRepository;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.ArrayList;

@Route("admin")
public class MainView extends VerticalLayout {

    private final QuestionRepository repo;

    private final QuestionEditor editor;

    final Grid<Question> grid;

    final TextField filter;

    private final Button addNewBtn;


    @Autowired
    public MainView(QuestionRepository repo, QuestionEditor editor) {
        this.repo = repo;
        this.editor = editor;
        this.grid = new Grid<>(Question.class);
        this.filter = new TextField();
        this.addNewBtn = new Button("New question", VaadinIcon.PLUS.create());

        // build layout
        HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn);
        add(actions, grid, editor);

        grid.setHeight("300px");
        grid.setColumns("question", "answers");

        grid.getColumnByKey("question").setResizable(true);
        grid.getColumnByKey("answers").setResizable(true);

        filter.setPlaceholder("Filter by question");

        // Replace listing with filtered content when user changes filter
        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filter.addValueChangeListener(e -> listQuestions(e.getValue()));

        // Connect selected Question to editor or hide if none is selected
        grid.asSingleSelect().addValueChangeListener(e -> {
            editor.editQuestion(e.getValue());
        });

        // Instantiate and edit new Question the new button is clicked
        addNewBtn.addClickListener(e -> editor.editQuestion(new Question( "", new ArrayList<>())));

        // Listen changes made by the editor, refresh data from backend
        editor.setChangeHandler(() -> {
            editor.setVisible(false);
            listQuestions(filter.getValue());
        });

        // Initialize listing
        listQuestions(null);
    }

    void listQuestions(String filterText) {
        if (StringUtils.isEmpty(filterText)) {
            grid.setItems(repo.findAll());
        }
        else {
            grid.setItems(repo.findByQuestion(filterText));
        }
    }

}