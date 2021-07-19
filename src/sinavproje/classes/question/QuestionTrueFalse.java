package sinavproje.classes.question;

import sinavproje.classes.other.Choice;
import sinavproje.types.QuestionType;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class QuestionTrueFalse extends QuestionChoice {
    private boolean answer;

    public QuestionTrueFalse(String text)
    {
        super(text);
        this.setType(QuestionType.TRUEFALSE);
        createChoice(false);
    }

    private void createChoice(boolean answer) {
        this.clearChoices();
        this.addChoice("Doğru", answer);
        this.addChoice("Yanlış", !answer);
    }

    public boolean isTrue() {
        return answer;
    }

    public boolean isFalse() {
        return !answer;
    }

    public boolean check(String answer) {
        if(this.answer == true && (answer.toLowerCase(Locale.ROOT).equals("doğru") || answer.toLowerCase(Locale.ROOT).equals("true")))
            return true;

        if(this.answer == false && (answer.toLowerCase(Locale.ROOT).equals("yanlış") || answer.toLowerCase(Locale.ROOT).equals("false")))
            return true;

        return false;
    }
    public boolean check(boolean answer) {
        return (this.answer == answer);
    }

    public void setAnswer(boolean answer) {
        this.answer = answer;
        this.createChoice(answer);
    }

    @Override
    public String getAnswer() {
        return Boolean.toString(answer);
    }

    @Override
    public String getAnswersEx() {
        return answer ? "true" : "false";
    }

    @Override
    public String getAnswerFull() {
        return answer ? "Doğru" : "Yanlış";
    }

    @Override
    public String getExamExportForm(int number, String answer) {
        int a = answerStrToIndex(answer);
        String export = String.format("Soru %d) %s", number+1, this.getText());
        List<String> choicetexts = new ArrayList<String>(5);
        for(Choice c : this.getChoices()) {
            if(c.isFree()) continue;
            choicetexts.add(String.format("[%c] %s", ((a == this.getChoices().indexOf(c)) ? 'x' : ' '), c.getChoiceText()));
        }
        export += String.format(" (Puan: %d/%d) (Cevap: %s)\r\n", this.isTrue() && a == 0 ? this.getPoint() : 0, this.getPoint(), this.getTrueChoice().getChoiceText());
        return export + String.join("\r\n", choicetexts);
    }
}
