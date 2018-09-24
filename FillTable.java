import java.sql.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class FillTable{

	private Connection c = null;
	private Statement stmt = null;

	private String csv_name;
	private String db_name;
	private String table_name;

	public FillTable(String par_csv,String par_db,String par_table){
		csv_name = par_csv;
		db_name = par_db;
		table_name = par_table;
	}

	public void insert(){

		try {
    		Class.forName("org.sqlite.JDBC");
    		c = DriverManager.getConnection("jdbc:sqlite:"+db_name);
    		c.setAutoCommit(false);
    		System.out.println("Opened database successfully");

    		stmt = c.createStatement();
    		
    		String baseSQL = "INSERT INTO " + table_name;
    		File file = new File(csv_name);
    		try{
    			Scanner inputStream = new Scanner(file);
    			String keys = inputStream.nextLine(); // get table key (1st line)
    			baseSQL = baseSQL + " (" + keys + ") ";

    			while(inputStream.hasNextLine()){
    				String data = inputStream.nextLine();
    				String entry = baseSQL + "VALUES ("+data+");";
    				System.out.println(entry);
    				stmt.executeUpdate(entry);
    			}
    		}
    		catch(FileNotFoundException e){
         		e.printStackTrace();
      		}

    		stmt.close();
    		c.commit();
    		c.close();
		} catch ( Exception e ) {
    		System.err.println( e.getClass().getName() + ": " + e.getMessage() );
    		System.exit(0);
   		}
    	System.out.println("Records created successfully");
	}

	public static void main(String[] args) {
		if(args.length != 3){
    		System.out.println("needs 3 command args\narg1: Schema csv file\narg2: db name\narg3: table name");
    	}
    	else{
    		FillTable ft1 = new FillTable(args[0], args[1], args[2]);
    		ft1.insert();
    	}
	}
}