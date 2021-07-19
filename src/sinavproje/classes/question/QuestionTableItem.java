package sinavproje.classes.question;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class QuestionTableItem {
    private StringProperty id;
    private StringProperty text;
    private StringProperty difficulty;
    private StringProperty point;
    private StringProperty type;
    private StringProperty answer;

    public QuestionTableItem(int id, String text, String answer, String difficulty, int point, String type) {
        this.id = new SimpleStringProperty(String.valueOf(id));
        this.text = new SimpleStringProperty(text.replace("\r\n", " "));
        this.difficulty = new SimpleStringProperty(difficulty);
        this.point = new SimpleStringProperty(String.valueOf(point));
        this.type = new SimpleStringProperty(type);
        this.answer = new SimpleStringProperty(answer.replace("\r\n", " "));
    }

    public String getAnswer() {
        return answer.get();
    }

    public StringProperty answerProperty() {
        return answer;
    }

    public String getType() {
        return type.get();
    }

    public StringProperty typeProperty() {
        return type;
    }

    public String getPoint() {
        return point.get();
    }

    public StringProperty pointProperty() {
        return point;
    }

    public String getDifficulty() {
        return difficulty.get();
    }

    public StringProperty difficultyProperty() {
        return difficulty;
    }

    public String getText() {
        return text.get();
    }

    public StringProperty textProperty() {
        return text;
    }

    public String getId() {
        return id.get();
    }

    public StringProperty idProperty() {
        return id;
    }
}
