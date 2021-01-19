package jm.task.core.jdbc.util;

import com.mysql.cj.jdbc.MysqlDataSource;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Util {

    //private  static final String DB_URL= "jdbc:mysql://localhost:3306/core_task_schema?allowPublicKeyRetrieval=true&useSSL=false&useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC";
    private  static final String DB_USER = "root";
    private  static final String DB_PASSWORD = "Z@mbezi33";
    private  static final String DB_SERVER_NAME = "localhost";
    private  static final int DB_PORT_NUMBER = 3306;
    private  static final String DB_NAME = "core_task_schema";

    private Connection myConnection;
    public static  Connection getConnection() throws SQLException, ClassNotFoundException {

        Class.forName("com.mysql.cj.jdbc.Driver");

        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setUseSSL( false );
        dataSource.setServerTimezone( "UTC" );
        dataSource.setServerName( DB_SERVER_NAME );
        dataSource.setDatabaseName( DB_NAME );
        dataSource.setPortNumber( DB_PORT_NUMBER );
        dataSource.setUser( DB_USER );
        dataSource.setPassword( DB_PASSWORD );
        dataSource.setAllowPublicKeyRetrieval(true);
        dataSource.setCharacterEncoding(StandardCharsets.UTF_8.name());

        return dataSource.getConnection();

    }

    public static void closeConnection(Connection conn) throws SQLException {
            conn.close();
    }
}