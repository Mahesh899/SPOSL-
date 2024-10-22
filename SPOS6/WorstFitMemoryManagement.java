
public class WorstFitMemoryManagement {

    private static final int MEMORY_SIZE = 100;
    private static final int BLOCK_SIZE = 10;
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
        int largestBlockIndex = -1;
        int maxBlockSize = -1;
        for (int i = 0; i < MEMORY_SIZE; i += BLOCK_SIZE) {
            if (memory[i] >= size && memory[i] > maxBlockSize) {
                largestBlockIndex = i;
                maxBlockSize = memory[i];
            }
        }
        if (largestBlockIndex != -1) {
            System.out.println("Process " + processId + " allocated block " + (largestBlockIndex / BLOCK_SIZE)
                    + " of size " + size);
            memory[largestBlockIndex] -= size;
            if (memory[largestBlockIndex] == 0) {
                System.out.println("Block " + (largestBlockIndex / BLOCK_SIZE) + "fully utilized and marked as free");
            }
        } else {
            System.out.println("Process " + processId + " is waiting for memory");
        }
    }

    public static void releaseMemory(int processId, int blockId) {
        int i = blockId * BLOCK_SIZE;
        int size = BLOCK_SIZE;
        while (memory[i] == 0) {
            i = (i - BLOCK_SIZE + MEMORY_SIZE) % MEMORY_SIZE;
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
