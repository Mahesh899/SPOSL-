
import java.util.Scanner;

public class LRUPageReplacement {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the length of reference string -- ");
        int n = sc.nextInt();
        int[] rs = new int[n];
        System.out.print("Enter the reference string -- ");
        for (int i = 0; i < n; i++) {
            rs[i] = sc.nextInt();
        }
        System.out.print("Enter the number of frames -- ");
        int f = sc.nextInt();
        int[] m = new int[f];
        int[] count = new int[f];
        boolean[] flag = new boolean[n];
        int pf = 0, next = 1;
        for (int i = 0; i < f; i++) {
            count[i] = 0;
            m[i] = -1;
        }
        System.out.println("\nThe Page Replacement process is -- ");
        for (int i = 0; i < n; i++) {
            boolean found = false;
            for (int j = 0; j < f; j++) {
                if (m[j] == rs[i]) {
                    flag[i] = true;
                    count[j] = next++;
                    found = true;
                    break;
                }
            }
            if (!found) {
                if (i < f) {
                    m[i] = rs[i];
                    count[i] = next++;
                } else {
                    int min = 0;
                    for (int j = 1; j < f; j++) {
                        if (count[min] > count[j]) {
                            min = j;
                        }
                    }
                    m[min] = rs[i];
                    count[min] = next++;
                }
                pf++;
            }
            for (int j = 0; j < f; j++) {
                System.out.print(m[j] + "\t");
            }
            if (!flag[i]) {
                System.out.println("PF No. -- " + pf);
            } else {
                System.out.println();
            }
        }
        System.out.println("\nThe number of page faults using LRU are " + pf);
        sc.close();
    }
}
