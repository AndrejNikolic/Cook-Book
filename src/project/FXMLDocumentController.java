/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author Andrej
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML private TableView<Obrok> dbObroci;
    @FXML private TableColumn<Obrok, String> col_obrok_ime = new TableColumn("Ime");
    @FXML private TableColumn<Obrok, Double> col_obrok_cena = new TableColumn("Cena");
    @FXML private TableColumn<Obrok, Double> col_obrok_prod_cena = new TableColumn("Prodajna cena");
    @FXML private Label labelSastojci;
    @FXML private ImageView imageObrok;
    
    private ObservableList<Obrok> dataObrok = FXCollections.observableArrayList();
    private ObservableList<Normativ> dataNorm = FXCollections.observableArrayList();
    
    public static int selectedId;
    public static String selectedIme;
    public static String selectedSlika;
    public static double selectedProdajna;
    
// Učitavanja tabele "obrok" iz baze
    public void loadObrokData() throws SQLException, ClassNotFoundException{
        
        col_obrok_ime.setCellValueFactory(new PropertyValueFactory<>("ime"));
        col_obrok_cena.setCellValueFactory(new PropertyValueFactory<>("cena"));
        col_obrok_prod_cena.setCellValueFactory(new PropertyValueFactory<>("prodajnaCena"));
        
        dbObroci.getItems().clear();
        
        try {
            String sql = "select * from obrok";
            ResultSet rs = DBUtil.dbExcecuteQuery(sql);
            
            while (rs.next()){
                dataObrok.add(new Obrok(
                        rs.getInt("id"), 
                        rs.getString("ime"),
                        rs.getDouble("cena"),
                        rs.getDouble("prodajna_cena"),
                        rs.getString("slika")
                ));
                dbObroci.setItems(dataObrok);
            }
        } catch (SQLException e) {
            System.err.println("Exception occur while loading data "+e);
            throw e;
        }
    }
    
// Učitavanja tabele "normativ" iz baze
    public void loadNormData( String sql ) throws ClassNotFoundException, SQLException {
        
        try {
            ResultSet rs = DBUtil.dbExcecuteQuery(sql);
            
            while (rs.next()){
                dataNorm.add(new Normativ(
                        rs.getInt("id"), 
                        rs.getString("ime"), 
                        rs.getDouble("potrebna_kolicina"), 
                        rs.getString("jedinica_mere"), 
                        rs.getDouble("cena"),
                        rs.getString("slika"),
                        rs.getDouble("pakovanje"),
                        rs.getDouble("cena_pakovanja")
                ));
            }
            
        } catch (SQLException e) {
            System.err.println("Exception occur while loading data "+e);
            throw e;
        }
    }

/* Metoda za odabir obroka koja: 
    - nalazi id selectovanog obroka, 
    - prolazi kroz sastojke(normative) selectovanog obroka,
    - izvlači potrebne informacije i štampa ih
*/
    private void selectObrok() throws ClassNotFoundException, SQLException, FileNotFoundException{
        
        String sastojci = "";
        
        try {
            selectedId = dbObroci.getSelectionModel().getSelectedItem().getId();
            //System.out.println(selectedId);
            loadNormData("select * from normativ where obrok_id='"+selectedId+"'");
            ObservableList<Normativ> normList = dataNorm;
            ObservableList<Obrok> obrokList = dbObroci.getSelectionModel().getSelectedItems();
            
            for (Obrok obrok : obrokList){
                //System.out.println(obrok.getSlika());
                Image img;
                
                if(obrok.getSlika() != null){
                    img = new Image(obrok.getSlika());
                    imageObrok.setImage(img);
                    //System.out.println(img);
                }else {
                    img = new Image(new FileInputStream("src/project/default-placeholder-500x500.png"));
                    imageObrok.setImage(img);
                    //System.out.println(img);
                }
            }
            
            for (Normativ norm : normList){
                sastojci += norm.getIme()+" ("+norm.getPotrebnaKolicina()+" "+norm.getJedMere()+")\n";
            }
            
            labelSastojci.setText(sastojci);
            
        } catch (SQLException e) {
            System.err.println("Exception occur while selecting data "+e);
            throw e;
        }
        
        dataNorm.clear();
    }
    
