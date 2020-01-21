/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project;

import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *
 * @author Andrej
 * 
 */

// Upozorenja pri određenim dešavanjima u toku aplikacije
public class Alerts {
    public static Optional <ButtonType> action;
    
    public static void Warning(String title, String header, String content){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        ((Stage)alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image("/img/alert.png"));
        alert.showAndWait();
    }
    public static void Information(String title, String header, String content){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        ((Stage)alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image("/img/info.png"));
        alert.showAndWait();
    }
    public static void Confirmation(String title, String header, String content){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        ((Stage)alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image("/img/question.png"));
        action = alert.showAndWait();
    }
}
