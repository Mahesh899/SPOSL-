import java.io.*;
import java.util.ArrayList;

class TableRow {
    String symbol;
    int address;
    int index;

    public TableRow(String symbol, int address) {
        this.symbol = symbol;
        this.address = address;
    }

    public TableRow(String symbol, int address, int index) {
        this(symbol, address);
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public int getAddress() {
        return address;
    }

    public void setAddress(int address) {
        this.address = address;
    }
}

public class Pass_2 {
    ArrayList<TableRow> SYMTAB, LITTAB;

    public Pass_2() {
        SYMTAB = new ArrayList<>();
        LITTAB = new ArrayList<>();
    }

    public static void main(String[] args) {
        Pass_2 pass2 = new Pass_2();
        try {
            pass2.generateCode("IC.txt");
            System.out.println("Intermediate Code:");
            pass2.printPass2Output("IC.txt");

            System.out.println("\nLiteral Table:");
            pass2.printPass2Output("LITTAB.txt");

            System.out.println("\nSymbol Table:");
            pass2.printPass2Output("SYMTAB.txt");

            System.out.println("\nGenerated Machine Code:");
            pass2.printPass2Output("PASS2.txt");
        } catch (Exception e) {
            System.err.println("Error during Pass 2: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void readtables() {
        try (BufferedReader symReader = new BufferedReader(new FileReader("SYMTAB.txt"));
             BufferedReader litReader = new BufferedReader(new FileReader("LITTAB.txt"))) {

            String line;
            while ((line = symReader.readLine()) != null) {
                String[] parts = line.split("\\s+");
                SYMTAB.add(new TableRow(parts[1], Integer.parseInt(parts[2]), Integer.parseInt(parts[0])));
            }

            while ((line = litReader.readLine()) != null) {
                String[] parts = line.split("\\s+");
                LITTAB.add(new TableRow(parts[1], Integer.parseInt(parts[2]), Integer.parseInt(parts[0])));
            }
        } catch (IOException e) {
            System.err.println("Error reading tables: " + e.getMessage());
        }
    }

    public void generateCode(String filename) throws IOException {
        readtables();

        try (BufferedReader br = new BufferedReader(new FileReader(filename));
             BufferedWriter bw = new BufferedWriter(new FileWriter("PASS2.txt"))) {

            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\s+");
                String code;

                if (parts[0].contains("AD") || parts[0].contains("DL,02")) {
                    bw.write("\n");
                    continue;
                } else if (parts.length == 2 && parts[0].contains("DL")) {
                    int constant = Integer.parseInt(parts[1].replaceAll("[^0-9]", ""));
                    code = String.format("00\t0\t%03d\n", constant);
                    bw.write(code);
                } else if (parts[0].contains("IS")) {
                    int opcode = Integer.parseInt(parts[0].replaceAll("[^0-9]", ""));
                    if (parts.length == 1) {
                        code = String.format("%02d\t0\t000\n", opcode);
                    } else if (parts.length == 3) {
                        int regcode = Integer.parseInt(parts[1]);
                        int address = getAddress(parts[2]);
                        code = String.format("%02d\t%d\t%03d\n", opcode, regcode, address);
                    } else {
                        int address = getAddress(parts[1]);
                        code = String.format("%02d\t0\t%03d\n", opcode, address);
                    }
                    bw.write(code);
                }
            }
        }
    }

    private int getAddress(String part) {
        int index = Integer.parseInt(part.replaceAll("[^0-9]", ""));
        if (part.contains("S")) {
            return SYMTAB.get(index - 1).getAddress();
        } else if (part.contains("L")) {
            return LITTAB.get(index - 1).getAddress();
        }
        return 0;
    }

    public void printPass2Output(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }
}
