package sinavproje.ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sinavproje.Main;
import sinavproje.classes.other.EasyMsg;
import sinavproje.classes.question.*;
import sinavproje.types.QuestionDifficulty;

import java.io.*;
import java.util.Optional;

public class ImportExportController {
    @FXML AnchorPane iePane;
    @FXML Label ieMsg;
    @FXML Button iButton, eButton, dButton;
    private EasyMsg eMsg;

    public void initialize() {
        eMsg = new EasyMsg(ieMsg);
        iButton.setOnAction(e -> importQuestions());
        eButton.setOnAction(e -> exportQuestions());
        dButton.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Silme Onayı");
            alert.setHeaderText("Ekli olan tüm sorular silinecektir.");
            alert.setContentText("Bunu yapmak istediğinizden emin misiniz?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                Main.data.deleteQuestions();
                eMsg.showMessage("Tüm sorular silindi!");
            }
        });
        ieMsg.setOnMouseClicked(e -> eMsg.hide());
    }

    private void importQuestions() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home") + "/Desktop"));
        fileChooser.setTitle("Soruları İçe Aktar");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter(".txt", "*.txt"));
        Stage stage = (Stage) iePane.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);
        if(selectedFile == null) return;
        int importCount = 0;
        try {
            InputStreamReader f = new InputStreamReader(new FileInputStream(selectedFile), "UTF-8");
            BufferedReader reader = new BufferedReader(f);
            String line;
            Question a = null;
            while ((line = reader.readLine()) != null) {
                a = null;
                int count = ( line.split("-\\|-", -1).length );
                if(count == 5) {
                    String[] parts = line.split("-\\|-", -1);
                    parts[1] = decodeLine(parts[1]);
                    switch (Integer.parseInt(parts[0])) {
                        case 0:
                            a = new QuestionClassic(parts[1]);
                            break;
                        case 1:
                            int countC = ( parts[4].split("\\#\\|\\#", -1).length );
                            String[] choices = parts[4].split("\\#\\|\\#", -1);
                            if(countC >= 3) {
                                int truechoice = Integer.parseInt(choices[0]);
                                if(truechoice >= 0 && truechoice < countC-1) {
                                    a = new QuestionChoice(parts[1]);
                                    for (int i = 1; i < countC; i++) {
                                        ((QuestionChoice) a).addChoice(choices[i], i-1 == truechoice ? true : false);
                                    }
                                }
                            }
                            break;
                        case 2:
                            a = new QuestionTrueFalse(parts[1]);
                            ((QuestionTrueFalse) a).setAnswer((parts[4].equals("true") ? true : false));
                            break;
                        case 3:
                            a = new QuestionGapFilling(parts[1]);
                            ((QuestionGapFilling) a).setAnswer(parts[4]);
                            break;
                        default:
                            continue;
                    }
                    if(a != null) {
                        int difficulty = Integer.parseInt(parts[2]);
                        int point = Integer.parseInt(parts[3]);
                        if(0 <= difficulty && difficulty <= 2)
                            a.setDifficulty(QuestionDifficulty.values()[Integer.parseInt(parts[2])]);
                        if(point > 0 && point <= 110)
                            a.setPoint(point);

                        Main.data.addQuestion(a);
                        importCount++;
                    }
                }
            }
            reader.close();
            f.close();
        }
        catch(Exception e) {
            eMsg.showError("Dosya okunurken hata oluştu!\r\n" + e.getMessage());
        }
        finally {
            eMsg.showMessage(importCount + " adet soru içe aktarıldı!");
        }
    }
    private void exportQuestions() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Soruları Dışa Aktar");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home") + "/Desktop"));
        fileChooser.setInitialFileName("sorular.txt");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter(".txt", "*.txt"));
        Stage stage = (Stage) iePane.getScene().getWindow();
        File selectedFile = fileChooser.showSaveDialog(stage);
        if(selectedFile == null) return;
        try {
            Writer writer = new OutputStreamWriter(new FileOutputStream(selectedFile), "UTF-8");
            BufferedWriter f = new BufferedWriter(writer);
            f.write(Question.exportString(Main.data.getQuestions()));
            f.close();
            writer.close();
        }
        catch(Exception e) {
            eMsg.showError("Dosya kaydedilirken hata oluştu!\r\n" + e.getMessage());
        }
        finally {
            eMsg.showMessage(selectedFile.getName() + " adlı dosya başarıyla kaydedildi.");
        }
    }
    private static String decodeLine(String s) {
        return s.replace("=|=", "\r\n");
    }
}
