package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.LockOptions;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.PersistenceException;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    private static final SessionFactory sessionFactory = Util.getSessionFactory();

    public UserDaoHibernateImpl() {
    }

    @Override
    public void createUsersTable() {
        try (Session sess = sessionFactory.openSession()) {
            Transaction tr = null;
            try {
                tr = sess.beginTransaction();
                sess.createSQLQuery("""
                        create table  if not exists user(
                            id bigint auto_increment,
                            name varchar(255) not null,
                            lastname varchar(255) not null,
                            age tinyint not null,
                            constraint user_pk
                                primary key (id)
                         );                     
                        """).executeUpdate();
                tr.commit();
            } catch (Exception e) {
                if (tr != null) {
                    tr.rollback();
                }
                System.out.println(e.getMessage());
            }
        }
    }

    @Override
    public void dropUsersTable() {
        try (Session sess = sessionFactory.openSession()) {
            Transaction tr = null;
            try {
                tr = sess.beginTransaction();
                sess.createSQLQuery(
                        "drop table if exists user;").executeUpdate();

                tr.commit();
            } catch (Exception e) {

                if (tr != null) {
                    tr.rollback();
                }
                System.out.println(e.getMessage());
            }
        }

    }

    @Override
    public void saveUser(String name, String lastName, byte age) {

        User aUser = new User(name, lastName, age);
        try (Session sess = sessionFactory.openSession()) {
            Transaction tr = null;
            try {
                tr = sess.beginTransaction();
                sess.persist(aUser);
                tr.commit();
            } catch (PersistenceException e) {
                System.out.printf("""
                                Creation of a new item failed because the object violates the data model:%s%n""",
                        e.getMessage());
                if (tr != null) {
                    tr.rollback();
                }
            } catch (Exception e) {
                if (tr != null) {
                    tr.rollback();
                }
                System.out.println(e.getMessage());
            }
        }
    }

    @Override
    public void removeUserById(long id) {
        try (Session sess = sessionFactory.openSession()) {
            Transaction tr = null;
            try {
                tr = sess.beginTransaction();
                User foundUser = sess.get(User.class, id, LockOptions.UPGRADE);
                if (foundUser != null) {
                    sess.delete(foundUser);
                    sess.flush();
                    tr.commit();
                } else {
                    System.out.println("No such user found for id:" + id);
                    tr.rollback();
                }
            } catch (Exception e) {
                if (tr != null) {
                    tr.rollback();
                }
                System.out.println(e.getMessage());
            }
        }
    }

    @Override
    public List<User> getAllUsers() {
        try (Session sess = sessionFactory.openSession()) {
            return sess.createQuery("from User", User.class).getResultList();
        }
    }

    @Override
    public void cleanUsersTable() {
        try (Session sess = sessionFactory.openSession()) {
            Transaction tr = null;
            try {
                tr = sess.beginTransaction();
                sess.createQuery("Delete from User").executeUpdate();
                sess.flush();
                tr.commit();

            } catch (Exception e) {
                if (tr != null) {
                    tr.rollback();
                }
                System.out.println(e.getMessage());
            }
        }
    }
}
