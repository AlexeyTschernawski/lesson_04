package repository;

import domain.Car;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CarRepositoryMap  implements CarRepository {

    private Map<Long, Car> database = new HashMap<>();
    private long currentId;

    @Override
    public Car save(Car car) {
        car.setId(++currentId);
        database.put(currentId, car);
        return car;
    }

    @Override
    public Car getById(Long id) {
        return database.get(id);
    }

    @Override
    public List<Car> getAll() {
        return new ArrayList<>(database.values());
    }

    @Override
    public Car update(Car car) {
        // TODO домашнее задание (подлежит изменению только цена автомобиля)
        Car existingCar = database.get(car.getId());
        if (existingCar != null) {
            existingCar.setPrice(car.getPrice());
            database.put(car.getId(), existingCar);
            return existingCar;
        }
        return null;
    }

    @Override
    public void delete(Long id) {
        // TODO домашнее задание
        database.remove(id);
    }
}