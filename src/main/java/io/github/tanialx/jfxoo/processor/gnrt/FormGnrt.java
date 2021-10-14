package io.github.tanialx.jfxoo.processor.gnrt;

import com.squareup.javapoet.*;
import io.github.tanialx.jfxoo.JFXooForm;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.ElementFilter;
import java.util.List;

import static io.github.tanialx.jfxoo.processor.gnrt.Helper.labelFormat;
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.PUBLIC;

public class FormGnrt {

    private ProcessingEnvironment procEnv;
    private final ClassName LABEL = ClassName.get("javafx.scene.control", "Label");
    private final ClassName TEXTFIELD = ClassName.get("javafx.scene.control", "TextField");
    private final ClassName POS = ClassName.get("javafx.geometry", "Pos");
    private final ClassName GRIDPANE = ClassName.get("javafx.scene.layout", "GridPane");

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
                        .addSuperinterface(JFXooForm.class)
                        .addField(FieldSpec.builder(GRIDPANE, "grid", PRIVATE).build())
                        .addMethod(get())
                        .addMethod(constructor())
                        .addMethod(layout(te))
                        .build())
                .indent("    ")
                .build();
    }

    private MethodSpec get() {
        MethodSpec.Builder mb = MethodSpec.methodBuilder("node");
        mb.addAnnotation(Override.class);
        mb.returns(Object.class);
        mb.addModifiers(PUBLIC);
        mb.addStatement("return grid");
        return mb.build();
    }

    private MethodSpec layout(TypeElement te) {
        MethodSpec.Builder mb = MethodSpec.methodBuilder("_layout");
        mb.addModifiers(PRIVATE);
        int row = 0;
        int col = 0;
        List<VariableElement> fs = ElementFilter.fieldsIn(te.getEnclosedElements());

        for (VariableElement f : fs) {
            String fieldName = f.getSimpleName().toString();
            String labelName = "label_" + fieldName;
            String txtfName = "txtF_" + fieldName;
            mb.addStatement("$T $L = new $T($S)", LABEL, labelName, LABEL, labelFormat(fieldName));
            mb.addStatement("$T $L = new $T()", TEXTFIELD, txtfName, TEXTFIELD);
            mb.addStatement("grid.add($L, $L, $L)", labelName, col, row);
            col++;
            mb.addStatement("grid.add($L, $L, $L)", txtfName, col, row);
            row++;
            col = 0;
        }
        return mb.build();
    }

    private MethodSpec constructor() {
        MethodSpec.Builder mb = MethodSpec.constructorBuilder();
        mb.addModifiers(PUBLIC);
        mb.addStatement("grid = new $T()", GRIDPANE);
        mb.addStatement("grid.setAlignment($T.CENTER)", POS);
        mb.addStatement("grid.setHgap($L)", 10);
        mb.addStatement("grid.setVgap($L)", 10);
        mb.addStatement("_layout()");
        return mb.build();
    }
}
