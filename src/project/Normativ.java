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
public class Normativ {
    private final IntegerProperty id;
    private final StringProperty ime;
    private final DoubleProperty potrebnaKolicina;
    private final StringProperty jedMere;
    private final DoubleProperty cena;
    private final StringProperty slika;
    private final DoubleProperty pakovanje;
    private final DoubleProperty cenaPakovanja;

    public Normativ(int id, String ime, double potrebnaKolicina, String jedMere, double cena, String slika, double pakovanje, double cenaPakovanja) {
        this.id = new SimpleIntegerProperty(id);
        this.ime = new SimpleStringProperty(ime);
        this.potrebnaKolicina = new SimpleDoubleProperty(potrebnaKolicina);
        this.jedMere = new SimpleStringProperty(jedMere);
        this.cena = new SimpleDoubleProperty(cena);
        this.slika = new SimpleStringProperty(slika);
        this.pakovanje = new SimpleDoubleProperty(pakovanje);
        this.cenaPakovanja = new SimpleDoubleProperty(cenaPakovanja);
    }

    public String getSlika() {
        return slika.get();
    }
    
    public void setSlika(String slikaProperty) {
        slika.set(slikaProperty);
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
    
    public double getPotrebnaKolicina() {
        return potrebnaKolicina.get();
    }

    public void setPotrebnaKolicina(double potrebnaKolicinaProperty) {
        potrebnaKolicina.set(potrebnaKolicinaProperty);
    }

    public String getJedMere() {
        return jedMere.get();
    }

    public void setJedMere(String jedMereProperty) {
        jedMere.set(jedMereProperty);
    }

    public double getCena() {
        return cena.get();
    }

    public void setCena(double cenaProperty) {
        cena.set(cenaProperty);
    }
    
    public double getPakovanje() {
        return pakovanje.get();
    }

    public void setPakovanje(double pakovanjeProperty) {
        pakovanje.set(pakovanjeProperty);
    }
    
    public double getCenaPakovanja() {
        return cenaPakovanja.get();
    }

    public void setCenaPakovanja(double cenaPakovanjaProperty) {
        cenaPakovanja.set(cenaPakovanjaProperty);
    }
    
}
