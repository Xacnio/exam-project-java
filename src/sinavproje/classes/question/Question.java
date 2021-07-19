package sinavproje.classes.question;

import sinavproje.types.QuestionDifficulty;
import sinavproje.types.QuestionType;

import java.util.ArrayList;
import java.util.List;

public abstract class Question implements Comparable<Question> {

    private static int AUTO_INCREMENT = 1;
    private int id;
    private QuestionType type;
    private int point;
    private QuestionDifficulty difficulty;
    private String text;
    public abstract String getAnswer();
    public abstract String getAnswerFull();
    public abstract String getAnswersEx();
    public abstract String getExamExportForm(int number, String answer);

    Question()
    {
        this.id = AUTO_INCREMENT++;
    }
    Question(int id)
    {
        this.id = id;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public QuestionDifficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(QuestionDifficulty difficulty) {
        this.difficulty = difficulty;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public QuestionType getType() {
        return type;
    }

    public String getTypeText() {
        if(type == QuestionType.CHOICE) return "Çoktan Seçmeli";
        else if(type == QuestionType.GAPFILLING) return "Boşluk Doldurma";
        else if(type == QuestionType.CLASSIC) return "Klasik";
        else if(type == QuestionType.TRUEFALSE) return "Doğru/Yanlış";
        return "Bilinmiyor";
    }

    public String getDifficultyText() {
        if(difficulty == QuestionDifficulty.EASY) return "Kolay";
        else if(difficulty == QuestionDifficulty.MEDIUM) return "Orta";
        else if(difficulty == QuestionDifficulty.HARD) return "Zor";
        return "Bilinmiyor";
    }

    protected void setType(QuestionType type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public static QuestionType choiceToType(String choice)
    {
        if(choice.equals("optionTypeClassic")) return QuestionType.CLASSIC;
        if(choice.equals("optionTypeTest")) return QuestionType.CHOICE;
        if(choice.equals("optionTypeTrueFalse")) return QuestionType.TRUEFALSE;
        return QuestionType.GAPFILLING;
    }

    public static String exportString(List<Question> list) {
        List<String> text = new ArrayList<String>();
        List<String> parts = new ArrayList<String>();
        for(Question q : list) {
            parts.clear();
            parts.add(String.valueOf(q.getType().ordinal()));
            parts.add(fixExportStr(q.getText()));
            parts.add(String.valueOf(q.getDifficulty().ordinal()));
            parts.add(String.valueOf(q.getPoint()));
            parts.add(fixExportStr(q.getAnswersEx()));
            text.add(String.join("-|-", parts));
        }
        return String.join("\r\n", text);
    }

    private static String fixExportStr(String s) {
        return s.replace("-|-", "").replace("=|=", "").replace("\r\n", "\n").replace("\n", "=|=");
    }

    @Override
    public int compareTo(Question o) {
        if(o.getPoint() < this.getPoint()) return 1;
        if(o.getPoint() > this.getPoint()) return -1;
        return 0;
    }
}
