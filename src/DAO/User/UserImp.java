package DAO.User;

import DAO.Role.IRole;
import DAO.Role.RoleImp;
import Entities.BdConnection;
import Entities.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserImp implements IUser
{

    /*
    Creation d'un objet Bdconnection qui va nous permettre de se connecter a la basse de donne et faire des operations sur celle ci
    */
    BdConnection bdConnection = new BdConnection();

    //Creation d'un objet irole pour la gestion des role
    IRole irole = new RoleImp();

    //Creation d'un objet resultset pour pouvoir recuperer les donnees de la bd
    ResultSet resultSet = null;

    //Un objet pattern qui permettra de verifier l'email est valide et ne contient pas des caractere speciaux
    Pattern pattern = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");

    //Création d'un pattern pour valider le mot de passe
    Pattern patternPassword = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[@#$%^&+=])[a-zA-Z0-9@#$%^&+=]{8,}$");

    //Creation d'un objet matcher pour la gestion des caractere speciaux de l'email
    Matcher matcherEmail = null ;

    //Création d'un matcher pour le mot de passe
    Matcher matcherPassword = null;

    //Pour la creation d'un objet pour recuperer le hash et d'autre objet nessecaire
    String hashString = null, email, password, password_bd = null,nouveauPassword;
    Scanner scan = new Scanner(System.in);


    //Pour le hashage
    byte[] bytes_md5,hash;

    // Creation d'un objet MessageDigest pour calculer un hachage MD5
    MessageDigest md;

    //L'algorithme de hashage MD5 pour password
    {

        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

    }


    /**
     * Cette méthode nous permet de creer un User dans la basse
     *
     */
    @Override
    public void createUserInDb()
    {
        User user = new User();
        boolean emailExist;

        //Boucler si L'email est invalide
        do {

            //Pour la verification de l'email
            emailExist = false;

            System.out.print("Veuillez entrer votre email: ");
            email = scan.next();

            //Creation d'un objet matcher si l'email contient des caractere speciaux
                matcherEmail = pattern.matcher(email);

            //Retourne true si et seulement si toute la chaîne de caractères de l'email correspond au motif(pattern)
            if (!matcherEmail.matches())
            {
                System.out.println("Veuillez saisir un email valide avec le format (chaine@chaine.chaine) sans caractères spéciaux.");

            }else
            {

                try
                {
                    //retoune l'id si l'email existe dans notre basse
                    resultSet = bdConnection.statement.executeQuery("SELECT id from user where email='"+email+"'");

                    //verification si le curseur est avent le premier element si oui alors resulset est vide donc l'email est absent dans la basse
                    if(resultSet.isBeforeFirst())
                    {
                        //L'email existe deja
                        emailExist = true;
                        System.out.println("Veuiller saisir un autre Email(ce email existe deja dans notre basse) ");
                        System.out.println();

                    }
                    else
                    {
                        //l'email n'est pas encore dans notre basse
                        emailExist = false;

                    }

                } catch (SQLException e) {
                    //Affiche les erreurs eventuel pouvant survenir lors de l'execution de la syntaxe sql
                    System.out.println(e);
                }
            }

        }while(!(matcherEmail.matches() && !emailExist));

        //Insertion de l'email dans la basse
        user.setEmail(email);

        //Boucle si le mdp est invalide
        do
        {

            System.out.print("Veuiller entrer votre password : ");
            password = scan.next().trim();
            matcherPassword = patternPassword.matcher(password);

            // Si le mot de passe ne correspond pas au pattern, afficher un message d'erreur
            if (!matcherPassword.matches()) {
                System.out.println("1.Votre mot de passe doit contenir au moins 8 caractères,");
                System.out.println("2.Votre mot de passe doit contenir au moins une lettre minuscule,");
                System.out.println("3.Votre mot de passe doit contenir au moins une lettre majuscule,");
                System.out.println("4.Votre mot de passe doit contenir au moins un chiffre et un caractère spécial,");
                System.out.println("5.Les espace seront automatiquement supprime.");
            }

        }while (!matcherPassword.matches());

        //Obtention du bytecode de password
        bytes_md5 = password.getBytes();

        //Password
        {
            md.update(bytes_md5);

            // Obtient le hachage
            hash = md.digest();

            // Convertit le hachage en chaîne de caractères
            hashString = new String(hash);

            //Ajout du password hasher
            user.setPassword(hashString);
            user.setPasswordHashed(hashString);
        }




        try {
            //retoune l'id du role
            resultSet = bdConnection.statement.executeQuery("SELECT id from role where UPPER(SUBSTRING(nom, 1, 4))='"+"USER"+"'");
            resultSet.next();
            user.setId(resultSet.getInt(1));
            //Instruction sql qui permettra d'inserer un user dans la bd
            String insertQuery = "INSERT INTO user (id, email, password, passwordHashed) VALUES (?, ?, ?, ?)";

            try (PreparedStatement preparedStatement = bdConnection.connection.prepareStatement(insertQuery)) {
                preparedStatement.setInt(1, user.getId());
                preparedStatement.setString(2, user.getEmail());
                preparedStatement.setString(3, user.getPassword());
                preparedStatement.setString(4, user.getPasswordHashed());

                // l'exécution de l'instruction insertQuery (Insertion des informations de user)
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                System.out.println(e);
            }



        } catch (SQLException e) {
            System.out.println(e);
        }



    }


    /**
     * Cette méthode nous permet d'afficher tout les User de la basse
     *
     */
    @Override
    public void afficherUsersInDb()
    {

        try {
            //retoune email,le password remplacer par les * et le role
            resultSet = bdConnection.statement.executeQuery("SELECT email,CONCAT('*', REPEAT('*', LENGTH(password) - 2), '*'),nom from role,user where user.id=role.id ");

            while (resultSet.next())
            {
                //Affichage des element de la basse
                System.out.println("|Email: "+resultSet.getString(1)+" |Password: "+resultSet.getString(2)+" |Role: "+resultSet.getString(3));
            }
            System.out.print("SAISIR QUELQUE CHOSE POUR PASSER: ");
            //mettre l'ecran en pause
            scan.next();

        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    /**
     * Cette méthode nous permet de verifier si le user est dans la bd et de verifier son role seul l'admin peut ajouter un role
     *
     */
    @Override
    public void getConnection() {
        System.out.print("Veuiller saisir votre email:");
        email = scan.next();
        System.out.print("Veuiller saisir votre mot de passe:");
        password = scan.next();

        try
        {
            //retoune le password si l'email existe dans la basse
            resultSet = bdConnection.statement.executeQuery("SELECT password from user where email='" + email + "'");


            //Pour eviter les erreur sql si resulSet est null
            if (resultSet.isBeforeFirst()) {

                //Hashage de password
                {
                    //Obtention du bytecode de password
                    bytes_md5 = password.getBytes();

                    md.update(bytes_md5);

                    // Obtient le hachage
                    hash = md.digest();

                    // Convertit le hachage en chaîne de caractères
                    hashString = new String(hash);

                    //Ajout du password hasher
                    password= hashString;

                }

                //positionne le resultSet sur la premiere ligne
                resultSet.next();
                //Pour retenir le password hashe de la bd
                password_bd = new String(resultSet.getString(1));


                if (password.equals(password_bd)) {

                    //Pour liberer les ressource
                    resultSet.close();

                    //retoune le nom du role de l'user
                    resultSet = bdConnection.statement.executeQuery("SELECT nom from role,user where email='" + email + "' AND user.id=role.id");


                    //positionne le resultSet sur la premiere ligne
                    resultSet.next();
                    //Verifie si user est L'admin
                    if (resultSet.getString(1).trim().equalsIgnoreCase("admin")) {
                        int choix = 0;
                        //Pour liberer les ressource
                        resultSet.close();
                        //retoune toute les colonnes de role
                        resultSet = bdConnection.statement.executeQuery("SELECT * from role");

                        do
                        {
                            System.out.println("                                          ___________________________________________________");
                            System.out.println("                                         |1.Saisir un Role                                   |");
                            System.out.println("                                         |2.Lister les Role                                  |");
                            System.out.println("                                         |3.Affecter un role a un user                       |");
                            System.out.println("                                         |4.Modifier un role                                 |");
                            System.out.println("                                         |5.Supprimer un role                                |");
                            System.out.println("                                         |6.Quitter                                          |");
                            System.out.println("                                         |___________________________________________________|");
                            System.out.println("                                                         Faite votre choix:");
                            choix = scan.nextInt();

                            switch (choix)
                            {
                                case 1:
                                    //Creation d'un role et l'affectation a un user
                                    irole.createRoleInDb();
                                    break;
                                case 2:
                                    //Liste de role
                                    irole.afficherRole();
                                    break;

                                case 3:
                                    //Affectation d'un role a un user
                                    irole.affectationRole();
                                    break;

                                case 4:
                                    //Modification d'un role
                                    irole.setNomInDb();
                                    break;
                                case 5:
                                    //MSuppression d'un role
                                    irole.supprimerRole();
                                    break;
                            }


                        }while(!(choix==6));




                    } else {

                        //Pour liberer les ressource
                        resultSet.close();

                        //retoune l'email et password du role de l'user
                        resultSet = bdConnection.statement.executeQuery("SELECT email,CONCAT('*', REPEAT('*', LENGTH(password) - 2), '*'),nom from user,role where email='" + email + "' AND user.id=role.id ");
                        while (resultSet.next()) {
                            //Affichage des element de la basse
                            System.out.println("|Email: "+resultSet.getString(1)+" |Password: "+resultSet.getString(2)+" |Role: "+resultSet.getString(3));

                        }
                        System.out.print("SAISIR QUELQUE CHOSE POUR PASSER: ");
                        //mettre l'ecran en pause
                        scan.next();


                    }

                }
                else
                {
                    String rep;
                    //Boucler sur rep tand que rep n'est pas egale a o ou n
                    do
                    {

                        //Si le password est incorrecte
                        System.out.print("Votre mot de passe est incorrecte est ce que vous vouliez la modifier (O/N) : ");
                        rep = scan.next();

                        switch (rep)
                        {
                            case "O":
                                setPasswordInDb();
                                break;
                            case "o":
                                setPasswordInDb();
                                break;

                        }

                    }while (!(rep.substring(0,1).equalsIgnoreCase("o") || rep.substring(0,1).equalsIgnoreCase("n")));
                }
            }
            else
            {
                System.out.println("Les information de connection son incorrect veuiller ressayer.");
            }


        } catch (SQLException e) {
            //Pour Afficher d'eventuel erreur
            System.out.println(e);
        }
    }

    /**
     * Cette méthode nous permet de modifier le password d'un user
     *
     */
    @Override
    public void  setPasswordInDb()
    {
        System.out.println("Nous avons besoins de votre email pour pouvoir modifier votre mot de passe.");
        System.out.println();
        System.out.print("Veuiller saisir votre email: ");
        email = scan.next();



        try
        {
            resultSet.close();
            //retoune le password si l'email existe dans la basse
            resultSet = bdConnection.statement.executeQuery("SELECT password from user where email='" + email + "'");


            //Pour eviter les erreur sql si resulSet n'est pas sur la premier ligne
            if (resultSet.isBeforeFirst()) {



                    do {

                        System.out.print("Veuiller saisir votre nouveau password:");
                        nouveauPassword = scan.next().trim();
                        matcherPassword = patternPassword.matcher(nouveauPassword);

                        // Si le mot de passe ne correspond pas au pattern, afficher un message d'erreur
                        if (!matcherPassword.matches()) {
                            System.out.println("1.Votre mot de passe doit contenir au moins 8 caractères,");
                            System.out.println("2.Votre mot de passe doit contenir au moins une lettre minuscule,");
                            System.out.println("3.Votre mot de passe doit contenir au moins une lettre majuscule,");
                            System.out.println("4.Votre mot de passe doit contenir au moins un chiffre et un caractère spécial,");
                            System.out.println("5.Les espace seront automatiquement supprime.");
                        }

                    } while (!matcherPassword.matches());

                    //Obtention du bytecode de password
                    bytes_md5 = nouveauPassword.getBytes();

                    //Password
                    {
                        md.update(bytes_md5);

                        // Obtient le hachage
                        hash = md.digest();

                        // Convertit le hachage en chaîne de caractères
                        hashString = new String(hash);

                        //Ajout du password hasher
                        nouveauPassword = hashString;

                    }

                    try {
                        //Connection a la basse et Modifiacation de L'user
                        bdConnection.statement.executeUpdate("UPDATE User SET password ='" + nouveauPassword + "', passwordHashed ='" + nouveauPassword + "'  WHERE email='" + email + "'");
                    } catch (SQLException e) {
                        System.out.println(e);
                    }



            }
            else
            {
                System.out.println("L'email saisie est incorecte.");
            }


        } catch (SQLException e) {
            //Pour Afficher d'eventuel erreur
            System.out.println(e);
        }


    }





    /**
     * Cette méthode nous permet de creer un menu pour L'ajout de l'User et de lister les user
     *
     */
    @Override
    public void menu()
    {
        int choix = 0;
        //Boucle sur le choix
        try {

            do {

                System.out.println(" ___________________________________________________");
                System.out.println("|1.Saisir un user                                   |");
                System.out.println("|2.Lister les user                                  |");
                System.out.println("|3.Se connecter (Seul l'admin peu ajouter des role) |");
                System.out.println("|4.Quitter                                          |");
                System.out.println("|___________________________________________________|");
                System.out.println("                Faite votre choix:");
                choix = scan.nextInt();

                switch (choix) {
                    case 1:
                        //Creation d'user
                        createUserInDb();
                        break;

                    case 2:
                        //Affichage de tout les user de la bd
                        afficherUsersInDb();
                        break;
                    case 3:
                        //Verification si l'user est dans la basse
                        getConnection();
                        break;
                }

            } while (!(choix == 4));


            //Fermeture
            try {
                bdConnection.connection.close();
                resultSet.close();
                bdConnection.statement.close();

            } catch (SQLException e) {
                System.out.println(e);

            }
        }catch (Exception e)
        {
            System.out.println(e);
        }
    }

}
