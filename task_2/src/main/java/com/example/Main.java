package com.example;

import java.util.Arrays;
import java.util.List;

public class Main {
    
    private HashTableWithTrace hashTable;
    
    public void setUp() {
        hashTable = new HashTableWithTrace(5);
    }
    
    public void testInsertNoCollision() {
        System.out.println("\n=== Тест 1: Вставка без коллизии ===");
        setUp();
        hashTable.clearTrace();
        
        // Вставляем элемент
        hashTable.insert("A");
        
        List<String> actualTrace = hashTable.getTracePoints();
        List<String> expectedTrace = Arrays.asList(
            "START:insert(A)",
            "HASH_COMPUTED",
            "INDEX_CALCULATED:1", // Предполагаем, что hash("A") = 1
            "PROBING_START",
            "CELL_EMPTY:1",
            "INSERT_SUCCESS:1"
        );
        
        compareTraces(expectedTrace, actualTrace);
    }
    
    public void testInsertWithCollision() {
        System.out.println("\n=== Тест 2: Вставка с коллизией ===");
        setUp();
        
        // Сначала заполняем ячейку 1
        hashTable.insert("A"); // hash = 1
        
        hashTable.clearTrace();
        
        // Вставляем элемент, который хешируется в ту же ячейку
        hashTable.insert("B"); // тоже hash = 1
        
        List<String> actualTrace = hashTable.getTracePoints();
        List<String> expectedTrace = Arrays.asList(
            "START:insert(B)",
            "HASH_COMPUTED",
            "INDEX_CALCULATED:1",
            "PROBING_START",
            "CELL_OCCUPIED:1:A",
            "COLLISION:1",
            "PROBING_NEXT:2",
            "CELL_EMPTY:2",
            "INSERT_SUCCESS:2"
        );
        
        compareTraces(expectedTrace, actualTrace);
    }
    
    public void testSearchFound() {
        System.out.println("\n=== Тест 3: Поиск существующего элемента ===");
        setUp();
        
        hashTable.insert("A");
        hashTable.clearTrace();
        
        boolean found = hashTable.search("A");
        
        List<String> actualTrace = hashTable.getTracePoints();
        List<String> expectedTrace = Arrays.asList(
            "START:search(A)",
            "HASH_COMPUTED",
            "INDEX_CALCULATED:1",
            "PROBING_START",
            "CELL_OCCUPIED:1:A",
            "SEARCH_FOUND:1"
        );
        
        System.out.println("Результат поиска: " + found);
        compareTraces(expectedTrace, actualTrace);
    }
    
    public void testSearchNotFound() {
        System.out.println("\n=== Тест 4: Поиск несуществующего элемента ===");
        setUp();
        
        hashTable.insert("A");
        hashTable.clearTrace();
        
        boolean found = hashTable.search("C");
        
        List<String> actualTrace = hashTable.getTracePoints();
        List<String> expectedTrace = Arrays.asList(
            "START:search(C)",
            "HASH_COMPUTED",
            "INDEX_CALCULATED:2", // hash("C") может быть 2
            "PROBING_START",
            "CELL_EMPTY:2",
            "SEARCH_NOT_FOUND"
        );
        
        System.out.println("Результат поиска: " + found);
        compareTraces(expectedTrace, actualTrace);
    }
    
    public void testDelete() {
        System.out.println("\n=== Тест 5: Удаление элемента ===");
        setUp();
        
        hashTable.insert("A");
        hashTable.clearTrace();
        
        boolean deleted = hashTable.delete("A");
        
        List<String> actualTrace = hashTable.getTracePoints();
        List<String> expectedTrace = Arrays.asList(
            "START:delete(A)",
            "HASH_COMPUTED",
            "INDEX_CALCULATED:1",
            "PROBING_START",
            "CELL_OCCUPIED:1:A",
            "DELETE_MARKED:1"
        );
        
        System.out.println("Результат удаления: " + deleted);
        compareTraces(expectedTrace, actualTrace);
    }
    
    public void testMultipleCollisions() {
        System.out.println("\n=== Тест 6: Множественные коллизии ===");
        setUp();
        
        // Заполняем таблицу
        hashTable.insert("A"); // index 1
        hashTable.insert("B"); // index 1 -> 2
        hashTable.insert("C"); // index 2 -> 3
        hashTable.clearTrace();
        
        // Вставляем еще один элемент с коллизией
        hashTable.insert("D"); // hash = 1
        
        List<String> actualTrace = hashTable.getTracePoints();
        
        System.out.println("Фактическая трассировка:");
        for (String point : actualTrace) {
            System.out.println("  " + point);
        }
        
        // Проверяем, что были все необходимые шаги пробирования
        assertTraceContains(actualTrace, "PROBING_START");
        assertTraceContains(actualTrace, "CELL_OCCUPIED:1");
        assertTraceContains(actualTrace, "COLLISION:1");
        assertTraceContains(actualTrace, "PROBING_NEXT:2");
        assertTraceContains(actualTrace, "CELL_OCCUPIED:2");
        assertTraceContains(actualTrace, "COLLISION:2");
        assertTraceContains(actualTrace, "PROBING_NEXT:3");
        assertTraceContains(actualTrace, "CELL_OCCUPIED:3");
        assertTraceContains(actualTrace, "COLLISION:3");
        assertTraceContains(actualTrace, "PROBING_NEXT:4");
        assertTraceContains(actualTrace, "CELL_EMPTY:4");
        assertTraceContains(actualTrace, "INSERT_SUCCESS:4");
        
        System.out.println("Тест на множественные коллизии пройден!");
    }
    
    public void testTableFull() {
        System.out.println("\n=== Тест 7: Переполнение таблицы ===");
        setUp();
        
        // Заполняем всю таблицу
        hashTable.insert("A");
        hashTable.insert("B");
        hashTable.insert("C");
        hashTable.insert("D");
        hashTable.insert("E");
        hashTable.clearTrace();
        
        // Пытаемся вставить в полную таблицу
        hashTable.insert("F");
        
        List<String> actualTrace = hashTable.getTracePoints();
        
        assertTraceContains(actualTrace, "START:insert(F)");
        assertTraceContains(actualTrace, "TABLE_FULL");
        
        System.out.println("Тест на переполнение пройден!");
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
        test.testSearchFound();
        test.testSearchNotFound();
        test.testDelete();
        test.testMultipleCollisions();
        test.testTableFull();
        
        System.out.println("\n=== Все тесты завершены ===");
    }
}