
import java.util.Scanner;

public class BestFitMemoryManagement {

    static final int MAX = 25;

    public static void main(String[] args) {
        int[] frag = new int[MAX];
        int[] b = new int[MAX];
        int[] f = new int[MAX];
        int[] bf = new int[MAX];
        int[] ff = new int[MAX];
        int nb, nf, temp;
        int lowest = 10000;
        Scanner sc = new Scanner(System.in);
        System.out.println("Memory Management Scheme Best Fit");
        System.out.print("Enter the number of blocks: ");
        nb = sc.nextInt();
        System.out.print("Enter the number of files: ");
        nf = sc.nextInt();
        System.out.println("Enter the size of the blocks:");
        for (int i = 1; i <= nb; i++) {
            System.out.print("Block " + i + ": ");
            b[i] = sc.nextInt();
        }
        System.out.println("Enter the size of the files:");
        for (int i = 1; i <= nf; i++) {
            System.out.print("File " + i + ": ");
            f[i] = sc.nextInt();
        }
        for (int i = 1; i <= nf; i++) {
            for (int j = 1; j <= nb; j++) {
                if (bf[j] != 1) {
                    temp = b[j] - f[i];
                    if (temp >= 0) {
                        if (lowest > temp) {
                            ff[i] = j;
                            lowest = temp;
                        }
                    }
                }
            }
            frag[i] = lowest;
            bf[ff[i]] = 1;
            lowest = 10000;
        }
        System.out.println("\nFile No\tFile Size\tBlock No\tBlock Size\tFragment");
        for (int i = 1; i <= nf && ff[i] != 0; i++) {
            System.out.println(i + "\t\t" + f[i] + "\t\t" + ff[i] + "\t\t" + b[ff[i]] + "\t\t" + frag[i]);
        }
        sc.close();
    }
}
