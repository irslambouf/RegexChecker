import java.io.*;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import com.google.re2j.Pattern;
import com.google.re2j.Matcher;

public class Main {

    public static void main(String[] args) {
        long start = System.nanoTime();

        if (args.length < 4) {
            System.out.println("Please provide two arguments");
            System.out.println("USAGE: regexchecker.exe [regex] [flag] [in:filename] [out:filename]");
            return;
        }
        Pattern p;
        try {
            p = Pattern.compile(args[0]);
        } catch (PatternSyntaxException e) {
            System.err.println("Pattern failed to compile, exiting...");
            return;
        }

        int flag;
        try {
            flag = Integer.parseInt(args[1]);

            if (flag < 0 || flag > 1) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            System.err.println("Failed to provide valid flag, values are: 0 (matches) or 1 (does not match)... exiting");
            return;
        }

        FileOutputStream fos;
        try {
            fos = new FileOutputStream(args[3]);
        } catch (FileNotFoundException e) {
            System.err.println("failed to write or create output file... exiting");
            e.printStackTrace();
            return;
        }

        FileAndFolderReaderBinary reader = new FileAndFolderReaderBinary(args[2]);

        byte[] line;
        int count = 0;
        while ((line = reader.readLine()) != null) {
            String sLine = new String(line);
            count++;
            Matcher m = p.matcher(sLine);

            boolean condition = (flag == 0) == m.matches();

            if (condition) {
                try {
                    fos.write(line);
                    fos.flush();
                } catch (IOException e) {
                    System.err.println("Failed to write line to file");
                    System.err.println(sLine);
                }
            }

            if (count % 100000 == 0) {
                System.out.println("Processed: " + count + " lines");
            }
        }

        System.out.println(System.nanoTime() - start);
    }
}

