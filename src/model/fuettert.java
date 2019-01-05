package model;

import control.StaticData;

import java.sql.*;

public class fuettert {
    private static Connection con;
    private static Statement stmt;

    {
        try {
            con = DriverManager.getConnection("jdbc:mysql://mysql.webhosting24.1blu.de/db85565x2810214?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "s85565_2810214", "kkgbeste");
            stmt = con.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }



    public static void add(int mitarbeiterID, int tierID){
        try {
            con = DriverManager.getConnection("jdbc:mysql://mysql.webhosting24.1blu.de/db85565x2810214?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "s85565_2810214", "kkgbeste");
            stmt = con.createStatement();
            stmt.execute("INSERT INTO "+ StaticData.fuettert+" (mitarbeiterID,tierID) VALUES ("+mitarbeiterID+","+ tierID+");");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void remove(int mitarbeiterID){
        try{
            con = DriverManager.getConnection("jdbc:mysql://mysql.webhosting24.1blu.de/db85565x2810214?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "s85565_2810214", "kkgbeste");
            stmt = con.createStatement();
            stmt.execute("DELETE FROM "+ StaticData.fuettert+" WHERE mitarbeiterID = "+mitarbeiterID+";");
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public static boolean hasRelation(int tierID){
        ResultSet result;
        try{

            result = stmt.executeQuery("SELECT fuettertID FROM "+ StaticData.fuettert+" WHERE EXISTS (SELECT * FROM "+ StaticData.fuettert+" WHERE tierID = "+tierID+")");
            while(result.next()){
                return true;
            }
        }catch(SQLException e){e.printStackTrace();}
        catch(NullPointerException e){return false;}
        return false;
    }
}
