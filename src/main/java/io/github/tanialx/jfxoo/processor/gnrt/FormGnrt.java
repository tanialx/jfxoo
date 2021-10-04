package io.github.tanialx.jfxoo.processor.gnrt;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.ElementFilter;
import java.util.List;

import static io.github.tanialx.jfxoo.processor.gnrt.Helper.labelFormat;
import static javax.lang.model.element.Modifier.PUBLIC;

public class FormGnrt {

    private ProcessingEnvironment procEnv;

    public FormGnrt(ProcessingEnvironment procEnv) {
        this.procEnv = procEnv;
    }

    public JavaFile run(TypeElement te) {
        final String pkg = procEnv.getElementUtils().getPackageOf(te).toString();
        final String _class = "JFXooForm" + te.getSimpleName();
        return JavaFile.builder(
                pkg,
                TypeSpec.classBuilder(_class)
                        .addModifiers(PUBLIC)
                        .superclass(GridPane.class)
                        .addMethod(constructor(te))
                        .build())
                .indent("    ")
                .build();
    }

    private MethodSpec constructor(TypeElement te) {
        MethodSpec.Builder mb = MethodSpec.constructorBuilder();
        mb.addModifiers(PUBLIC);
        mb.addStatement("super()");

        int row = 0;
        int col = 0;
        List<VariableElement> fs = ElementFilter.fieldsIn(te.getEnclosedElements());
        for (VariableElement f : fs) {
            String fieldName = f.getSimpleName().toString();
            String labelName = "label_" + fieldName;
            String txtfName = "txtF_" + fieldName;
            mb.addStatement("$T $L = new $T($S)", Label.class, labelName, Label.class, labelFormat(fieldName));
            mb.addStatement("$T $L = new $T()", TextField.class, txtfName, TextField.class);
            mb.addStatement("this.add($L, $L, $L)", labelName, col, row);
            col++;
            mb.addStatement("this.add($L, $L, $L)", txtfName, col, row);
            row++;
            col = 0;
        }
        mb.addStatement("this.setAlignment($T.CENTER)", Pos.class);
        mb.addStatement("this.setHgap($L)", 10);
        mb.addStatement("this.setVgap($L)", 10);
        return mb.build();
    }
}
