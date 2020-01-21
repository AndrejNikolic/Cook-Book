package project;


import com.sun.rowset.CachedRowSetImpl;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.rowset.CachedRowSet;

/**
 *
 * @author Andrej
 */

// Povezivanje sa bazom
public class DBUtil {
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static Connection connection = null;
    private static final String connString = "jdbc:mysql://localhost/project";
    
    public static void dbConnect() throws SQLException, ClassNotFoundException {
        try {   
            Class.forName(JDBC_DRIVER);
        } 
        catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver missing!");
            e.printStackTrace();
            throw e;
        }
        //System.out.println("JDBC Driver registered!");
        
        try {
            connection = DriverManager.getConnection(connString, "andrej", "Pr0j3k4t!Fax");
        } 
        catch (SQLException e) {
            System.err.println("Connection Failed!");
            throw e;
        }
    }
    
    public static void dbDisconnect() throws SQLException {
        try {
            if(connection != null && !connection.isClosed()){
                connection.close();
            }
        } catch (SQLException e) {
            throw e;
        }
    }
    
// Metoda za pozivanje sql statement-a (za select)
    public static ResultSet dbExcecuteQuery(String sqlStatement) throws SQLException, ClassNotFoundException {
        Statement stmt = null;
        ResultSet rs = null;
        CachedRowSet crs = null;
        try {
            dbConnect();
            stmt = connection.createStatement();
            rs = stmt.executeQuery(sqlStatement);
            crs = new CachedRowSetImpl();
            crs.populate(rs);
        } catch (SQLException e) {
            System.out.println("Problem occured in dbExcecuteQuery");
            throw e;
        }
        finally {
            if(stmt!=null){
                stmt.close();
            }
            if(rs!=null){
                rs.close();
            }
            dbDisconnect();
        }
        return crs;
    }

// Metoda za pozivanje sql statement-a (za delete, update i insert)    
    public static void dbExcecuteUpdate(String sqlStatement) throws SQLException, ClassNotFoundException {
        Statement stmt = null;
        try {
            dbConnect();
            stmt = connection.createStatement();
            stmt.executeUpdate(sqlStatement);
        } catch (SQLException e) {
            System.out.println("Problem occured in dbExcecuteQuery");
            throw e;
        }
        finally {
            if(stmt!=null){
                stmt.close();
            }
            dbDisconnect();
        }
    }
}
