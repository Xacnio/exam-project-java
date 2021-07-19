package sinavproje.classes.exam;

import sinavproje.classes.other.Choice;
import sinavproje.classes.question.Question;
import sinavproje.classes.question.QuestionChoice;
import sinavproje.classes.question.QuestionGapFilling;
import sinavproje.types.QuestionType;

import java.util.ArrayList;
import java.util.List;

public class UserExam {
    private Exam exam;
    private List<String> answers = new ArrayList<String>();
    private int iQuestion = -1;

    public UserExam(ExamTest t) {
        this.exam = t;
        createEmptyAnswers();
    }

    public UserExam(ExamClassic t) {
        this.exam = t;
        createEmptyAnswers();
    }

    public UserExam(ExamMixed t) {
        this.exam = t;
        createEmptyAnswers();
    }

    public Exam getExam() {
        return this.exam;
    }

    public void resetAnswers() {
        answers.clear();
        createEmptyAnswers();
    }

    public Question previous() {
        if(exam.getQuestions().get(--iQuestion) != null) {
            return exam.getQuestions().get(iQuestion);
        }
        return null;
    }

    public boolean hasPrevious() {
        if(iQuestion == 0) return false;
        return (exam.getQuestions().get((iQuestion - 1)) != null);
    }

    public boolean hasNext() {
        if(iQuestion + 1 >= exam.getQuestions().size()) return false;
        return (exam.getQuestions().get((iQuestion + 1)) != null);
    }

    public Question next() {
        if(exam.getQuestions().get(++iQuestion) != null) {
            return exam.getQuestions().get(iQuestion);
        }
        return null;
    }

    public int getCurrentIndex() {
        return iQuestion;
    }

    public String getAnswer(int i) {
        return this.answers.get(i);
    }

    public void setAnswer(int i, String s) {
        this.answers.set(i, s);
    }

    private void createEmptyAnswers() {
        for(Question q : exam.getQuestions()) {
            answers.add("");
        }
    }

    public int[] getExamPoints() {
        int truepoint = 0, truecount = 0, falsecount = 0, blankcount = 0;
        for(Question q : exam.getQuestions()) {
            if(q.getType() == QuestionType.CLASSIC) continue;
            int cc = 0;
            int i = exam.getQuestions().indexOf(q);
            if(q.getType() == QuestionType.TRUEFALSE || q.getType() == QuestionType.CHOICE) {
                if (this.answers.get(i).matches("\\d+")) {
                    int truec = Integer.parseInt(this.answers.get(i));
                    boolean findtrue = false;
                    for (Choice c : ((QuestionChoice) q).getChoices()) {
                        if (!c.isFree() && cc++ == truec && c.isTrue()) {
                            truepoint += q.getPoint();
                            truecount++;
                            findtrue = true;
                        }
                    }
                    if (!findtrue) falsecount++;
                } else {
                    blankcount++;
                }
            } else if(q.getType() == QuestionType.GAPFILLING) {
                String answer = this.answers.get(i);
                if(answer.length() == 0) blankcount++;
                else {
                    if(((QuestionGapFilling)q).check(answer)) {
                        truepoint += q.getPoint();
                        truecount++;
                    }
                    else {
                        falsecount++;
                    }
                }
            }
        }
        return new int[]{truepoint, truecount, falsecount, blankcount};
    }
}
