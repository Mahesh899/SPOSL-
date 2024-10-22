
import java.util.*;

class Process {

    int id, burstTime, priority, waitingTime, turnaroundTime, completionTime;

    Process(int id, int burstTime, int priority) {
        this.id = id;
        this.burstTime = burstTime;
        this.priority = priority;
    }
}

public class PriorityScheduling {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        List<Process> processes = new ArrayList<>();
        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();
        for (int i = 0; i < n; i++) {
            System.out.print("Enter burst time and priority for process " + (i + 1) + ": ");
            int bt = sc.nextInt();
            int p = sc.nextInt();
            processes.add(new Process(i + 1, bt, p));
        }
        processes.sort(Comparator.comparingInt(p -> p.priority));
        int currentTime = 0;
        for (Process proc : processes) {
            proc.waitingTime = currentTime;
            currentTime += proc.burstTime;
            proc.turnaroundTime = proc.waitingTime + proc.burstTime;
            proc.completionTime = currentTime;
        }
        System.out.println("PID\tBurst Time\tPriority\tWaiting Time\tTurnaround Time\tCompletion Time");
        for (Process proc : processes) {
            System.out.println(proc.id + "\t" + proc.burstTime + "\t\t" + proc.priority + "\t\t"
                    + proc.waitingTime + "\t\t" + proc.turnaroundTime + "\t\t" + proc.completionTime);
        }
        sc.close();
    }
}
