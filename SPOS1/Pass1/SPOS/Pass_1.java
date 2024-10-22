
import java.io.*;
import java.util.*;

class MnemonicTable {

    String mnemonic, opcode;
    int num;

    MnemonicTable(String mnemonic, String opcode, int num) {
        this.mnemonic = mnemonic;
        this.opcode = opcode;
        this.num = num;
    }
}

public class Pass_1 {

    Map<String, MnemonicTable> is = new Hashtable<>();
    List<String> symtab = new ArrayList<>();
    List<Integer> symaddr = new ArrayList<>();
    List<String> littab = new ArrayList<>();
    List<Integer> litaddr = new ArrayList<>();
    List<Integer> pooltab = new ArrayList<>();
    int LC = 0;

    public void createIS() {
        String[] mnemonics = {"STOP", "ADD", "SUB", "MULT", "MOVER", "MOVEM", "COMP", "BC", "DIV", "READ", "PRINT"};
        for (int i = 0; i < mnemonics.length; i++) {
            is.put(mnemonics[i], new MnemonicTable(mnemonics[i], String.format("%02d", i), 0));
        }
    }

    public void generateIC() throws IOException {
        BufferedWriter wr = new BufferedWriter(new FileWriter("ic.txt"));
        BufferedReader br = new BufferedReader(new FileReader("input.asm"));
        pooltab.add(0);

        String line;
        while ((line = br.readLine()) != null) {
            if (line.trim().isEmpty()) {
                continue;
            }

            String[] split = line.split("\\s+");
            if (split.length < 2) {
                System.out.println("Skipping malformed line: " + line);
                continue;
            }

            if (!symtab.contains(split[0])) {
                symtab.add(split[0]);
                symaddr.add(LC);
            }

            switch (split[1]) {
                case "START":
                    LC = Integer.parseInt(split[2]);
                    wr.write("(AD,01)(C," + split[2] + ") \n");
                    break;

                case "LTORG":
                case "END":
                    processLiterals();
                    if (split[1].equals("END")) {
                        wr.write("(AD,04)\n");
                    }
                    break;

                case "DS":
                    LC += Integer.parseInt(split[2]);
                    wr.write("(DL,01)(C," + split[2] + ")\n");
                    break;

                case "DC":
                    LC++;
                    wr.write("(DL,02)(C," + split[2].replace("'", "") + ")\n");
                    break;

                default:
                    processInstruction(split, wr);
                    LC++;
            }
        }
        writeTables();
        wr.close();
        br.close();
    }

    private void processInstruction(String[] split, BufferedWriter wr) throws IOException {
        if (is.containsKey(split[1])) {
            wr.write("(IS," + is.get(split[1]).opcode + ") ");
            if (split.length > 2 && symtab.contains(split[2])) {
                wr.write("(S," + symtab.indexOf(split[2]) + ")\n");
            } else if (split.length > 3 && split[3].contains("=")) {
                addLiteral(split[3], wr);
            }
        }
    }

    private void addLiteral(String literal, BufferedWriter wr) throws IOException {
        String norm = literal.replace("=", "").replace("'", "");
        if (!littab.contains(norm)) {
            littab.add(norm);
            litaddr.add(0);
        }
        wr.write("(L," + littab.indexOf(norm) + ")");
    }

    private void processLiterals() {
        for (int i = pooltab.get(pooltab.size() - 1); i < littab.size(); i++) {
            if (litaddr.get(i) == 0) {
                litaddr.set(i, LC++);
            }
        }
        pooltab.add(littab.size());
    }

    private void writeTables() throws IOException {
        writeFile("sym.txt", "Symbol Table", symtab, symaddr);
        writeFile("lit.txt", "Literal Table", littab, litaddr);
        writeFile("pool.txt", "Pool Table", pooltab, new ArrayList<>());
    }

    private void writeFile(String filename, String title, List<?> names, List<Integer> addresses) throws IOException {
        BufferedWriter wr = new BufferedWriter(new FileWriter(filename));
        wr.write(title + "\n----------------\nName  Address\n");
        for (int i = 0; i < names.size(); i++) {
            wr.write(names.get(i) + "   " + addresses.get(i) + "\n");
        }
        wr.close();
    }

    private void writeFile(String filename, String title, List<Integer> pooltab) throws IOException {
        BufferedWriter wr = new BufferedWriter(new FileWriter(filename));
        wr.write(title + "\n----------------\nIndex\n");
        for (int value : pooltab) {
            wr.write(value + "\n");
        }
        wr.close();
    }

    public static void main(String[] args) throws Exception {
        Pass_1 p = new Pass_1();
        p.createIS();
        p.generateIC();
    }
}
