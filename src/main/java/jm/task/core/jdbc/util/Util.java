package jm.task.core.jdbc.util;

import com.mysql.cj.jdbc.MysqlDataSource;
import jm.task.core.jdbc.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Properties;

public enum Util {

    INSTANCE;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/core_task_schema";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Z@mbezi33";

    private  MysqlDataSource jdbcDataSource;
    private  SessionFactory sessionFactory;


    public  MysqlDataSource getJDBCSource() throws SQLException {
        if (jdbcDataSource == null) {
            MysqlDataSource ds = new MysqlDataSource();
            jdbcDataSource = new MysqlDataSource();
            jdbcDataSource.setURL(DB_URL);
            jdbcDataSource.setUser(DB_USER);
            jdbcDataSource.setPassword(DB_PASSWORD);
            jdbcDataSource.setCharacterEncoding(StandardCharsets.UTF_8.name());
        }
        return jdbcDataSource;

    }

    public  SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            ServiceRegistry serviceRegistry = null;
            try {

                Configuration conf = new Configuration();
                Properties settings = new Properties();
                settings.put(Environment.DRIVER, "com.mysql.cj.jdbc.Driver");
                settings.put(Environment.URL, DB_URL);
                settings.put(Environment.USER, DB_USER);
                settings.put(Environment.PASS, DB_PASSWORD);
                //settings.put(Environment.SHOW_SQL, "true");
                settings.put(Environment.AUTO_CLOSE_SESSION, "false");
                settings.put(Environment.DIALECT, "org.hibernate.dialect.MySQL8Dialect");
                settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
             //   settings.put(Environment.HBM2DDL_AUTO, "validate");

                conf.setProperties(settings);
                conf.addAnnotatedClass(User.class);
                serviceRegistry =
                        new StandardServiceRegistryBuilder().applySettings(settings).build();
                sessionFactory = conf.buildSessionFactory(serviceRegistry);

            } catch (Exception e) {
                System.out.println("SessionFactory creation failed");
                if (serviceRegistry != null) {
                    StandardServiceRegistryBuilder.destroy(serviceRegistry);
                }
                if (sessionFactory != null) {
                    sessionFactory.close();
                }
            }
        }
        return sessionFactory;
    }

}
