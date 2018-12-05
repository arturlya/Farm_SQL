package control;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class SQL_Loader {

    public SQL_Loader(){

    }

    private void loadDatabase(){
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://mysql.webhosting24.1blu.de/db85565x2810214?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "s85565_2810214", "kkgbeste");
            Statement stmt = con.createStatement();
            try {
                stmt.execute("DROP TABLE " + StaticData.farm + ";");
                stmt.execute("DROP TABLE " + StaticData.kunde + ";");
                stmt.execute("DROP TABLE " + StaticData.pflanze + ";");
                stmt.execute("DROP TABLE " + StaticData.shop + ";");
                stmt.execute("DROP TABLE " + StaticData.lager + ";");
                stmt.execute("DROP TABLE " + StaticData.mitarbieter + ";");
                stmt.execute("DROP TABLE " + StaticData.tier + ";");

            } catch (Exception e) {
                System.out.println("Tabelle nicht gelöscht.");
            }

            try {
                stmt.execute("CREATE TABLE "+StaticData.kunde+" (" +
                        "kundenID int NOT NULL AUTO_INCREMENT,"+
                        "kundenName varchar(255) NOT NULL,"+
                        "kundenWunsch varchar (255) NOT NULL,"+
                        "kundenGeld int NOT NULL,"+
                        "PRIMARY KEY(kundenID)"+
                        ");");
                stmt.execute("CREATE TABLE "+StaticData.pflanze+" (" +
                        "pflanzenID int NOT NULL AUTO_INCREMENT,"+
                        "pflanzenArt varchar(255) NOT NULL,"+
                        "wachstumsRate double NOT NULL,"+
                        "wachstum double NOT NULL,"+
                        "PRIMARY KEY(pflanzenID)"+
                        ");");
                stmt.execute("CREATE TABLE "+StaticData.farm+" (" +
                        "farmID int NOT NULL AUTO_INCREMENT,"+
                        "farmGeld varchar(255) NOT NULL,"+
                        "wachstumsRate double NOT NULL,"+
                        "wachstum double NOT NULL,"+
                        "PRIMARY KEY(pflanzenID)"+
                        ");");
            } catch (Exception e){
                System.out.println("Keine neue Tabelle angelegt.");
            }
        }catch(Exception err){err.printStackTrace();}
    }
}
