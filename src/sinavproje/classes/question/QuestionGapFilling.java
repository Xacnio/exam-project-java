package sinavproje.classes.question;

import sinavproje.types.QuestionType;

public class QuestionGapFilling extends Question {
    private String answer;

    public QuestionGapFilling(String text)
    {
        super();
        this.setText(text);
        this.setType(QuestionType.GAPFILLING);
    }
    public boolean check(String answer) {
        return answer.toLowerCase().contains(this.answer.toLowerCase());
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
    @Override
    public String getAnswer() {
        return answer;
    }

    @Override
    public String getAnswerFull() {
        return answer;
    }

    @Override
    public String getAnswersEx() {
        return answer;
    }

    @Override
    public String getExamExportForm(int number, String answer) {
        boolean istrue = false;
        String export = String.format("Soru %d) %s", number+1, this.getText());
        if(this.check(answer)) {
            istrue = true;
        }
        if(answer.isEmpty()) answer = "Cevaplanmadı!";
        export += String.format(" (Puan: %d/%d) (Cevap: %s)\r\n", istrue ? this.getPoint() : 0, this.getPoint(), this.getAnswer());
        return export + answer;
    }
}
