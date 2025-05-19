package com.pi4j.spring.boot.sample.app.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter

public class Bonlivraison {

    private String bon_livraison;//numero de bon livraison fourni par prestashop
    private String date_validation_cagette_termine;//date a la quelle cagette est rempli
    private String nom_client;
    private int numero_cagette_alloue;
    //si la valeur est a 0, elle est disponible, si (1 a 6), la cagette est en cours d'utilisation
    private String ip_controller;

    private List<ColisArticle> commande_colis_articles;

    public String getBon_livraison() {
        return bon_livraison;
    }

    public void setBon_livraison(String bon_livraison) {
        this.bon_livraison = bon_livraison;
    }

    public String getDate_validation_cagette_termine() {
        return date_validation_cagette_termine;
    }

    public void setDate_validation_cagette_termine(String date_validation_cagette_termine) {
        this.date_validation_cagette_termine = date_validation_cagette_termine;
    }

    public String getNom_client() {
        return nom_client;
    }

    public void setNom_client(String nom_client) {
        this.nom_client = nom_client;
    }

    public int getNumero_cagette_alloue() {
        return numero_cagette_alloue;
    }

    public void setNumero_cagette_alloue(int numero_cagette_alloue) {
        this.numero_cagette_alloue = numero_cagette_alloue;
    }

    public String getIp_controller() {
        return ip_controller;
    }

    public void setIp_controller(String ip_controller) {
        this.ip_controller = ip_controller;
    }

    public List<ColisArticle> getCommande_colis_articles() {
        return commande_colis_articles;
    }

    public void setCommande_colis_articles(List<ColisArticle> commande_colis_articles) {
        this.commande_colis_articles = commande_colis_articles;
    }

    

}
