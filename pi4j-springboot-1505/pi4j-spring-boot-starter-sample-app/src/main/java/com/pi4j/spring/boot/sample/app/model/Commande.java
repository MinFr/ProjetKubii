package com.pi4j.spring.boot.sample.app.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class Commande {

    private String _id;

    private String bon_livraison;
    private String date_du_controle;
    private String client;
    private String private_key;
    
    private List<ColisArticle> commande_colis_articles;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getBon_livraison() {
        return bon_livraison;
    }

    public void setBon_livraison(String bon_livraison) {
        this.bon_livraison = bon_livraison;
    }

    public String getDate_du_controle() {
        return date_du_controle;
    }

    public void setDate_du_controle(String date_du_controle) {
        this.date_du_controle = date_du_controle;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getPrivate_key() {
        return private_key;
    }

    public void setPrivate_key(String private_key) {
        this.private_key = private_key;
    }

    public List<ColisArticle> getCommande_colis_articles() {
        return commande_colis_articles;
    }

    public void setCommande_colis_articles(List<ColisArticle> commande_colis_articles) {
        this.commande_colis_articles = commande_colis_articles;
    }



    
    
}
