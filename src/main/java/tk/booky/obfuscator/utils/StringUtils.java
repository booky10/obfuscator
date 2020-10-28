package tk.booky.obfuscator.utils;

import tk.booky.obfuscator.main.Obfuscator;

public class StringUtils {

    private final static char[] DICT_SPACES = new char[]{
            '\u2000', '\u2001', '\u2002', '\u2003', '\u2004', '\u2005', '\u2006', '\u2007', '\u2008', '\u2009', '\u200A', '\u200B', '\u200C', '\u200D', '\u200E', '\u200F'
    };

    public static String crazyString(Obfuscator obfuscator, int length) {
        char[] buildString = new char[length];
        for (int i = 0; i < length; i++)
            buildString[i] = DICT_SPACES[obfuscator.getRandom().nextInt(DICT_SPACES.length)];
        return new String(buildString);
    }
}