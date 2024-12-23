import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Database {
    private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASSWORD = "Gapek124";
    private Connection connection;

    public Connection connect() throws SQLException {
        this.connection= DriverManager.getConnection(URL, USER, PASSWORD);
        return connection;
    }
    public void insert(Entity entity){
        Object []data=entity.getData();
        String query = "INSERT INTO " + entity.getTable() + " (" + entity.getColumns() + ") VALUES (" + String.join(", ", java.util.Collections.nCopies(data.length, "?")) + ")";
        try (PreparedStatement pst = this.connection.prepareStatement(query)) {
            for (int i = 0; i < data.length; i++) {
                if (data[i] instanceof Integer) {
                    pst.setInt(i + 1, (Integer) data[i]);
                } else if (data[i] instanceof Float) {
                    pst.setFloat(i + 1, (Float) data[i]);
                } else if (data[i] instanceof Double) {
                    pst.setDouble(i + 1, (Double) data[i]);
                } else if (data[i] instanceof java.sql.Date) {
                    pst.setDate(i + 1, (java.sql.Date) data[i]);
                } else if (data[i] instanceof java.sql.Timestamp) {
                    pst.setTimestamp(i + 1, (java.sql.Timestamp) data[i]);
                } else if (data[i] instanceof Boolean) {
                    pst.setBoolean(i + 1, (Boolean) data[i]);
                } else {
                    pst.setString(i + 1, data[i].toString());
                }
                
            }
            pst.executeUpdate();
            System.out.println("Data inserted successfully.");
        }
        catch (SQLException e) {
            System.out.println("Error when inserting data");
            e.printStackTrace();
        }
    }
    public ArrayList<ArrayList<String>> read(Entity entity){
        ArrayList<ArrayList<String>> result = new ArrayList<>();
        String columns= entity.getColumns();
        String query = "SELECT " + columns + " FROM " + entity.getTable();
        String[] columnArray = columns.split(",\\s*");
        try (PreparedStatement pst = this.connection.prepareStatement(query); 
            ResultSet rs = pst.executeQuery()) {
            System.out.println("Returned columns:");
            while (rs.next()) {
                ArrayList<String> row=new ArrayList<>();
                for (String column : columnArray) {
                    //System.out.print(rs.getString(column.trim()) + " "); 
                    row.add(rs.getString(column.trim()));
                }
                result.add(row);
                //System.out.println();
            }
        } catch (SQLException e) {
            System.out.println("Error reading data");
            e.printStackTrace();
        }
        return result;
    }
}
