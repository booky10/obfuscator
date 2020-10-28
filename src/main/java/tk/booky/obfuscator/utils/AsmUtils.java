package tk.booky.obfuscator.utils;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.CodeSizeEvaluator;
import org.objectweb.asm.tree.*;

public class AsmUtils {

    public static Boolean isPushInt(AbstractInsnNode insn) {
        int opcode = insn.getOpcode();

        return (opcode >= Opcodes.ICONST_M1 && opcode <= Opcodes.ICONST_5)
                || opcode == Opcodes.BIPUSH
                || opcode == Opcodes.SIPUSH
                || (opcode == Opcodes.LDC && ((LdcInsnNode) insn).cst instanceof Integer);
    }

    public static Integer getPushedInt(AbstractInsnNode insn) {
        int op = insn.getOpcode();

        if (op >= Opcodes.ICONST_M1 && op <= Opcodes.ICONST_5) return op - Opcodes.ICONST_0;
        else if (op == Opcodes.BIPUSH || op == Opcodes.SIPUSH) return ((IntInsnNode) insn).operand;
        else if (op == Opcodes.LDC) {
            Object cst = ((LdcInsnNode) insn).cst;
            if (cst instanceof Integer) return (int) cst;
        }

        throw new IllegalArgumentException("Insn is not a push int instruction!");
    }

    public static AbstractInsnNode pushInt(int value) {
        if (value >= -1 && value <= 5) return new InsnNode(Opcodes.ICONST_0 + value);
        else if (value >= Byte.MIN_VALUE && value <= Byte.MAX_VALUE) return new IntInsnNode(Opcodes.BIPUSH, value);
        else if (value >= Short.MIN_VALUE && value <= Short.MAX_VALUE) return new IntInsnNode(Opcodes.SIPUSH, value);
        else return new LdcInsnNode(value);
    }

    public static Boolean isPushLong(AbstractInsnNode insn) {
        int op = insn.getOpcode();

        return op == Opcodes.LCONST_0
                || op == Opcodes.LCONST_1
                || (op == Opcodes.LDC && ((LdcInsnNode) insn).cst instanceof Long);
    }

    public static Long getPushedLong(AbstractInsnNode insn) {
        int op = insn.getOpcode();

        if (op == Opcodes.LCONST_0) return 0L;
        else if (op == Opcodes.LCONST_1) return 1L;
        else if (op == Opcodes.LDC) {
            Object cst = ((LdcInsnNode) insn).cst;
            if (cst instanceof Long) return (long) cst;
        }

        throw new IllegalArgumentException("insn is not a push long instruction");
    }

    public static AbstractInsnNode pushLong(long value) {
        if (value == 0) return new InsnNode(Opcodes.LCONST_0);
        else if (value == 1) return new InsnNode(Opcodes.LCONST_1);
        else return new LdcInsnNode(value);
    }

    public static Integer codeSize(MethodNode methodNode) {
        CodeSizeEvaluator evaluator = new CodeSizeEvaluator(null);
        methodNode.accept(evaluator);

        return evaluator.getMaxSize();
    }
}
