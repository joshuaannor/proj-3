public class Handle {
    private int blockIndex;
    private int offset;
    private int size;

    public Handle(int blockIndex, int offset, int size) {
        this.blockIndex = blockIndex;
        this.offset = offset;
        this.size = size;
    }

    public int getBlockIndex() {
        return blockIndex;
    }

    public int getOffset() {
        return offset;
    }

    public int getSize() {
        return size;
    }

    public String getKey() {
        return blockIndex + "-" + offset;
    }
}

