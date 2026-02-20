package com.example;

import java.util.LinkedList;
import java.util.List;

public class HashTable {
	private String[] table;
	private int size;
	private int capacity;
	private List<String> sequencePoints;

	public static final String TP_START = "START";
	public static final String TP_HASH_COMPUTED = "HASH_COMPUTED";
	public static final String TP_INDEX_CALCULATED = "INDEX_CALCULATED";
	public static final String TP_CELL_EMPTY = "CELL_EMPTY";
	public static final String TP_CELL_OCCUPIED = "CELL_OCCUPIED";
	public static final String TP_COLLISION = "COLLISION";
	public static final String TP_FOUND_START = "FOUND_START";
	public static final String TP_FOUND_NEXT = "FOUND_NEXT";
	public static final String TP_INSERT_SUCCESS = "INSERT_SUCCESS";
	public static final String TP_SEARCH_FOUND = "SEARCH_FOUND"; 
	public static final String TP_SEARCH_NOT_FOUND = "SEARCH_NOT_FOUND";
	public static final String TP_DELETE_MARKED = "DELETE_MARKED";
	public static final String TP_TABLE_FULL = "TABLE_FULL";

	public HashTable(int capacity) {
		this.capacity = capacity;
		this.table = new String[capacity];
		this.size = 0;
		this.sequencePoints = new LinkedList<>();
	}

	public List<String> getSequencePoints() {
		return sequencePoints;
	}

	public void clearSequence() {
		sequencePoints.clear();
	}

	private int hash(String key) {
		sequencePoints.add(TP_HASH_COMPUTED);
		int hash = key.charAt(key.length() - 1);
		for (int i = key.length() - 2; i > -1; i--) {
			hash = (16 * hash + key.charAt(i));
		}
		return hash;
	}

	public void insert(String key) {
		sequencePoints.add(TP_START + ":insert(" + key + ")");

		if (size >= capacity) {
			sequencePoints.add(TP_TABLE_FULL);
			return;
		}

		int summa = hash(key);
		int index = summa % capacity;
		sequencePoints.add(TP_INDEX_CALCULATED + ":" + index);

		int i = 0;

		sequencePoints.add(TP_FOUND_START);

		while (i < capacity) {
			if (table[index] == null || table[index].equals("DELETED")) {
				sequencePoints.add(TP_CELL_EMPTY + ":" + index);
				table[index] = key;
				size++;
				sequencePoints.add(TP_INSERT_SUCCESS + ":" + index);
				return;
			} else {
				sequencePoints.add(TP_CELL_OCCUPIED + ":" + index + ":" + table[index]);
				sequencePoints.add(TP_COLLISION + ":" + index);
				i++;
				index = (index + (7 - summa % 7)) % capacity;
				sequencePoints.add(TP_FOUND_NEXT + ":" + index);
			}
		}
		sequencePoints.add(TP_TABLE_FULL);
	}

	public boolean search(String key) {
		sequencePoints.add(TP_START + ":search(" + key + ")");

		int summa = hash(key);
		int index = summa % capacity;
		sequencePoints.add(TP_INDEX_CALCULATED + ":" + index);

		int i = 0;

		sequencePoints.add(TP_FOUND_START);
		printTable();
		while (i < capacity) {
			if (table[index] == null) {
				sequencePoints.add(TP_CELL_EMPTY + ":" + index);
				sequencePoints.add(TP_SEARCH_NOT_FOUND);
				return false;
			} else if (table[index].equals(key)) {
				sequencePoints.add(TP_CELL_OCCUPIED + ":" + index + ":" + table[index]);
				sequencePoints.add(TP_SEARCH_FOUND + ":" + index);
				return true;
			} else {
				sequencePoints.add(TP_CELL_OCCUPIED + ":" + index + ":" + table[index]);
				sequencePoints.add(TP_COLLISION + ":" + index);
				i++;
				index = (index + (7 - summa % 7)) % capacity;
				sequencePoints.add(TP_FOUND_NEXT + ":" + index);
			}
		}

		sequencePoints.add(TP_SEARCH_NOT_FOUND);
		return false;
	}

	public boolean delete(String key) {
		sequencePoints.add(TP_START + ":delete(" + key + ")");

		int summa = hash(key);
		int index = summa % capacity;
		sequencePoints.add(TP_INDEX_CALCULATED + ":" + index);
		int i = 0;
		sequencePoints.add(TP_FOUND_START);

		while (i < capacity) {
			if (table[index] == null) {
				sequencePoints.add(TP_CELL_EMPTY + ":" + index);
				sequencePoints.add(TP_SEARCH_NOT_FOUND);
				return false;
			} else if (table[index].equals(key)) {
				sequencePoints.add(TP_CELL_OCCUPIED + ":" + index + ":" + table[index]);
				table[index] = "DELETED";
				size--;
				sequencePoints.add(TP_DELETE_MARKED + ":" + index);
				return true;
			} else {
				sequencePoints.add(TP_CELL_OCCUPIED + ":" + index + ":" + table[index]);
				sequencePoints.add(TP_COLLISION + ":" + index);
				i++;
				index = (index + (7 - summa % 7)) % capacity;
				sequencePoints.add(TP_FOUND_NEXT + ":" + index);
			}
		}

		sequencePoints.add(TP_SEARCH_NOT_FOUND);
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