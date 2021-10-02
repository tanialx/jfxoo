package io.github.tanialx.jfxoo.processor.gnrt;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import javafx.scene.layout.GridPane;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

public class FormGnrt {

    public JavaFile run(TypeElement te) {
        final String pkg = "";
        final String _class = "JFXooForm" + te.getSimpleName();
        return JavaFile.builder(
                pkg,
                TypeSpec.classBuilder(_class)
                        .addModifiers(Modifier.PUBLIC)
                        .superclass(GridPane.class)
                        .addMethod(constructor(te))
                        .build())
                .indent("    ")
                .build();
    }

    private MethodSpec constructor(TypeElement te) {
        return MethodSpec.constructorBuilder()
                // TODO: Impl
                .build();
    }
}
