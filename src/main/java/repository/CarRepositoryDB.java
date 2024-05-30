package repository;

import constants.Constants;
import domain.Car;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static constants.Constants.*;


public class CarRepositoryDB implements CarRepository{

    private Connection getConnection() {
        try {
            Class.forName(DB_DRIVER_PATH);

            String dbUrl = String.format("%s%s?user=%s&password=%s",
                    DB_ADDRESS, DB_NAME, DB_USERNAME, DB_PASSWORD);

            return DriverManager.getConnection(dbUrl);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Car save(Car car) {
        try (Connection connection = getConnection()){
            String query = String.format(
                    "INSERT INTO car (brand, price, year) VALUES ('%s', %s, %d);",
                    car.getBrand(), car.getPrice(), car.getYear());

            Statement statement = connection.createStatement();
            statement.execute(query, Statement.RETURN_GENERATED_KEYS);
            ResultSet resultSet = statement.getGeneratedKeys();
            resultSet.next();

            Long id = resultSet.getLong(1);
            car.setId(id);

            return car;

        } catch (Exception e) {
            throw  new RuntimeException(e);
        }
    }

    @Override
    public Car getById(Long id) {
        try (Connection connection = getConnection()){

            String query = String.format("SELECT * FROM car WHERE id = %d;", id);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            resultSet.next();

            String brand = resultSet.getString("brand");
            BigDecimal price = resultSet.getBigDecimal("price");
            int year = resultSet.getInt("year");

            return new Car(id, brand, price, year);

        } catch (Exception e) {
            throw  new RuntimeException(e);
        }

    }

    @Override
    public List<Car> getAll() {
        List<Car> cars = new ArrayList<>();
        try (Connection connection = getConnection()) {
            String query = "SELECT * FROM car";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String brand = resultSet.getString("brand");
                BigDecimal price = resultSet.getBigDecimal("price");
                int year = resultSet.getInt("year");

                cars.add(new Car(id, brand, price, year));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return cars;
    }

    @Override
    public Car update(Car car) {
        try (Connection connection = getConnection()) {
            String query = String.format(
                    "UPDATE car SET brand = '%s', price = %s, year = %d WHERE id = %d;",
                    car.getBrand(), car.getPrice(), car.getYear(), car.getId());

            Statement statement = connection.createStatement();
            statement.executeUpdate(query);

            return car;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Long id) {
        try (Connection connection = getConnection()) {
            String query = String.format("DELETE FROM car WHERE id = %d;", id);
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
