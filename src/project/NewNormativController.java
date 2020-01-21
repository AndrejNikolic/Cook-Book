/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author Andrej
 */
public class NewNormativController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML private TextField inputIme,inputJedMere,inputCena,inputPakovanje,searchNorm,inputPotrebno;
    @FXML private ListView listNorm;
    @FXML private ImageView newNormImg;
    @FXML private Label kolicinaMera;
    
    @FXML
    private void addNewNorm(ActionEvent event) throws ClassNotFoundException, SQLException {
        
        String sql = "insert into normativ(obrok_id, ime, potrebna_kolicina, jedinica_mere, cena, pakovanje, cena_pakovanja, slika) " + 
                "values('"+FXMLDocumentController.selectedId+"','"
                +inputIme.getText()+"','"
                +inputPotrebno.getText()+"','"
                +inputJedMere.getText()+"','"
                +inputCena.getText()+"','"
                +inputPakovanje.getText().replaceAll("[^0-9]+", "")+"','"
                +oldPrice+"','"
                +image+"')";
        
        if(validatePolja()){
            try {
                DBUtil.dbExcecuteUpdate(sql);
                //System.out.println("Added new normativ");
                Alerts.Information("Ingrediant added", "YOU DID IT!", "New ingredient successfully added!");
                
                Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
                stage.close();
                
            } catch (SQLException e) {
                System.err.println("Exception occur while inserting data "+e);
                throw e;
            }
        }
        
    }
    
    @FXML
    private void searchCenoteka(){
        String search = searchNorm.getText();
        
        listNorm.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        listNorm.getItems().clear();
        
        try {
            Document website = Jsoup.connect("https://cenoteka.rs/pretraga?q="+search).get();
            Elements searchName = website.select("#products .list-articles-content .article-row");
            
            for (Element el : searchName) {
                if(el.select(".article-qty").html()!=null){
                    String ime = el.select(".article-name a").html();
                    String slika = el.select(".article-image img").attr("src");
                    String jedMere = el.select(".article-qty").html();
                    double cena = Double.parseDouble(el.selectFirst(".article-price .price.lowest").html().replace(".","").replace(',','.'));
                    //System.out.println(ime + jedMere + cenaStr);
                    listNorm.getItems().add(new Cenoteka(ime, jedMere, slika, cena));
                }
            }
            
        } catch (IOException e) {
            System.out.println(e);
        }
        
        
        listNorm.setOnMouseClicked(e -> {
            selectCenoteka();
        });
        listNorm.setOnKeyReleased(e -> {
            if(e.getCode()==KeyCode.UP || e.getCode()==KeyCode.DOWN){
                selectCenoteka();
            }
        });
    }
    
    double oldPrice = 0;
    String image;
    
    private void selectCenoteka(){
        
        ObservableList<Cenoteka> cenotekaList;
        cenotekaList = listNorm.getSelectionModel().getSelectedItems();
        
        inputPotrebno.setEditable(true);
        inputIme.setEditable(true);
        
        for (Cenoteka cenoteka : cenotekaList) {
            inputIme.setText(cenoteka.getIme());
            inputPakovanje.setText(cenoteka.getJedinicaMere());
            inputJedMere.setText(cenoteka.getJedinicaMere().replaceAll("[^A-Za-z]+", ""));
            kolicinaMera.setText(cenoteka.getJedinicaMere().replaceAll("[^A-Za-z]+", ""));
            inputCena.setText(Double.toString(cenoteka.getCena()));
            inputPotrebno.setText(cenoteka.getJedinicaMere().replaceAll("[^0-9]+", ""));
            
            Image img = new Image("https://cenoteka.rs"+cenoteka.getSlika());
            
            newNormImg.setImage(img);
            oldPrice = cenoteka.getCena();
            image = "https://cenoteka.rs"+cenoteka.getSlika();
        }
    }
    
    private boolean validatePolja(){
        if(inputIme.getText().isEmpty() || inputPotrebno.getText().isEmpty()){
            Alerts.Warning("WARNING!", null, "Please find and choose product from search and don't leave feilds empty");
            return false;
        }
        return true;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        searchNorm.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER){
                searchCenoteka();
            }
        });
        
        inputPotrebno.setEditable(false);
        
        ObservableList<Cenoteka> cenotekaList;
        cenotekaList = listNorm.getSelectionModel().getSelectedItems();
        
        DecimalFormat f = new DecimalFormat("##.00");
        
        inputPotrebno.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {

            for (Cenoteka cenoteka : cenotekaList) { 

                try {
                    double newCena = cenoteka.getCena() / Double.parseDouble(cenoteka.getJedinicaMere().replaceAll("[^0-9]+", "")) * Double.parseDouble(inputPotrebno.getText());
                    inputCena.setText(f.format(newCena));
                } catch (NumberFormatException e) {
                    Alerts.Warning("Warning", "THIS IS NOT A NUMBER!", "Please enter a valid number and, if needed, use dot (.) to separate");
                    inputPotrebno.setText(oldValue);
                }

            }
                
        });
        
    }    
    
}
