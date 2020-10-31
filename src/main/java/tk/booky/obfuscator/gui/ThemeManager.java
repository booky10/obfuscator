package tk.booky.obfuscator.gui;
// Created by booky10 in Obfuscator (16:02 31.10.20)

import javax.swing.*;

public class ThemeManager {

    public static void setDefault() {
        tryGTK();
    }

    public static void tryGTK() {
        try {
            tryCustom("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
        } catch (IllegalStateException exception) {
            trySystem();
        }
    }

    public static void trySystem() {
        try {
            tryCustom(UIManager.getSystemLookAndFeelClassName());
        } catch (IllegalStateException throwable) {
            tryCrossPlatform();
        }
    }

    public static void tryCrossPlatform() {
        try {
            tryCustom(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (IllegalStateException throwable) {
            throwable.printStackTrace();
            System.exit(-1);
        }
    }

    public static void tryCustom(String theme) {
        try {
            UIManager.setLookAndFeel(theme);
        } catch (Throwable throwable) {
            throw new IllegalStateException(throwable);
        }
    }
}