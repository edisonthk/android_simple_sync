import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/*
 * DatabaseAdapter is to easy developer to read, write database
 * 
 * DatabaseAdapter is implemented with jConnector class.
 * By declaring DatabaseAdapter, you are already able to access to MySQL database
 * without asking jConnector again to connect MySQL database.
 * 
 * Method implemented in this class have
 * createTable 	-> to allow database create table
 * 				-> changing sentences in addBatch method if you want to change column be created
 * 
 * updateData 	-> update database  
 * deleteData	-> delete a single row with designated column
 * retrieveData	-> Select all item inside table declared and return ResultSet 
 * close		-> close database
 * 
 * Caution !!! If you using different table you need to replace mDbTable to your own table name 
 */

public class DatabaseAdapter {
	public String mDbTable = "blank";
	
	private static Connection mDBconnect;
	private static Statement mDBstatement;
	
	public DatabaseAdapter() throws SQLException{
		jConnector c = new jConnector();
		mDBconnect = c.connection;
		mDBstatement = (Statement)mDBconnect.createStatement();
	}	
	
	public void createTable() throws SQLException{
		
		mDBconnect.setAutoCommit(false);		
						
		mDBstatement.addBatch("CREATE TABLE "+ mDbTable +"( androidTalk TEXT "+
				", desktopTalk TEXT )");		
						
		mDBstatement.executeBatch();
		mDBconnect.commit();		
	}	
	
	public void updateData (String column,String value)throws SQLException{
		
		mDBstatement.executeUpdate("UPDATE "+mDbTable+" SET "+ column +" = '"+value+"'");		
		
	}
	
	// Cannot delete multiple items but one
	public boolean deleteData (String column,String target) throws SQLException{
					
		mDBstatement.executeUpdate("DELETE FROM "+ mDbTable +"WHERE "
		+column+ " = " +target+ ";");
		return true;
		
	}
	
	public ResultSet retrieveData() throws SQLException{
		
		return (ResultSet) mDBstatement.executeQuery("SELECT * FROM "+ mDbTable);		
		
	}
	
	public void close() throws SQLException{
		
		if(mDBconnect != null){
			mDBconnect.close();
		}
		if(mDBstatement != null){
			mDBstatement.close();
		}
		
	}
			

}

