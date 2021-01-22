package jm.task.core.jdbc.util;

import com.mysql.cj.jdbc.MysqlDataSource;
import jm.task.core.jdbc.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

public class Util{

    private  static final String DB_URL= "jdbc:mysql://localhost:3306/core_task_schema";
    private  static final String DB_USER = "root";
    private  static final String DB_PASSWORD = "Z@mbezi33";
    private  static final String DB_SERVER_NAME = "localhost";
    private  static final int DB_PORT_NUMBER = 3306;
    private  static final String DB_NAME = "core_task_schema";

    private static MysqlDataSource jdbcDataSource;
    private static StandardServiceRegistry registry;
    private static SessionFactory sessionFactory;

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

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {

                Configuration conf = new Configuration();
                Properties settings = new Properties();
                settings.put(Environment.DRIVER,"com.mysql.cj.jdbc.Driver");
                settings.put(Environment.URL,DB_URL);
                settings.put(Environment.USER,DB_USER);
                settings.put(Environment.PASS,DB_PASSWORD);
                //settings.put(Environment.SHOW_SQL,"true");
                settings.put(Environment.AUTO_CLOSE_SESSION,"true");
                settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS,"thread");

                conf.setProperties(settings);
                conf.addAnnotatedClass(User.class);
                ServiceRegistry serviceRegistry =
                        new StandardServiceRegistryBuilder().applySettings(settings).build();
                sessionFactory =  conf.buildSessionFactory(serviceRegistry);

            } catch (Exception e) {
                System.out.println("SessionFactory creation failed");
                if (registry != null) {
                    StandardServiceRegistryBuilder.destroy(registry);
                }
            }
        }
        return sessionFactory;
    }

}
