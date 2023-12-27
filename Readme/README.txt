Dans ce projet, j'ai essayé de créer une application qui se connecte à une base de données MySQL nommée gestion_user (les informations sur la base de données se trouvent dans le fichier BD.txt, situé à l'extérieur de ce document).

Cette application donne la possibilité à chaque personne de créer un compte, de lister les utilisateurs inscrits dans la base, de se connecter et de quitter.
Les fonctionnaliter sont :
 ___________________________________________________
|1.Saisir un user                                   | //Permet à chaque personne de créer un compte. D'autres fonctionnalités sont gérées, comme le contrôle de l'adresse e-mail.comme le contole de l'email)
|2.Lister les user                                  | //Permet à chaque personne de lister les utilisateurs.
|3.Se connecter (Seul l'admin peu ajouter les role) | //Permet à une personne de se connecter. Seul l'administrateur peut ajouter des rôles.
|4.Quitter                                          | //Permet à une personne de quitter l'application.
|___________________________________________________|


Si une personne se connecte et qu'elle est l'administrateur (défini dans la base de données, il suffit de mettre l'ID du rôle "Admin" dans l'ID de l'utilisateur qui deviendra administrateur), alors elle aura la possibilité de:
 ___________________________________________________
|1.Saisir un Role                                   |//Permet à l'administrateur de créer un nouveau rôle.
|2.Lister les Role                                  |//Permet à l'administrateur de lister les rôles existants.
|3.Affecter un role a un user                       |//Permet à l'administrateur de changer le rôle d'un utilisateur qui n'est pas administrateur.
|4.Modifier un role                                 |//Permet à l'administrateur de modifier les informations d'un rôle existant. Cette modification affectera également tous les utilisateurs qui ont ce rôle.
|5.Supprimer un role                                |//Permet à l'administrateur de supprimer un rôle existant. Cette suppression affectera également tous les utilisateurs qui ont ce rôle.
|6.Quitter                                          |//Permet à l'administrateur de quitter l'application.
|___________________________________________________|

Si une personne se connecte et qu'elle n'est pas l'administrateur, alors elle n'aura que les informations suivantes :

Son nom d'utilisateur
Son mot de passe
Son rôle


Pendant la connection si la personne oublie son mot de passe elle a la possibiliter de la changer grace a son email (si on avait d'autre information personnel la modification serait plus securiser car ici tout monde peu changer le mot de passe de tout le monde grace au fonctionnaliter lister les user).