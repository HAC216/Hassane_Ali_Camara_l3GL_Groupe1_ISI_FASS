package Main;

import DAO.User.IUser;
import DAO.User.UserImp;

public class Main {


    public static void main(String[] args) {

        //Creation d'un objet UserImp qui implement IUser
        IUser userImpl = new UserImp();

        //Menu d'affichage
        userImpl.menu();

    }
}