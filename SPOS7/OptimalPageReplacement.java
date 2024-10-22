
import java.util.Arrays;

public class OptimalPageReplacement {

    static final int NUM_FRAMES = 3;
    static final int NUM_PAGES = 10;
    // Function to find the page that will be referenced furthest in the future

    static int findOptimalPage(int[] pageReferences, int[] pageFrames, int index) {
        int farthest = -1;
        int farthestIndex = -1;
        for (int i = 0; i < NUM_FRAMES; i++) {
            int j;
            for (j = index; j < NUM_PAGES; j++) {
                if (pageFrames[i] == pageReferences[j]) {
                    if (j > farthest) {
                        farthest = j;
                        farthestIndex = i;
                    }
                    break;
                }
            }
            if (j == NUM_PAGES) {
                return i;
            }
        }
        return (farthestIndex == -1) ? 0 : farthestIndex;
    }

    public static void main(String[] args) {
        int[] pageReferences = {7, 0, 1, 2, 0, 3, 0, 4, 2, 3};
        int[] pageFrames = new int[NUM_FRAMES];
        boolean[] isPageInFrame = new boolean[NUM_FRAMES];
        int pageFaults = 0;
        Arrays.fill(pageFrames, -1);
        Arrays.fill(isPageInFrame, false);
        System.out.print("Page Reference String: ");
        for (int i = 0; i < NUM_PAGES; i++) {
            System.out.print(pageReferences[i] + " ");
        }
        System.out.println();
        for (int i = 0; i < NUM_PAGES; i++) {
            int page = pageReferences[i];
            boolean found = false;
            for (int j = 0; j < NUM_FRAMES; j++) {
                if (pageFrames[j] == page) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                int pageToReplace = findOptimalPage(pageReferences, pageFrames, i + 1);
                pageFrames[pageToReplace] = page;
                pageFaults++;
                System.out.println("Page " + page + " loaded into frame " + pageToReplace);
            }
            System.out.print("Frames: ");
            for (int frame : pageFrames) {
                System.out.print(frame + " ");
            }
            System.out.println();
        }
        System.out.println("Total Page Faults: " + pageFaults);
    }
}
