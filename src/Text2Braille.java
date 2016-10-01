package com.chickasaw.text2braille;
import java.io.*;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Text2Braille {
    private String[] strChars;
    private String[] strCodes;
    private Pair[] intChrLen;
    private int intChars;
    private int intBrailleChars;
    private int[][] intInds;

    public Text2Braille(String strInput) {
        this(strInput, "C:\\Users\\alexh\\Desktop\\grade2.txt");
    }
    public Text2Braille(String strInput, String encoding) {
        intChars = strInput.length();
        this.initialize(encoding);
    }
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Please provide more arguments!");
        } else {
            String strCode = String.join(" ", args);
            Text2Braille ttb = new Text2Braille(strCode);
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
    public int getIntChars() {
        return this.intChars;
    }
    public int getIntBrailleChars() {
        return this.intBrailleChars;
    }
    public String[] text2Bin(String input) {
        Pattern pnum = Pattern.compile("\\d+");
        Matcher mnum = pnum.matcher(input);
        while (mnum.find()) {
            input = input.substring(0, mnum.start()) + "#" + input.substring(mnum.start());
        }
        mnum.reset();
/*        pnum = Pattern.compile("[A-Z]+");
        mnum = pnum.matcher(input);
        while (mnum.find()) {
            input = input.substring(0, mnum.start()) + "__%c__" + input.substring(mnum.start());
        }*/

        int iCM;
        int iC = 0;
        for (String strPat:strChars) {
            // IF the pattern has a dash at the beginning AND is not length 1, then an .* is necessary!
            if (strPat.length() != 1 && (strPat.charAt(0) == '-' || strPat.charAt(strPat.length()-1) == '-')) {

                boolean logBeg = false;
                boolean logEnd = false;
                if (strPat.charAt(0) == '-') {
                    strPat = strPat.substring(1);
                    strChars[iC] = strChars[iC].substring(1);
                    logBeg = true;
                }
                if (strPat.charAt(strPat.length() - 1) == '-') {
                    strPat = strPat.substring(0, strPat.length() - 1);
                    strChars[iC] = strChars[iC].substring(0, strChars[iC].length() - 1);
                    logEnd = true;
                }
                strPat = "(" + Pattern.quote(strPat) + ")";
                if (logBeg) {
                    strPat = ".*" + strPat;
                } else {
                    strPat = "\\s" + strPat;
                }
                if (logEnd) {
                    strPat = strPat + ".*";
                } else {
                    strPat = strPat + "\\s";
                }
            } else if (strPat.substring(1).equals("NUMBER")) {
                strPat = "(#)";
                strChars[iC] = "#";
            } else if (strPat.substring(1).equals("CAPS")) {
                strPat = "(__%C__)";
                strChars[iC] = "__%C__";
            } else {
                strPat = "(" + Pattern.quote(strPat) + ")";
            }
            iCM = 0;
            Pattern pat = Pattern.compile(strPat, Pattern.CASE_INSENSITIVE);
            Matcher mat = pat.matcher(input);
            while (mat.find()) {
                iCM = iCM + 1;
            }
            mat.reset();
            intInds[iC] = new int[iCM];
            iCM = 0;
            for (; iCM < intInds[iC].length; iCM++) {
                mat.find();
                intInds[iC][iCM] = mat.start(1);
            }
            iC = iC + 1;
        }

        int intSInd = 0;
        String[] strOut = new String[input.length()];
        String[] strBraille = new String[input.length()];
        while (intSInd < input.length()) {
            boolean logFound = false;
            for (iC = 0; iC < intInds.length && !logFound; iC++) {
                for (iCM = 0; iCM < intInds[iC].length; iCM++) {
                    if (intInds[iC][iCM] == intSInd) {
                        strOut[intSInd] = strCodes[iC];
                        intSInd = intSInd + strChars[iC].length();
                        logFound = true;
                    }
                }
            }
        }
        String[] output;
        int cNotNull = 0;
        for (String str:strOut) {
            if (str != null) {
                cNotNull++;
            }
        }
        iC = 0;
        output = new String[cNotNull];
        for (String str:strOut) {
            if (str != null) {
                output[iC] = str;
                iC++;
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
        String line;
        int intLines = 0;
        try (BufferedReader inputStream = new BufferedReader(new FileReader(encoding))) {
            while ((line = inputStream.readLine()) != null) {
                intLines++;
            }
        } catch (IOException x) {
            System.out.println("Unable to open Braille Library");
            System.exit(1);
        }
        try (BufferedReader inputStream = new BufferedReader(new FileReader(encoding))) {
            line = null;
            strChars = new String[intLines];
            strCodes = new String[intLines];
            intChrLen = new Pair[intLines];
            intInds = new int[intLines][];
            int intCount = 0;
            while ((line = inputStream.readLine()) != null) {
                strChars[intCount] = line.substring(0, line.indexOf(':', 1));
                if (strChars[intCount].substring(0, 1).equals("%")) {
                    intChrLen[intCount] = new Pair(intCount, 0);
                } else {
                    intChrLen[intCount] = new Pair(intCount, strChars[intCount].length());
                }
                strCodes[intCount] = line.substring(line.lastIndexOf(':') + 1, line.lastIndexOf(';'));
                intCount++;
            }
        } catch (IOException x) {
            System.out.println("Unable to open Braille Library");
            System.exit(1);
        }
        Arrays.sort(intChrLen);
        String[] strCharsCopy = new String[strChars.length];
        String[] strCodesCopy = new String[strCodes.length];
        int index = 0;
        int intCount = 0;
        for (Pair ind:intChrLen) {
            index = ind.getInd();
            strCharsCopy[intCount] = strChars[index];
            strCodesCopy[intCount] = strCodes[index];
            intCount++;
        }
        strChars = strCharsCopy;
        strCodes = strCodesCopy;
        // Now we have sorted versions of the characters and codes, based on LENGTH of phrases!
    }
}