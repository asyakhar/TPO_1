package com.example;

import java.util.LinkedList;
import java.util.List;

public class HashTableWithTrace {
	private String[] table;
	private int size; // коливество точек в таблице
	private int capacity;
	private List<String> tracePoints; // Для записи характерных точек

	public static final String TP_START = "START";
	public static final String TP_HASH_COMPUTED = "HASH_COMPUTED";
	public static final String TP_INDEX_CALCULATED = "INDEX_CALCULATED";
	public static final String TP_CELL_EMPTY = "CELL_EMPTY";
	public static final String TP_CELL_OCCUPIED = "CELL_OCCUPIED"; // ячейка занята
	public static final String TP_COLLISION = "COLLISION"; // коллизия
	public static final String TP_FOUND_START = "FOUND_START";
	public static final String TP_FOUND_NEXT = "FOUND_NEXT"; // переход к след ячейке
	public static final String TP_INSERT_SUCCESS = "INSERT_SUCCESS";
	public static final String TP_SEARCH_FOUND = "SEARCH_FOUND"; // элемент найден при поиске
	public static final String TP_SEARCH_NOT_FOUND = "SEARCH_NOT_FOUND";
	public static final String TP_DELETE_MARKED = "DELETE_MARKED";
	public static final String TP_TABLE_FULL = "TABLE_FULL";

	public HashTableWithTrace(int capacity) {
		this.capacity = capacity;
		this.table = new String[capacity];
		this.size = 0;
		this.tracePoints = new LinkedList<>();
	}

	public List<String> getTracePoints() {
		return tracePoints;
	}

	public void clearTrace() {
		tracePoints.clear();
	}

	private int hash(String key) {
		tracePoints.add(TP_HASH_COMPUTED);
		int hash = key.charAt(key.length() - 1);
		for (int i = key.length() - 2; i > -1; i--) {
			System.out.println(hash + " " + key.charAt(i));
			hash = (16 * hash + key.charAt(i));
		}
		return hash;
	}

	public void insert(String key) {
		tracePoints.add(TP_START + ":insert(" + key + ")");

		if (size >= capacity) {
			tracePoints.add(TP_TABLE_FULL);
			return;
		}

		int summ = hash(key);
		int hash = summ % capacity;
		tracePoints.add(TP_INDEX_CALCULATED + ":" + hash);

		int index = hash;
		int i = 0;

		tracePoints.add(TP_FOUND_START);

		while (i < capacity) {
			if (table[index] == null || table[index].equals("DELETED")) {
				tracePoints.add(TP_CELL_EMPTY + ":" + index);
				table[index] = key;
				size++;
				tracePoints.add(TP_INSERT_SUCCESS + ":" + index);
				return;
			} else {
				tracePoints.add(TP_CELL_OCCUPIED + ":" + index + ":" + table[index]);
				tracePoints.add(TP_COLLISION + ":" + index);
				i++;
				index = (index + (7 - summ % 7)) % capacity;
				tracePoints.add(TP_FOUND_NEXT + ":" + index);
			}
		}
		System.out.println(index);
		tracePoints.add(TP_TABLE_FULL);
	}

	public boolean search(String key) {
		tracePoints.add(TP_START + ":search(" + key + ")");

		int summ = hash(key);
		int hash = summ % capacity;
		tracePoints.add(TP_INDEX_CALCULATED + ":" + hash);

		int index = hash;
		int i = 0;

		tracePoints.add(TP_FOUND_START);
		printTable();
		while (i < capacity) {
			if (table[index] == null) {
				tracePoints.add(TP_CELL_EMPTY + ":" + index);
				tracePoints.add(TP_SEARCH_NOT_FOUND);
				return false;
			} else if (table[index].equals(key)) {
				tracePoints.add(TP_CELL_OCCUPIED + ":" + index + ":" + table[index]);
				tracePoints.add(TP_SEARCH_FOUND + ":" + index);
				return true;
			} else {
				tracePoints.add(TP_CELL_OCCUPIED + ":" + index + ":" + table[index]);
				tracePoints.add(TP_COLLISION + ":" + index);
				i++;
				index = (index + (7 - summ % 7)) % capacity;
				tracePoints.add(TP_FOUND_NEXT + ":" + index);
			}
		}

		tracePoints.add(TP_SEARCH_NOT_FOUND);
		return false;
	}

	public boolean delete(String key) {
		tracePoints.add(TP_START + ":delete(" + key + ")");

		int summ = hash(key);
		int index = summ % capacity;
		tracePoints.add(TP_INDEX_CALCULATED + ":" + index);
		int i = 0;
		tracePoints.add(TP_FOUND_START);

		while (i < capacity) {
			if (table[index] == null) {
				tracePoints.add(TP_CELL_EMPTY + ":" + index);
				tracePoints.add(TP_SEARCH_NOT_FOUND);
				return false;
			} else if (table[index].equals(key)) {
				tracePoints.add(TP_CELL_OCCUPIED + ":" + index + ":" + table[index]);
				table[index] = "DELETED";
				size--;
				tracePoints.add(TP_DELETE_MARKED + ":" + index);
				return true;
			} else {
				tracePoints.add(TP_CELL_OCCUPIED + ":" + index + ":" + table[index]);
				tracePoints.add(TP_COLLISION + ":" + index);
				i++;
				index = (index + (7 - summ % 7)) % capacity;
				tracePoints.add(TP_FOUND_NEXT + ":" + index);
			}
		}

		tracePoints.add(TP_SEARCH_NOT_FOUND);
		return false;
	}

	public boolean validateTable() {
		System.out.println("\n=== Проверка корректности таблицы ===");
		boolean isValid = true;

		for (int i = 0; i < capacity; i++) {
			String value = table[i];
			if (value == null || value.equals("DELETED")) {
				continue;
			}

			int expectedIndex = hash(value);
			int currentIndex = i;

			if (expectedIndex == currentIndex) {
				System.out.println("[" + i + "] = " + value + " (на своём месте)");
			} else {
				boolean foundInProbing = false;
				int probeIndex = expectedIndex;
				int step = 0;

				// Ищем, должен ли этот элемент быть на текущей позиции
				while (step < capacity) {
					if (probeIndex == currentIndex) {
						foundInProbing = true;
						break;
					}
					if (table[probeIndex] == null || table[probeIndex].equals(value)) {
						break;
					}
					step++;
					probeIndex = (expectedIndex + step) % capacity;
				}

				if (foundInProbing) {
					System.out.println("  ✓ [" + i + "] = " + value + " (корректная позиция после коллизии, ожидался ["
							+ expectedIndex + "])");
				} else {
					System.out.println("  ✗ [" + i + "] = " + value + " (НЕКОРРЕКТНАЯ ПОЗИЦИЯ! Должен быть в ["
							+ expectedIndex + "] или в цепочке пробирования)");
					isValid = false;
				}
			}
		}
		return isValid;
	}

	public void printTable() {
		System.out.println("Хеш-таблица:");
		for (int i = 0; i < capacity; i++) {
			System.out.println("  [" + i + "]: "
					+ (table[i] == null ? "null" : table[i].equals("DELETED") ? "DELETED" : table[i]));
		}
	}
}