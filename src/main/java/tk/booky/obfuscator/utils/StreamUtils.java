package tk.booky.obfuscator.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StreamUtils {

    public static void copy(InputStream input, OutputStream output) throws IOException {
        int read;
        byte[] buffer = new byte[0x1000];

        while ((read = input.read(buffer)) != -1) output.write(buffer, 0, read);
    }
}
