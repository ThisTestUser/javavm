package com.javadeobfuscator.javavm.instructions;

import com.javadeobfuscator.javavm.Locals;
import com.javadeobfuscator.javavm.MethodExecution;
import com.javadeobfuscator.javavm.Stack;
import com.javadeobfuscator.javavm.exceptions.ExecutionException;
import com.javadeobfuscator.javavm.utils.BiFloatInstruction;
import com.javadeobfuscator.javavm.utils.ExecutionUtils;
import com.javadeobfuscator.javavm.values.JavaUnknown;
import com.javadeobfuscator.javavm.values.JavaValue;
import com.javadeobfuscator.javavm.values.JavaValueType;
import com.javadeobfuscator.javavm.values.JavaWrapper;
import org.objectweb.asm.tree.AbstractInsnNode;

import java.util.List;

public class FloatMathInstruction extends Instruction {
    private final BiFloatInstruction _function;
    private final boolean _returnInt;

    public FloatMathInstruction(BiFloatInstruction function, boolean returnInt) {
        this._function = function;
        this._returnInt = returnInt;
    }

    @Override
    public void execute(MethodExecution execution, AbstractInsnNode currentInsn, Stack stack, Locals locals, List<AbstractInsnNode> branchTo) {
        JavaValue b = stack.pop().get();
        JavaValue a = stack.pop().get();

        if (ExecutionUtils.areValuesUnknown(a, b)) {
            stack.push(JavaWrapper.wrap(new JavaUnknown(execution.getVM(), execution.getVM().FLOAT, JavaUnknown.UnknownCause.FLOAT_MATH, b, a)));
            return;
        }

        if (!a.is(JavaValueType.FLOAT) || !b.is(JavaValueType.FLOAT)) {
            throw new ExecutionException("Expected to find float on stack");
        }

        float result = _function.apply(a.asFloat(), b.asFloat());
        if (!_returnInt) {
            stack.push(JavaWrapper.createFloat(execution.getVM(), result));
        } else {
            stack.push(JavaWrapper.createInteger(execution.getVM(), (int) result));
        }
    }
}
