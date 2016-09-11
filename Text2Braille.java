import java.io.*;
public class Text2Braille {
    private static String[] strChars;
    private static String[] strCodes;
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Please provide more arguments!");
        } else {
            Text2Braille.initialize();
            String[] code = Text2Braille.text2Bin(args[0]);
            char[][] dispCode = Text2Braille.bin2Braille(code);
            for (int count = 0; count < dispCode.length; count++) {
                for (int count2 = 0; count2 < dispCode[count].length; count2++) {
                    System.out.print(dispCode[count][count2]);
                }
                System.out.print("\r\n");
            }
        }
    }
    public static String[] text2Bin(String input) {
        String[] output = new String[input.toCharArray().length];
        int intCount = 0;
        for (char c:input.toCharArray()) {
            for (int count = 0; count < 63; count++) {
                if (String.valueOf(c).toUpperCase().equals(strChars[count])) {
                    output[intCount] = strCodes[count];
                    intCount++;
                }
            }
        }
        return output;
    }
    public static char[][] bin2Braille(String[] bin) {
        char[][] dispCode = new char[3][bin.length * 2];
        int intCount = 0;
        for (String str:bin) {
            dispCode[0][2 * intCount] = str.charAt(0);
            dispCode[0][2 * intCount + 1] = str.charAt(1);
            dispCode[1][2 * intCount] = str.charAt(2);
            dispCode[1][2 * intCount + 1] = str.charAt(3);
            dispCode[2][2 * intCount] = str.charAt(4);
            dispCode[2][2 * intCount + 1] = str.charAt(5);
            intCount++;
        }
        return dispCode;
    }
    public static void initialize() {
        String line = null;
        int intLines = 0;
        try (BufferedReader inputStream = new BufferedReader(new FileReader("table.txt"))) {
            while ((line = inputStream.readLine()) != null) {
                intLines++;
            }
        } catch (IOException x) {
            System.out.println("SHIT!");
            System.exit(1);
        }
        try (BufferedReader inputStream = new BufferedReader(new FileReader("table.txt"))) {
            line = null;
            strChars = new String[intLines];
            strCodes = new String[intLines];
            int intCount = 0;
            while ((line = inputStream.readLine()) != null) {
                strChars[intCount] = String.valueOf(line.charAt(0));
                strCodes[intCount] = line.substring(2, 8);
                intCount++;
            }
        } catch (IOException x) {
            System.out.println("SHIT!");
            System.exit(1);
        }
    }
}