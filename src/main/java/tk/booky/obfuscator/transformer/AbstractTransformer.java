package tk.booky.obfuscator.transformer;

import org.objectweb.asm.tree.ClassNode;
import tk.booky.obfuscator.main.Obfuscator;

import java.util.Random;

public abstract class AbstractTransformer {

    protected final Obfuscator obfuscator;
    protected final Random random;

    public AbstractTransformer(Obfuscator obfuscator) {
        this.obfuscator = obfuscator;
        this.random = obfuscator.getRandom();
    }

    public abstract void visit(ClassNode classNode);

    public void afterVisit() {
    }

    public void after() {
    }
}
