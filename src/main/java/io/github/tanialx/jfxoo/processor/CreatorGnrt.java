package io.github.tanialx.jfxoo.processor;

import com.squareup.javapoet.*;
import io.github.tanialx.jfxoo.JFXooCreator;
import io.github.tanialx.jfxoo.JFXooForm;
import io.github.tanialx.jfxoo.JFXooTable;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import java.util.ArrayList;
import java.util.List;

import static javax.lang.model.element.Modifier.PUBLIC;

public class CreatorGnrt {

    private final List<TypeElement> forms;
    private final List<TypeElement> table;
    private final ProcessingEnvironment procEnv;

    public CreatorGnrt(ProcessingEnvironment procEnv) {
        forms = new ArrayList<>();
        table = new ArrayList<>();
        this.procEnv = procEnv;
    }

    public boolean pending() {
        return !forms.isEmpty() || !table.isEmpty();
    }

    public void form(TypeElement te) {
        forms.add(te);
    }

    public void table(TypeElement te) {
        table.add(te);
    }

    public JavaFile run() {
        final String pkg = "io.github.tanialx.jfxoo";
        final String _class = "JFXooCreatorImpl";
        return JavaFile.builder(
                        pkg,
                        TypeSpec.classBuilder(_class)
                                .addModifiers(PUBLIC)
                                .addSuperinterface(JFXooCreator.class)
                                .addMethod(form())
                                .addMethod(table())
                                .build())
                .indent("    ")
                .build();
    }

    private MethodSpec form() {
        Elements elements = procEnv.getElementUtils();
        final TypeVariableName TYPE_VAR = TypeVariableName.get("T");
        final TypeName FORM_TYPE = ParameterizedTypeName.get(ClassName.get(JFXooForm.class), TYPE_VAR);

        MethodSpec.Builder mb = MethodSpec.methodBuilder("form");
        mb.addTypeVariable(TYPE_VAR);
        mb.returns(FORM_TYPE);
        mb.addModifiers(PUBLIC);
        mb.addAnnotation(Override.class);
        mb.addParameter(ParameterSpec.builder(String.class, "name").build());
        mb.addParameter(ParameterSpec.builder(ParameterizedTypeName.get(ClassName.get(Class.class), TYPE_VAR), "T").build());
        mb.addStatement("$T form = null", FORM_TYPE);
        CodeBlock.Builder cb = CodeBlock.builder();
        cb.beginControlFlow("switch(name)");
        forms.forEach(te -> {
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

    private MethodSpec table() {
        Elements elements = procEnv.getElementUtils();
        final TypeVariableName TYPE_VAR = TypeVariableName.get("T");
        final TypeName TYPE = ParameterizedTypeName.get(ClassName.get(JFXooTable.class), TYPE_VAR);

        MethodSpec.Builder mb = MethodSpec.methodBuilder("table");
        mb.addTypeVariable(TYPE_VAR);
        mb.returns(TYPE);
        mb.addModifiers(PUBLIC);
        mb.addAnnotation(Override.class);
        mb.addParameter(ParameterSpec.builder(String.class, "name").build());
        mb.addParameter(ParameterSpec.builder(ParameterizedTypeName.get(ClassName.get(Class.class), TYPE_VAR), "T").build());
        mb.addStatement("$T table = null", TYPE);
        mb.beginControlFlow("switch(name)");
        table.forEach(te -> {
            ClassName generatedClass = ClassName.get(elements.getPackageOf(te).toString(),
                    "JFXooTable" + te.getSimpleName().toString());
            mb.addStatement("case $S -> table = ($T) new $T()", te.getSimpleName().toString(),
                    TYPE, generatedClass);
        });
        mb.addStatement("default -> table = null");
        mb.endControlFlow();
        mb.addStatement("return table");
        return mb.build();
    }
}
