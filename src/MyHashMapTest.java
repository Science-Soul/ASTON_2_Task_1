import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MyHashMapTest {

    private MyHashMap<String, Integer> map;

    @BeforeEach
    void setUp() {
        // Создаем чистую мапу перед каждым тестом
        map = new MyHashMap<>();
    }

    @Test
    void testPutAndGet() {
        map.put("Apple", 100);
        map.put("Banana", 200);

        assertEquals(2, map.size());
        assertEquals(100, map.get("Apple"));
        assertEquals(200, map.get("Banana"));
    }

    @Test
    void testPutUpdateValue() {
        map.put("Apple", 100);
        map.put("Apple", 150); // Обновляем значение по существующему ключу

        assertEquals(1, map.size()); // Размер не должен увеличиться
        assertEquals(150, map.get("Apple"));
    }

    @Test
    void testGetNonExistentKey() {
        assertNull(map.get("MissingKey"));
    }

    @Test
    void testPutAndGetNullKey() {
        map.put(null, 999);

        assertEquals(1, map.size());
        assertEquals(999, map.get(null));

        // Проверяем обновление null-ключа
        map.put(null, 888);
        assertEquals(888, map.get(null));
    }

    @Test
    void testRemove() {
        map.put("Apple", 100);
        map.put("Banana", 200);

        Integer removedValue = map.remove("Apple");

        assertEquals(1, map.size());
        assertEquals(100, removedValue);
        assertNull(map.get("Apple")); // Убеждаемся, что элемента больше нет
        assertEquals(200, map.get("Banana")); // Другие элементы не задеты
    }

    @Test
    void testRemoveNonExistentKey() {
        map.put("Apple", 100);

        assertNull(map.remove("MissingKey"));
        assertEquals(1, map.size());
    }

    @Test
    void testCollisionHandling() {
        // Создаем ключи, которые гарантированно дадут одинаковый hashCode, вызывая коллизию.
        // У строк "FB" и "Ea" в Java абсолютно одинаковый hashCode (2236).
        String key1 = "FB";
        String key2 = "Ea";

        assertEquals(key1.hashCode(), key2.hashCode(), "Ключи должны иметь одинаковый hashCode");

        map.put(key1, 1);
        map.put(key2, 2);

        // Проверяем, что оба элемента успешно уживаются в одном бакете
        assertEquals(2, map.size());
        assertEquals(1, map.get(key1));
        assertEquals(2, map.get(key2));

        // Проверяем корректное удаление из середины/конца коллизионной цепочки
        Integer removed = map.remove(key1);
        assertEquals(1, removed);
        assertNull(map.get(key1));
        assertEquals(2, map.get(key2)); // Второй элемент цепочки остался доступен
    }

    @Test
    void testResizeTriggering() {
        // Начальный размер 16. Load factor 0.75 означает ресайз после 12-го элемента.
        for (int i = 1; i <= 20; i++) {
            map.put("Key" + i, i);
        }

        // Проверяем, что после ресайза данные не потерялись и размер совпадает
        assertEquals(20, map.size());
        for (int i = 1; i <= 20; i++) {
            assertEquals(i, map.get("Key" + i));
        }
    }

    @Test
    void testConstructorWithCapacity() {
        // Проверяем, что конструктор принимает начальный размер и не падает
        MyHashMap<Integer, String> customMap = new MyHashMap<>(10);
        customMap.put(1, "One");

        assertEquals(1, customMap.size());
        assertEquals("One", customMap.get(1));
    }

    @Test
    void testConstructorWithNegativeCapacity() {
        // Конструктор должен выбрасывать исключение при некорректном размере
        assertThrows(IllegalArgumentException.class, () -> new MyHashMap<>(-5));
    }
}
