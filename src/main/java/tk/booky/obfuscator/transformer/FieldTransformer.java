package tk.booky.obfuscator.transformer;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import tk.booky.obfuscator.main.Obfuscator;
import tk.booky.obfuscator.utils.RandomUtils;

public class FieldTransformer extends AbstractTransformer {

    public FieldTransformer(Obfuscator obfuscator) {
        super(obfuscator);
    }

    @Override
    public void visit(ClassNode classNode) {
        if ((classNode.access & Opcodes.ACC_INTERFACE) != 0) return;

        for (int i = random.nextInt(3) + 1; i >= 0; i--) {
            ClassNode target = RandomUtils.choice(random, obfuscator.getClasses());
            if ((target.access & Opcodes.ACC_INTERFACE) != 0) continue;

            String name = "__junk_field" + Math.abs(random.nextLong());
            target.fields.add(new FieldNode(Opcodes.ACC_PUBLIC, name, "L" + classNode.name + ";", null, null));
        }
    }
}
