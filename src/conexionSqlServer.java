import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class conexionSqlServer {
    private static final String URL = "jdbc:sqlserver://ERUIZ;databaseName=Universidad;integratedSecurity=true;encrypt=false";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }
}