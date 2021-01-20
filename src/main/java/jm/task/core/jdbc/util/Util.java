package jm.task.core.jdbc.util;


import com.mysql.cj.jdbc.MysqlDataSource;
import jm.task.core.jdbc.dao.UserDao;
import jm.task.core.jdbc.dao.UserDaoJDBCImpl;

import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Util{

    public enum Mode {
        JDBC,
        HIBERNATE
    }
    private  static final String DB_URL= "jdbc:mysql://localhost:3306/core_task_schema";
    private  static final String DB_USER = "root";
    private  static final String DB_PASSWORD = "Z@mbezi33";
    private  static final String DB_SERVER_NAME = "localhost";
    private  static final int DB_PORT_NUMBER = 3306;
    private  static final String DB_NAME = "core_task_schema";

    private static MysqlDataSource jdbcDataSource;

    public Connection getConnection() throws SQLException {
        Connection conn = jdbcDataSource.getConnection();
        conn.setAutoCommit(false);
        return conn;
    }

    public void  setJDBCSource() throws SQLException, ClassNotFoundException {

        Class.forName("com.mysql.cj.jdbc.Driver");
        jdbcDataSource = new MysqlDataSource();
        jdbcDataSource.setURL(DB_URL);
        jdbcDataSource.setUseSSL( false );
        jdbcDataSource.setServerTimezone( "UTC" );
        jdbcDataSource.setUser( DB_USER );
        jdbcDataSource.setPassword( DB_PASSWORD );
        jdbcDataSource.setAllowPublicKeyRetrieval(true);
        jdbcDataSource.setCharacterEncoding(StandardCharsets.UTF_8.name());

    }

}
