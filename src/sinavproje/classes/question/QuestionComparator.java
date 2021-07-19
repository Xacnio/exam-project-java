package sinavproje.classes.question;

import sinavproje.types.QuestionSortType;

import java.util.Comparator;

public class QuestionComparator implements Comparator {

    private QuestionSortType type;

    public QuestionComparator(QuestionSortType type) {
        this.type = type;
    }

    @Override
    public int compare(Object o1, Object o2) {
        Question t1 = (Question)o1;
        Question t2 = (Question)o2;
        if(type == QuestionSortType.ID_ASC || type == QuestionSortType.ID_DESC) {
            if(t1.getId() > t2.getId())
                return (type == QuestionSortType.ID_ASC) ? 1 : -1;
            else if(t1.getId() < t2.getId())
                return (type == QuestionSortType.ID_ASC) ? -1 : 1;
        }
        else if(type == QuestionSortType.POINT_ASC || type == QuestionSortType.POINT_DESC) {
            if (t1.getPoint() > t2.getPoint())
                return (type == QuestionSortType.POINT_ASC) ? 1 : -1;
            else if (t1.getPoint() < t2.getPoint())
                return (type == QuestionSortType.POINT_ASC) ? -1 : 1;
        }
        else if(type == QuestionSortType.DIFFICULTY_ASC || type == QuestionSortType.DIFFICULTY_DESC) {
            if (t1.getDifficulty().ordinal() > t2.getDifficulty().ordinal())
                return (type == QuestionSortType.DIFFICULTY_ASC) ? 1 : -1;
            else if (t1.getDifficulty().ordinal() < t2.getDifficulty().ordinal())
                return (type == QuestionSortType.DIFFICULTY_ASC) ? -1 : 1;
        }
        return 0;
    }
}
