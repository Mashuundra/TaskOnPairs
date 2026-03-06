package main;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    // Статический реестр всех загруженных сотрудников (симулирует БД)
    private static Map<Integer, Employee> employeesRegistry = new HashMap<>();

    private int id;
    private String name;
    private transient String password;
    private String position;

    public Employee(int id, String name, String password, String position) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.position = position;
        // При создании нового объекта через конструктор, добавляем его в реестр
        employeesRegistry.put(id, this);
    }

    public static Employee getFromRegistry(int id) {
        return employeesRegistry.get(id);
    }

    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
    }

    @Serial
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        if (employeesRegistry.containsKey(this.id)) {
            throw new InvalidObjectException("main.Employee with id " + id + " already exists in registry");
        }
        employeesRegistry.put(this.id, this);
    }
}