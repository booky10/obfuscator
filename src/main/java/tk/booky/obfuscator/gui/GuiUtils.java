package tk.booky.obfuscator.gui;
// Created by booky10 in Obfuscator (17:14 31.10.20)

import tk.booky.obfuscator.main.Boot;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.io.File;
import java.net.URISyntaxException;

public class GuiUtils {

    public static String chooseFile(File currFolder, Component parent, FileFilter filter) {
        return chooseFile(currFolder, parent, filter, false);
    }

    public static String chooseFile(File currFolder, Component parent, FileFilter filter, Boolean save) {
        if (currFolder == null) currFolder = getJarFolder();

        JFileChooser chooser = new JFileChooser(currFolder);
        chooser.setFileFilter(filter);
        chooser.setAcceptAllFileFilterUsed(false);

        int result;
        if (save) result = chooser.showSaveDialog(parent);
        else result = chooser.showOpenDialog(parent);

        if (result == JFileChooser.APPROVE_OPTION) return chooser.getSelectedFile().getAbsolutePath();
        else return null;
    }

    public static File getJarFolder() {
        try {
            return new File(Boot.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
        } catch (URISyntaxException exception) {
            throw new Error(exception);
        }
    }
}