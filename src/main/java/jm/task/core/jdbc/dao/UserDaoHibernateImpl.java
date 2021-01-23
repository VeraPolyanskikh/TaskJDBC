package jm.task.core.jdbc.dao;
import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.*;

import javax.persistence.EntityExistsException;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;


import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    private static final SessionFactory sessionFactory = Util.getSessionFactory();

    public UserDaoHibernateImpl() {

    }

    @Override
    public void createUsersTable() {
        try(Session sess = sessionFactory.openSession()) {
            Transaction tr  = null;
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
                sess.flush();
                tr.commit();
            }catch(Exception e) {
                if (tr != null) {
                    tr.rollback();
                }
                System.out.println(e.getMessage());
            }
        }
    }

    @Override
    public void dropUsersTable() {
        try(Session sess = sessionFactory.openSession()) {
            Transaction tr  = null;
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
        try(Session sess = sessionFactory.openSession()) {
            Transaction tr  = null;
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
            }catch(Exception e) {
                if(tr != null ) {
                    tr.rollback();
                }
                System.out.println(e.getMessage());
            }
        }
    }

    @Override
    public void removeUserById(long id) {
        try(Session sess = sessionFactory.openSession()) {
            Transaction tr  = null;
            try {
                tr = sess.beginTransaction();
                User foundUser = sess.get(User.class, id,LockOptions.UPGRADE);
                if(foundUser != null) {
                    sess.delete(foundUser);
                    sess.flush();
                    tr.commit();
                }else{
                    System.out.println("No such user found for id:"+id);
                    tr.rollback();
                }
            }catch(Exception e) {
                if(tr != null) {
                    tr.rollback();
                }
                System.out.println(e.getMessage());
            }
        }
    }

    @Override
    public List<User> getAllUsers() {

        try (Session sess = sessionFactory.openSession()) {
            CriteriaQuery<User> cr = sess.getCriteriaBuilder().createQuery(User.class);
            cr.from(User.class);
            TypedQuery<User> query = sess.createQuery(cr);
            return query.getResultList();
        }

    }

    @Override
    public void cleanUsersTable() {
        try(Session sess = sessionFactory.openSession()) {
            Transaction tr  = null;
            try {
                tr = sess.beginTransaction();
                CriteriaDelete<User> cd = sess.getCriteriaBuilder().createCriteriaDelete(User.class);
                cd.from(User.class);
                sess.createQuery(cd).executeUpdate();
                sess.flush();
                tr.commit();

            }catch(Exception e) {
                if (tr != null) {
                    tr.rollback();
                }
                System.out.println(e.getMessage());
            }
        }
    }
}
