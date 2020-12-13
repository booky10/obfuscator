package tk.booky.obfuscator.arguments;
// Created by booky10 in Obfuscator (15:35 31.10.20)

import joptsimple.OptionException;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArgumentParser {

    public static ProgramOptions parse(String[] arguments) {
        System.out.println("Reading arguments...");
        Thread.currentThread().setName("Argument Parser Thread");

        OptionParser options = new OptionParser();
        options.allowsUnrecognizedOptions();

        OptionSpec<File> optionInput = options.accepts("input").withRequiredArg().required().ofType(File.class);
        OptionSpec<File> optionOutput = options.accepts("output").withRequiredArg().required().ofType(File.class);
        OptionSpec<String> optionRenamingExcluded = options.accepts("renaming-excluded").withRequiredArg().defaultsTo("none");
        OptionSpec<String> optionExcludedTransformers = options.accepts("excluded-transformers").withRequiredArg().defaultsTo("none");

        options.accepts("gui");
        options.accepts("debug");

        try {
            OptionSpec<String> optionNoneOptions = options.nonOptions();
            OptionSet optionSet = options.parse(arguments);
            List<String> noneOptions = new ArrayList<>(optionSet.valuesOf(optionNoneOptions));

            if (!noneOptions.isEmpty()) System.out.println("Completely ignored arguments: " + noneOptions);

            File inputFile = optionSet.valueOf(optionInput);
            File outputFile = optionSet.valueOf(optionOutput);

            List<String> renamingExcluded = parseStringList(optionSet, optionRenamingExcluded);
            List<String> excludedTransformers = parseStringList(optionSet, optionExcludedTransformers);

            boolean gui = optionSet.has("gui");
            Boolean debug = optionSet.has("debug");

            if (gui)
                return new ProgramOptions();
            else
                return new ProgramOptions(inputFile, outputFile, renamingExcluded, excludedTransformers, debug);
        } catch (OptionException exception) {
            if (exception.getClass().getName().equals("joptsimple.MissingRequiredOptionsException"))
                return new ProgramOptions();
            throw new Error(exception);
        }
    }

    private static List<String> parseStringList(OptionSet set, OptionSpec<String> spec) {
        List<String> list = Arrays.asList(set.valueOf(spec).split(","));
        list = new ArrayList<>(list);
        list.remove("none");
        return list;
    }
}