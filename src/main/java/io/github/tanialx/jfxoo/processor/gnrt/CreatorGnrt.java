package io.github.tanialx.jfxoo.processor.gnrt;

import com.squareup.javapoet.*;
import io.github.tanialx.jfxoo.JFXooCreator;
import io.github.tanialx.jfxoo.JFXooForm;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import java.util.ArrayList;
import java.util.List;

import static javax.lang.model.element.Modifier.PUBLIC;

public class CreatorGnrt {

    private final List<TypeElement> tes;
    private final ProcessingEnvironment procEnv;

    public CreatorGnrt(ProcessingEnvironment procEnv) {
        tes = new ArrayList<>();
        this.procEnv = procEnv;
    }

    public void add(TypeElement te) {
        tes.add(te);
    }

    public JavaFile run() {
        final String pkg = "io.github.tanialx.jfxoo";
        final String _class = "JFXooCreatorImpl";
        return JavaFile.builder(
                pkg,
                TypeSpec.classBuilder(_class)
                        .addModifiers(PUBLIC)
                        .addSuperinterface(JFXooCreator.class)
                        .addMethod(_create())
                        .build())
                .indent("    ")
                .build();
    }

    private MethodSpec _create() {
        Elements elements = procEnv.getElementUtils();
        final TypeVariableName TYPE_VAR = TypeVariableName.get("T");
        final TypeName FORM_TYPE = ParameterizedTypeName.get(ClassName.get(JFXooForm.class), TYPE_VAR);
        MethodSpec.Builder mb = MethodSpec.methodBuilder("create");
        mb.addTypeVariable(TYPE_VAR);
        mb.returns(FORM_TYPE);
        mb.addModifiers(PUBLIC);
        mb.addAnnotation(Override.class);
        mb.addParameter(ParameterSpec.builder(String.class, "name").build());
        mb.addParameter(ParameterSpec.builder(ParameterizedTypeName.get(ClassName.get(Class.class), TYPE_VAR), "T").build());
        mb.addStatement("$T form = null", FORM_TYPE);
        CodeBlock.Builder cb = CodeBlock.builder();
        cb.beginControlFlow("switch(name)");
        tes.forEach(te -> {
            ClassName generatedClass = ClassName.get(elements.getPackageOf(te).toString(),
                    "JFXooForm" + te.getSimpleName().toString());
            cb.addStatement("case $S -> form = ($T) new $T()", te.getSimpleName().toString(),
                    FORM_TYPE, generatedClass);
        });
        cb.addStatement("default -> form = null");
        cb.endControlFlow();
        mb.addCode(cb.build());
        mb.addStatement("return form");
        return mb.build();
    }
}
