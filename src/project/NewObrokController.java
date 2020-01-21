/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project;

import java.io.FileNotFoundException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Andrej
 */
public class NewObrokController implements Initializable {

    @FXML private TextField inputObrokIme, inputURL;
    @FXML private ImageView mealImage;
    
    
    @FXML
    private void saveObrok(ActionEvent event) throws ClassNotFoundException, SQLException, FileNotFoundException {
        
        String sql;
        
        if(img != null){
            sql = "insert into obrok(ime, slika) " + 
                "values('"+inputObrokIme.getText()+"','"+image+"')";
        }else {
            sql = "insert into obrok(ime) " + 
                "values('"+inputObrokIme.getText()+"')";
        }
        
        if(inputObrokIme.getText().isEmpty()){
            Alerts.Warning("Name empty", null, "Please fill out meal name");
        } else {
            try {
                DBUtil.dbExcecuteUpdate(sql);
                //System.out.println("Added new normativ");
                Alerts.Information("Meal added", "YOU DID IT!", "New meal successfully added!");
                
                Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
                stage.close();
                
            } catch (SQLException e) {
                System.err.println("Exception occur while inserting data "+e);
                throw e;
            }
        }
            
    }
    
    String image;
    Image img;
    
    @FXML
    private void addImage(ActionEvent event){
        
        image = inputURL.getText();
        try {
            img = new Image(image);
            mealImage.setImage(img);
        } catch (Exception e) {
            Alerts.Warning("Not an image", null, "This link does not provide any online image");
            inputURL.clear();
            //System.out.println("Oops" + e);
        }
    } 
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
