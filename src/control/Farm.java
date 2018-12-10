package control;

import model.Tier;
import model.abitur.datenbanken.mysql.DatabaseConnector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class Farm {

    private Statement stmt;

    public Farm(){
        try{
            Connection con = DriverManager.getConnection("jdbc:mysql://mysql.webhosting24.1blu.de/db85565x2810214?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "s85565_2810214", "kkgbeste");
            stmt = con.createStatement();
            stmt.execute("INSERT INTO "+StaticData.farm+"(farmGeld,farmName,farmFelder) VALUES(500,'TESTFARM',100);");
        }catch(Exception err){err.printStackTrace();}
    }

    public void kill(Tier tier){
        if(tier.getBesonderheiten().equals("Eier")){

        }
        if(tier.getBesonderheiten().equals("Milch")){

        }
    }
}
