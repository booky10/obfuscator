package tk.booky.obfuscator.main;

import joptsimple.OptionException;
import joptsimple.OptionParser;
import joptsimple.OptionSet;

import java.io.File;
import java.io.IOException;

public class Boot {

    public static void main(String[] args) {
        OptionParser parser = new OptionParser();
        parser.accepts("input").withRequiredArg().required().ofType(File.class);
        parser.accepts("output").withRequiredArg().required().ofType(File.class);

        OptionSet options;

        try {
            options = parser.parse(args);
        } catch (OptionException exception) {
            System.out.println("Usage: obfuscator --input <inputjar> --output <outputjar>");
            System.out.println(exception.getMessage());

            System.exit(-1);
            return;
        }

        File inputFile = (File) options.valueOf("input");
        File outputFile = (File) options.valueOf("output");

        try {
            new Obfuscator(inputFile, outputFile);
        } catch (IOException exception) {
            exception.printStackTrace();

            System.exit(-1);
        }
    }
}
