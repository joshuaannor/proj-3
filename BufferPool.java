import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

public class BufferPool {
    private RandomAccessFile file;
    private BufferBlock[] blocks;
    private int diskReads;
    private int diskWrites;
    private int cacheHits;
    private HashTable artistHashTable;
    private HashTable songHashTable;

    public BufferPool(String memFile, int numBuffs, int buffSize, int initHashSize) throws IOException {
        // Create memory pool file
        file = new RandomAccessFile(memFile, "rw");

        // Initialize buffer blocks
        blocks = new BufferBlock[numBuffs];
        for (int i = 0; i < numBuffs; i++) {
            blocks[i] = new BufferBlock(ByteBuffer.allocate(buffSize), -1);
        }

        // Initialize hash tables
        artistHashTable = new HashTable(initHashSize);
        songHashTable = new HashTable(initHashSize);

        // Other initialization steps, if needed
    }

    private class BufferBlock {
        ByteBuffer buffer;
        long filePosition;
        boolean isDirty;
        long lastAccessed;

        BufferBlock(ByteBuffer buffer, long filePosition) {
            this.buffer = buffer;
            this.filePosition = filePosition;
            this.isDirty = false;
            this.lastAccessed = System.currentTimeMillis();
        }
    }

    public short getKey(int index) throws IOException {
        BufferBlock block = getBufferBlock(index / 1024);
        int positionInBlock = (index % 1024) * 4;
        return block.buffer.getShort(positionInBlock);
    }

    private BufferBlock getBufferBlock(long blockIndex) throws IOException {
        for (BufferBlock block : blocks) {
            if (block.filePosition == blockIndex) {
                cacheHits++;
                block.lastAccessed = System.currentTimeMillis();
                return block;
            }
        }

        BufferBlock lruBlock = null;
        for (BufferBlock block : blocks) {
            if (lruBlock == null || block.lastAccessed < lruBlock.lastAccessed) {
                lruBlock = block;
            }
        }

        if (lruBlock.isDirty) {
            diskWrites++;
            file.seek(lruBlock.filePosition);
            file.write(lruBlock.buffer.array());
            lruBlock.buffer.clear();
        }

        diskReads++;
        file.seek(blockIndex);
        file.read(lruBlock.buffer.array());
        lruBlock.buffer.flip();
        lruBlock.filePosition = blockIndex;
        lruBlock.isDirty = false;
        lruBlock.lastAccessed = System.currentTimeMillis();
        return lruBlock;
    }

    public void writeRecord(int index, short key, short data) throws IOException {
        BufferBlock block = getBufferBlock(index / 1024);
        int positionInBlock = (index % 1024) * 4;
        block.buffer.putShort(positionInBlock, key);
        block.buffer.putShort(positionInBlock + 2, data);
        block.isDirty = true;
    }

    // Other methods and inner classes related to the BufferPool

    public void close() throws IOException {
        for (BufferBlock block : blocks) {
            if (block.isDirty) {
                file.seek(block.filePosition);
                file.write(block.buffer.array());
                block.buffer.clear();
            }
        }
        file.close();
    }
}
