package control;

import java.sql.*;

/**
 * Zur Benutzung dieser Klasse muss ein JDBC-Connector als Bibliothek in das Projekt eingebunden sein.
 */
public class SQL_Demo {

    private String name;

    public SQL_Demo(){
        name = StaticData.kunde;
        runDemo();
    }

    public void runDemo(){

        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://mysql.webhosting24.1blu.de/db85565x2810214?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "s85565_2810214", "kkgbeste");
            Statement stmt = con.createStatement();

            /*try {
                stmt.execute("DROP TABLE "+name+";");
            } catch (Exception e){
                System.out.println("Tabelle nicht gelöscht.");
            }

            try {
                stmt.execute("CREATE TABLE "+name+" (" +
                        "wunschID int NOT NULL AUTO_INCREMENT,"+
                        "kundenName varchar(255) NOT NULL,"+
                        "kundenWunsch varchar (255) NOT NULL,"+
                        "anzahl int NOT NULL,"+
                        "kundenGeld int NOT NULL,"+
                        "PRIMARY KEY(wunschID,kappa)"+
                        ");");
                } catch (Exception e){
                    System.out.println("Keine neue Tabelle angelegt.");
            }*/

            //stmt.execute("Insert into "+name+" (kundenName,kundenWunsch,anzahl,kundenGeld) VALUES ('Florian Wirtz','Rind',"+(int)(Math.random()*10+1)+","+(int)(Math.random()*11+10)+")");

        } catch(Exception e){
            e.printStackTrace();
        }


    }

}
