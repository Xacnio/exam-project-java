package sinavproje;

import sinavproje.ui.controllers.QuestionsController;

public class ControllerManager {
    private QuestionsController questionsController;

    public QuestionsController getQuestionsController() {
        return questionsController;
    }

    public void setQuestionsController(QuestionsController questionsController) {
        this.questionsController = questionsController;
    }
}
