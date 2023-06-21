public class HashTable {
    private static final int DEFAULT_INITIAL_SIZE = 10;
    private Entry[] table;
    private int size;

    public HashTable() {
        this(DEFAULT_INITIAL_SIZE);
    }

    public HashTable(int initialSize) {
        size = initialSize;
        table = new Entry[size];
    }

    public void put(String key, Handle value) {
        int index = getIndex(key);
        Entry entry = new Entry(key, value);

        if (table[index] == null) {
            table[index] = entry;
        } else {
            Entry current = table[index];
            while (current.next != null) {
                current = current.next;
            }
            current.next = entry;
        }
    }

    public Handle get(String key) {
        int index = getIndex(key);
        Entry current = table[index];

        while (current != null) {
            if (current.key.equals(key)) {
                return current.value;
            }
            current = current.next;
        }

        return null;
    }

    public void remove(String key) {
        int index = getIndex(key);
        Entry current = table[index];
        Entry prev = null;

        while (current != null) {
            if (current.key.equals(key)) {
                if (prev == null) {
                    table[index] = current.next;
                } else {
                    prev.next = current.next;
                }
                return;
            }
            prev = current;
            current = current.next;
        }
    }

    public String[] getKeys() {
        int count = 0;
        for (Entry entry : table) {
            Entry current = entry;
            while (current != null) {
                count++;
                current = current.next;
            }
        }

        String[] keys = new String[count];
        int index = 0;
        for (Entry entry : table) {
            Entry current = entry;
            while (current != null) {
                keys[index] = current.key;
                index++;
                current = current.next;
            }
        }

        return keys;
    }

    private int getIndex(String key) {
        int hash = key.hashCode();
        return Math.abs(hash) % size;
    }

    private static class Entry {
        String key;
        Handle value;
        Entry next;

        Entry(String key, Handle value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }

    public boolean containsKey(String key) {
        int index = getIndex(key);
        Entry current = table[index];

        while (current != null) {
            if (current.key.equals(key)) {
                return true;
            }
            current = current.next;
        }

        return false;
    }
}
