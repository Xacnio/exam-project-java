package sinavproje.classes.other;

public class Choice {
    boolean using = false;
    private String choice;
    private boolean isAnswer;
    private char letter = 'A';

    public Choice(int i) {
        this.choice = "";
        this.isAnswer = false;
        this.letter += i;
    }

    public Choice(int i, String choice, boolean isAnswer) {
        this.choice = choice;
        this.isAnswer = isAnswer;
        this.letter += i;
        this.using = true;
    }

    public boolean isFree() {
        return !this.using;
    }

    public char getLetter() {
        return this.letter;
    }

    public String getChoiceText() {
        return choice;
    }

    public boolean isTrue() {
        return isAnswer;
    }
}
