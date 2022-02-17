package ifpr.pgua.eic.projetovendas.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class FabricaConexoes {
    
   private static int MAX_CONNECTIONS=25;

   private static String URL_DB = "url";
   private static String DB_NAME = "databasename";
   private static String USERNAME = "username";
   private static String PASSWORD = "password";

   private static String CON_STRING="jdbc:mysql:"+URL_DB+DB_NAME;

   private Connection[] conexoes;
   
   private static FabricaConexoes instance;

   private FabricaConexoes(){
       conexoes = new Connection[MAX_CONNECTIONS];
   }
   
   public static FabricaConexoes getInstance(){
       if(instance != null){
          return instance;
       }
       instance = new FabricaConexoes();
       return instance;
   }
  
   public Connection getConnection() throws SQLException{

       for(int i=0;i<MAX_CONNECTIONS;i++){
           if(instance.conexoes[i]==null || instance.conexoes[i].isClosed()){
               instance.conexoes[i] = DriverManager.getConnection(CON_STRING,USERNAME,PASSWORD);
               return instance.conexoes[i];
           }
       }
       throw new SQLException("Máximo de conexões");
   }

}
