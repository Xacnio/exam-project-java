package sinavproje.classes.exam;

import sinavproje.classes.question.Question;
import sinavproje.types.ExamType;

import java.io.File;
import java.util.List;

public abstract class Exam {
    private String name;
    private ExamType type;
    protected abstract void generateQuestions(List<Question> questions);
    public abstract boolean exportExam(UserExam user, File file);
    public abstract List<Question> getQuestions();

    Exam(String name) { setName(name); }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name.trim();
    }

    protected void setType(ExamType t) {
        this.type = t;
    }

    public ExamType getType() { return this.type; }

    public int getTotalPoint() {
        int p = 0;
        for(Question q : this.getQuestions()) {
            p += q.getPoint();
        }
        return p;
    }
}
