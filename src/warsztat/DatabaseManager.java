/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package warsztat;


import java.sql.*;
import java.util.ArrayList;

/**
 *
 * @author Kamil Sagalara
 */
public class DatabaseManager implements IDatabaseInterface{
    

    private String dbName;
    private String connectionString;
    private Connection c;

    public DatabaseManager(String dbName) {
        this.dbName = dbName;
        connectionString = "jdbc:sqlite:"+dbName+".db";
    }
    
    
    private Connection getConnection()
    {
        Connection c = null;
        
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            
            System.out.println(e.getMessage());
        }
        
        
        try {          
            c=DriverManager.getConnection(connectionString);         
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        
        return c;
    }
    
    @Override
    public void AddItem(PartsDataModel parts) throws SQLException{
        
        PreparedStatement preparedStatement=null;
        Connection c=null;
        String insertSQL = "INSERT INTO MainWorkshop (Name,Type, Parameters," +
                                "Code, Quantity, Usage, Other, Missing)" +
                                "VALUES (?,?,?,?,?,?,?,?)";       
            try {
            c = getConnection();
            c.setAutoCommit(false);
       
            
            preparedStatement = c.prepareStatement(insertSQL);
            preparedStatement.setString(1, parts.getName());
            preparedStatement.setString(2, parts.getType());
            preparedStatement.setString(3, parts.getParameters());
            preparedStatement.setString(4, parts.getCode());
            preparedStatement.setInt(5, parts.getQuantity());
            preparedStatement.setString(6, parts.getUsage());
            preparedStatement.setString(7, parts.getOther());
            preparedStatement.setString(8, parts.getMissing());
            
            preparedStatement.executeUpdate();
            c.commit();
            
            preparedStatement.close();
            c.close();
            
            } catch (SQLException e) {  
                c.close();
                throw e;
            }    
        }

    @Override
    public void EditItem(PartsDataModel parts) {
        
        PreparedStatement preparedStatement;
        String updateSQL = "UPDATE MainWorkshop "
                + "SET Name=?,Type=?,Parameters=?,Code=?,Quantity=?,Usage=?,"
                + "Other=?,Missing=?"
                + "WHERE ID=?";
        
        System.out.println(parts.getName());
        
        try {
            Connection c  = getConnection();
            
            preparedStatement = c.prepareStatement(updateSQL);
            preparedStatement.setString(1, parts.getName());
            preparedStatement.setString(2, parts.getType());
            preparedStatement.setString(3, parts.getParameters());
            preparedStatement.setString(4, parts.getCode());
            preparedStatement.setInt(5, parts.getQuantity());
            preparedStatement.setString(6, parts.getUsage());
            preparedStatement.setString(7, parts.getOther());
            preparedStatement.setString(8, parts.getMissing());
            preparedStatement.setInt(9, parts.getID());
            preparedStatement.executeUpdate();

            if (preparedStatement != null) {
                preparedStatement.close();
            }

            if (c != null) {
                c.close();
            }
            
            
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error: "+e.getMessage());
        }
    }

    @Override
    public void DeleteItem(PartsDataModel parts){
        PreparedStatement preparedStatement;
        String updateSQL = "DELETE FROM MainWorkshop "
                + "WHERE ID = ?";
        try {
            Connection c  = getConnection();
            
            preparedStatement = c.prepareStatement(updateSQL);
            preparedStatement.setInt(1, parts.getID());
            preparedStatement.executeUpdate();
            
            if (preparedStatement != null) {
                preparedStatement.close();
            }

            if (c != null) {
                c.close();
            }
            
            
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
    
    @Override
    public ArrayList<PartsDataModel> GetAllItems(){
        
        ArrayList<PartsDataModel> list = new ArrayList<>();
        Statement statement;
        String selectSQL = "SELECT * FROM MainWorkshop";
        
        try {
            Connection c  = getConnection();
            
            statement = c.createStatement();
            ResultSet rs = statement.executeQuery(selectSQL);
            
            while(rs.next())
            {
                PartsDataModel data = new PartsDataModel(rs.getInt("ID"),rs.getString("Name"),
                rs.getString("Type"),rs.getString("Parameters"),
                        rs.getString("Code"),rs.getInt("Quantity"),
                        rs.getString("Usage"),rs.getString("Other"),
                        rs.getString("Missing"));        
                
                list.add(data);
                
            }
            
            if (statement != null) {
                statement.close();
            }

            if (c != null) {
                c.close();
            }
            
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        
        return list;
    }
    
    
    //Initialization - do not use for now    
    private static final String CREATE_MAINWORKSHOP_STATEMENT=
            "Create Table MainWorkshop"
            + "(ID INTEGER PRIMARY KEY AUTOINCREMENT"
            + "Name TEXT PRIMARY KEY NOT NULL,"
            + "Type TEXT,"
            + "Parameters TEXT,"
            + "Code TEXT,"
            + "Quantity INT,"
            + "Usage TEXT,"
            + "Other TEXT,"
            + "Missing TEXT)";
    private void CreateDatabase(){
        try {
            Class.forName("org.sqlite.JDBC");
            Connection c = DriverManager.getConnection
        (connectionString);
            
            
        } catch (Exception e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
    }
    private void AddWorkShopTable()
    {
        try {
            Class.forName("org.sqlite.JDBC");
            Connection c=DriverManager.getConnection(connectionString);
            Statement statement = c.createStatement();
            statement.executeUpdate(CREATE_MAINWORKSHOP_STATEMENT);
            statement.close();
            c.close();
            
        } catch (Exception e) {
         System.err.println( e.getClass().getName() + ": " + e.getMessage() );
         System.exit(0);
        }
    }





}
