
import java.io.*;
import java.util.HashMap;
import java.util.Vector;

class MNTEntry {

    String name;
    int pp, kp, mdtp, kpdtp;

    public MNTEntry(String name, int pp, int kp, int mdtp, int kpdtp) {
        this.name = name;
        this.pp = pp;
        this.kp = kp;
        this.mdtp = mdtp;
        this.kpdtp = kpdtp;
    }

    public String getName() {
        return name;
    }

    public int getPp() {
        return pp;
    }

    public int getKp() {
        return kp;
    }

    public int getMdtp() {
        return mdtp;
    }

    public int getKpdtp() {
        return kpdtp;
    }
}

public class MacroPass2 {

    public static void main(String[] args) throws Exception {
        BufferedReader irb = new BufferedReader(new FileReader("intermediate.txt"));
        BufferedReader mdtb = new BufferedReader(new FileReader("mdt.txt"));
        BufferedReader kpdtb = new BufferedReader(new FileReader("kpdt.txt"));
        BufferedReader mntb = new BufferedReader(new FileReader("mnt.txt"));

        FileWriter fr = new FileWriter("pass2.txt");

        HashMap<String, MNTEntry> mnt = new HashMap<>();
        HashMap<Integer, String> aptab = new HashMap<>();
        HashMap<String, Integer> aptabInverse = new HashMap<>();

        Vector<String> mdt = new Vector<>();
        Vector<String> kpdt = new Vector<>();

        String line;
        while ((line = mdtb.readLine()) != null) {
            mdt.add(line);
        }

        while ((line = kpdtb.readLine()) != null) {
            kpdt.add(line);
        }

        while ((line = mntb.readLine()) != null) {
            String[] parts = line.split("\\s+");
            mnt.put(parts[0], new MNTEntry(parts[0], Integer.parseInt(parts[1]),
                    Integer.parseInt(parts[2]), Integer.parseInt(parts[3]), Integer.parseInt(parts[4])));
        }

        while ((line = irb.readLine()) != null) {
            String[] parts = line.split("\\s+");

            if (mnt.containsKey(parts[0])) {
                MNTEntry entry = mnt.get(parts[0]);
                int pp = entry.getPp();
                int kp = entry.getKp();
                int kpdtp = entry.getKpdtp();
                int mdtp = entry.getMdtp();

                int paramNo = 1;
                for (int i = 0; i < pp; i++) {
                    parts[paramNo] = parts[paramNo].replace(",", "");
                    aptab.put(paramNo, parts[paramNo]);
                    aptabInverse.put(parts[paramNo], paramNo);
                    paramNo++;
                }

                int j = kpdtp - 1;
                for (int i = 0; i < kp; i++) {
                    String[] temp = kpdt.get(j).split("\t");
                    aptab.put(paramNo, temp[1]);
                    aptabInverse.put(temp[0], paramNo);
                    j++;
                    paramNo++;
                }

                for (int i = pp + 1; i < parts.length; i++) {
                    parts[i] = parts[i].replace(",", "");
                    String[] splits = parts[i].split("=");
                    String name = splits[0].replaceAll("&", "");
                    aptab.put(aptabInverse.get(name), splits[1]);
                }

                int i = mdtp - 1;
                while (!mdt.get(i).equalsIgnoreCase("MEND")) {
                    String[] splits = mdt.get(i).split("\\s+");
                    fr.write("+");
                    for (String split : splits) {
                        if (split.contains("(P,")) {
                            split = split.replaceAll("[^0-9]", "");
                            String value = aptab.get(Integer.parseInt(split));
                            fr.write(value + "\t");
                        } else {
                            fr.write(split + "\t");
                        }
                    }
                    fr.write("\n");
                    i++;
                }

                aptab.clear();
                aptabInverse.clear();
            } else {
                fr.write(line + "\n");
            }
        }

        fr.close();
        mntb.close();
        mdtb.close();
        kpdtb.close();
        irb.close();

        System.out.println("Macro Pass2 Processing done. :)");

        // Print contents of all relevant files
        printFileContent("intermediate.txt");
        printFileContent("mdt.txt");
        printFileContent("kpdt.txt");
        printFileContent("mnt.txt");
        printFileContent("pass2.txt");
    }

    public static void printFileContent(String filename) throws IOException {
        System.out.println("\nContents of " + filename + ":");
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        }
    }
}
