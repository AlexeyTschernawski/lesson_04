package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import domain.Car;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import repository.CarRepositoryDB;
import repository.CarRepositoryHibernate;
import repository.CarRepositoryMap;
import service.CarService;
import service.CarServiceImpl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class CarServlet extends HttpServlet {

    //private CarService service = new CarServiceImpl(new CarRepositoryMap());
    private CarService service = new CarServiceImpl(new CarRepositoryDB());
    //private CarService service = new CarServiceImpl(new CarRepositoryHibernate());

    // Получение автомобиля или всех автомобилей:
    // GET http://localhost:8080/cars?id=3 - один авто
    // GET http://localhost:8080/cars - все авто

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // req - объект запроса, он содержит всю информацию, которую прислал нам клиент
        // resp - объект ответа, который будет отправлен клиенту после того, как
        //        отработает наш метод. Этот объект мы наполняем информацией, которую
        //        мы и хотим отправить клиенту

        Map<String, String[]> params = req.getParameterMap();
        // "id" : ["3"]

        if (params.isEmpty()) {
            // Получение всех автомобилей
            List<Car> cars = service.getAll();
            ObjectMapper mapper = new ObjectMapper();
            String jsonResponse = mapper.writeValueAsString(cars);
            resp.setContentType("application/json");
            resp.getWriter().write(jsonResponse);
        } else {
            Long id = Long.parseLong(params.get("id")[0]);
            Car car = service.getById(id);
            if (car != null) {
                ObjectMapper mapper = new ObjectMapper();
                String jsonResponse = mapper.writeValueAsString(car);
                resp.setContentType("application/json");
                resp.getWriter().write(jsonResponse);
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write("Car not found");
            }
        }
    }

    // Сохранение автомобиля в БД:
    // POST http://localhost:8080/cars - в теле будет приходить объект автомобиля

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        Car car = mapper.readValue(req.getReader(), Car.class);
        service.save(car);
        resp.getWriter().write(car.toString());
    }

    // Изменение уже существующего в БД автомобиля:
    // PUT http://localhost:8080/cars - в теле будет приходить объект автомобиля, подлежащий изменению

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // TODO домашнее задание
        ObjectMapper mapper = new ObjectMapper();
        Car car = mapper.readValue(req.getReader(), Car.class);
        Car updatedCar = service.update(car);
        if (updatedCar != null) {
            String jsonResponse = mapper.writeValueAsString(updatedCar);
            resp.setContentType("application/json");
            resp.getWriter().write(jsonResponse);
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write("Car not found for update");
        }
    }

    // Удаление автомобиля из БД
    // DELETE http://localhost:8080/cars?id=3

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // TODO домашнее задание
        Map<String, String[]> params = req.getParameterMap();
        if (params.containsKey("id")) {
            Long id = Long.parseLong(params.get("id")[0]);
            service.delete(id);
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT); // 204 No Content
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400 Bad Request
            resp.getWriter().write("Missing id parameter");
        }
    }
}