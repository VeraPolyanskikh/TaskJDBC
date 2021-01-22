package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    private  Util util;

    public UserDaoJDBCImpl() {
        try {
            util = new Util();
            util.setJDBCSource();
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }

    public void createUsersTable() {

        try (Connection conn = util.getConnection()) {
            try (Statement st = conn.createStatement()) {
                st.executeUpdate("""
                create table  if not exists user(
                    id bigint auto_increment,
                    name varchar(255) not null,
                    lastname varchar(255) not null,
                    age tinyint not null,
                    constraint user_pk
                        primary key (id)
                 );                     
                """);
                conn.commit();
            }
            catch(SQLException e) {
                e.printStackTrace();
                try{
                    conn.rollback();
                }catch(Exception e2) {
                    e2.printStackTrace();
                }
            }
        }catch(SQLException e) {
            e.printStackTrace();
        }

    }

    public void dropUsersTable() {
        try (Connection conn = util.getConnection()) {
            try (Statement st = conn.createStatement()) {
                st.executeUpdate("""
                    drop table if exists user;
                    """);
                conn.commit();

            }catch(SQLException e) {
                e.printStackTrace();
                try{
                    conn.rollback();
                }catch(Exception e2) {
                    e2.printStackTrace();
                }
            }
        }catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        try (Connection conn = util.getConnection()) {
            try (Statement st = conn.createStatement()) {
                st.executeUpdate(String.format("""
                    insert into user
                    (name,lastName,age)
                    values('%s','%s',%d);
                    """,name,lastName,age));
                conn.commit();

            }catch(SQLException e) {
                e.printStackTrace();
                try{
                    conn.rollback();
                }catch(Exception e2) {
                    e2.printStackTrace();
                }
            }
        }catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeUserById(long id) {
        try (Connection conn = util.getConnection()) {
            try (Statement st = conn.createStatement()) {
                st.executeUpdate("DELETE FROM user WHERE id="+id );
                conn.commit();
            }catch(SQLException e) {
                try{
                    conn.rollback();
                }catch(Exception e2) {
                   e.addSuppressed(e2);
                   throw e;
                }
            }
        }catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public List<User> getAllUsers() {
        List<User> lst  = new ArrayList<>();
        try(Connection conn = util.getConnection();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT  * FROM user;")){
            while(rs.next()) {
                User user = new User(rs.getString("name"),
                        rs.getString("lastName"),rs.getByte("age"));
                user.setId(rs.getLong("id"));
                lst.add(user);
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
        return lst;

    }

    public void cleanUsersTable(){
        try (Connection conn = util.getConnection()) {
            try (Statement st = conn.createStatement()) {
                st.executeUpdate("TRUNCATE TABLE user;");
                conn.commit();
            }catch(SQLException e) {
                e.printStackTrace();
                try{
                    conn.rollback();
                }catch(Exception e2) {
                    e2.printStackTrace();
                }
            }
        }catch(SQLException e) {
            e.printStackTrace();
        }
    }
}