// Otvara nov prozor za dodavanje novog obroka
    @FXML
    private void dodajObrok(ActionEvent event) {
        try {
            FXMLLoader loadNorms = new FXMLLoader(getClass().getResource("NewObrok.fxml"));
            Parent root = (Parent) loadNorms.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("New meal");
            stage.getIcons().add(new Image("img/meal.png"));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (Exception e) {
            System.err.println("Error!");
        }
    }
    
// Otvara nov prozor za izmenu obroka
    @FXML
    private void izmeniObrok(ActionEvent event) {
        if(dbObroci.getSelectionModel().getSelectedItem() != null){
            try {
                selectedId = dbObroci.getSelectionModel().getSelectedItem().getId();
                selectedIme = dbObroci.getSelectionModel().getSelectedItem().getIme();
                selectedProdajna = dbObroci.getSelectionModel().getSelectedItem().getProdajnaCena();
                selectedSlika = dbObroci.getSelectionModel().getSelectedItem().getSlika();
                FXMLLoader loadNorms = new FXMLLoader(getClass().getResource("Ingredients.fxml"));
                Parent root = (Parent) loadNorms.load();
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Edit/Add Ingredients");
                stage.getIcons().add(new Image("/img/newNorm-favicon.png"));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.show();
            } catch (IOException e) {
                Alerts.Warning("WARNING!", "Exception occur while trying to load data!", "Please select meal to edit");
            }
        }else{
            Alerts.Warning("WARNING!", "Exception occur while trying to select data!", "Please select meal from list");
        }
    }

// Briše izabrani obrok
    @FXML
    private void deleteObrok(ActionEvent event) throws SQLException, ClassNotFoundException{
        
        if(dbObroci.getSelectionModel().getSelectedItem() != null){
            String sql="delete from obrok where id='"+dbObroci.getSelectionModel().getSelectedItem().getId()+"'";
            
            Alerts.Confirmation("Confirm delete", "One more time", "Are you sure you want to delete selected meal?");
            
            if(Alerts.action.get() == ButtonType.OK){
                try {
                    DBUtil.dbExcecuteUpdate(sql);
                    loadObrokData();

                } catch (SQLException e) {
                    System.err.println("Exception occur while deleting data "+e);
                    throw e;
                }
            }
        }else{
            Alerts.Warning("WARNING!", "Exception occur while trying to select data!", "Please select meal from list");
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Učitavanje tabele obroka
        try {
            loadObrokData();
        } catch (SQLException | ClassNotFoundException e) {
            Logger.getLogger(IngredientsController.class.getName()).log(Level.SEVERE, null, e);
        }
        
// poziva metodu "selectObrok" ako korisnik klikne na obrok ili klikne gore ili dole na tastaturi
        dbObroci.setOnMouseClicked(e ->{
            try {
                if(dbObroci.getSelectionModel().getSelectedItem() != null){
                    selectObrok();
                }else{
                    Alerts.Warning("WARNING!", "Exception occur while trying to select data!", "Please select meal from list");
                }
                
            } catch (ClassNotFoundException | SQLException | FileNotFoundException ex) {
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        dbObroci.setOnKeyReleased(e -> {
            if(e.getCode()==KeyCode.UP || e.getCode()==KeyCode.DOWN){
                try {
                if(dbObroci.getSelectionModel().getSelectedItem() != null){
                    selectObrok();
                }else{
                    Alerts.Warning("WARNING!", "Exception occur while trying to select data!", "Please select meal from list");
                }
                
            } catch (ClassNotFoundException | SQLException | FileNotFoundException ex) {
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            }
            }
        });
    }    
    
}
