package pl.sda.arppl4.school_daily_hibernate.dao;

import jakarta.persistence.TypedQuery;

import org.hibernate.Session;
import org.hibernate.SessionException;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import pl.sda.arppl4.school_daily_hibernate.util.HibernateUtil;

import java.util.*;


public class GenericDao<T> {
    public void add(T addObject) {
        SessionFactory factory = HibernateUtil.getSessionFactory();

        Transaction transaction = null;
        try (Session session = factory.openSession()) {
            transaction = session.beginTransaction();

            session.merge(addObject);

            transaction.commit();
        } catch (SessionException sessionException) {
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    public void remove(T removeObject) {
        SessionFactory factory = HibernateUtil.getSessionFactory();
        try (Session session = factory.openSession()) {
            Transaction transaction = session.beginTransaction();

            session.remove(removeObject);

            transaction.commit();
        }
    }


    public List<T> list(Class<T> classType) {
        List<T> list = new ArrayList<>();

        SessionFactory factory = HibernateUtil.getSessionFactory();
        try (Session session = factory.openSession()) {
//            Class<T> z = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
            TypedQuery<T> query = session.createQuery("from " + classType.getName(), classType);
            List<T> queryResult = query.getResultList();
            list.addAll(queryResult);
        } catch (SessionException sessionException) {
            System.err.println("Wrong data");
        }
        return list;
    }
    public void update(T updateObject) {
        SessionFactory fabrykaPolaczen = HibernateUtil.getSessionFactory();

        Transaction transaction = null;
        try (Session session = fabrykaPolaczen.openSession()) {
            transaction = session.beginTransaction();

            session.merge(updateObject);

            transaction.commit();
        } catch (SessionException sessionException) {
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }


    public Optional<T> get(Long id, Class<T> classType) {
        SessionFactory factory = HibernateUtil.getSessionFactory();
        try (Session session = factory.openSession()) {
            T foundObject = session.get(classType, id);
            return Optional.ofNullable(foundObject);
        }
    }
}

