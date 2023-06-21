public class Block {
        private int index;
        private int size;

        public Block(int index, int size) {
            this.index = index;
            this.size = size;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }
    }
