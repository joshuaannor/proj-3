import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * The Buffer class represents a buffer that holds data read from or written to
 * a disk. Each buffer corresponds to a specific block on the disk.
 * 
 * @author Mark Fuentes, Joshua Annor
 * @version 06/09/23
 */
public class Buffer {
    private int block;
    private byte[] data;
    private RandomAccessFile disk;
    private int position;
    private boolean isDirty;
    private final static int BLOCK_SIZE = 4096;

    /**
     * Creates a new Buffer object associated with the specified disk file and
     * block index
     * 
     * @param disk
     *            the disk file
     * @param block
     *            the index of the block
     */
    public Buffer(RandomAccessFile disk, int block) {
        data = new byte[BLOCK_SIZE];
        this.disk = disk;
        this.block = block;
        position = block * BLOCK_SIZE;
        isDirty = false;
    }

    /**
     * Retrieves the data stored in the buffer
     * 
     * @return the data array
     */
    public byte[] read() {
//        Quicksort.setCacheHits(Quicksort.getCacheHits() + 1);
        return data;
    }

    /**
     * Sets the data in the buffer to the specified new data
     * 
     * @param newData
     *            the new data to be set
     */
    public void write(byte[] newData) {
        data = newData;
        isDirty = true;
    }

    /**
     * Writes the data in the buffer back to the disk file.
     * 
     * @throws IOException
     *             if an I/O error occurs
     */
    public void writeBack() throws IOException {
        if (isDirty) {
            disk.seek(position);
            disk.write(data);
            isDirty = false;
//            Quicksort.setDiskWrites(Quicksort.getDiskWrites() + 1);
        }
    }

    /**
     * Reads data from the disk file into the buffer
     * 
     * @throws IOException
     *             if an I/O error occurs
     */
    public void diskRead() throws IOException {
        data = new byte[BLOCK_SIZE];
        disk.seek(position);
        disk.read(data);
//        Quicksort.setDiskReads(Quicksort.getDiskReads() + 1);
    }

    /**
     * Returns the block index associated with the buffer
     * 
     * @return the block index
     */
    public int getBlock() {
        return block;
    }
}
