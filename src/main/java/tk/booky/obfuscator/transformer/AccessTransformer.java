package tk.booky.obfuscator.transformer;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;
import tk.booky.obfuscator.main.Obfuscator;

public class AccessTransformer extends AbstractTransformer {

    public AccessTransformer(Obfuscator obfuscator) {
        super(obfuscator);
    }

    @Override
    public void visit(ClassNode classNode) {
        classNode.access &= ~(Opcodes.ACC_PRIVATE | Opcodes.ACC_PROTECTED | Opcodes.ACC_FINAL);
        classNode.access |= Opcodes.ACC_PUBLIC;

        if ((classNode.access & Opcodes.ACC_INTERFACE) != 0) return;

        for (FieldNode field : classNode.fields) {
            field.access &= ~(Opcodes.ACC_PRIVATE | Opcodes.ACC_PROTECTED | Opcodes.ACC_FINAL);
            field.access |= Opcodes.ACC_PUBLIC;
        }
        for (MethodNode method : classNode.methods) {
            method.access &= ~(Opcodes.ACC_PRIVATE | Opcodes.ACC_PROTECTED | Opcodes.ACC_FINAL);
            method.access |= Opcodes.ACC_PUBLIC;
        }
    }
}
