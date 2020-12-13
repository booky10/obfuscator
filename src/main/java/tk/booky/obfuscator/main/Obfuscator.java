package tk.booky.obfuscator.main;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import tk.booky.obfuscator.transformer.*;
import tk.booky.obfuscator.utils.StreamUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipException;

public class Obfuscator {

    private final Random random;
    private final List<ClassNode> classes = new ArrayList<>();

    public Obfuscator(File inputFile, File outputFile, List<String> renamingExcluded, List<String> excludedTransformers, Boolean debug) throws IOException {
        if (!inputFile.exists()) throw new IOException("Input jar does not exits!");

        Thread.currentThread().setName("Obfuscator Thread");
        random = new Random();

        List<AbstractTransformer> transformers = new ArrayList<>();
        if (!excludedTransformers.contains("string"))
            transformers.add(new StringTransformer(this));
        if (!excludedTransformers.contains("renaming"))
            transformers.add(new RenamingTransformer(this, renamingExcluded));
        if (!excludedTransformers.contains("constant"))
            transformers.add(new ConstantTransformer(this));
        if (!excludedTransformers.contains("field"))
            transformers.add(new FieldTransformer(this));
        if (!excludedTransformers.contains("access"))
            transformers.add(new AccessTransformer(this));
        if (!excludedTransformers.contains("shuffle"))
            transformers.add(new ShuffleTransformer(this));
        if (!excludedTransformers.contains("crasher"))
            transformers.add(new CrasherTransformer(this));

        JarFile inputJar = new JarFile(inputFile);

        try (JarOutputStream output = new JarOutputStream(new FileOutputStream(outputFile))) {
            System.out.println("Reading jar entries...");

            for (Enumeration<JarEntry> iterator = inputJar.entries(); iterator.hasMoreElements(); ) {
                JarEntry entry = iterator.nextElement();
                if (debug) System.out.println("Reading entry \"" + entry.getName() + "\"...");

                try (InputStream input = inputJar.getInputStream(entry)) {
                    if (entry.getName().endsWith(".class")) {
                        if (debug) System.out.println("Jar entry \"" + entry.getName() + "\" is a class, processing...");
                        ClassReader reader = new ClassReader(input);
                        ClassNode classNode = new ClassNode();

                        reader.accept(classNode, 0);
                        classes.add(classNode);
                        if (debug) System.out.println("Done reading jar class entry \"" + entry.getName() + "\"!");
                    } else try {
                        if (debug) System.out.println("Jar entry \"" + entry.getName() + "\" is a resource, processing...");
                        output.putNextEntry(new JarEntry(entry.getName()));
                        if (debug) System.out.println("Writing jar resource entry \"" + entry.getName() + "\"...");
                        StreamUtils.copy(input, output);
                        if (debug) System.out.println("Done writing jar resource entry \"" + entry.getName() + "\"!");
                    } catch (ZipException exception) {
                        if (debug) System.out.println("Error while reading jar resource entry \"" + entry.getName() + "\": " + exception);
                    }
                }
            }

            if (debug) System.out.println("Shuffling class entries...");
            Collections.shuffle(classes, random);
            if (debug) System.out.println("Shuffled class entries!");

            System.out.println("Transforming classes...");
            for (AbstractTransformer transformer : transformers) {
                System.out.println("Running \"" + transformer.getName() + "\"...");
                classes.forEach(transformer::visit);
                if (debug) System.out.println("Running \"" + transformer.getName() + "\" after visit...");
                transformer.afterVisit();
            }
            for (AbstractTransformer transformer : transformers) {
                if (debug) System.out.println("Running \"" + transformer.getName() + "\" after after visit...");
                transformer.after();
            }

            System.out.println("Writing classes...");
            for (ClassNode classNode : classes) {
                if (debug) System.out.println("Writing \"" + classNode.name + ".class\"...");
                ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
                classNode.accept(writer);

                output.putNextEntry(new JarEntry(classNode.name + ".class"));
                output.write(writer.toByteArray());
                if (debug) System.out.println("Done writing \"" + classNode.name + ".class\"!");
            }
            if (debug) System.out.println("Done Obfuscating!");
        }
    }

    public Random getRandom() {
        return random;
    }

    public List<ClassNode> getClasses() {
        return classes;
    }

    public void removeClass(ClassNode classNode) {
        classes.removeIf(node -> node.name.equals(classNode.name));
    }

    public void addClass(ClassNode classNode) {
        classes.add(classNode);
    }
}
