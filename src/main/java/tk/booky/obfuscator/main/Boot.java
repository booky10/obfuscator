package tk.booky.obfuscator.main;

import tk.booky.obfuscator.arguments.ArgumentParser;
import tk.booky.obfuscator.arguments.ProgramOptions;
import tk.booky.obfuscator.gui.ObfuscatorGui;

public class Boot {

    public static void main(String[] args) {
        try {
            ProgramOptions options = ArgumentParser.parse(args);
            if (options.isInGuiMode()) new ObfuscatorGui();
            else new Obfuscator(options.getInput(), options.getOutput(), options.getExcluded());
        } catch (Throwable exception) {
            exception.printStackTrace();
            System.exit(-1);
        }
    }
}
