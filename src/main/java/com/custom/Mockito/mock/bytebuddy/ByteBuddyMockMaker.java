package com.custom.Mockito.mock.bytebuddy;

import com.custom.Mockito.TargetObject;
import com.custom.Mockito.mock.MockMaker;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.asm.AsmVisitorWrapper;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;
import net.bytebuddy.jar.asm.MethodVisitor;
import net.bytebuddy.matcher.ElementMatchers;

public class ByteBuddyMockMaker implements MockMaker {

    public Class mock(Class mockClass) {
        new ByteBuddy()
                .redefine(mockClass)
                .visit(new AsmVisitorWrapper.ForDeclaredMethods()
                                .method(
                                        ElementMatchers.any(),
                                        Advice.to(ByteBuddyMockHandler.class)
                                )
//                .constructor(
//                    ElementMatchers.any(),
//                    new MethodVisitorWrapper() {
//                        @Override
//                        public MethodVisitor wrap(
//                            TypeDescription instrumentedType,
//                            MethodDescription instrumentedMethod,
//                            MethodVisitor methodVisitor,
//                            Context implementationContext,
//                            TypePool typePool,
//                            int writerFlags,
//                            int readerFlags
//                        ) {
//                            return new ConstructorVisitor(OpenedClassReader.ASM_API, methodVisitor);
//                        }
//                    }
//                )
                )
                .make()
                .load(TargetObject.class.getClassLoader(), ClassReloadingStrategy.fromInstalledAgent());
        return mockClass;
    }

    @Override
    public boolean canMock(Class mockClass) {
        return true;
    }

    class ConstructorVisitor extends MethodVisitor {

        protected ConstructorVisitor(int api, MethodVisitor methodVisitor) {
            super(api, methodVisitor);
        }

        @Override
        public void visitCode() {
            super.visitCode();
        }

    }
}
