package tk.booky.obfuscator.transformer;
// Created by booky10 in Obfuscator (19:08 28.10.20)

import org.objectweb.asm.tree.ClassNode;
import tk.booky.obfuscator.main.Obfuscator;

import java.util.List;

public class RenamingTransformer extends AbstractTransformer {

    private List<String> renamingExcluded;

    public RenamingTransformer(Obfuscator obfuscator, List<String> renamingExcluded) {
        super(obfuscator);
        this.renamingExcluded = renamingExcluded;
    }

    @Override
    public void visit(ClassNode classNode) {

    }
}