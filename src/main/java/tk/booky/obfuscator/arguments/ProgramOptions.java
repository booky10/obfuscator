package tk.booky.obfuscator.arguments;
// Created by booky10 in Obfuscator (15:35 31.10.20)

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProgramOptions {

    private final File input, output;
    private final List<String> excluded, excludedTransformers;
    private final Boolean guiMode, debug;

    public ProgramOptions(File input, File output, List<String> excluded, List<String> excludedTransformers, Boolean debug) {
        this.input = input;
        this.output = output;
        this.excluded = excluded;
        this.excludedTransformers = new ArrayList<>();
        guiMode = false;
        this.debug = debug;

        for (String transformer : excludedTransformers)
            excludedTransformers.add(transformer.toLowerCase());
    }

    public ProgramOptions() {
        input = null;
        output = null;
        excluded = Collections.emptyList();
        excludedTransformers = Collections.emptyList();
        guiMode = true;
        debug = false;
    }

    public Boolean isInGuiMode() {
        return guiMode;
    }

    public File getInput() {
        checkForGuiMode();
        return input;
    }

    public File getOutput() {
        checkForGuiMode();
        return output;
    }

    public List<String> getExcluded() {
        checkForGuiMode();
        return excluded;
    }

    public List<String> getExcludedTransformers() {
        return excludedTransformers;
    }

    private void checkForGuiMode() {
        if (isInGuiMode()) throw new IllegalStateException("No input file available, application in Gui mode");
    }

    public Boolean getDebug() {
        return debug;
    }
}