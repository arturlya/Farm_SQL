package control;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class SQL_Loader {



    public SQL_Loader(){
        loadDatabase();
    }

    private void loadDatabase(){
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://mysql.webhosting24.1blu.de/db85565x2810214?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "s85565_2810214", "kkgbeste");
            Statement stmt = con.createStatement();
            try {
                stmt.execute("DROP TABLE " + StaticData.farm + ";");
               // stmt.execute("DROP TABLE " + StaticData.kunde + ";");
                stmt.execute("DROP TABLE " + StaticData.pflanze + ";");
                stmt.execute("DROP TABLE " + StaticData.shop + ";");
                stmt.execute("DROP TABLE " + StaticData.lager + ";");
                stmt.execute("DROP TABLE " + StaticData.mitarbieter + ";");
                stmt.execute("DROP TABLE " + StaticData.tier + ";");
                stmt.execute("DROP TABLE "+StaticData.mutationskammer+";");
                stmt.execute("DROP TABLE "+StaticData.wird_gekreuzt+";");

            } catch (Exception e) {
                System.out.println("Tabelle nicht gelöscht.");
            }

            try {
                /*
                stmt.execute("CREATE TABLE "+StaticData.kunde+" (" +
                        "kundenID int NOT NULL AUTO_INCREMENT,"+
                        "kundenName varchar(255) NOT NULL,"+
                        "kundenWunsch varchar (255) NOT NULL,"+
                        ","+
                        "kundenGeld int NOT NULL,"+
                        "PRIMARY KEY(kundenID)"+
                        ");");*/
                stmt.execute("CREATE TABLE "+StaticData.farm+" (" +
                        "farmID int NOT NULL AUTO_INCREMENT,"+
                        "farmGeld double NOT NULL,"+
                        "farmName varchar(255) NOT NULL,"+
                        "farmFelder int NOT NULL,"+
                        "PRIMARY KEY(farmID)"+
                        ");");
                stmt.execute("CREATE TABLE "+StaticData.pflanze+" (" +
                        "pflanzenID int NOT NULL AUTO_INCREMENT,"+
                        "pflanzenArt varchar(255) NOT NULL,"+
                        "wachstumsRate double NOT NULL,"+
                        "wachstum double NOT NULL,"+
                        "feldBelegung int NOT NULL,"+
                        "farmID int NOT NULL,"+
                        "PRIMARY KEY(pflanzenID),"+
                        "FOREIGN KEY(farmID) REFERENCES "+StaticData.farm+"(farmID)"+
                        ");");
                stmt.execute("CREATE TABLE "+StaticData.tier+" (" +
                        "tierID int NOT NULL AUTO_INCREMENT,"+
                        "fleischArt varchar(255) NOT NULL,"+
                        "besonderheiten varchar(255) NOT NULL,"+
                        "wachstumsRate double NOT NULL,"+
                        "wachstum double NOT NULL,"+
                        "farmID int NOT NULL,"+
                        "PRIMARY KEY(tierID),"+
                        "FOREIGN KEY(farmID) REFERENCES "+StaticData.farm+"(farmID)"+
                        ");");
                stmt.execute("CREATE TABLE "+StaticData.lager+" (" +
                        "lagerID int NOT NULL AUTO_INCREMENT,"+
                        "lagerPlatz int NOT NULL,"+
                        "belegterPlatz int NOT NULL,"+
                        "farmID int NOT NULL,"+
                        "PRIMARY KEY(lagerID),"+
                        "FOREIGN KEY(farmID) REFERENCES "+StaticData.farm+"(farmID)"+
                        ");");
                stmt.execute("CREATE TABLE "+StaticData.shop+" (" +
                        "kaufID int NOT NULL AUTO_INCREMENT,"+
                        "entitaet double NOT NULL,"+
                        "preis varchar(255) NOT NULL,"+
                        "entitaetsArt int NOT NULL,"+
                        "PRIMARY KEY(kaufID)"+
                        ");");
                stmt.execute("CREATE TABLE "+StaticData.mitarbieter+" (" +
                        "mitarbeiterID int NOT NULL AUTO_INCREMENT,"+
                        "job varchar(255) NOT NULL,"+
                        "arbeitsplatz varchar(255) NOT NULL,"+
                        "lohn double NOT NULL,"+
                        "PRIMARY KEY(mitarbeiterID),"+
                        "FOREIGN KEY(arbeitsplatz) "+StaticData.farm+"(farmID)"+
                        ");");
            /*    stmt.execute("CREATE TABLE "+StaticData.mutationskammer+" (" +
                        "mutationskammerID int NOT NULL AUTO_INCREMENT,"+
                        "mutationskammern int NOT NULL,"+
                        "farmName varchar(255) NOT NULL,"+
                        "farmFelder int NOT NULL,"+
                        "PRIMARY KEY(farmID)"+
                        ");");
                stmt.execute("CREATE TABLE "+StaticData.wird_gekreuzt+" (" +
                        "kreuzungsID int NOT NULL AUTO_INCREMENT,"+
                        "tier1ID double NOT NULL,"+
                        "tier2ID varchar(255) NOT NULL,"+
                        "mutationskammer int NOT NULL,"+
                        "PRIMARY KEY(kreuzungsID)"+
                        ");");*/
            } catch (Exception e){
                System.out.println("Keine neue Tabelle angelegt.");
            }
        }catch(Exception err){err.printStackTrace();}
    }
}
