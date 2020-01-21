/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Andrej
 */
public class Obrok {
    private final IntegerProperty id;
    private final StringProperty ime;
    private final DoubleProperty cena;
    private final DoubleProperty prodajnaCena;
    private final StringProperty slika;

    public Obrok(int id, String ime, double cena, double prodajnaCena, String slika) {
        this.id = new SimpleIntegerProperty(id);
        this.ime = new SimpleStringProperty(ime);
        this.cena = new SimpleDoubleProperty(cena);
        this.prodajnaCena = new SimpleDoubleProperty(prodajnaCena);
        this.slika = new SimpleStringProperty(slika);
    }
    
    public int getId() {
        return id.get();
    }

    public void setId(int idProperty) {
        id.set(idProperty);
    }

    public String getIme() {
        return ime.get();
    }

    public void setIme(String imeProperty) {
        ime.set(imeProperty);
    }
    
    public double getCena() {
        return cena.get();
    }

    public void setCena(double cenaProperty) {
        cena.set(cenaProperty);
    }
    
    public double getProdajnaCena() {
        return prodajnaCena.get();
    }

    public void setProdajnaCena(double prodajnaCenaProperty) {
        prodajnaCena.set(prodajnaCenaProperty);
    }
    
    public void setSlika(String slikaProperty) {
        slika.set(slikaProperty);
    }
    
    public String getSlika() {
        return slika.get();
    }
    
}
