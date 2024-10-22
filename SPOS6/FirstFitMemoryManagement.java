
public class FirstFitMemoryManagement {

    private static final int MEMORY_SIZE = 1000;
    private static final int BLOCK_SIZE = 20;
    private static int[] memory = new int[MEMORY_SIZE];

    public static void initializeMemory() {
        for (int i = 0; i < MEMORY_SIZE; i += BLOCK_SIZE) {
            memory[i] = BLOCK_SIZE;
        }
    }

    public static void printMemory() {
        for (int i = 0; i < MEMORY_SIZE; i += BLOCK_SIZE) {
            System.out.print(memory[i] + " ");
        }
        System.out.println();
    }

    public static void allocateMemory(int processId, int size) {
        for (int i = 0; i < MEMORY_SIZE; i += BLOCK_SIZE) {
            if (memory[i] >= size) {
                System.out.println("Process " + processId + " allocated block " + (i / BLOCK_SIZE) + " of size "
                        + size);
                memory[i] -= size;
                if (memory[i] == 0) {
                    System.out.println("Block " + (i / BLOCK_SIZE) + " freed");
                }
                return;
            }
        }
        System.out.println("Process " + processId + " is waiting for memory");
    }

    public static void releaseMemory(int processId, int blockId) {
        int i = blockId * BLOCK_SIZE;
        int size = BLOCK_SIZE;
        while (memory[i] == 0) {
            i -= BLOCK_SIZE;
            size += BLOCK_SIZE;
        }
        System.out.println("Block " + (i / BLOCK_SIZE) + " of size " + size + " freed by process " + processId);
        memory[i] += size;
    }

    public static void main(String[] args) {
        initializeMemory();
        allocateMemory(1, 50);
        allocateMemory(2, 100);
        allocateMemory(3, 30);
        printMemory();
        releaseMemory(2, 2);
        printMemory();
        allocateMemory(4, 25);
        printMemory();
    }
}
