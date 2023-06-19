import java.util.ArrayList;
import java.util.List;

public class MemManager {
    private byte[] memoryPool;
    private List<FreeBlock> freeBlocks;

    public MemManager(int poolSize) {
        memoryPool = new byte[poolSize];
        freeBlocks = new ArrayList<>();
        freeBlocks.add(new FreeBlock(0, poolSize));
    }

    public Handle insert(byte[] space, int size) {
        int position = findFreeBlock(size);
        if (position == -1) {
            expandMemoryPool(size);
            position = findFreeBlock(size);
        }

        // Update the free block list
        FreeBlock block = freeBlocks.remove(position);
        int remainingSize = block.size - size;
        if (remainingSize > 0) {
            freeBlocks.add(position, new FreeBlock(block.start + size, remainingSize));
        }

        // Copy the record into the memory pool
        System.arraycopy(space, 0, memoryPool, block.start, size);

        return new Handle(block.start, size);
    }

    public void remove(Handle theHandle) {
        // Mark the block as a free block
        FreeBlock freeBlock = new FreeBlock(theHandle.start, theHandle.length);
        int position = findInsertionPoint(freeBlock);
        mergeAdjacentBlocks(position, freeBlock);
    }

    public int get(byte[] space, Handle theHandle, int size) {
        // Copy the record from the memory pool into the given space
        int copySize = Math.min(size, theHandle.length);
        System.arraycopy(memoryPool, theHandle.start, space, 0, copySize);
        return copySize;
    }

    public void dump() {
        System.out.println("Free Blocks:");
        for (FreeBlock block : freeBlocks) {
            System.out.println(block);
        }
    }

    private int findFreeBlock(int size) {
        for (int i = 0; i < freeBlocks.size(); i++) {
            if (freeBlocks.get(i).size >= size) {
                return i;
            }
        }
        return -1;
    }

    private void expandMemoryPool(int size) {
        int currentSize = memoryPool.length;
        int newSize = Math.max(currentSize * 2, size);
        byte[] newMemoryPool = new byte[newSize];
        System.arraycopy(memoryPool, 0, newMemoryPool, 0, currentSize);
        memoryPool = newMemoryPool;
        freeBlocks.add(new FreeBlock(currentSize, newSize - currentSize));
    }

    private int findInsertionPoint(FreeBlock freeBlock) {
        int position = 0;
        while (position < freeBlocks.size() && freeBlocks.get(position).start < freeBlock.start) {
            position++;
        }
        return position;
    }

    private void mergeAdjacentBlocks(int position, FreeBlock freeBlock) {
        if (position > 0) {
            FreeBlock previousBlock = freeBlocks.get(position - 1);
            if (previousBlock.start + previousBlock.size == freeBlock.start) {
                previousBlock.size += freeBlock.size;
                freeBlocks.remove(position);
                position--;
                freeBlock = previousBlock;
            }
        }
        if (position < freeBlocks.size() - 1) {
            FreeBlock nextBlock = freeBlocks.get(position + 1);
            if (freeBlock.start + freeBlock.size == nextBlock.start) {
                freeBlock.size += nextBlock.size;
                freeBlocks.remove(position + 1);
            }
        }
        freeBlocks.add(position, freeBlock);
    }

    public class Handle {
        private int start;
        private int length;

        public Handle(int start, int length) {
            this.start = start;
            this.length = length;
        }

        public int getStart() {
            return start;
        }

        public int getLength() {
            return length;
        }
    }

    private static class FreeBlock {
        private int start;
        private int size;

        public FreeBlock(int start, int size) {
            this.start = start;
            this.size = size;
        }

        @Override
        public String toString() {
            return "Block: start=" + start + ", size=" + size;
        }
    }
}
