package sinavproje.classes.other;

import javafx.scene.control.Label;
import javafx.scene.paint.Color;

public class EasyMsg {
    Label label = null;

    public EasyMsg(Label label) {
        this.label = label;
        hide();
    }

    public void hide()
    {
        if(label.isVisible()) label.setVisible(false);
    }

    public void showMessage(String m) {
        if(!label.isVisible()) label.setVisible(true);
        label.setText(m);
        label.setTextFill(Color.BLACK);
    }

    public void showError(String m) {
        if(!label.isVisible()) label.setVisible(true);
        label.setText(m);
        label.setTextFill(Color.RED);
    }
}
