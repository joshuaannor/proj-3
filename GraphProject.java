import java.io.IOException;
import java.io.RandomAccessFile;

public class MainGraph {

    public static void main(String[] args) throws IOException {
        // Retrieve the command-line arguments
        if (args.length < 6) {
            System.err.println("Insufficient command-line arguments. Usage: java MainGraph {memFile} {numBuffs} {buffSize} {initHashSize} {commandFile} {statFile}");
            System.exit(1);
        }

        String memFile = args[0]; // Name of the file that will store the memory pool.
        int numBuffs = Integer.parseInt(args[1]); // Number of buffers allocated for the buffer pool.
        int buffSize = Integer.parseInt(args[2]); // Size for a block in the memory pool (and also the size of a buffer in the buffer pool).
        int initHashSize = Integer.parseInt(args[3]); // Initial size of the hash table.
        String commandFile = args[4]; // Name of the command file to read.
        String statFile = args[5]; // Name of the statistics file to generate.

        // Initialize the BufferPool
        BufferPool bufferPool = new BufferPool(memFile, numBuffs, buffSize, initHashSize);

        // Process the commands from the command file
        processCommands(bufferPool, commandFile);

        // Generate and output the statistics
        generateStatistics(bufferPool, statFile);

        // Close the BufferPool
        bufferPool.close();
    }

    private static void processCommands(BufferPool bufferPool, String commandFile) throws IOException {
        // TODO: Implement logic to read the command file and process the commands
        // Use the buffer pool to store and manage data String[] tokens = command.trim().split("\\s+");

        if (tokens.length > 0) {
            String commandName = tokens[0].toLowerCase();
            switch (commandName) {
                case "insert":
                    // Process insert command
                    processInsertCommand(bufferPool, tokens);
                    break;
                case "remove":
                    // Process remove command
                    processRemoveCommand(bufferPool, tokens);
                    break;
                case "print":
                    // Process print command
                    processPrintCommand(bufferPool, tokens);
                    break;
                case "graph":
                    // Process graph command
                    processGraphCommand(bufferPool, tokens);
                    break;
                default:
                    // Invalid command
                    System.out.println("Invalid command: " + command);
                    break;
            }
        }
    }

    private static void generateStatistics(BufferPool bufferPool, String statFile) {
        //  Implement logic to generate statistics about the execution of the program
        // Retrieve the required statistics from the buffer pool
        // Write the statistics to the statFile and also output them to standard output
    }
}

/**
 * 
 */

import java.io.*;
import java.util.*;

/**
 * @author 
 *
 */

public class GraphProject {
    private static final int INIT_HASH_SIZE = 100;
    private static final int INIT_NUM_BUFFS = 10;
    private static final int BUFF_SIZE = 4096;
    private static final String MEM_FILE = "mem.dat";
    private static final String CMD_FILE = "cmd.txt";
    private static final String STAT_FILE = "stat.txt";
    private static MemManager memManager;
    private static HashTable<String, Handle> artistHashTable;
    private static HashTable<String, Handle> songHashTable;
    private static Graph<String> graph;
    private static int numCacheHits;
    private static int numCacheMisses;
    private static int numDiskReads;

    public static void main(String[] args) throws IOException {
        memManager = new MemManager(BUFF_SIZE * INIT_NUM_BUFFS);
        artistHashTable = new HashTable<>(INIT_HASH_SIZE);
        songHashTable = new HashTable<>(INIT_HASH_SIZE);
        graph = new Graph<>();
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
        Handle artistHandle = artistHashTable.get(artist);
        if (artistHandle == null) {
            artistHandle = memManager.insert(artist.getBytes(), artist
                .length());
            artistHashTable.put(artist, artistHandle);
        }
        Handle songHandle = songHashTable.get(song);
        if (songHandle == null) {
            songHandle = memManager.insert(song.getBytes(), song.length());
            songHashTable.put(song, songHandle);
        }
        graph.addEdge(artistHandle, songHandle);
    }


    private static void remove(String artist, String song) {
        Handle artistHandle = artistHashTable.get(artist);
        if (artistHandle != null) {
            artistHashTable.remove(artist);
            memManager.remove(artistHandle);
        }
        Handle songHandle = songHashTable.get(song);
        if (songHandle != null) {
            songHashTable.remove(song);
            memManager.remove(songHandle);
        }
        graph.removeEdge(artistHandle, songHandle);
    }


    private static void print(String type) {
        if (type.equals("artist")) {
            printArtists();
        }
        else if (type.equals("song")) {
            printSongs();
        }
        else if (type.equals("blocks")) {
            printBlocks();
        }
        else if (type.equals("graph")) {
            printGraph();
        }
        else {
            System.out.println("Unknown type: " + type);
        }
    }


    private static void printArtists() {
        System.out.println("Artists:");
        for (String artist : artistHashTable.keySet()) {
            System.out.println(artist);
        }
    }


    private static void printSongs() {
        System.out.println("Songs:");
        for (String song : songHashTable.keySet()) {
            System.out.println(song);
        }
    }


    private static void printBlocks() {
        System.out.println("Blocks:");
        for (Block block : memManager.getBlocks()) {
            System.out.println(block);
        }
    }


    private static void printGraph() {
        System.out.println("Graph:");
        graph.print();
    }
}
