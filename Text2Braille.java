import java.io.*;
public class Text2Braille {
    private static String[] strChars = new String[64];
    private static String[] strCodes = new String[64];
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Please provide more arguments!");
        } else {
            Text2Braille.initialize();
            String[] code = Text2Braille.text2Bin(args[0]);
            for (String str:code) {
                System.out.print(str + ";");
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
    public static void initialize() {
        try (BufferedReader inputStream = new BufferedReader(new FileReader("table.txt"))) {
            String line = null;
            int intCount = 0;
            while ((line = inputStream.readLine()) != null) {
                strChars[intCount] = String.valueOf(line.charAt(0));
                strCodes[intCount] = line.substring(2, 8);
                intCount++;
            }
        } catch (IOException x) {
            System.out.println("SHIT!");
        }
    }
}