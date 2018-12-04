import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class Main {

    public static void main(String[] args) {
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

        boolean matches = true;
        try {
            int flag = Integer.parseInt(args[1]);

            if (flag < 0 || flag > 1) {
                throw new NumberFormatException();
            }

            if (flag == 1) {
                matches = false;
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

            if (matches) {
                if (m.matches()) {
                    try {
                        fos.write(line);
                        fos.flush();
                    } catch (IOException e) {
                        System.err.println("Failed to write line to file");
                        System.err.println(sLine);
                    }
                }
            } else {
                if (!m.matches()) {
                    try {
                        System.out.println(sLine);
                        fos.write(line);
                        fos.flush();
                    } catch (IOException e) {
                        System.err.println("Failed to write line to file");
                        System.err.println(sLine);
                    }
                }
            }

            if (count % 10000 == 0) {
                System.out.println("Processed: " + count + " lines");
            }
        }

    }
}
