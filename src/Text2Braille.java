package com.chickasaw.text2braille;
import java.io.*;
import java.util.Arrays;
public class Text2Braille {
    private String[] strChars;
    private String[] strCodes;
    private Pair[] intChrLen;
    private int intChars;
    private int intBrailleChars;

    public Text2Braille(String strInput) {
        intChars = strInput.length();
        this.initialize("default");
    }
    public Text2Braille(String strInput, String encoding) {
        intChars = strInput.length();
        this.initialize(encoding);
    }
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Please provide more arguments!");
        } else {
            for (String str:args) {
                Text2Braille ttb = new Text2Braille(str);
                String[] code = ttb.text2Bin(args[0]);
                char[][] dispCode = ttb.bin2Braille(code);
                for (int count = 0; count < dispCode.length; count++) {
                    for (int count2 = 0; count2 < dispCode[count].length; count2++) {
                        System.out.print(dispCode[count][count2]);
                    }
                    System.out.print("\r\n");
                }
            }
        }
    }
    public String[] text2Bin(String input) {
        String[] output = new String[input.toCharArray().length];
        int intCount = 0;
        for (int count = 0; count < strChars.length; count++) {
            
        }
        for (char c:input.toCharArray()) {
            for (int count = 0; count < strChars.length; count++) {
                if (String.valueOf(c).toUpperCase().equals(strChars[count])) {
                    output[intCount] = strCodes[count];
                    intCount++;
                }
            }
        }
        return output;
    }
    public char[][] bin2Braille(String[] bin) {
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
    public void initialize(String encoding) {
        String line = null;
        int intLines = 0;
        try (BufferedReader inputStream = new BufferedReader(new FileReader("C:\\Users\\alexh\\Desktop\\" + encoding + ".txt"))) {
            while ((line = inputStream.readLine()) != null) {
                intLines++;
            }
        } catch (IOException x) {
            System.out.println("SHIT!");
            System.exit(1);
        }
        try (BufferedReader inputStream = new BufferedReader(new FileReader("C:\\Users\\alexh\\Desktop\\" + encoding + ".txt"))) {
            line = null;
            strChars = new String[intLines];
            strCodes = new String[intLines];
            intChrLen = new Pair[intLines];
            int intCount = 0;
            while ((line = inputStream.readLine()) != null) {
                strChars[intCount] = line.substring(0, line.indexOf(':', 1));
                if (!strChars[intCount].substring(0, 1).equals("%")) {
                    intChrLen[intCount] = new Pair(intCount, 0);
                } else {
                    intChrLen[intCount] = new Pair(intCount, strChars[intCount].length());
                }
                strCodes[intCount] = line.substring(line.indexOf(':', 1) + 1, line.indexOf(';', 1));
                intCount++;
            }
        } catch (IOException x) {
            System.out.println("SHIT!");
            System.exit(1);
        }
        Arrays.sort(intChrLen);
        String[] strCharsCopy = strChars;
        String[] strCodesCopy = strCodes;
        int index = 0;
        int intCount = 0;
        for (Pair ind:intChrLen) {
            index = ind.getInd();
            strChars[intCount] = strCharsCopy[index];
            strCodes[intCount] = strCodesCopy[index];
        }
    }
}