package Entities;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;


public class BdConnection {


    //Creation d'un objet connection accessible pour tout les objets de BdConnection
    public Connection connection;

    {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Utilisez le nom du service (nom du conteneur) comme hôte
            connection = DriverManager.getConnection("jdbc:mysql://mysql-1:3306/gestion_user", "root", "");

        } catch (SQLException e) {
            System.out.println(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    //Creation d'un objet statement accessible pour tout les objet de BdConnection
    public Statement statement;

    {
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }




}
