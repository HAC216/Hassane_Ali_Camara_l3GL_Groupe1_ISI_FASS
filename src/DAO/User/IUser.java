package DAO.User;

public interface IUser {
    void createUserInDb();
    void afficherUsersInDb();

    void getConnection();

    void setPasswordInDb();
    void menu();
}
