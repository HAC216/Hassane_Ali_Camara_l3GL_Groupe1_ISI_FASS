package DAO.Role;

import Entities.BdConnection;
import Entities.Role;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class RoleImp implements IRole {


    /*
    Creation d'un objet Bdconnection qui va nous permettre de se connecter a la basse de donne et faire des operations sur celle ci
    */
    BdConnection bdConnection = new BdConnection();

    //Creation d'un objet resultset pour pouvoir recuperer les donnees de la bd
    ResultSet resultSet = null;

    //Creation d'un role
    Role role = new Role();

    //pour la gestion de l'id et une variable tampon
    int id;
    Scanner scan = new Scanner(System.in);
    //Pour la gestion des boucles
    String rep, email;

    //Liste de id role valide
    List<Integer> listIdValide = new ArrayList<>();

    /**
     * Cette méthode nous permet de modifier un nom de role
     */
    @Override
    public void setNomInDb() {
        String nom;

        try {
            //retoune email,le password remplacer par les * et le role
            resultSet = bdConnection.statement.executeQuery("SELECT nom FROM role WHERE UPPER(SUBSTRING(role.nom, 1, 5)) != '" + "ADMIN" + "'");

            if (resultSet.isBeforeFirst()) {

        try {
            //retoune email,le password remplacer par les * et le role
            resultSet = bdConnection.statement.executeQuery("SELECT * from role");

            if (resultSet.isBeforeFirst()) {

        do {



            System.out.print("La modification d'un role va entrener la modification de tout les user qui on se role ,voulez vous continuer:(0/n): ");
            rep = scan.next();

            if (rep.substring(0, 1).equalsIgnoreCase("o")) {


                //Afficher des role pour pouvoir choisir un role
                try {
                    //retoune l'id et le nom du role
                    resultSet = bdConnection.statement.executeQuery("SELECT * from role where UPPER(SUBSTRING(role.nom, 1, 5)) != '" + "ADMIN" + "'");

                    while (resultSet.next()) {
                        //Affichage des element de la basse
                        System.out.println("|ID: " + resultSet.getInt(1) + " |NOM: " + resultSet.getString(2));
                        //Recuperation des id

                        listIdValide.add(resultSet.getInt(1));

                    }


                } catch (SQLException e) {
                    System.out.println(e);
                }

                do {
                    System.out.print("Veuiller saisir l'id:");
                    id = scan.nextInt();

                } while (!(listIdValide.contains(id)));


                System.out.print("Veuiller saisir le nouvaeu non:");
                nom = scan.next();

                try {
                    //Modification d'un role precise par l'id
                    bdConnection.statement.executeUpdate("UPDATE role SET nom='" + nom + "' WHERE id=" + id + "");

                } catch (SQLException e) {
                    System.out.println(e);
                }
            }

        } while (!(rep.substring(0, 1).equalsIgnoreCase("o") || rep.substring(0, 1).equalsIgnoreCase("n")));

            } else {
                System.out.println("Il n'y a pas de role.");
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        //pour liberer listIdValide
        listIdValide.clear();
            } else {
                System.out.println("Il n'ya pas de role different de admin.");
                System.out.println();
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    /**
     * Cette méthode nous permet de creer un nom de role
     */
    @Override
    public void createRoleInDb() {
        Role role = new Role();
        System.out.print("Veuiller saisir le nom du role: ");
        role.setNom(scan.next());

        //Instruction sql qui permettra d'inserer un role dans la bd
        String insertQuery = "INSERT INTO role (nom) VALUES ( '" + role.getNom() + "')";
        try {

            //l'excecution de l'instruction inserTQuery(Insertion des informations de user)
            bdConnection.statement.executeUpdate(insertQuery);

        } catch (SQLException e) {
            //Affiche les erreurs eventuel pouvant survenir lors de l'execution de la syntaxe sql
            System.out.println(e);
        }

        do {

            System.out.print("Est ce que vous voulier l'affecter a un user (o/n):");
            rep = scan.next();

            if (rep.substring(0, 1).equalsIgnoreCase("o")) {
                //pour affecter un role a un user
                affectationRole();

            }

        } while (!(rep.substring(0, 1).equalsIgnoreCase("o") || rep.substring(0, 1).equalsIgnoreCase("n")));


    }

    /**
     * Cette méthode nous permet d'afficher tout les role de la basse
     */
    @Override
    public void afficherRole() {

        try {
            //retoune l'id et le nom du role
            resultSet = bdConnection.statement.executeQuery("SELECT * from role");

            while (resultSet.next()) {
                //Affichage des element de la basse
                System.out.println("|ID: " + resultSet.getInt(1) + " |NOM: " + resultSet.getString(2));
                //Recuperation des id

                listIdValide.add(resultSet.getInt(1));

            }


        } catch (SQLException e) {
            System.out.println(e);
        }

    }

    /**
     * Cette méthode nous permet d'affecter un role a un user
     */
    @Override
    public void affectationRole() {

        try {
            //retoune email,le password remplacer par les * et le role
            resultSet = bdConnection.statement.executeQuery("SELECT email,nom FROM role, user WHERE user.id = role.id AND UPPER(SUBSTRING(role.nom, 1, 5)) != '" + "ADMIN" + "'");

            if (resultSet.isBeforeFirst()) {

                while (resultSet.next()) {
                    //Affichage des element de la basse
                    System.out.println("|Email: " + resultSet.getString(1) + " |Password: " + resultSet.getString(2) + " |Role: " + resultSet.getString(3));


                }


                do {

                    System.out.print("Veuiller saisir l'email de l'user:");
                    email = scan.next();

                    try {
                        //retoune le password si l'email existe dans la basse
                        resultSet = bdConnection.statement.executeQuery("SELECT password from user where email='" + email + "'");


                    } catch (SQLException e) {
                        //Pour Afficher d'eventuel erreur
                        System.out.println(e);
                    }
                    if (!resultSet.isBeforeFirst()) {
                        System.out.println("Veuiller ressasir l'email.");
                        System.out.println("");
                    }
                } while (!resultSet.isBeforeFirst());


                //Afficher des role pour pouvoir choisir un role
                try {
                    //retoune l'id et le nom du role
                    resultSet = bdConnection.statement.executeQuery("SELECT * from role where UPPER(SUBSTRING(role.nom, 1, 5)) != '" + "ADMIN" + "'");

                    while (resultSet.next()) {
                        //Affichage des element de la basse
                        System.out.println("|ID: " + resultSet.getInt(1) + " |NOM: " + resultSet.getString(2));
                        //Recuperation des id

                        listIdValide.add(resultSet.getInt(1));

                    }


                } catch (SQLException e) {
                    System.out.println(e);
                }
                do {
                    System.out.print("Veuiller saisir l'id du role:");
                    id = scan.nextInt();
                } while (!(listIdValide.contains(id)));


                try {
                    //Affectation d'un role a un user
                    bdConnection.statement.executeUpdate("UPDATE user SET id=" + id + " WHERE email='" + email + "'");

                } catch (SQLException e) {
                    System.out.println(e);
                }

            } else {
                System.out.println("Il n'ya pas de user qui on un role different de admin.");
                System.out.println();
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        //pour liberer listIdValide
        listIdValide.clear();

    }

    /**
     * Cette méthode nous permet de supprimer un role avectout les user qui y sont associer
     */
    @Override
    public void supprimerRole() {

        try {
            //retoune email,le password remplacer par les * et le role
            resultSet = bdConnection.statement.executeQuery("SELECT nom FROM role WHERE  UPPER(SUBSTRING(role.nom, 1, 5)) != '" + "ADMIN" + "'");

            if (resultSet.isBeforeFirst()) {

        try {
            //retoune email,le password remplacer par les * et le role
            resultSet = bdConnection.statement.executeQuery("SELECT * from role");

            if (resultSet.isBeforeFirst()) {


                do {


                    System.out.print("La suppresion d'un role va entrener la suppresion du role mais aussi la suppresion de tout les user qui on se role ,voulez vous voir si le role est utiliser:(0/n): ");
                    rep = scan.next();

                    if (rep.substring(0, 1).equalsIgnoreCase("o")) {


                        try {
                            //retoune email,le password remplacer par les * et le role
                            resultSet = bdConnection.statement.executeQuery("SELECT email,CONCAT('*', REPEAT('*', LENGTH(password) - 2), '*'),nom from role,user where user.id=role.id AND UPPER(SUBSTRING(role.nom, 1, 5)) != '" + "ADMIN" + "'");

                            while (resultSet.next()) {
                                //Affichage des element de la basse
                                System.out.println("|Email: " + resultSet.getString(1) + " |Password: " + resultSet.getString(2) + " |Role: " + resultSet.getString(3));
                            }
                            System.out.print("SAISIR QUELQUE CHOSE POUR PASSER: ");
                            //mettre l'ecran en pause
                            scan.next();

                        } catch (SQLException e) {
                            System.out.println(e);
                        }
                        do {


                            System.out.print("voulez le vous continuer:(0/n): ");
                            rep = scan.next();

                            if (rep.substring(0, 1).equalsIgnoreCase("o")) {


                                //Afficher des role pour pouvoir choisir un role
                                try {
                                    //retoune l'id et le nom du role
                                    resultSet = bdConnection.statement.executeQuery("SELECT * from role where UPPER(SUBSTRING(role.nom, 1, 5)) != '" + "ADMIN" + "'");

                                    while (resultSet.next()) {
                                        //Affichage des element de la basse
                                        System.out.println("|ID: " + resultSet.getInt(1) + " |NOM: " + resultSet.getString(2));
                                        //Recuperation des id

                                        listIdValide.add(resultSet.getInt(1));

                                    }


                                } catch (SQLException e) {
                                    System.out.println(e);
                                }


                                do {
                                    System.out.print("Veuiller saisir l'id:");
                                    id = scan.nextInt();
                                } while (!(listIdValide.contains(id)));


                                try {
                                    //Suppression du role et de tout les user precise par l'id
                                    bdConnection.statement.executeUpdate("DELETE FROM role WHERE id=" + id + "");
                                    //Suppression de tout les user precise par l'id
                                    bdConnection.statement.executeUpdate("DELETE FROM user WHERE id=" + id + "");

                                } catch (SQLException e) {
                                    System.out.println(e);
                                }
                            }
                        } while (!(rep.substring(0, 1).equalsIgnoreCase("o") || rep.substring(0, 1).equalsIgnoreCase("n")));
                    }

                } while (!(rep.substring(0, 1).equalsIgnoreCase("o") || rep.substring(0, 1).equalsIgnoreCase("n")));

            } else {
                System.out.println("Il n'y a pas de role.");
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        listIdValide.clear();
            } else {
                System.out.println("Il n'ya pas de user qui on un role different de admin.");
                System.out.println();
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
}