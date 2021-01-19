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

    private Connection myConn;
    public UserDaoJDBCImpl() {

        try {
            myConn = Util.getConnection();
            myConn.setAutoCommit(false);

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void createUsersTable() {
        try {
            Statement st = myConn.createStatement();
            st.executeUpdate("""
               create table  if not exists user
                     (
                     	id bigint auto_increment,
                     	name varchar(255) not null,
                     	lastname varchar(255) not null,
                     	age tinyint not null,
                     	constraint user_pk
                     		primary key (id)
                     );                     
                """);
            myConn.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void dropUsersTable() {
        try {

            Statement st = myConn.createStatement();
            st.executeUpdate("""
                drop table if exists user;
                """);
            myConn.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        try {

            Statement st = myConn.createStatement();
            st.executeUpdate(String.format("""
                insert into user
                (name,lastName,age)
                values('%s','%s',%d);
                """,name,lastName,age));
            myConn.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeUserById(long id) {
        try {
            Statement st = myConn.createStatement();
            st.executeUpdate("""
               DELETE FROM user
               WHERE id="""+id);
            myConn.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<User> getAllUsers() {
        List<User> lst  = new ArrayList<>();
        try {
            Statement st = myConn.createStatement();
            ResultSet rs = st.executeQuery("SELECT  * FROM user;");
            while(rs.next()) {
                User user = new User(rs.getString("name"),rs.getString("lastName")
                        ,rs.getByte("age"));
                user.setId(rs.getLong("id"));
                lst.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lst;

    }

    public void cleanUsersTable() {
        try {

            Statement st = myConn.createStatement();
            st.executeUpdate("TRUNCATE TABLE user;");
            myConn.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection(){
        try {
            Util.closeConnection(myConn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
