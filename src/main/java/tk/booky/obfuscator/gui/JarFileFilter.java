package tk.booky.obfuscator.gui;
// Created by booky10 in Obfuscator (17:16 31.10.20)

import javax.swing.filechooser.FileFilter;
import java.io.File;

public class JarFileFilter extends FileFilter {

    @Override
    public boolean accept(File file) {
        String name = file.getName();
        return file.isDirectory() || name.endsWith(".jar");
    }

    @Override
    public String getDescription() {
        return "Java Files (*.jar)";
    }
}