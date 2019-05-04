/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.gsb.rv.entites;

import java.time.LocalDate;

/**
 *
 * @author etudiant
 */
public class Praticien {
    private int numero;
    private String nom;
    private String ville;
    private Double coefNotoriete;
    private LocalDate dateDerniereVisite;
    private Integer dernierCoefConfiance;
    private String adresse;
    private String codePostel;
    private String prenom;
    
    public Praticien (int numero, String nom, String ville, Double coefNotoriete, LocalDate dateDerniereVisite, Integer dernierCoefConfiance){
        this.numero = numero;
        this.nom = nom;
        this.ville = ville;
        this.coefNotoriete = coefNotoriete;
        this.dateDerniereVisite = dateDerniereVisite;
        this.dernierCoefConfiance = dernierCoefConfiance;
    }
    
    public Praticien (int numero, String nom, String ville, Double coefNotoriete, LocalDate dateDerniereVisite, Integer dernierCoefConfiance, String adresse,String codePostel,String prenom){
        this.numero = numero;
        this.nom = nom;
        this.ville = ville;
        this.coefNotoriete = coefNotoriete;
        this.dateDerniereVisite = dateDerniereVisite;
        this.dernierCoefConfiance = dernierCoefConfiance;
        this.adresse = adresse;
        this.codePostel = codePostel;
        this.prenom = prenom;
    }

    public Praticien() {}

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public void setCoefNotoriete(Double coefNotoriete) {
        this.coefNotoriete = coefNotoriete;
    }

    public void setDateDerniereVisite(LocalDate dateDerniereVisite) {
        this.dateDerniereVisite = dateDerniereVisite;
    }

    public void setDernierCoefConfiance(Integer dernierCoefConfiance) {
        this.dernierCoefConfiance = dernierCoefConfiance;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public void setCodePostel(String codePostel) {
        this.codePostel = codePostel;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public int getNumero() {
        return numero;
    }

    public String getNom() {
        return nom;
    }

    public String getVille() {
        return ville;
    }

    public Double getCoefNotoriete() {
        return coefNotoriete;
    }

    public LocalDate getDateDerniereVisite() {
        return dateDerniereVisite;
    }


    public Integer getDernierCoefConfiance() {
        return dernierCoefConfiance;
    }

    public String getAdresse() {
        return adresse;
    }

    public String getCodePostel() {
        return codePostel;
    }

    public String getPrenom() {
        return prenom;
    }
    
    @Override
    public String toString() {
        return "Praticien{" + "numero=" + numero + ", nom=" + nom + ", ville=" + ville + ", coefNotoriete=" + coefNotoriete + ", dateDerniereVisite=" + dateDerniereVisite + ", dernierCoefConfiance=" + dernierCoefConfiance + '}';
    }
}
