package sinavproje.ui.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sinavproje.Main;
import sinavproje.classes.exam.*;
import sinavproje.classes.other.Choice;
import sinavproje.classes.other.EasyMsg;
import sinavproje.classes.question.Question;
import sinavproje.classes.question.QuestionChoice;
import sinavproje.types.ExamType;
import sinavproje.types.QuestionType;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ExamController {
    @FXML private Button submitNewExam, nextButton, previousButton, finishButton, closeExam, exportExam;
    @FXML private Label newExamMessage, counter, examName, examPoint, examTrueFalseBlank;
    @FXML private TextField optionExamName;
    @FXML private ToggleGroup examType, questionChoice;
    @FXML private VBox nExam, sExamChoose, sExamClassic, sExamFinish;
    @FXML private GridPane examButtons;
    @FXML private AnchorPane newExamPane;
    @FXML private Label optionA, optionB, optionC, optionD, optionE, questionText, questionLabel;
    @FXML private RadioButton radioA, radioB, radioC, radioD, radioE, radioNA;
    @FXML private Label labelA, labelB, labelC, labelD, labelE;
    @FXML private Label questionTextClassic, questionLabelClassic;
    @FXML private TextArea questionClassicInput;
    @FXML private TextField questionGapFillingInput;
    @FXML private Label questionTextGapFilling, questionLabelGapFilling;
    @FXML private VBox sExamGapFilling;

    private EasyMsg eMsg;
    private UserExam user;

    public void initialize() {
        eMsg = new EasyMsg(newExamMessage);
        submitNewExam.setOnAction(e -> submit());
        nextButton.setOnAction(e -> next());
        previousButton.setOnAction(e -> previous());
        questionChoice.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observableValue, Toggle toggle, Toggle t1) {
                saveChoice(t1);
            }
        });
        questionClassicInput.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                saveChoice(t1);
            }
        });
        questionGapFillingInput.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                saveChoice(t1);
            }
        });
        finishButton.setOnAction(e -> finishExam());
        closeExam.setOnAction(e -> closeExam());
        exportExam.setOnAction(e -> exportExam());
    }

    private void exportExam() {
        Stage stage = (Stage) newExamPane.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("S??nav ????z??m??n?? D????a Aktar");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home") + "/Desktop"));
        fileChooser.setInitialFileName(user.getExam().getName() + " - ????z??mler.txt");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter(".txt", "*.txt"));
        File selectedFile = fileChooser.showSaveDialog(stage);
        if(selectedFile != null) {
            user.getExam().exportExam(user, selectedFile);
        }
    }

    private void next() {
        if(user.hasNext()) showQuestion(user.next());
    }

    private void previous() {
        if(user.hasPrevious()) showQuestion(user.previous());
    }

    private void saveChoice(String s) {
        user.setAnswer(user.getCurrentIndex(), s);
    }

    private void saveChoice(Toggle t1) {
        RadioButton selected = ((RadioButton)t1);
        List<String> ids = Arrays.asList("radioA","radioB","radioC","radioD","radioE");
        if(selected.getId().contains("radioNA")) {
            user.setAnswer(user.getCurrentIndex(), "");
        } else {
            user.setAnswer(user.getCurrentIndex(), String.format("%d", ids.indexOf(selected.getId())));
        }
    }

    private void finishExam() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("S??nav Biti?? Onay??");
        alert.setHeaderText("S??nav?? bitirmek ??zeresiniz. Geri d??n?????? yoktur.");
        alert.setContentText("Bunu yapmak istedi??inizden emin misiniz?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            sExamChoose.setVisible(false);
            sExamClassic.setVisible(false);
            sExamGapFilling.setVisible(false);
            sExamFinish.setVisible(true);
            examButtons.setVisible(false);
            int total = user.getExam().getTotalPoint();
            int[] data = user.getExamPoints();
            ExamType t = user.getExam().getType();
            if(t == ExamType.TEST) {
                examPoint.setText(String.format("S??nav Puan??n??z: %d/%d", data[0], total));
                examTrueFalseBlank.setText(String.format("Do??ru/Yanl????/Bo??: %d/%d/%d", data[1], data[2], data[3]));
            }
            else if(t == ExamType.CLASSIC) {
                examPoint.setText(String.format("S??nav Puan??n??z: %d/%d", data[0], total));
                examTrueFalseBlank.setText(String.format("Do??ru/Yanl????/Bo??: %d/%d/%d\n(klasik sorular hari??tir)", data[1], data[2], data[3]));
            }
            else if(t == ExamType.MIXED) {
                examPoint.setText(String.format("S??nav Puan??n??z: %d/%d", data[0], total));
                examTrueFalseBlank.setText(String.format("Do??ru/Yanl????/Bo??: %d/%d/%d\n(klasik sorular hari??tir)", data[1], data[2], data[3]));
            }
        }
    }

    private void closeExam() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("S??nav Sonu?? ????k???? Onay??");
        alert.setHeaderText("S??nav sonucunu kapatmak ??zeresiniz.");
        alert.setContentText("Bunu yapmak istedi??inizden emin misiniz?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            closeExam.setText("S??nav?? Kapat");
            nExam.setVisible(true);
            sExamFinish.setVisible(false);
        }
    }

    private void submit() {
        eMsg.hide();
        String examName = optionExamName.getText().trim();
        if(examName.isEmpty()) eMsg.showError("S??nav ad??n?? giriniz!");
        else if(Main.data.getQuestions().size() == 0) eMsg.showError("Eklenmi?? hi?? soru yok, s??nav olu??turulam??yor!");
        else {
            String examTypeStr = ((RadioButton)examType.getSelectedToggle()).getId();
            Exam a = null;
            if(examTypeStr.equals("optionExamTest")) {
                a = new ExamTest(examName, Main.data.getQuestions());
                user = new UserExam((ExamTest) a);
            }
            else if(examTypeStr.equals("optionExamClassic")) {
                a = new ExamClassic(examName, Main.data.getQuestions());
                user = new UserExam((ExamClassic) a);
            }
            else if(examTypeStr.equals("optionExamMixed")) {
                a = new ExamMixed(examName, Main.data.getQuestions());
                user = new UserExam((ExamMixed) a);
            }
            if(a == null || user == null || !user.hasNext()) {
                a = null;
                user = null;
                eMsg.showError("S??nav olu??turulamad??!\r\nUygun say??da ya da yeterli puanda soru bulunamad??!");
            }
            else {
                createSolveExam();
            }
        }
    }

    private void createSolveExam() {
        this.examName.setText("S??nav Ad??: " + this.user.getExam().getName());
        nExam.setVisible(false);
        Question q = user.next();
        showQuestion(q);
        optionExamName.setText("");
        examButtons.setVisible(true);
    }

    private void showQuestion(Question q) {
        if(q.getType() == QuestionType.CHOICE || q.getType() == QuestionType.TRUEFALSE) {
            this.showChoosableQuestion(q);
        }
        else if(q.getType() == QuestionType.CLASSIC) {
            this.showClassicQuestion(q);
        }
        else if(q.getType() == QuestionType.GAPFILLING) {
            this.showGapFillingQuestion(q);
        }
    }

    private void showGapFillingQuestion(Question q) {
        questionTextGapFilling.setText(q.getText());
        counter.setText((user.getExam().getQuestions().indexOf(q) + 1) + "/" + user.getExam().getQuestions().size());
        questionLabelGapFilling.setText(String.format("Soru %d)", user.getExam().getQuestions().indexOf(q) + 1));
        String answer = user.getAnswer(user.getCurrentIndex());
        questionGapFillingInput.setText(answer);
        setQuestionType(2);
    }

    private void showClassicQuestion(Question q) {
        questionTextClassic.setText(q.getText());
        counter.setText((user.getExam().getQuestions().indexOf(q) + 1) + "/" + user.getExam().getQuestions().size());
        questionLabelClassic.setText(String.format("Soru %d)", user.getExam().getQuestions().indexOf(q) + 1));
        String answer = user.getAnswer(user.getCurrentIndex());
        questionClassicInput.setText(answer);
        setQuestionType(1);
    }

    private void showChoosableQuestion(Question q) {
        Label[] choiceLabels = {optionA, optionB, optionC, optionD, optionE};
        Label[] choiceLtLabels = {labelA, labelB, labelC, labelD, labelE};
        RadioButton[] rbuttons = {radioA, radioB, radioC, radioD, radioE};
        for (Choice c : ((QuestionChoice) q).getChoices()) {
            int i = ((QuestionChoice) q).getChoices().indexOf(c);
            String text = c.getChoiceText();
            if(!c.isFree()) {
                rbuttons[i].setVisible(true);
                choiceLtLabels[i].setVisible(true);
                choiceLabels[i].setText(text);
            }
            else {
                rbuttons[i].setVisible(false);
                choiceLtLabels[i].setVisible(false);
                choiceLabels[i].setText("");
            }
        }
        questionText.setText(q.getText());
        counter.setText((user.getExam().getQuestions().indexOf(q) + 1) + "/" + user.getExam().getQuestions().size());
        questionLabel.setText(String.format("Soru %d)", user.getExam().getQuestions().indexOf(q) + 1));
        String answer = user.getAnswer(user.getCurrentIndex());
        int a = QuestionChoice.answerStrToIndex(answer);
        if (a >= 0 && a < rbuttons.length) rbuttons[a].setSelected(true);
        else radioNA.setSelected(true);
        setQuestionType(0);
    }

    private void setQuestionType(int type) {
        sExamClassic.setVisible(false);
        sExamGapFilling.setVisible(false);
        sExamChoose.setVisible(false);
        switch(type) {
            case 0:
                sExamChoose.setVisible(true);
                break;
            case 1:
                sExamClassic.setVisible(true);
                break;
            case 2:
                sExamGapFilling.setVisible(true);
                break;
        }
    }
}
