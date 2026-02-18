package com.example;

import java.util.Arrays;
import java.util.List;

public class Main {
    
    private HashTableWithTrace hashTable;
    
    public void setUp(int count) {
        hashTable = new HashTableWithTrace(count);
    }
    
    public void testInsertNoCollision() {
        System.out.println("\n=== Тест 1: Вставка без коллизии ===");
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

    public void testInsertWithCollision() {
        System.out.println("\n=== Тест 2: Вставка с коллизией ===");
        setUp(29);
        
        hashTable.insert("1"); 
        hashTable.clearTrace();
        hashTable.insert("N"); 
        
        List<String> actualTrace = hashTable.getTracePoints();
        List<String> expectedTrace = Arrays.asList(
            "START:insert(1)",
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
    
    public void testInsertWithSecondCollision() {
        System.out.println("\n=== Тест 3: Вставка с двойной коллизией ===");
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

    public void testSearchFound() {
        System.out.println("\n=== Тест 4: Поиск существующего элемента ===");
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
    
    public void testSearchNotFound() {
        System.out.println("\n=== Тест 5: Поиск несуществующего элемента ===");
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
    
    public void testDeleteExsist() {
        System.out.println("\n=== Тест 6: Удаление элемента ===");
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
    
    public void testDeleteNotExsist() {
        System.out.println("\n=== Тест 7: Удаление элемента, которого не существует===");
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
    
    public void testDeleteWithCollision() {
        System.out.println("\n=== Тест 8: Удаление элемента с коллизиями===");
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

    public void testMultipleCollisions() {
        System.out.println("\n=== Тест 9: Множественные коллизии ===");
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
  
    public void testTableFull() {
        System.out.println("\n=== Тест 10: Переполнение таблицы ===");
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
        for (String point : expected) {
            System.out.println("  " + point);
        }
        
        System.out.println("Фактическая трассировка:");
        for (String point : actual) {
            System.out.println("  " + point);
        }
        
        boolean match = expected.size() == actual.size();
        if (match) {
            for (int i = 0; i < expected.size(); i++) {
                if (!actual.get(i).startsWith(expected.get(i).split(":")[0])) {
                    match = false;
                    break;
                }
            }
        }
        if (match) {
            System.out.println("✓ ТЕСТ ПРОЙДЕН");
        } else {
            System.out.println("✗ ТЕСТ НЕ ПРОЙДЕН");
        }
    }
    
    private void assertTraceContains(List<String> trace, String expectedPoint) {
        for (String point : trace) {
            if (point.startsWith(expectedPoint)) {
                System.out.println("  Найдено: " + point);
                return;
            }
        }
        System.out.println("  ОШИБКА: Не найдено " + expectedPoint);
    }
    
    public static void main(String[] args) {
        Main test = new Main();
        
        test.testInsertNoCollision();
        test.testInsertWithCollision();
        test.testInsertWithSecondCollision();
        test.testSearchFound();
        test.testSearchNotFound();
        test.testDeleteExsist();
        test.testDeleteNotExsist();
        test.testDeleteWithCollision();
        test.testMultipleCollisions();
        test.testTableFull();
        
        System.out.println("\n=== Все тесты завершены ===");
    }
}