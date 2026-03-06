import org.junit.jupiter.api.*;
import java.io.*;
import java.lang.reflect.Field;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class EmployeeTest {
    private static final String TEST_FILE = "employee.test.ser";

    private void clearRegistry() throws Exception {
        Field field = main.Employee.class.getDeclaredField("employeesRegistry");
        field.setAccessible(true);
        ((Map<?,?>) field.get(null)).clear();
    }

    private Object getFieldValue(Object obj, String fieldName) throws Exception {
        Field field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(obj);
    }

    @BeforeEach
    void setUp() throws Exception {
        clearRegistry();
        new File(TEST_FILE).delete();
    }

    @AfterEach
    void tearDown() {
        new File(TEST_FILE).delete();
    }

    @Test
    public void testPasswordNotSerialized() throws Exception {
        main.Employee original = new main.Employee(1, "Alice", "secret", "dev");

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(TEST_FILE))) {
            out.writeObject(original);
        }

        Field passField = main.Employee.class.getDeclaredField("password");
        passField.setAccessible(true);
        passField.set(original, "newpass");

        clearRegistry();

        main.Employee deserialized;
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(TEST_FILE))) {
            deserialized = (main.Employee) in.readObject();
        }

        assertNull(getFieldValue(deserialized, "password"));
        assertEquals(1, getFieldValue(deserialized, "id"));
        assertEquals("Alice", getFieldValue(deserialized, "name"));
        assertEquals("dev", getFieldValue(deserialized, "position"));
    }

    @Test
    public void testDuplicateIdThrows() throws Exception {
        main.Employee emp1 = new main.Employee(2, "Bob", "pass", "qa");
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(TEST_FILE))) {
            out.writeObject(emp1);
        }

        clearRegistry();
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(TEST_FILE))) {
            in.readObject();
        }

        main.Employee emp2 = new main.Employee(2, "Charlie", "123", "pm");
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(TEST_FILE))) {
            out.writeObject(emp2);
        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(TEST_FILE))) {
            assertThrows(InvalidObjectException.class, in::readObject);
        }
    }

    @Test
    public void testRegistryUpdatedAfterDeserialization() throws Exception {
        main.Employee original = new main.Employee(3, "Diana", "pwd", "hr");
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(TEST_FILE))) {
            out.writeObject(original);
        }

        clearRegistry();

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(TEST_FILE))) {
            in.readObject();
        }

        main.Employee fromReg = main.Employee.getFromRegistry(3);
        assertNotNull(fromReg);
        assertEquals("Diana", getFieldValue(fromReg, "name"));
    }
}