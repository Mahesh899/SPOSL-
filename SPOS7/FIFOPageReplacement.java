
public class FIFOPageReplacement {

    public static void main(String[] args) {
        int[] incomingStream = {4, 1, 2, 4, 5};
        int pageFaults = 0;
        int frames = 3;
        int pages = incomingStream.length;
        System.out.printf(" Incoming \t Frame 1 \t Frame 2 \t Frame 3 \n");
        int[] temp = new int[frames];
        for (int m = 0; m < frames; m++) {
            temp[m] = -1;
        }
        for (int m = 0; m < pages; m++) {
            int s = 0;
            for (int n = 0; n < frames; n++) {
                if (incomingStream[m] == temp[n]) {
                    s++;
                    pageFaults--;
                }
            }
            pageFaults++;
            if (pageFaults <= frames && s == 0) {
                temp[m] = incomingStream[m];
            } else if (s == 0) {
                temp[(pageFaults - 1) % frames] = incomingStream[m];
            }
            System.out.print("\n" + incomingStream[m] + "\t\t\t");
            for (int n = 0; n < frames; n++) {
                if (temp[n] != -1) {
                    System.out.print(temp[n] + "\t\t\t");
                } else {
                    System.out.print("-\t\t\t");
                }
            }
        }
        System.out.println("\nTotal Page Faults:\t" + pageFaults);
    }
}
