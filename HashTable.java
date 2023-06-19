public class HashTable {
    private static final int DEFAULT_INITIAL_SIZE = 10;
    private int size;
    private Entry[] table;

    /**
     * Represents an entry in the hash table.
     */
    private static class Entry {
        String key;
        int handle;
        Entry next;

        Entry(String key, int handle) {
            this.key = key;
            this.handle = handle;
            this.next = null;
        }
    }

    /**
     * Constructs a hash table with the specified initial size.
     *
     * @param initialSize the initial size of the hash table
     */
    public HashTable(int initialSize) {
        size = initialSize;
        table = new Entry[size];
    }

    /**
     * Constructs a hash table with the default initial size.
     */
    public HashTable() {
        this(DEFAULT_INITIAL_SIZE);
    }

    /**
     * Inserts a key-handle pair into the hash table.
     *
     * @param key    the key
     * @param handle the handle
     */
    public void insert(String key, int handle) {
        int index = getIndex(key);
        Entry entry = new Entry(key, handle);

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

    /**
     * Removes the entry with the specified key from the hash table.
     *
     * @param key the key to remove
     * @return true if the entry is found and removed, false otherwise
     */
    public boolean remove(String key) {
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
                return true;
            }
            prev = current;
            current = current.next;
        }

        return false;
    }

    /**
     * Retrieves the handle associated with the specified key from the hash table.
     *
     * @param key the key to search for
     * @return the handle if found, or -1 if not found
     */
    public int getHandle(String key) {
        int index = getIndex(key);
        Entry current = table[index];

        while (current != null) {
            if (current.key.equals(key)) {
                return current.handle;
            }
            current = current.next;
        }

        return -1;
    }

    /**
     * Prints the contents of the hash table.
     */
    public void print() {
        for (int i = 0; i < size; i++) {
            System.out.print("Slot " + i + ": ");
            Entry entry = table[i];
            while (entry != null) {
                System.out.print("(" + entry.key + ", " + entry.handle + ") ");
                entry = entry.next;
            }
            System.out.println();
        }
    }

    /**
     * Computes the hash index for the given key.
     *
     * @param key the key
     * @return the hash index
     */
    private int getIndex(String key) {
        int hash = key.hashCode();
        return Math.abs(hash) % size;
    }
}
