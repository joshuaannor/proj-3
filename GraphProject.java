import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class GraphProject {
    private static final int INIT_HASH_SIZE = 100;
    private static final int INIT_NUM_BUFFS = 10;
    private static final int BUFF_SIZE = 4096;
    private static final String MEM_FILE = "mem.dat";
    private static final String CMD_FILE = "cmd.txt";
    private static final String STAT_FILE = "stat.txt";
    private static MemManager memManager;
    private static HashTable artistHashTable;
    private static HashTable songHashTable;

    private static GraphStruct graph;
    private static int numCacheHits;
    private static int numCacheMisses;
    private static int numDiskReads;

    public static void main(String[] args) throws IOException {
        memManager = new MemManager(BUFF_SIZE * INIT_NUM_BUFFS);
        artistHashTable = new HashTable(INIT_HASH_SIZE);
        songHashTable = new HashTable(INIT_HASH_SIZE);
        graph = new GraphStruct();
        numCacheHits = 0;
        numCacheMisses = 0;
        numDiskReads = 0;
        readCommands(CMD_FILE);
        memManager.dump();
        System.out.println("Cache hits: " + numCacheHits);
        System.out.println("Cache misses: " + numCacheMisses);
        System.out.println("Disk reads: " + numDiskReads);
        PrintWriter statFile = new PrintWriter(new FileWriter(STAT_FILE, true));
        statFile.println("Cache hits: " + numCacheHits);
        statFile.println("Cache misses: " + numCacheMisses);
        statFile.println("Disk reads: " + numDiskReads);
        statFile.close();
    }

    private static void readCommands(String fileName) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] tokens = line.split(" ");
            String command = tokens[0];
            switch (command) {
                case "insert":
                    insert(tokens[1], tokens[2]);
                    break;
                case "remove":
                    remove(tokens[1], tokens[2]);
                    break;
                case "print":
                    print(tokens[1]);
                    break;
                default:
                    System.out.println("Unknown command: " + command);
            }
        }
        reader.close();
    }

    private static void insert(String artist, String song) throws IOException {
        if (!artistHashTable.containsKey(artist)) {
            Handle artistHandle = memManager.insert(artist.getBytes(), artist.length());
            artistHashTable.put(artist, artistHandle);
        }
        if (!songHashTable.containsKey(song)) {
            Handle songHandle = memManager.insert(song.getBytes(), song.length());
            songHashTable.put(song, songHandle);
        }

        Handle artistHandle = artistHashTable.get(artist);
        Handle songHandle = songHashTable.get(song);
        graph.addEdge(artistHandle, songHandle);
    }

    private static void remove(String artist, String song) {
        if (artistHashTable.containsKey(artist)) {
            Handle artistHandle = artistHashTable.get(artist);
            artistHashTable.remove(artist);
            memManager.remove(artistHandle);
        }
        if (songHashTable.containsKey(song)) {
            Handle songHandle = songHashTable.get(song);
            songHashTable.remove(song);
            memManager.remove(songHandle);
        }

        Handle artistHandle = artistHashTable.get(artist);
        Handle songHandle = songHashTable.get(song);
        graph.removeEdge(artistHandle, songHandle);
    }

    private static void print(String type) {
        switch (type) {
            case "artist":
                printArtists();
                break;
            case "song":
                printSongs();
                break;
            case "blocks":
                printBlocks();
                break;
            case "graph":
                printGraph();
                break;
            default:
                System.out.println("Unknown type: " + type);
                break;
        }
    }

    private static void printArtists() {
        System.out.println("Artists:");
        String[] artists = artistHashTable.getKeys();
        for (String artist : artists) {
            System.out.println(artist);
        }
    }

    private static void printSongs() {
        System.out.println("Songs:");
        String[] songs = songHashTable.getKeys();
        for (String song : songs) {
            System.out.println(song);
        }
    }

    private static void printBlocks() {
        System.out.println("Blocks:");
        Block[] blocks = memManager.getBlocks();
        for (Block block : blocks) {
            System.out.println(block);
        }
    }

    private static void printGraph() {
        System.out.println("Graph:");
        graph.printGraph();
    }
}
