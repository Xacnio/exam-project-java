package sinavproje.classes.question;

import sinavproje.classes.other.Choice;
import sinavproje.types.QuestionType;

import java.util.ArrayList;
import java.util.List;

public class QuestionChoice extends Question {
    public static final int MAX_CHOICE = 5;
    private List<Choice> choices = new ArrayList<Choice>(MAX_CHOICE);

    public QuestionChoice(String text)
    {
        super();
        this.setText(text);
        this.setType(QuestionType.CHOICE);
        initChoices();
    }

    private void initChoices() {
        for(int i = 0; i < MAX_CHOICE; i++) {
            this.choices.add(new Choice(i));
        }
    }

    protected void clearChoices() {
        this.choices.clear();
        initChoices();
    }

    public void addChoice(String choice, boolean isanswer)
    {
        for(int i = 0; i < MAX_CHOICE; i++) {
            if(this.choices.get(i).isFree()) {
                this.choices.set(i, new Choice(i, choice, isanswer));
                break;
            }
        }
    }

    public Choice getTrueChoice() {
        for(Choice c: this.getChoices()) {
            if(c.isFree()) continue;
            if(c.isTrue()) return c;
        }
        return null;
    }

    public List<Choice> getChoices() {
        return this.choices;
    }

    public boolean searchChoices(String choice) {
        for(Choice s : choices) {
            if(s.isFree()) continue;
            if(s.getChoiceText().toLowerCase().contains((choice)))
                return true;
        }
        return false;
    }

    public static int answerStrToIndex(String s) {
        if(s.matches("\\d+"))
            return Integer.parseInt(s);
        return -1;
    }

    @Override
    public String getAnswer() {
        for(Choice c : choices) {
            if(c.isFree()) continue;
            if(c.isTrue()) return c.getChoiceText();
        }
        return "";
    }

    @Override
    public String getAnswerFull() {
        for(Choice c : choices) {
            if(c.isFree()) continue;
            if(c.isTrue()) return c.getLetter() + ") " + c.getChoiceText();
        }
        return "";
    }

    @Override
    public String getAnswersEx() {
        int i = 0, x = 0;
        List<String> choices = new ArrayList<String>(5);
        for(Choice c : this.choices) {
            if(c.isFree()) continue;
            if(c.isTrue()) x = i;
            choices.add(c.getChoiceText());
            i++;
        }
        return x + "#|#" + String.join("#|#", choices);
    }

    @Override
    public String getExamExportForm(int number, String answer) {
        int a = answerStrToIndex(answer);
        boolean istrue = false;
        String export = String.format("Soru %d) %s", number+1, this.getText());
        List<String> choicetexts = new ArrayList<String>(5);
        for(Choice c : this.getChoices()) {
            if(c.isFree()) continue;
            if(a == this.getChoices().indexOf(c) && c.isTrue()) istrue = true;
            choicetexts.add(String.format("[%c] %c) %s", ((a == this.getChoices().indexOf(c)) ? 'x' : ' '), c.getLetter(), c.getChoiceText()));
        }
        export += String.format(" (Puan: %d/%d) (Cevap: %c)\r\n", istrue ? this.getPoint() : 0, this.getPoint(), this.getTrueChoice().getLetter());
        return export + String.join("\r\n", choicetexts);
    }
}