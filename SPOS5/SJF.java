
import java.util.Scanner;

public class SJF {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the number of processes: ");
        int n = sc.nextInt();

        int[] bt = new int[n]; // burst time
        int[] at = new int[n]; // arrival time
        int[] wt = new int[n]; // waiting time
        int[] tat = new int[n]; // turnaround time

        System.out.println("Enter burst time for each process:");
        for (int i = 0; i < n; i++) {
            System.out.print("P" + (i + 1) + ": ");
            bt[i] = sc.nextInt();
        }

        System.out.println("Enter arrival time for each process:");
        for (int i = 0; i < n; i++) {
            System.out.print("P" + (i + 1) + ": ");
            at[i] = sc.nextInt();
        }

        int[] remainingTime = bt.clone();
        int[] completionTime = new int[n];
        boolean[] isCompleted = new boolean[n];

        int currentTime = 0;
        int completed = 0;

        while (completed != n) {
            int minIndex = -1;
            int minRemainingTime = Integer.MAX_VALUE;

            for (int i = 0; i < n; i++) {
                if (at[i] <= currentTime && !isCompleted[i] && remainingTime[i] < minRemainingTime) {
                    minRemainingTime = remainingTime[i];
                    minIndex = i;
                }
            }

            if (minIndex == -1) {
                currentTime++;
            } else {
                remainingTime[minIndex]--;
                currentTime++;

                if (remainingTime[minIndex] == 0) {
                    completionTime[minIndex] = currentTime;
                    isCompleted[minIndex] = true;
                    completed++;

                    wt[minIndex] = completionTime[minIndex] - bt[minIndex] - at[minIndex];
                    if (wt[minIndex] < 0) {
                        wt[minIndex] = 0;
                    }

                    tat[minIndex] = completionTime[minIndex] - at[minIndex];
                }
            }
        }

        System.out.println("\nProcess\tBurst Time\tArrival Time\tWaiting Time\tTurnaround Time");
        for (int i = 0; i < n; i++) {
            System.out.println("P" + (i + 1) + "\t" + bt[i] + "\t\t" + at[i] + "\t\t" + wt[i] + "\t\t" + tat[i]);
        }
        sc.close();
    }
}
