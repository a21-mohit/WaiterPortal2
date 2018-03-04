Android app to used by waiters in Restaurant Order System

ManagerPortal, MySql database, REST APIs must be on same system.

project.zip in ManagerPortal git contains required database and php files for REST api.

Set these constants in /src/main/java/com/possystem/waiterportal/BackgroundWorker.java according to your use case.

final String managerIP = "192.168.0.10"; //ManagerPortal system IP

final int managerPort = 9765; //ManagerPortal listening Port

final String restAPIip = "192.168.0.10";  // REST APIs 

final String login_url = "http://"+restAPIip+"/restaurant/login.php";

final String category_url="http://"+restAPIip+"/restaurant/category.php";

final String menuitem_url="http://"+restAPIip+"/restaurant/menuitem.php";