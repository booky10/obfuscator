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

public class Obfuscator {

    private final Random random;
    private final List<ClassNode> classes = new ArrayList<>();

    public Obfuscator(File inputFile, File outputFile, List<String> renamingExcluded) throws IOException {
        random = new Random();

        List<AbstractTransformer> transformers = new ArrayList<>();
        transformers.add(new StringTransformer(this));
        transformers.add(new RenamingTransformer(this, renamingExcluded));
        transformers.add(new ConstantTransformer(this));
        transformers.add(new JunkFieldTransformer(this));
        transformers.add(new AccessTransformer(this));
        transformers.add(new ShuffleTransformer(this));
        transformers.add(new CrasherTransformer(this));

        JarFile inputJar = new JarFile(inputFile);

        try (JarOutputStream output = new JarOutputStream(new FileOutputStream(outputFile))) {
            System.out.println("Reading jar...");

            for (Enumeration<JarEntry> iterator = inputJar.entries(); iterator.hasMoreElements(); ) {
                JarEntry entry = iterator.nextElement();

                try (InputStream input = inputJar.getInputStream(entry)) {
                    if (entry.getName().endsWith(".class")) {
                        ClassReader reader = new ClassReader(input);
                        ClassNode classNode = new ClassNode();

                        reader.accept(classNode, 0);
                        classes.add(classNode);
                    } else {
                        output.putNextEntry(new JarEntry(entry.getName()));
                        StreamUtils.copy(input, output);
                    }
                }
            }

            Collections.shuffle(classes, random);

            System.out.println("Transforming classes...");
            for (AbstractTransformer transformer : transformers) {
                System.out.println("Running " + transformer.getClass().getSimpleName() + "...");
                classes.forEach(transformer::visit);
                transformer.afterVisit();
            }
            for (AbstractTransformer transformer : transformers) transformer.after();

            System.out.println("Writing classes...");
            for (ClassNode classNode : classes) {
                ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
                classNode.accept(writer);

                output.putNextEntry(new JarEntry(classNode.name + ".class"));
                output.write(writer.toByteArray());
            }
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
