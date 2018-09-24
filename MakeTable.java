import java.sql.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;


public class MakeTable {

   private Connection c = null;
   private Statement stmt = null;

   private String csv_name;
   private String db_name;
   private String table_name;


   public MakeTable(String par_csv,String par_db,String par_table){
      csv_name = par_csv;
      db_name = par_db;
      table_name = par_table;
   }

   public void create(){
      List<String> values = new ArrayList<String>();

      // Start of Scanner Code
      File file = new File(csv_name);
      try{
         Scanner inputStream = new Scanner(file);
         while(inputStream.hasNextLine()){
            String data = inputStream.nextLine(); //gets one line
            values.add(data);
            System.out.println(data);
         }
      }
      catch(FileNotFoundException e){
         e.printStackTrace();
      }
      try {
         Class.forName("org.sqlite.JDBC");
         c = DriverManager.getConnection("jdbc:sqlite:"+db_name);
         System.out.println("Opened database successfully");

         stmt = c.createStatement();
         
         String sql = "CREATE TABLE " +table_name+"(";
                        // "(ID INT PRIMARY KEY     NOT NULL," +
                        // " NAME           TEXT    NOT NULL)"; 
         
         for(String line: values){
            sql = sql +"\n"+ line;
         }
         sql = sql+")";

         stmt.executeUpdate(sql);
         stmt.close();
         c.close();
      } catch ( Exception e ) {
         System.err.println( e.getClass().getName() + ": " + e.getMessage() );
         System.exit(0);
      }
      System.out.println("Table created successfully");
   }

   public static void main( String args[] ) {
      // needs 3 command args
      // arg1: Schema csv file
      // arg2: db name
      // arg3: table name

      // tests command line args
      // for(String arg : args){
      //    System.out.println(arg);
      // }
      if(args.length != 3){
         System.out.println("needs 3 command args\narg1: Schema csv file\narg2: db name\narg3: table name");
      }
      else{
         MakeTable mt1 = new MakeTable(args[0], args[1], args[2]);
         mt1.create();
      }
   }
}