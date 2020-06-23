package helpers.datebase;

import helpers.Common;
import helpers.dictionary.Profile;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class Connections {

    public static final String FILE_PROPERTIES_TESTS_DB = "test-db.properties";

    private static final String FILE_PROPERTIES_DIR = "/src/main/resources/";

    private static final String ORG_POSTGRESQL_DRIVER = "org.postgresql.Driver";
    private static final String MYSQL_DRIVER = "com.mysql.cj.jdbc.Driver";

    private static Properties propertiesTestsDb = getPropertiesToDatabase(FILE_PROPERTIES_TESTS_DB);

    public static Connection getStorePostgresql() {
        return getStorePostgresql(Profile.getProfileFromProperty());
    }

    public static Connection getStorePostgresql(Profile profile) {
        return getPostgresqlTestsDbConnection(profile, DatabaseName.STORE_POSTGRESQL);
    }

    public static Connection getStoreMysql() {
        return getStoreMysql(Profile.getProfileFromProperty());
    }

    public static Connection getStoreMysql(Profile profile) {
        return getMysqlTestsDbConnection(profile, DatabaseName.STORE_MYSQL);
    }

    public static Connection getLoginWeb(Profile profile) {
        return getMysqlTestsDbConnection(profile, DatabaseName.STORE_MYSQL);
    }

    private static Connection getPostgresqlTestsDbConnection(Profile profile, DatabaseName databaseName) {
        return getPostgresqlConnection(ConnectionParams.getParams(propertiesTestsDb, databaseName, profile));
    }

    private static Connection getPostgresqlConnection(ConnectionParams connectionParams) {
        Connection connection;
        try {
            Class.forName(ORG_POSTGRESQL_DRIVER);
            connection = DriverManager.getConnection(connectionParams.getUrl(), connectionParams.getLogin(), connectionParams.getPassword());
        } catch (SQLException e) {
            Common.reporter().logError("Connection error: ", e);
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            Common.reporter().logError("Failed load class ", e);
            throw new RuntimeException("Failed to initialize Postgresql driver: ", e);
        }
        return connection;
    }

    private static Connection getMysqlTestsDbConnection(Profile profile, DatabaseName databaseName) {
        return getMysqlConnection(ConnectionParams.getParams(propertiesTestsDb, databaseName, profile));
    }

    private static Connection getMysqlConnection(ConnectionParams connectionParams) {
        Connection connection;
        try {
            Class.forName(MYSQL_DRIVER);
            connection = DriverManager.getConnection(connectionParams.getUrl(), connectionParams.getLogin(), connectionParams.getPassword());
        } catch (SQLException e) {
            Common.reporter().logError("Connection error: ", e);
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            Common.reporter().logError("Failed load class ", e);
            throw new RuntimeException("Failed to initialize MySql driver: ", e);
        }
        return connection;
    }

    private static Properties getPropertiesToDatabase(String fileName) {
        Properties properties = new Properties();
        try {
            properties.load(new StringReader(Common.getFileContentString(
                    new File(String.format("%s%s%s", System.getProperty("user.dir"), FILE_PROPERTIES_DIR, fileName)))));
        } catch (IOException e) {
            Common.reporter().logError("Failed to load .properties file: ", e);
            throw new RuntimeException(e);
        }
        return properties;
    }

    public static ResultSet executeSelect(Connection connection, String selectQuery) {
        try {
            Statement statement = connection.createStatement();
            return statement.executeQuery(selectQuery);
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static void executeQuery(Connection connection, String query) {
        try {
            Statement statement = connection.createStatement();
            statement.execute(query);
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
