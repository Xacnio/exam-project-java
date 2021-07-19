package sinavproje.ui.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import sinavproje.*;
import sinavproje.classes.other.EasyMsg;
import sinavproje.classes.question.*;
import sinavproje.types.QuestionDifficulty;
import sinavproje.types.QuestionType;

public class AddQuestionController {
    @FXML
    private TextArea questionText;
    @FXML private Button submitAddQuestion;
    @FXML private Label addQuestionError;
    @FXML private ToggleGroup type, truefalse, difficulty;
    @FXML private RadioButton optionTrue, optionFalse;
    @FXML private TextField optionA, optionB, optionC, optionD, optionE;
    @FXML private RadioButton optionAnswerA, optionAnswerB, optionAnswerC, optionAnswerD, optionAnswerE;
    private RadioButton[] choiceanswers;
    @FXML private TextField optionGapFillingText, questionPoint;
    private TextField[] choices;
    private EasyMsg eMsg;

    public void initialize() {
        eMsg = new EasyMsg(addQuestionError);
        choices = new TextField[]{optionA, optionB, optionC, optionD, optionE};
        choiceanswers = new RadioButton[]{optionAnswerA, optionAnswerB, optionAnswerC, optionAnswerD, optionAnswerE};

        submitAddQuestion.setOnAction(e -> submitButton());

        questionPoint.textProperty().addListener(new ChangeListener<String>()
        {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if(!t1.matches("\\d*") || t1.length() > 3)
                {
                    questionPoint.setText(s);
                }
            }
        });

        type.selectedToggleProperty().addListener(new ChangeListener<Toggle>()
        {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldToggle, Toggle newToggle)
            {
                toggleChoices(false);
                optionGapFillingText.setDisable(true);
                toggleTrueFalse(false);
                RadioButton selected = ((RadioButton)newToggle);
                if(selected.getId().equals("optionTypeTest"))
                {
                    toggleChoices(true);
                }
                else if(selected.getId().equals("optionTypeGapFilling"))
                {
                    optionGapFillingText.setDisable(false);
                }
                else if(selected.getId().equals("optionTypeTrueFalse"))
                {
                    toggleTrueFalse(true);
                }
            }
        });
    }

    private void submitButton()
    {
        String text = questionText.getText().trim();
        int point = questionPoint.getText().length() > 0 ? Integer.parseInt(questionPoint.getText()) : 0;
        if(text.isEmpty()) eMsg.showError("Soru içeriğini boş bırakmayın!");
        else if(questionPoint.getText().isEmpty()) eMsg.showError("Soru puan içeriğini boş bırakmayın!");
        else if(!questionPoint.getText().matches("\\d*")) eMsg.showError("Soru puan değeri sayısal değerde olmalıdır!");
        else if(!(point > 0 && point <= 110)) eMsg.showError("Soru puan değeri 1 ile 110 arasında olmalıdır.");
        else
        {
            QuestionType selectedType = Question.choiceToType(((RadioButton)(type.getSelectedToggle())).getId());
            Question question = null;
            if(selectedType == QuestionType.CLASSIC) {
                question = new QuestionClassic(text);
            }
            else if(selectedType == QuestionType.CHOICE) {
                TextField[] choices = {optionA,optionB,optionC,optionD,optionE};
                RadioButton[] choiceradio = {optionAnswerA,optionAnswerB,optionAnswerC,optionAnswerD,optionAnswerE};
                boolean findchoice = false;
                for(RadioButton b : choiceradio) {
                    if(b.isSelected()) findchoice = true;
                }
                if(!findchoice) {
                    eMsg.showError("Doğru şıkkı belirleyiniz!");
                }
                else {
                    question = new QuestionChoice(text);
                    int i = 0;
                    for (TextField c : choices) {
                        if (c.getText().trim().length() > 0)
                            ((QuestionChoice) question).addChoice(c.getText().trim(), choiceradio[i].isSelected());
                        i++;
                    }
                }
            }
            else if(selectedType == QuestionType.TRUEFALSE) {
                if(truefalse.getSelectedToggle() == null) {
                    eMsg.showError("Sorunun cevabını belirlemediniz (Doğru/Yanlış).");
                }
                else {
                    question = new QuestionTrueFalse(text);
                    boolean answer = ((RadioButton) (truefalse.getSelectedToggle())).getId().equals("optionTrue");
                    ((QuestionTrueFalse) question).setAnswer(answer);
                }
            }
            else if(selectedType == QuestionType.GAPFILLING) {
                String answer = optionGapFillingText.getText().trim();
                if(answer.isEmpty()) {
                    eMsg.showError("Boşluk doldurma sorusunun cevabını belirleyin!");
                } else {
                    question = new QuestionGapFilling(text);
                    ((QuestionGapFilling) question).setAnswer(answer);
                }
            }
            if(question != null) {
                question.setPoint(point);
                String diffstr = ((RadioButton)difficulty.getSelectedToggle()).getText();
                question.setDifficulty(diffstr.equals("Kolay") ? QuestionDifficulty.EASY : (diffstr.equals("Zor") ? QuestionDifficulty.HARD : QuestionDifficulty.MEDIUM));
                Main.data.addQuestion(question);
                eMsg.showMessage("Soru eklendi, Soru No: " + question.getId());
                reset();
            }
        }
    }
    private void toggleChoices(boolean show)
    {
        for(int i=0; i < choices.length; i++)
        {
            TextField ch = (TextField)choices[i];
            ch.setDisable(!show);
        }
        for(int i=0; i < choiceanswers.length; i++)
        {
            RadioButton ch = (RadioButton)choiceanswers[i];
            ch.setDisable(!show);
        }
    }
    private void toggleTrueFalse(boolean show)
    {
        optionTrue.setDisable(!show);
        optionFalse.setDisable(!show);
    }

    private void reset()
    {
        optionA.setText("");
        optionB.setText("");
        optionC.setText("");
        optionD.setText("");
        optionE.setText("");
        optionAnswerA.setSelected(false);
        optionAnswerB.setSelected(false);
        optionAnswerC.setSelected(false);
        optionAnswerD.setSelected(false);
        optionAnswerE.setSelected(false);
        optionTrue.setSelected(false);
        optionFalse.setSelected(false);
        optionGapFillingText.setText("");
        questionText.setText("");
    }
}
