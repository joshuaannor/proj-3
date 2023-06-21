public class MemManager {
    private byte[] memory;
    private Block[] blocks;
    private int blockCount;

    public MemManager(int size) {
        memory = new byte[size];
        blocks = new Block[size];
        blocks[0] = new Block(0, size);
        blockCount = 1;
    }

    public Handle insert(byte[] data, int size) {
        int blockIndex = findFreeBlock(size);
        if (blockIndex == -1) {
            throw new IllegalStateException("No free blocks available to insert data.");
        }

        Block freeBlock = blocks[blockIndex];
        int offset = freeBlock.getIndex();
        System.arraycopy(data, 0, memory, offset, size);

        if (freeBlock.getSize() > size) {
            // Create a new block for the remaining free space
            int newBlockSize = freeBlock.getSize() - size;
            int newBlockIndex = freeBlock.getIndex() + size;
            blocks[blockCount] = new Block(newBlockIndex, newBlockSize);
            blockCount++;
        }

        freeBlock.setSize(size);
        return new Handle(blockIndex, offset, size);
    }

    public void remove(Handle handle) {
        Block block = blocks[handle.getBlockIndex()];
        block.setSize(0);
    }

    public void dump() {
        for (int i = 0; i < blockCount; i++) {
            Block block = blocks[i];
            int offset = block.getIndex();
            int size = block.getSize();
            byte[] data = new byte[size];
            System.arraycopy(memory, offset, data, 0, size);
            System.out.println("Block " + i + ": Offset=" + offset + ", Size=" + size + ", Data=" + new String(data));
        }
    }
    public Block[] getBlocks() {
        Block[] result = new Block[blockCount];
        int index = 0;
        for (int i = 0; i < blockCount; i++) {
            Block block = blocks[i];
            if (block != null) {
                result[index] = block;
                index++;
            }
        }
        return result;
    }
    private int findFreeBlock(int size) {
        for (int i = 0; i < blockCount; i++) {
            Block block = blocks[i];
            if (block.getSize() == 0 && block.getSize() >= size) {
                return i;
            }
        }
        return -1;
    }
}
