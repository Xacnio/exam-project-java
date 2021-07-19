package sinavproje.classes.question;

import sinavproje.types.QuestionType;

public class QuestionClassic extends Question {
    public QuestionClassic(String text)
    {
        super();
        this.setText(text);
        this.setType(QuestionType.CLASSIC);
    }
    @Override
    public String getAnswer() {
        return "";
    }

    @Override
    public String getAnswersEx() {
        return "";
    }

    @Override
    public String getAnswerFull() {
        return "";
    }

    @Override
    public String getExamExportForm(int number, String answer) {
        if(answer.isEmpty()) answer = "Cevaplanmadı!";
        String export = String.format("Soru %d) %s", number+1, this.getText());
        export += String.format(" (Puan: %d)\r\n", this.getPoint());
        return export + answer;
    }
}
