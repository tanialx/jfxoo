package io.github.tanialx.jfxoo.processor.gnrt;

import com.squareup.javapoet.*;
import io.github.tanialx.jfxoo.JFXooForm;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static io.github.tanialx.jfxoo.processor.gnrt.Helper.labelFormat;
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.PUBLIC;

public class FormGnrt {

    private final Types types;
    private final Elements elements;
    private final ClassName LABEL = ClassName.get("javafx.scene.control", "Label");
    private final ClassName TEXTFIELD = ClassName.get("javafx.scene.control", "TextField");
    private final ClassName POS = ClassName.get("javafx.geometry", "Pos");
    private final ClassName GRIDPANE = ClassName.get("javafx.scene.layout", "GridPane");
    private final ClassName NODE = ClassName.get("javafx.scene", "Node");
    private final ClassName DATEPICKER = ClassName.get("javafx.scene.control", "DatePicker");

    private List<Field> fs;

    @Builder
    @Getter
    @Setter
    public static class Field {
        private String name;
        private TypeMirror type;
        private String inputControlName;
        private String getter;
        private String setter;
    }

    public FormGnrt(ProcessingEnvironment procEnv) {
        this.types = procEnv.getTypeUtils();
        this.elements = procEnv.getElementUtils();
    }

    private List<Field> fields(TypeElement te) {
        return ElementFilter.fieldsIn(te.getEnclosedElements()).stream().map(ve -> {
            String fieldName = ve.getSimpleName().toString();
            String nameInMethod = Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
            return Field.builder()
                    .name(fieldName)
                    .setter(String.format("set%s", nameInMethod))
                    .getter(String.format("get%s", nameInMethod))
                    .inputControlName("in_" + fieldName)
                    .type(ve.asType())
                    .build();
        }).collect(Collectors.toList());
    }

    public JavaFile run(TypeElement te) {
        final String pkg = elements.getPackageOf(te).toString();
        final String _class = "JFXooForm" + te.getSimpleName();

        // collect all props that should be displayed as fields on generated form
        fs = fields(te);

        return JavaFile.builder(
                pkg,
                TypeSpec.classBuilder(_class)
                        .addModifiers(PUBLIC)
                        .addSuperinterface(ParameterizedTypeName.get(
                                ClassName.get(JFXooForm.class),
                                TypeName.get(te.asType())))
                        .addFields(props())
                        .addMethod(JFXooForm_get())
                        .addMethod(constructor())
                        .addMethod(layout())
                        .addMethod(JFXooForm_init(te))
                        .addMethod(JFXooForm_value(te))
                        .build())
                .indent("    ")
                .build();
    }

    private MethodSpec JFXooForm_value(TypeElement te) {
        TypeName type = TypeName.get(te.asType());
        MethodSpec.Builder mb = MethodSpec.methodBuilder("value");
        mb.addModifiers(PUBLIC);
        mb.addAnnotation(Override.class);
        mb.returns(type);
        final String OBJ_VAR = "t";
        mb.addStatement("$T $L = new $T()", type, OBJ_VAR, type);
        for (Field f : fs) {
            String inputName = f.getInputControlName();
            String setter = f.getSetter();
            // TODO: handle different data types
            if (sameType(f, String.class)) {
                mb.addStatement("$L.$L($L.getText())", OBJ_VAR, setter, inputName);
            } else if (sameType(f, LocalDate.class)) {
                mb.addStatement("$L.$L($L.getValue())", OBJ_VAR, setter, inputName);
            } else if (sameType(f, BigDecimal.class)) {
                mb.addStatement("$L.$L(new $T($L.getText()))", OBJ_VAR, setter, BigDecimal.class, inputName);
            } else if (sameType(f, Integer.class)) {
                mb.addStatement("$L.$L($T.parseInt($L.getText()))", OBJ_VAR, setter, Integer.class, inputName);
            } else {
                mb.addStatement("$L.$L(\"\")", OBJ_VAR, setter);
            }
        }
        mb.addStatement("return $L", OBJ_VAR);
        return mb.build();
    }

    private MethodSpec JFXooForm_init(TypeElement te) {
        MethodSpec.Builder mb = MethodSpec.methodBuilder("init");
        mb.addModifiers(PUBLIC);
        mb.addAnnotation(Override.class);

        TypeName paramType = TypeName.get(te.asType());
        String paramName = te.getSimpleName().toString().toLowerCase();
        mb.addParameter(ParameterSpec.builder(paramType, paramName).build());

        for (Field f : fs) {
            String inputName = f.getInputControlName();
            String getter = f.getGetter();
            // TODO: handle different data types
            if (sameType(f, LocalDate.class)) {
                mb.addStatement("$L.setValue($L.$L())", inputName, paramName, getter);
            } else if (sameType(f, String.class)) {
                mb.addStatement("$L.setText($L.$L())", inputName, paramName, getter);
            } else {
                mb.addStatement("$L.setText($L.$L().toString())", inputName, paramName, getter);
            }
        }
        return mb.build();
    }

    private List<FieldSpec> props() {
        // TODO: ui controls for simple data types
        // String   -> TextField
        // Number   -> TextField
        // Enum     -> Dropdown
        // Boolean  -> Checkbox
        // Date     -> Date Picker (?)
        List<FieldSpec> fss = new ArrayList<>();
        fss.add(FieldSpec.builder(GRIDPANE, "grid", PRIVATE).build());
        for (Field f : fs) {
            if (sameType(f, LocalDate.class)) {
                fss.add(FieldSpec.builder(DATEPICKER, f.getInputControlName(), PRIVATE).build());
            } else {
                fss.add(FieldSpec.builder(TEXTFIELD, f.getInputControlName(), PRIVATE).build());
            }
        }
        return fss;
    }

    private MethodSpec JFXooForm_get() {
        MethodSpec.Builder mb = MethodSpec.methodBuilder("node");
        mb.addAnnotation(Override.class);
        mb.returns(NODE);
        mb.addModifiers(PUBLIC);
        mb.addStatement("return grid");
        return mb.build();
    }

    private MethodSpec layout() {
        // TODO: Form controls (Save, Cancel buttons)
        MethodSpec.Builder mb = MethodSpec.methodBuilder("_layout");
        mb.addModifiers(PRIVATE);
        int row = 0;
        int col = 0;

        for (Field f : fs) {
            String labelName = "label_" + f.getName();
            String inputName = f.getInputControlName();
            mb.addStatement("$T $L = new $T($S)", LABEL, labelName, LABEL, labelFormat(f.getName()));
            if (sameType(f, LocalDate.class)) {
                mb.addStatement("$L = new $T()", inputName, DATEPICKER);
            } else {
                mb.addStatement("$L = new $T()", inputName, TEXTFIELD);
            }
            mb.addStatement("grid.add($L, $L, $L)", labelName, col, row);
            col++;
            mb.addStatement("grid.add($L, $L, $L)", inputName, col, row);
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

    public boolean sameType(Field f, Class<?> c) {
        return types.isSameType(f.getType(), elements.getTypeElement(c.getCanonicalName()).asType());
    }
}
