package tk.booky.obfuscator.transformer;
// Created by booky10 in Obfuscator (18:36 28.10.20)

import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import tk.booky.obfuscator.main.Obfuscator;
import tk.booky.obfuscator.utils.StringUtils;

import java.lang.reflect.Modifier;
import java.util.ArrayList;

public class CrasherTransformer extends AbstractTransformer {

    private static final String EMPTY_STRINGS;

    static {
        StringBuilder builder = new StringBuilder();
        for (int j = 0; j < 50000; j++) builder.append("\n");
        EMPTY_STRINGS = builder.toString();
    }

    public CrasherTransformer(Obfuscator obfuscator) {
        super(obfuscator);
    }

    @Override
    public void visit(ClassNode classNode) {
        if (Modifier.isInterface(classNode.access)) return;

        if (classNode.signature == null) classNode.signature = StringUtils.crazyString(obfuscator, 10);
        for (MethodNode method : classNode.methods) {
            if (method.invisibleAnnotations == null) method.invisibleAnnotations = new ArrayList<>();
            for (int i = 0; i < 50; i++) method.invisibleAnnotations.add(new AnnotationNode(EMPTY_STRINGS));
        }
    }
}