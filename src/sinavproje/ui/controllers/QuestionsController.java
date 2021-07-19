package sinavproje.ui.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import sinavproje.Main;
import sinavproje.classes.question.*;
import sinavproje.classes.other.Choice;
import sinavproje.types.QuestionSortType;
import sinavproje.types.QuestionType;

import java.util.*;

public class QuestionsController {
    @FXML private Label questionTextLabel;
    @FXML private Label questionAnswers;
    @FXML private Label questionType, questionDifficulty, questionPoint, questionId;
    @FXML private Button questionDeleteButton, questionDetailsHide;
    @FXML private TextField searchText, searchChoiceText, searchPoint;
    @FXML private ComboBox searchType, searchTrueChoice, searchDifficulty, searchOrder;
    @FXML private TableView questionsTable;

    private boolean statusDetails = true;
    private int selectedId = 0;
    private QuestionSortType sortType = QuestionSortType.ID_ASC;

    public void initialize()
    {
        tableinit();
        hideDetails();
        Main.controllers.setQuestionsController(this);
        searchOrder.valueProperty().addListener((o, oldValue, newValue) -> {
            int i = searchOrder.getSelectionModel().getSelectedIndex();
            sortType = sortType.values()[i];
            search();
        });
        questionsTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {
                if(t1 != null) {
                    int id = Integer.parseInt(((QuestionTableItem) t1).getId());
                    Question q = Main.data.getQuestionById(id);
                    if (q != null) {
                        questionTextLabel.setText(q.getText());
                        questionAnswers.setText(answerString(q));
                        questionType.setText("TİP: " + q.getTypeText());
                        questionDifficulty.setText("Zorluk: " + q.getDifficultyText());
                        questionPoint.setText("Puan: " + q.getPoint());
                        questionId.setText("ID: " + q.getId());
                        showDetails();
                        selectedId = q.getId();
                    }
                }
            }
        });
        questionDetailsHide.setOnAction(e -> {
            questionsTable.getSelectionModel().select(null);
            questionsTable.requestFocus();
            hideDetails();
            selectedId = 0;
        });
        questionDeleteButton.setOnAction(e -> {
            int tmp = selectedId;
            int previousOption = questionsTable.getSelectionModel().getSelectedIndex() - 1;
            Main.data.deleteQuestionById(tmp);
            update();
            if(previousOption >= 0) {
                questionsTable.requestFocus();
                questionsTable.getFocusModel().focus(previousOption);
                questionsTable.getSelectionModel().select(previousOption);
            } else {
                questionsTable.getSelectionModel().select(null);
                questionsTable.requestFocus();
                hideDetails();
                selectedId = 0;
            }
        });
        searchText.textProperty().addListener((observable, oldValue, newValue) -> search());
        searchChoiceText.textProperty().addListener((observable, oldValue, newValue) -> search());
        searchPoint.textProperty().addListener((observable, oldValue, newValue) -> search());
        searchPoint.setOnAction(e -> search());
        searchType.setOnAction(e -> choicesUpdate());
        searchTrueChoice.setOnAction(e -> search());
        searchDifficulty.setOnAction(e -> search());
    }

    public void showDetails()
    {
        if(statusDetails) return;
        questionTextLabel.setVisible(true);
        questionAnswers.setVisible(true);
        questionDeleteButton.setVisible(true);
        questionType.setVisible(true);
        questionDifficulty.setVisible(true);
        questionPoint.setVisible(true);
        questionDetailsHide.setVisible(true);
        questionId.setVisible(true);
        statusDetails = true;
    }

    public void hideDetails()
    {
        if(!statusDetails) return;
        questionTextLabel.setVisible(false);
        questionAnswers.setVisible(false);
        questionDeleteButton.setVisible(false);
        questionType.setVisible(false);
        questionDifficulty.setVisible(false);
        questionPoint.setVisible(false);
        questionDetailsHide.setVisible(false);
        questionId.setVisible(false);
        statusDetails = false;
    }
    private String answerString(Question q)
    {
        String text = "";
        if(q.getType() == QuestionType.CHOICE) {
            char choice_char = 'A';
            for(Choice choice : ((QuestionChoice)q).getChoices())
            {
                if(choice.isFree()) continue;
                text += (choice_char++) + "- " + choice.getChoiceText();
                if(choice.isTrue()) text += " (Doğru Cevap)";
                text += "\n";
            }
            return text;
        }
        else if(q.getType() == QuestionType.TRUEFALSE)
        {
            return "Doğru/Yanlış\n\nCevap: " + (((QuestionTrueFalse) q).isTrue() ? "Doğru" : "Yanlış");
        }
        else if(q.getType() == QuestionType.GAPFILLING)
        {
            return "Cevap: " + q.getAnswer();
        }
        return "";
    }

    public void update()
    {
        List<Question> tmp = Main.data.getQuestions();
        sortQuestions(tmp);
        //Collections.sort(tmp);
        questionsTable.getItems().clear();
        for(Question q : tmp)
        {
            questionsTable.getItems().add(new QuestionTableItem(q.getId(), q.getText(), q.getAnswerFull(), q.getDifficultyText(), q.getPoint(), q.getTypeText()));
        }
    }

    private List<Question> sortQuestions(List<Question> items) {
        Collections.sort(items, new QuestionComparator(sortType));
        return items;
    }

    public void update(List<Question> qList)
    {
        questionsTable.getItems().clear();
        sortQuestions(qList);
        //Collections.sort(qList);
        for(Question q : qList)
        {
            questionsTable.getItems().add(new QuestionTableItem(q.getId(), q.getText(), q.getAnswerFull(), q.getDifficultyText(), q.getPoint(), q.getTypeText()));
        }
    }

    private void search()
    {
        hideDetails();
        String text = searchText.getText().trim().toLowerCase();
        String choicetext = searchChoiceText.getText();
        String typetext = (searchType.getSelectionModel().getSelectedItem() == null) ? "" : searchType.getSelectionModel().getSelectedItem().toString();
        String answerchoice = (searchTrueChoice.getSelectionModel().getSelectedItem() == null) ? "" : searchTrueChoice.getSelectionModel().getSelectedItem().toString();
        String difficultytext = (searchDifficulty.getSelectionModel().getSelectedItem() == null) ? "" : searchDifficulty.getSelectionModel().getSelectedItem().toString();
        String pointtext = searchPoint.getText();
        update(Main.data.search(text, choicetext, (typetext.equals("Tip") ? new String[]{} : new String[]{typetext}), difficultytext, pointtext, answerchoice));
    }

    private void choicesUpdate() {
        if(searchType.getValue().equals("Çoktan Seçmeli") || searchType.getValue().equals("Doğru/Yanlış")) {
            searchTrueChoice.setDisable(false);
            ObservableList<String> data = FXCollections.observableArrayList();
            data.add("Doğru Şık");
            if (searchType.getValue().equals("Çoktan Seçmeli"))
                data.addAll("A", "B", "C", "D", "E");
            else
                data.addAll("Doğru", "Yanlış");

            searchTrueChoice.getItems().clear();
            searchTrueChoice.getItems().addAll(data);
            searchTrueChoice.getSelectionModel().select(0);

        }
        else {
            searchTrueChoice.setDisable(true);
            searchTrueChoice.getItems().clear();
            searchTrueChoice.getItems().add("Doğru Şık");
            searchTrueChoice.getSelectionModel().select(0);
        }
        search();
    }

    private void tableinit() {
        questionsTable.getColumns().clear();
        TableColumn idColumn = new TableColumn("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        idColumn.setSortable(false);
        idColumn.setMaxWidth(30.0);
        idColumn.setMinWidth(30.0);
        idColumn.setStyle("-fx-alignment: CENTER;");

        TableColumn textColumn = new TableColumn("Soru İçeriği");
        textColumn.setCellValueFactory(new PropertyValueFactory<>("text"));
        textColumn.setSortable(false);
        textColumn.setMinWidth(470.0);

        TableColumn answerColumn = new TableColumn("Cevap");
        answerColumn.setCellValueFactory(new PropertyValueFactory<>("answer"));
        answerColumn.setSortable(false);
        answerColumn.setMinWidth(160.0);
        answerColumn.setMaxWidth(160.0);

        TableColumn diffColumn = new TableColumn("Zorluk");
        diffColumn.setCellValueFactory(new PropertyValueFactory<>("difficulty"));
        diffColumn.setSortable(false);
        diffColumn.setMaxWidth(60.0);
        diffColumn.setMinWidth(60.0);
        diffColumn.setStyle("-fx-alignment: CENTER;");

        TableColumn pointColumn = new TableColumn("Puan");
        pointColumn.setCellValueFactory(new PropertyValueFactory<>("point"));
        pointColumn.setSortable(false);
        pointColumn.setMaxWidth(40.0);
        pointColumn.setMinWidth(40.0);
        pointColumn.setStyle("-fx-alignment: CENTER;");

        TableColumn typeColumn = new TableColumn("Tip");
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        typeColumn.setSortable(false);
        typeColumn.setMaxWidth(100.0);
        typeColumn.setMinWidth(100.0);
        typeColumn.setStyle("-fx-alignment: CENTER;");

        questionsTable.getColumns().addAll(idColumn, textColumn, answerColumn, diffColumn, pointColumn, typeColumn);

        questionsTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
    }
}
