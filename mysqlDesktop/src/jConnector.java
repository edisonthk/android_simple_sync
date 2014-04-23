
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;;

/*
 * jConnector is to connect your mySQL database.
 * 
 *  You need to replace the username and password to log into your database 
 *  Look at line 30  
 *  
 */

public class jConnector {
	
	public Connection connection = null;
	
	public jConnector(){		
		
		try{
			Class.forName("com.mysql.jdbc.Driver");			
		}catch(ClassNotFoundException e){
			System.out.println("Where is your MySQL JDBC Driver?");
			e.printStackTrace();
			return;		
		}
		
		try {
			
			/* 3 things need to be replaced ( database,username and password )
			 * 
			 * if you are using xampp "localhost:3306" no need to change 
			 * but if not you need to change whole things.
			 * 
			 * You need to replace database_name, username, password to your own account.
			 *  
			 */
			
			connection = DriverManager
			.getConnection("jdbc:mysql://localhost:3306/database_name","username","password");	 
			
		} catch (SQLException e) {
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
			return;
		}
		
		if (connection != null) {
			System.out.println("You made it, take control your database now!");
		} else {
			System.out.println("Failed to make connection!");	
		}
	}
}
