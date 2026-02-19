
import java.util.Arrays;
import java.util.List;

import com.example.HashTableWithTrace;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TableTest {

    private HashTableWithTrace hashTable;
    public void setUp(int count) {
        hashTable = new HashTableWithTrace(count);
    }

    @Test
    @DisplayName("Вставка без коллизии ")
    public void testInsertNoCollision() {
        System.out.println("\nТЕСТ: Вставка без коллизии ");
        setUp(29);
        hashTable.clearTrace();
        hashTable.insert("A");

        List<String> actualTrace = hashTable.getTracePoints();
        List<String> expectedTrace = Arrays.asList(
                "START:insert(A)",
                "HASH_COMPUTED",
                "INDEX_CALCULATED:7",
                "FOUND_START",
                "CELL_EMPTY:7",
                "INSERT_SUCCESS:7"
        );
        compareTraces(expectedTrace, actualTrace);
    }
    @Test
    @DisplayName("Вставка с коллизией")
    public void testInsertWithCollision() {
        System.out.println("\nТЕСТ: Вставка с коллизией ");
        setUp(29);

        hashTable.insert("1");
        hashTable.clearTrace();
        hashTable.insert("N");

        List<String> actualTrace = hashTable.getTracePoints();
        List<String> expectedTrace = Arrays.asList(
                "START:insert(N)",
                "HASH_COMPUTED",
                "INDEX_CALCULATED:20",
                "FOUND_START",
                "CELL_OCCUPIED:20:1",
                "COLLISION:20",
                "FOUND_NEXT:26",
                "CELL_EMPTY:26",
                "INSERT_SUCCESS:26"
        );
        compareTraces(expectedTrace, actualTrace);
    }
    @Test
    @DisplayName("Вставка с двойной коллизией")
    public void testInsertWithSecondCollision() {
        System.out.println("\nТЕСТ: Вставка с двойной коллизией");
        setUp(29);

        hashTable.insert("1");
        hashTable.clearTrace();
        hashTable.insert("1");
        hashTable.clearTrace();
        hashTable.insert("1");

        List<String> actualTrace = hashTable.getTracePoints();
        List<String> expectedTrace = Arrays.asList(
                "START:insert(1)",
                "HASH_COMPUTED",
                "INDEX_CALCULATED:20",
                "FOUND_START",
                "CELL_OCCUPIED:20:1",
                "COLLISION:20",
                "FOUND_NEXT:27",
                "CELL_OCCUPIED:27:1",
                "COLLISION:27",
                "FOUND_NEXT:5",
                "CELL_EMPTY:5",
                "INSERT_SUCCESS:5"
        );
        compareTraces(expectedTrace, actualTrace);
    }

    @Test
    @DisplayName("Поиск существующего элемента")
    public void testSearchFound() {
        System.out.println("\nТЕСТ: Поиск существующего элемента");
        setUp(29);

        hashTable.insert("cat");
        hashTable.clearTrace();
        for (String point : hashTable.getTracePoints()) {
            System.out.println("  " + point);
        }
        boolean found = hashTable.search("cat");

        List<String> actualTrace = hashTable.getTracePoints();
        List<String> expectedTrace = Arrays.asList(
                "START:search(cat)",
                "HASH_COMPUTED",
                "INDEX_CALCULATED:27",
                "FOUND_START",
                "CELL_OCCUPIED:27:cat",
                "SEARCH_FOUND:27"
        );

        System.out.println("Результат поиска: " + found);
        compareTraces(expectedTrace, actualTrace);
    }
    @Test
    @DisplayName("Поиск несуществующего элемента")
    public void testSearchNotFound() {
        System.out.println("\n ТЕСТ: Поиск несуществующего элемента");
        setUp(29);

        hashTable.insert("itmo");
        hashTable.clearTrace();

        boolean found = hashTable.search("vt");

        List<String> actualTrace = hashTable.getTracePoints();
        List<String> expectedTrace = Arrays.asList(
                "START:search(vt)",
                "HASH_COMPUTED",
                "INDEX_CALCULATED:2",
                "FOUND_START",
                "CELL_EMPTY:2",
                "SEARCH_NOT_FOUND"
        );

        System.out.println("Результат поиска: " + found);
        compareTraces(expectedTrace, actualTrace);
    }
    @Test
    @DisplayName("Удаление элемента")
    public void testDeleteExsist() {
        System.out.println("\n ТЕСТ: Удаление элемента ");
        setUp(29);

        hashTable.insert("itmo");
        hashTable.clearTrace();

        boolean deleted = hashTable.delete("itmo");

        List<String> actualTrace = hashTable.getTracePoints();
        List<String> expectedTrace = Arrays.asList(
                "START:delete(itmo)",
                "HASH_COMPUTED",
                "INDEX_CALCULATED:18",
                "FOUND_START",
                "CELL_OCCUPIED:18:itmo",
                "DELETE_MARKED:18"
        );

        System.out.println("Результат удаления: " + deleted);
        compareTraces(expectedTrace, actualTrace);
    }
    @Test
    @DisplayName("Удаление несуществующего элемента")
    public void testDeleteNotExsist() {
        System.out.println("\nТЕСТ: Удаление несуществующего элемента");
        setUp(29);

        boolean deleted = hashTable.delete("itmo");

        List<String> actualTrace = hashTable.getTracePoints();
        List<String> expectedTrace = Arrays.asList(
                "START:delete(itmo)",
                "HASH_COMPUTED",
                "INDEX_CALCULATED:18",
                "FOUND_START",
                "CELL_EMPTY:18",
                "SEARCH_NOT_FOUND"
        );

        System.out.println("Результат удаления: " + deleted);
        compareTraces(expectedTrace, actualTrace);
    }

    @Test
    @DisplayName("Удаление элемента с коллизиями")
    public void testDeleteWithCollision() {
        System.out.println("\nТЕСТ: Удаление элемента с коллизиями");
        setUp(29);
        hashTable.insert("itmo");
        hashTable.clearTrace();
        hashTable.insert("/");
        hashTable.clearTrace();
        boolean deleted = hashTable.delete("/");

        List<String> actualTrace = hashTable.getTracePoints();
        List<String> expectedTrace = Arrays.asList(
                "START:delete(/)",
                "HASH_COMPUTED",
                "INDEX_CALCULATED:18",
                "FOUND_START",
                "CELL_OCCUPIED:18:itmo",
                "COLLISION:18",
                "FOUND_NEXT:20",
                "CELL_OCCUPIED:20:/",
                "DELETE_MARKED:20"
        );

        System.out.println("Результат удаления: " + deleted);
        compareTraces(expectedTrace, actualTrace);
    }
    @Test
    @DisplayName("Множественные коллизии")
    public void testMultipleCollisions() {
        System.out.println("\n ТЕСТ: Множественные коллизии");
        setUp(29);

        hashTable.insert("1");
        hashTable.insert("7");
        hashTable.insert("=");
        hashTable.clearTrace();

        hashTable.insert("N");

        List<String> actualTrace = hashTable.getTracePoints();
        List<String> expectedTrace = Arrays.asList(
                "START:insert(N)",
                "HASH_COMPUTED",
                "INDEX_CALCULATED:20",
                "FOUND_START",
                "CELL_OCCUPIED:20:1",
                "COLLISION:20",
                "FOUND_NEXT:26",
                "CELL_OCCUPIED:26:7",
                "COLLISION:26",
                "FOUND_NEXT:3",
                "CELL_OCCUPIED:3:=",
                "COLLISION:3",
                "FOUND_NEXT:9",
                "CELL_EMPTY:9",
                "INSERT_SUCCESS:9"
        );
        compareTraces(expectedTrace, actualTrace);
    }
    @Test
    @DisplayName("Переполнение таблицы")
    public void testTableFull() {
        System.out.println("\nТЕСТ: Переполнение таблицы ");
        setUp(5);

        hashTable.insert("A");
        hashTable.insert("B");
        hashTable.insert("C");
        hashTable.insert("D");
        hashTable.insert("E");
        hashTable.clearTrace();

        hashTable.insert("F");

        List<String> actualTrace = hashTable.getTracePoints();
        List<String> expectedTrace = Arrays.asList(
                "START:insert(F)",
                "TABLE_FULL"
        );
        compareTraces(expectedTrace, actualTrace);

    }

    private void compareTraces(List<String> expected, List<String> actual) {
        System.out.println("Ожидаемая трассировка:");
        expected.forEach(s -> System.out.println("  " + s));

        System.out.println("Фактическая трассировка:");
        actual.forEach(s -> System.out.println("  " + s));

        assertEquals(expected.size(), actual.size(), "Размеры трассировок не совпадают");

        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i), actual.get(i),
                    "Несовпадение на шаге " + i);
        }

        System.out.println("ТЕСТ ПРОЙДЕН!!!");
    }
}