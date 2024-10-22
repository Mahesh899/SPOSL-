
import java.util.*;

class Process {

    int id, burstTime, remainingTime, waitingTime, turnaroundTime, completionTime;

    Process(int id, int burstTime) {
        this.id = id;
        this.burstTime = burstTime;
        this.remainingTime = burstTime;
    }
}

public class RoundRobinScheduling {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        List<Process> processes = new ArrayList<>();
        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();
        System.out.print("Enter time quantum: ");
        int timeQuantum = sc.nextInt();
        for (int i = 0; i < n; i++) {
            System.out.print("Enter burst time for process " + (i + 1) + ": ");
            int bt = sc.nextInt();
            processes.add(new Process(i + 1, bt));
        }
        int currentTime = 0;
        Queue<Process> queue = new LinkedList<>(processes);
        while (!queue.isEmpty()) {
            Process proc = queue.poll();
            if (proc.remainingTime > timeQuantum) {
                currentTime += timeQuantum;
                proc.remainingTime -= timeQuantum;
                queue.add(proc);
            } else {
                currentTime += proc.remainingTime;
                proc.remainingTime = 0;
                proc.completionTime = currentTime;
                proc.turnaroundTime = proc.completionTime;
                proc.waitingTime = proc.turnaroundTime - proc.burstTime;
            }
        }
        System.out.println("PID\tBurst Time\tWaiting Time\tTurnaround Time\tCompletion Time");
        for (Process proc : processes) {
            System.out.println(proc.id + "\t" + proc.burstTime + "\t\t" + proc.waitingTime + "\t\t"
                    + proc.turnaroundTime + "\t\t" + proc.completionTime);
        }
        sc.close();
    }
}
