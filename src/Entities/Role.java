package Entities;

public class Role {

    private int id;
    private String nom;

    //Getters
    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    //Setters
    public void setId(int id) {
        this.id = id;
    }


    public void setNom(String nom) {
        this.nom = nom;
    }

    //Contructeur
    public Role() {

    }
}
