package tk.booky.obfuscator.transformer;

import org.objectweb.asm.tree.ClassNode;
import tk.booky.obfuscator.main.Obfuscator;

import java.util.Collections;

public class ShuffleTransformer extends AbstractTransformer {

    public ShuffleTransformer(Obfuscator obfuscator) {
        super(obfuscator);
    }

    @Override
    public void visit(ClassNode classNode) {
        Collections.shuffle(classNode.fields, random);
        Collections.shuffle(classNode.methods, random);
    }
}
