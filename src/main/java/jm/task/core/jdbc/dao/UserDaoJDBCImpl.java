package jm.task.core.jdbc.dao;

import com.mysql.cj.jdbc.MysqlDataSource;
import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {

    public UserDaoJDBCImpl() {
    }

    public void createUsersTable() {

        try (Connection conn = Util.INSTANCE.getJDBCSource().getConnection()) {
            conn.setAutoCommit(false);
            try (Statement st = conn.createStatement()) {
                st.executeUpdate("""
                        create table  if not exists user(
                            id bigint auto_increment,
                            name varchar(255) not null,
                            lastname varchar(255) not null,
                            age tinyint not null,
                            constraint user_pk
                                primary key (id)
                         );""");
                conn.commit();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                try {
                    conn.rollback();
                } catch (Exception e2) {
                    e.addSuppressed(e2);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public void dropUsersTable() {
        try (Connection conn = Util.INSTANCE.getJDBCSource().getConnection()) {
            conn.setAutoCommit(false);
            try (Statement st = conn.createStatement()) {
                st.executeUpdate("""
                        drop table if exists user;
                        """);
                conn.commit();

            } catch (SQLException e) {
                System.out.println(e.getMessage());
                try {
                    conn.rollback();
                } catch (Exception e2) {
                    e.addSuppressed(e2);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        try (Connection conn = Util.INSTANCE.getJDBCSource().getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement st = conn.prepareStatement("""
                    insert into user
                    (name,lastName,age)
                    values(?,?,?);
                    """)) {
                st.setString(1, name);
                st.setString(2, lastName);
                st.setByte(3, age);
                st.executeUpdate();
                conn.commit();

            } catch (SQLIntegrityConstraintViolationException e) {
                System.out.printf("""
                                Creation of a new user item failed because the record violates the data model :%s%n""",
                        e.getMessage());
                try {
                    conn.rollback();
                } catch (Exception e2) {
                    e.addSuppressed(e2);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void removeUserById(long id) {
        try (Connection conn = Util.INSTANCE.getJDBCSource().getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement st = conn.prepareStatement(
                    "DELETE FROM user WHERE id=?")) {
                st.setLong(1, id);
                conn.commit();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                try {
                    conn.rollback();
                } catch (Exception e2) {
                    e.addSuppressed(e2);
                    throw e;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<User> getAllUsers() {
        List<User> lst = new ArrayList<>();
        try (Connection conn = Util.INSTANCE.getJDBCSource().getConnection();
             PreparedStatement st = conn.prepareStatement("SELECT  * FROM user;")) {
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                User user = new User(rs.getString("name"),
                        rs.getString("lastName"), rs.getByte("age"));
                user.setId(rs.getLong("id"));
                lst.add(user);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return lst;

    }

    public void cleanUsersTable() {
        try (Connection conn = Util.INSTANCE.getJDBCSource().getConnection()) {
            conn.setAutoCommit(false);
            try (Statement st = conn.createStatement()) {

                st.executeUpdate("TRUNCATE TABLE user;");
                conn.rollback();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                try {
                    conn.rollback();
                } catch (Exception e2) {
                    e.addSuppressed(e2);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
