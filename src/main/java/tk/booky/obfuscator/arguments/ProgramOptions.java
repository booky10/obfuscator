package tk.booky.obfuscator.arguments;
// Created by booky10 in Obfuscator (15:35 31.10.20)

import java.io.File;
import java.util.Collections;
import java.util.List;

public class ProgramOptions {

    private final File input, output;
    private final List<String> excluded;
    private final Boolean guiMode;

    public ProgramOptions(File input, File output, List<String> excluded) {
        this.input = input;
        this.output = output;
        this.excluded = excluded;
        guiMode = false;
    }

    public ProgramOptions() {
        input = null;
        output = null;
        excluded = Collections.emptyList();
        guiMode = true;
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

    private void checkForGuiMode() {
        if (isInGuiMode()) throw new IllegalStateException("No input file available, application in Gui mode");
    }
}