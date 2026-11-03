package dataaccess;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

public class DatabaseManager {
    private static String databaseName;
    private static String dbUsername;
    private static String dbPassword;
    private static String connectionUrl;

    /*
     * Load the database information for the db.properties file.
     */
    static {
        loadPropertiesFromResources();
        createDatabase();
    }

    /**
     * Creates the database if it does not already exist.
     */
    static public void createDatabase() {
        try (var conn = DriverManager.getConnection(connectionUrl, dbUsername, dbPassword);) {
            var statement = conn.createStatement();
            statement.executeUpdate("CREATE DATABASE IF NOT EXISTS " + databaseName);
            conn.setCatalog(databaseName);
            var updatedStatement = conn.createStatement();
            updatedStatement.executeUpdate("""
                CREATE TABLE IF NOT EXISTS users (
                    username TEXT,
                    email TEXT,
                    password TEXT
                );
                """);
            updatedStatement.executeUpdate("""
                CREATE TABLE IF NOT EXISTS auth (
                    authtoken TEXT,
                    username TEXT
                );
                """);
            updatedStatement.executeUpdate("""
                CREATE TABLE IF NOT EXISTS game (
                    gameID INT,
                    whiteUsername TEXT,
                    blackUsername TEXT,
                    gameName TEXT,
                    game TEXT
                );
                """);
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    /**
     * Create a connection to the database and sets the catalog based upon the
     * properties specified in db.properties. Connections to the database should
     * be short-lived, and you must close the connection when you are done with it.
     * The easiest way to do that is with a try-with-resource block.
     * <br/>
     * <code>
     * try (var conn = DatabaseManager.getConnection()) {
     * // execute SQL statements.
     * }
     * </code>
     */
    static Connection getConnection() throws DataAccessException {
        try {
            //do not wrap the following line with a try-with-resources
            var conn = DriverManager.getConnection(connectionUrl, dbUsername, dbPassword);
            conn.setCatalog(databaseName);
            return conn;
        } catch (SQLException ex) {
            throw new DataAccessException("failed to get connection", ex);
        }
    }

    private static void loadPropertiesFromResources() {
        try (var propStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties")) {
            if (propStream == null) {
                throw new Exception("Unable to load db.properties");
            }
            Properties props = new Properties();
            props.load(propStream);
            loadProperties(props);
        } catch (Exception ex) {
            throw new RuntimeException("unable to process db.properties", ex);
        }
    }

    private static void loadProperties(Properties props) {
        databaseName = props.getProperty("db.name");
        dbUsername = props.getProperty("db.user");
        dbPassword = props.getProperty("db.password");

        var host = props.getProperty("db.host");
        var port = Integer.parseInt(props.getProperty("db.port"));
        connectionUrl = String.format("jdbc:mysql://%s:%d", host, port);
    }

    public static void execute(String sqlBlock) {
        try (var conn = DatabaseManager.getConnection();) {
            var statement = conn.createStatement();
            statement.execute(sqlBlock);
        } catch (SQLException | DataAccessException ex) {
            System.out.println(ex);
        }
    }

    public static void executeWithValues(String sqlBlock, ArrayList<String> values) {
        try (var conn = DatabaseManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sqlBlock)) {
            for (int i = 0; i < values.size(); i++) {
                ps.setString(i + 1, values.get(i));
            }
            ps.executeUpdate();
        } catch (SQLException | DataAccessException ex) {
            System.out.println(ex);
        }
    }

    public static HashMap<String, String> selectOne(String sqlBlock) {
        try (var conn = DatabaseManager.getConnection();) {
            var statement = conn.prepareStatement(sqlBlock);
            ResultSet rs = statement.executeQuery();
            return mapify(rs);
        } catch (SQLException | DataAccessException ex) {
            System.out.println(ex);
            return null;
        }
    }

    public static ArrayList<HashMap<String, String>> selectAll(String sqlBlock) {
        ArrayList<HashMap<String, String>> rsList = new ArrayList<>();
        try (var conn = DatabaseManager.getConnection();) {
            var statement = conn.prepareStatement(sqlBlock);
            ResultSet rs = statement.executeQuery();
            while (true) {
                HashMap<String, String> row = mapify(rs);
                if (row == null) {break;}
                rsList.add(row);
            }
            return rsList;
        } catch (SQLException | DataAccessException ex) {
            System.out.println(ex);
            return rsList;
        }
    }

    private static HashMap<String, String> mapify(ResultSet rs) throws SQLException {
        ResultSetMetaData fields = rs.getMetaData();
        HashMap<String, String> object = new HashMap<>();

        if (!rs.next()) {return null;}

        for (int i = 1; i <= fields.getColumnCount(); i++) {
            object.put(fields.getColumnLabel(i), rs.getString(i));
        }

        return object;
    }
}
