package jm.task.core.jdbc.dao;


import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.*;
import javax.persistence.*;

import javax.persistence.criteria.CriteriaQuery;

import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
     private final SessionFactory sessionFactory = Util.getSessionFactory();

    public UserDaoHibernateImpl() {
    }

    @Override
    public void createUsersTable() {

        //sess.setHibernateFlushMode(FlushMode.COMMIT);
        Transaction tr  = null;
        try (Session sess = sessionFactory.openSession()) {
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
            if(tr != null) {
                tr.rollback();
            }
            throw e;
        }
    }

    @Override
    public void dropUsersTable() {
        //sess.setHibernateFlushMode(FlushMode.COMMIT);
        Transaction tr  = null;
        try (Session sess = sessionFactory.openSession()) {
            tr = sess.beginTransaction();
            sess.createSQLQuery(
                    "drop table if exists user;").executeUpdate();

            tr.commit();
        } catch (Exception e) {
            if (tr != null) {
                tr.rollback();
            }
            throw e;
        }

    }

    @Override
    public void saveUser(String name, String lastName, byte age) {

        User aUser = new User(name, lastName, age);
        Transaction tr  = null;
        try (Session sess = sessionFactory.openSession()) {
            tr = sess.beginTransaction();
            sess.persist(aUser);
            tr.commit();
        } catch(Exception e) {
            if(tr != null ) {
                tr.rollback();
            }
            throw e;
        }
    }

    @Override
    public void removeUserById(long id) {

        Transaction tr  = null;
        try (Session sess = sessionFactory.openSession()) {
            //sess.setHibernateFlushMode(FlushMode.COMMIT);
            tr = sess.beginTransaction();
            User foundUser = sess.get(User.class, id,LockOptions.UPGRADE);
            if(foundUser != null) {
                sess.delete(foundUser);
                tr.commit();
            }else{
                System.out.println("No such user found fro id "+id);
                tr.rollback();
            }
        }catch(Exception e) {
            if(tr != null) {
                tr.rollback();
            }
            throw e;
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

        Transaction tr  = null;
        try (Session sess = sessionFactory.openSession()) {
            tr = sess.beginTransaction();
            sess.createSQLQuery(
                    "truncate table user;").executeUpdate();

            tr.commit();
            //sess.clear();
        }catch(Exception e) {
            if(tr != null) {
                tr.rollback();
            }
            throw e;
        }
    }
}
