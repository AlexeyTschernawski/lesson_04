package repository;

import domain.Car;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.hibernate.cfg.Configuration;

import java.util.List;

public class CarRepositoryHibernate implements CarRepository {

    private EntityManager entityManager;

    public CarRepositoryHibernate() {
        entityManager = new Configuration()
                .configure("hibernate/postgres.cfg.xml")
                .buildSessionFactory()
                .createEntityManager();

    }

    @Override
    public Car save(Car car) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(car);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Transaction cancelled");
        }
        return car;
    }

    @Override
    public Car getById(Long id) {
        return entityManager.find(Car.class, id);
    }

    @Override
    public List<Car> getAll() {
        // TODO домашнее задание
        return entityManager.createQuery("FROM Car", Car.class).getResultList();
    }

    @Override
    public Car update(Car car) {
        // TODO домашнее задание
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            car = entityManager.merge(car);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Transaction cancelled", e);
        }
        return car;
    }

    @Override
    public void delete(Long id) {
        // TODO домашнее задание
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            Car car = entityManager.find(Car.class, id);
            if (car != null) {
                entityManager.remove(car);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Transaction cancelled", e);
        }
    }
}