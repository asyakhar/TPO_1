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

		int summa = hash(key);
		int index = summa % capacity;
		tracePoints.add(TP_INDEX_CALCULATED + ":" + index);

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
				index = (index + (7 - summa % 7)) % capacity;
				tracePoints.add(TP_FOUND_NEXT + ":" + index);
			}
		}
		tracePoints.add(TP_TABLE_FULL);
	}

	public boolean search(String key) {
		tracePoints.add(TP_START + ":search(" + key + ")");

		int summa = hash(key);
		int index = summa % capacity;
		tracePoints.add(TP_INDEX_CALCULATED + ":" + index);

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
				index = (index + (7 - summa % 7)) % capacity;
				tracePoints.add(TP_FOUND_NEXT + ":" + index);
			}
		}

		tracePoints.add(TP_SEARCH_NOT_FOUND);
		return false;
	}

	public boolean delete(String key) {
		tracePoints.add(TP_START + ":delete(" + key + ")");

		int summa = hash(key);
		int index = summa % capacity;
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
				index = (index + (7 - summa % 7)) % capacity;
				tracePoints.add(TP_FOUND_NEXT + ":" + index);
			}
		}

		tracePoints.add(TP_SEARCH_NOT_FOUND);
		return false;
	}

	public void printTable() {
		System.out.println("Хеш-таблица:");
		for (int i = 0; i < capacity; i++) {
			System.out.println("  [" + i + "]: "
					+ (table[i] == null ? "null" : table[i].equals("DELETED") ? "DELETED" : table[i]));
		}
	}
}