/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Andrej
 */
public class IngredientsController implements Initializable {

    @FXML private TableView<Normativ> dbNormativi;
    @FXML private TableColumn<Normativ, Double> col_kolicina = new TableColumn("Količina");
    @FXML private TableColumn<Normativ, String> col_ime = new TableColumn("Ime");
    @FXML private TableColumn<Normativ, String> col_jed_mere = new TableColumn("Jed. mere");
    @FXML private TableColumn<Normativ, Double> col_cena = new TableColumn("Cena");
    @FXML private TitledPane titledIzmena;
    @FXML private ImageView imageMeal, ingImage;
    
    @FXML private TextField inputIme, inputKolicina, inputImeObroka, inputProdajna, inputImageMeal;
    @FXML private Label labelJedMere, printCenaObroka;
    
    private ObservableList<Normativ> data = FXCollections.observableArrayList();
    double cenaPerUnit = 0;
    double cenaObroka;
    
// Učitavanja tabele "normativ" iz baze
    public void loadData() throws SQLException, ClassNotFoundException{
        
        col_ime.setCellValueFactory(new PropertyValueFactory<>("ime"));
        col_kolicina.setCellValueFactory(new PropertyValueFactory<>("potrebnaKolicina"));
        col_jed_mere.setCellValueFactory(new PropertyValueFactory<>("jedMere"));
        col_cena.setCellValueFactory(new PropertyValueFactory<>("cena"));
        
        dbNormativi.getItems().clear();
        
        try {
            String sql = "select * from normativ where obrok_id='"+FXMLDocumentController.selectedId+"'";
            ResultSet rs = DBUtil.dbExcecuteQuery(sql);
            
            while (rs.next()){
                data.add(new Normativ(
                        rs.getInt("id"), 
                        rs.getString("ime"), 
                        rs.getDouble("potrebna_kolicina"), 
                        rs.getString("jedinica_mere"), 
                        rs.getDouble("cena"),
                        rs.getString("slika"),
                        rs.getDouble("pakovanje"),
                        rs.getDouble("cena_pakovanja")
                ));
                dbNormativi.setItems(data);
            }
        } catch (SQLException e) {
            System.err.println("Exception occur while loading data "+e);
            throw e;
        }
        
        calculatePrice();
        inputIme.clear();
        inputKolicina.clear();
        ingImage.setImage(new Image("/project/default-placeholder-500x500.png"));
    }
    
// Otvara nov prozor za dodavanje novog normativa/sastojka
    @FXML
    private void addNewNorm(ActionEvent event){
        try {
            FXMLLoader loadNewNorm = new FXMLLoader(getClass().getResource("NewNormativ.fxml"));
            Parent root1 = (Parent) loadNewNorm.load();
            Stage stage = new Stage();
            stage.setTitle("New ingredient");
            stage.getIcons().add(new Image("/img/newNorm-favicon.png"));
            stage.setScene(new Scene(root1));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (IOException e) {
            System.err.println("Error!");
        }
    }
    
// Čuvanje promena novog normativa/sastojka
    @FXML
    private void saveChanges(ActionEvent event) throws SQLException, ClassNotFoundException{
        
        if(inputIme.getText().isEmpty() || inputKolicina.getText().isEmpty()){
            Alerts.Warning("WARNING!", null, "Please fill out empty fields");
        }
        else{
            try {
                DecimalFormat f = new DecimalFormat("##.00");
                double newCena = cenaPerUnit * Double.parseDouble(inputKolicina.getText());

                String sql = "update normativ set ime='"+inputIme.getText()
                        +"', potrebna_kolicina='"+inputKolicina.getText()
                        +"', cena='"+f.format(newCena)
                        +"' where id='"+dbNormativi.getSelectionModel().getSelectedItem().getId()+"'";

                try {
                    DBUtil.dbExcecuteUpdate(sql);
                    Alerts.Information("Ingredient added", "YOU DID IT!", "New ingredient successfully edited!");
                    
                    titledIzmena.setExpanded(false);
                    inputIme.clear();
                    inputKolicina.clear();
                    
                    loadData();
                } catch (SQLException e) {
                    System.err.println("Exception occur while editing data "+e);
                    throw e;
                }
            } catch (NumberFormatException e) {
                Alerts.Warning("NOT A NUMBER!", "THIS IS NOT A NUMBER!", "Please enter a valid number and, if needed, use dot (.) to separate");
                inputKolicina.clear();
            }
            
        }
        
    }
    
// Brosanje novog normativa/sastojka
    @FXML
    private void deleteNorm(ActionEvent event) throws SQLException, ClassNotFoundException{
        
        if(dbNormativi.getSelectionModel().getSelectedItem() != null){
            String sql="delete from normativ where id='"+dbNormativi.getSelectionModel().getSelectedItem().getId()+"'";
            
            Alerts.Confirmation("Confirm delete", "One more time", "Are you sure you want to delete selected ingredient?");
            
            if(Alerts.action.get() == ButtonType.OK){
                try {
                    DBUtil.dbExcecuteUpdate(sql);
                    //System.out.println("Deleted normativ with id="+dbNormativi.getSelectionModel().getSelectedItem().getId());
                    loadData();

                } catch (SQLException e) {
                    System.err.println("Exception occur while deleting data "+e);
                    throw e;
                }
            }
        }else{
            Alerts.Warning("WARNING!", "Exception occur while trying to select data!", "Please select ingredient from list");
        }
        
        
        
    }

// Odabir sastojka koji omogućuje izmenu sastojka i učitava i pokazuje sliku sastojka iz tabele
    
    private void selectSastojak(){
        ObservableList<Normativ> sastojci = dbNormativi.getSelectionModel().getSelectedItems();
        
        for(Normativ sastojak : sastojci){
            Image img = new Image(sastojak.getSlika());
            
            ingImage.setImage(img);
            
            inputIme.setText(sastojak.getIme());
            inputKolicina.setText(Double.toString(sastojak.getPotrebnaKolicina()));
            labelJedMere.setText(sastojak.getJedMere());
            
            cenaPerUnit = sastojak.getCenaPakovanja() / sastojak.getPakovanje();
        }
    }

// Računa ukupnu cenu sastojaka iz obroka    
    private void calculatePrice(){
        ObservableList<Normativ> normList = data;
        cenaObroka = 0;
        
        for (Normativ norm : normList) {
            cenaObroka += norm.getCena();
        }
        
        DecimalFormat f = new DecimalFormat("##.00");
        printCenaObroka.setText(f.format(cenaObroka));
        System.out.println(cenaObroka);
    }
    
// Update-uje izmene u obroku ili u sastojcima obroka u tabelama
    @FXML
    private void saveObrok(ActionEvent event) throws ClassNotFoundException, SQLException{
        
        if(inputImeObroka.getText().isEmpty() || inputProdajna.getText().isEmpty()){
            Alerts.Warning("WARNING!", null, "Please fill out empty fields");
        } else {
            try {
                double prodajnaCena = Double.parseDouble(inputProdajna.getText());
                
                String sql;
                
                if(imgMeal != null){
                    sql = "update obrok set ime='"+inputImeObroka.getText()
                        +"', cena='"+cenaObroka
                        +"', prodajna_cena='"+prodajnaCena+"', slika='"+imageMealText+"' where id='"+FXMLDocumentController.selectedId+"'";
                }else {
                    sql = "update obrok set ime='"+inputImeObroka.getText()
                        +"', cena='"+cenaObroka
                        +"', prodajna_cena='"+prodajnaCena+"' where id='"+FXMLDocumentController.selectedId+"'";
                }
                
                try {
                    DBUtil.dbExcecuteUpdate(sql);
                    Alerts.Information("Meal added", "YOU DID IT!", "Meal successfully edited!");
                    
                    Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
                    stage.close();
                    
                } catch (SQLException e) {
                    System.err.println("Exception occur while editing meal data "+e);
                    throw e;
                }
            
            } catch (NumberFormatException e) {
                Alerts.Warning("Warning", "THIS IS NOT A NUMBER!", "Please enter a valid number and, if needed, use dot (.) to separate");
                inputProdajna.clear();
            }
        }
        
    }
    
    String imageMealText;
    Image imgMeal;
    
// Učitava sliku sa neta i prikazuje je u aplikaciji
    @FXML
    private void loadNewImageMeal(ActionEvent event){
        imageMealText = inputImageMeal.getText();
        try {
            imgMeal = new Image(imageMealText);
            imageMeal.setImage(imgMeal);
        } catch (Exception e) {
            Alerts.Warning("Not an image", null, "This link does not provide any online image");
            inputImageMeal.clear();
            System.out.println("Oops" + e);
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Učitavanje tabele normativa
        try {
            loadData();
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("OOPS");
            Logger.getLogger(IngredientsController.class.getName()).log(Level.SEVERE, null, e);
        }
// poziva metodu "selectSastojak" ako korisnik klikne na normativ ili klikne gore ili dole na tastaturi
        dbNormativi.setOnMouseClicked(e -> {
            selectSastojak();
        });
        dbNormativi.setOnKeyReleased(e -> {
            if(e.getCode()==KeyCode.UP || e.getCode()==KeyCode.DOWN){
                selectSastojak();
            }
        });
        
        if(FXMLDocumentController.selectedSlika!=null){
            imageMeal.setImage(new Image(FXMLDocumentController.selectedSlika));
        }
// učitava ime selectovanog obroka i prodajnu cenu i upisuje ih u input polja prozora
        inputImeObroka.setText(FXMLDocumentController.selectedIme);
        inputProdajna.setText(Double.toString(FXMLDocumentController.selectedProdajna));
        
    }    
    
}
