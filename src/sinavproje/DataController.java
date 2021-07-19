package sinavproje;

import sinavproje.classes.question.Question;
import sinavproje.classes.question.QuestionChoice;
import sinavproje.classes.question.QuestionTrueFalse;
import sinavproje.types.QuestionType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataController {
    private List<Question> questions = new ArrayList<Question>();

    public void addQuestion(Question q)
    {
        questions.add(q);
    }

    public List<Question> getQuestions()
    {
        return this.questions;
    }

    public Question getQuestionById(int id) {
        for(Question question : questions)
        {
            if(question.getId() == id) return question;
        }
        return null;
    }

    public void deleteQuestions() {
        questions.clear();
    }

    public void deleteQuestionById(int id) {
        for(Question question : questions)
        {
            if(question.getId() == id) {
                questions.remove(question);
                break;
            }
        }
    }

    public List<Question> search(String text, String choicetext, String[] typetext, String difftext, String pointtext, String answerchoice)
    {
        text = text.trim().toLowerCase();
        choicetext = choicetext.trim().toLowerCase();
        int point = pointtext.length() > 0 && pointtext.matches("\\d+") ? Integer.parseInt(pointtext.trim()) : 0;
        List<Question> data = new ArrayList<Question>();
        for(Question q : Main.data.getQuestions())
        {
            if(text.length() > 0 && !q.getText().toLowerCase().contains(text))
                continue;

            if(choicetext.length() > 0)
            {
                if(q.getType() != QuestionType.CHOICE && q.getType() != QuestionType.GAPFILLING) continue;
                if(q.getType() == QuestionType.CHOICE && !((QuestionChoice) q).searchChoices(choicetext)) continue;
                if(q.getType() == QuestionType.GAPFILLING && !q.getAnswer().contains(choicetext)) continue;
            }

            List<String> types = Arrays.asList(typetext);

            if(types.size() > 0 && types.indexOf(q.getTypeText()) == -1)
                continue;

            if(difftext.length() > 0 && !difftext.equals("Zorluk") && !difftext.equals(q.getDifficultyText()))
                continue;

            if(answerchoice.length() > 0) {
                if(q.getType() == QuestionType.CHOICE) {
                    List<String> choices = new ArrayList<String>(Arrays.asList("A", "B", "C", "D", "E"));
                    int i = choices.indexOf(answerchoice);
                    if(i != -1 && !((QuestionChoice)q).getChoices().get(i).isTrue()) continue;
                }
                else if(q.getType() == QuestionType.TRUEFALSE && Arrays.asList("Doğru","Yanlış").indexOf(answerchoice) != -1) {
                    boolean istrue = (answerchoice.contains("Doğru")) ? true : false;
                    if(!((QuestionTrueFalse)q).check(istrue)) continue;
                }
            }

            if(point > 0 && point <= 110) {
                if(q.getPoint() != point) continue;
            }

            data.add(q);
        }
        return data;
    }
}
