/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project;

/**
 *
 * @author Andrej
 */
public class Cenoteka {
    private String ime;
    private String jedinicaMere;
    private String slika;
    private double cena;

    public Cenoteka(String ime, String jedinicaMere, String slika, double cena) {
        this.ime = ime;
        this.jedinicaMere = jedinicaMere;
        this.slika = slika;
        this.cena = cena;
    }

    public Cenoteka(String ime) {
        this.ime = ime;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getJedinicaMere() {
        return jedinicaMere;
    }

    public void setJedinicaMere(String jedinicaMere) {
        this.jedinicaMere = jedinicaMere;
    }

    public String getSlika() {
        return slika;
    }

    public void setSlika(String slika) {
        this.slika = slika;
    }

    public double getCena() {
        return cena;
    }

    public void setCena(double cena) {
        this.cena = cena;
    }

    @Override
    public String toString() {
        return ime + " | " + jedinicaMere + " | " + cena ;
    }
    
}
