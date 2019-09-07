package com.maddogs.game.view;

import com.maddogs.game.model.Question;
import com.maddogs.game.repository.QuestionRepository;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
@UIScope
public class QuestionEditor extends VerticalLayout implements KeyNotifier {

    private final QuestionRepository repository;

    /**
     * The currently edited question
     */
    private Question questionObj;

    /* Fields to edit properties in Question entity */
    private TextField question = new TextField("Question");
    private ListEditor answers = new ListEditor("Answers");

    /* Action buttons */
    private Button save = new Button("Save", VaadinIcon.CHECK.create());
    private Button cancel = new Button("Cancel");
    private Button delete = new Button("Delete", VaadinIcon.TRASH.create());
    private HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);

    private Binder<Question> binder = new Binder<>(Question.class);
    private ChangeHandler changeHandler;

    @Autowired
    public QuestionEditor(QuestionRepository repository) {
        this.repository = repository;

        add(question, answers, actions);

        // bind using naming convention
        binder.bindInstanceFields(this);

        // Configure and style components
        setSpacing(true);

        save.getElement().getThemeList().add("primary");
        delete.getElement().getThemeList().add("error");

        addKeyPressListener(Key.ENTER, e -> save());

        // wire action buttons to save, delete and reset
        save.addClickListener(e -> save());
        delete.addClickListener(e -> delete());
        cancel.addClickListener(e -> editQuestion(questionObj));
        setVisible(false);
    }

    void delete() {
        repository.delete(questionObj);
        changeHandler.onChange();
    }

    void save() {
        repository.save(questionObj);
        changeHandler.onChange();
    }

    public interface ChangeHandler {
        void onChange();
    }

    public final void editQuestion(Question c) {
        if (c == null) {
            setVisible(false);
            return;
        }
        final boolean persisted = c.getQuestionId() != null;
        if (persisted) {
            // Find fresh entity for editing
            questionObj = repository.findById(c.getQuestionId()).get();
        }
        else {
            questionObj = c;
        }
        cancel.setVisible(persisted);

        answers.clear();
        answers.setValue(questionObj.getAnswers());
        answers.refresh();

        // Bind question properties to similarly named fields
        binder.setBean(questionObj);
        binder.bindInstanceFields(this);

        setVisible(true);

        question.focus();
    }

    public void setChangeHandler(ChangeHandler h) {
        // ChangeHandler is notified when either save or delete is clicked
        changeHandler = h;
    }

}