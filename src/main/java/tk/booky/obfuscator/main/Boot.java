package tk.booky.obfuscator.main;

import joptsimple.OptionException;
import joptsimple.OptionParser;
import joptsimple.OptionSet;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Boot {

    public static void main(String[] args) {
        OptionParser parser = new OptionParser();
        parser.accepts("input").withRequiredArg().required().ofType(File.class);
        parser.accepts("output").withRequiredArg().required().ofType(File.class);
        parser.accepts("renaming-excluded").withRequiredArg().required().ofType(String.class);

        OptionSet options;

        try {
            options = parser.parse(args);
            if (options == null) throw new NullPointerException();
        } catch (OptionException | NullPointerException exception) {
            System.out.println("Usage: obfuscator --input <input jar> --output <output jar> --renaming-excluded <excluded classes>");
            System.out.println(exception.getMessage());

            System.exit(-1);
            return;
        }

        File input = (File) options.valueOf("input");
        File output = (File) options.valueOf("output");
        List<String> renamingExcluded = Arrays.asList(((String) options.valueOf("renaming-excluded")).split(","));

        try {
            new Obfuscator(input, output, renamingExcluded);
        } catch (IOException exception) {
            exception.printStackTrace();
            System.exit(-1);
        }
    }
}
