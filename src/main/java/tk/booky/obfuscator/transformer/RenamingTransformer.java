package tk.booky.obfuscator.transformer;
// Created by booky10 in Obfuscator (19:08 28.10.20)

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.ClassRemapper;
import org.objectweb.asm.commons.Remapper;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;
import tk.booky.obfuscator.main.Obfuscator;
import tk.booky.obfuscator.utils.remapping.MemberRemapper;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

public class RenamingTransformer extends AbstractTransformer {

    private final List<Pattern> renamingExcludedPatterns = new ArrayList<>();
    private final List<ClassNode> classes = new ArrayList<>();

    private Boolean enabled = true;

    public RenamingTransformer(Obfuscator obfuscator, List<String> renamingExcluded) {
        super(obfuscator);
        if (renamingExcluded.contains("**")) enabled = false;
        else
            for (String excluded : renamingExcluded) renamingExcludedPatterns.add(compileExcludePattern(excluded));
    }

    @Override
    public void visit(ClassNode classNode) {
        if (!enabled || isExcluded(classNode)) return;
        classes.add(classNode);
    }

    @Override
    public void afterVisit() {
        if (!enabled) return;
        Remapper simpleRemapper = new MemberRemapper(generateMappings());

        for (ClassNode classNode : classes) {
            ClassNode copy = new ClassNode();
            classNode.accept(new ClassRemapper(copy, simpleRemapper));

            if (copy.methods != null)
                for (int i = 0; i < copy.methods.size(); i++) classNode.methods.set(i, copy.methods.get(i));
            if (copy.fields != null)
                for (int i = 0; i < copy.fields.size(); i++) classNode.fields.set(i, copy.fields.get(i));

            obfuscator.removeClass(classNode);
            obfuscator.addClass(copy);
        }
    }

    private HashMap<String, String> generateMappings() {
        HashMap<String, String> mappings = new HashMap<>();

        for (ClassNode classNode : classes) {
            for (MethodNode methodNode : classNode.methods) {
                methodNode.access &= ~Opcodes.ACC_PRIVATE;
                methodNode.access &= ~Opcodes.ACC_PROTECTED;
                methodNode.access |= Opcodes.ACC_PUBLIC;
            }

            for (FieldNode fieldNode : classNode.fields) {
                fieldNode.access &= ~Opcodes.ACC_PRIVATE;
                fieldNode.access &= ~Opcodes.ACC_PROTECTED;
                fieldNode.access |= Opcodes.ACC_PUBLIC;
            }

            for (MethodNode methodNode : classNode.methods) {
                if (methodNode.name.equals("main")) continue;
                if (methodNode.name.equals("premain")) continue;
                if (methodNode.name.startsWith("<")) continue;
                if (Modifier.isNative(methodNode.access)) continue;

                classNode.access &= ~Opcodes.ACC_PRIVATE;
                classNode.access &= ~Opcodes.ACC_PROTECTED;
                classNode.access |= Opcodes.ACC_PUBLIC;

                mappings.put(classNode.name, getPackageName() + getSimpleName(classNode));
            }
        }

        return mappings;
    }

    private String getSimpleName(ClassNode node) {
        String[] split = node.name.split("/");
        return split[split.length - 1];
    }

    private Pattern compileExcludePattern(String pattern) {
        StringBuilder builder = new StringBuilder();
        char[] chars = pattern.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];

            if (c == '*') if (chars.length - 1 != i && chars[i + 1] == '*') {
                builder.append(".*");
                i++;
            } else builder.append("[^/]*");
            else if (c == '.') builder.append('/');
            else builder.append(c);
        }

        return Pattern.compile(builder.toString());
    }

    private boolean isExcluded(ClassNode classNode) {
        for (Pattern pattern : renamingExcludedPatterns) if (pattern.matcher(classNode.name).matches()) return true;
        return false;
    }

    public String getPackageName() {
        return "Hey/";
    }
}