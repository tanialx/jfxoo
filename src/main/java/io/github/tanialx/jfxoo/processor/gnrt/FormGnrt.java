package io.github.tanialx.jfxoo.processor.gnrt;

import com.squareup.javapoet.*;
import io.github.tanialx.jfxoo.JFXooForm;
import io.github.tanialx.jfxoo.annotation.JFXooTable;
import io.github.tanialx.jfxoo.annotation.JFXooVar;
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

import static io.github.tanialx.jfxoo.processor.CLSName.*;
import static io.github.tanialx.jfxoo.processor.gnrt.Helper.*;
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.PUBLIC;

public class FormGnrt {

    private final Types types;
    private final Elements elements;
    private List<Field> fs;

    @Builder
    @Getter
    @Setter
    public static class Field {
        private String name;
        private TypeMirror type;
        private String pkg;
        private String inputControlName;
        private String getter;
        private String setter;
        private ClassName control;
    }

    public FormGnrt(ProcessingEnvironment procEnv) {
        this.types = procEnv.getTypeUtils();
        this.elements = procEnv.getElementUtils();
    }

    private List<Field> fields(TypeElement te) {
        return ElementFilter.fieldsIn(te.getEnclosedElements()).stream().map(ve -> {
            String fieldName = ve.getSimpleName().toString();
            String nameInMethod = Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
            String setter = String.format("set%s", nameInMethod);
            String inputName = "in_" + fieldName;
            // TODO: ui controls for simple data types
            // String   -> TextField
            // Number   -> TextField
            // Enum     -> Dropdown
            // Boolean  -> Checkbox
            // Date     -> Date Picker (?)
            JFXooVar jfXooVar = ve.getAnnotation(JFXooVar.class);
            ClassName control = null;
            if (jfXooVar != null) {
                switch (jfXooVar.type()) {
                    case password -> control = PASSWORD_FIELD;
                    case textarea -> control = TEXTAREA;
                }
            }
            if (control == null) {
                TypeMirror t = ve.asType();
                if (sameType(t, LocalDate.class)) {
                    control = DATE_PICKER;
                } else if (sameType(t, BigDecimal.class)) {
                    control = TEXT_FIELD;
                } else if (sameType(t, Boolean.class)) {
                    control = CHECKBOX;
                } else if (isFromType(TypeName.get(t), ClassName.get(List.class))) {
                    control = JFXOO_TABLE;
                } else {
                    control = TEXT_FIELD;
                }
            }
            return Field.builder().name(fieldName).setter(setter).getter(String.format("get%s", nameInMethod)).inputControlName(inputName).type(ve.asType()).control(control).pkg(elements.getPackageOf(ve).toString()).build();
        }).collect(Collectors.toList());
    }

    public JavaFile run(TypeElement te) {
        final String pkg = elements.getPackageOf(te).toString();
        final String _class = "JFXooForm" + te.getSimpleName();

        // collect all props that should be displayed as fields on generated form
        fs = fields(te);

        return JavaFile.builder(pkg, TypeSpec.classBuilder(_class).addModifiers(PUBLIC).addSuperinterface(ParameterizedTypeName.get(ClassName.get(JFXooForm.class), TypeName.get(te.asType()))).addFields(props()).addMethod(JFXooForm_get()).addMethod(constructor()).addMethod(layout(te)).addMethod(JFXooForm_init(te)).addMethod(JFXooForm_value(te)).build()).indent("    ").build();
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
            if (f.control == TEXT_FIELD || f.control == TEXTAREA || f.control == PASSWORD_FIELD) {
                if (sameType(f, String.class)) {
                    mb.addStatement("$L.$L($L.getText())", OBJ_VAR, setter, inputName);
                } else if (sameType(f, BigDecimal.class)) {
                    mb.addStatement("$L.$L(new $T($L.getText()))", OBJ_VAR, setter, BigDecimal.class, inputName);
                } else if (sameType(f, Integer.class)) {
                    mb.addStatement("$L.$L($T.parseInt($L.getText()))", OBJ_VAR, setter, Integer.class, inputName);
                }
            } else if (f.control == DATE_PICKER) {
                mb.addStatement("$L.$L($L.getValue())", OBJ_VAR, setter, inputName);
            } else if (f.control == CHECKBOX) {
                mb.addStatement("$L.$L($L.isSelected())", OBJ_VAR, setter, inputName);
            } else if (f.control == JFXOO_TABLE) {

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
            if (f.control == DATE_PICKER) {
                mb.addStatement("$L.setValue($L.$L())", inputName, paramName, getter);
            } else if (f.control == TEXT_FIELD || f.control == TEXTAREA || f.control == PASSWORD_FIELD) {
                if (sameType(f.type, String.class)) {
                    mb.addStatement("$L.setText($L.$L())", inputName, paramName, getter);
                } else {
                    mb.addStatement("$L.setText($L.$L().toString())", inputName, paramName, getter);
                }
            } else if (f.control == CHECKBOX) {
                mb.addStatement("$L.setSelected($L.$L())", inputName, paramName, getter);
            } else if (f.control == ClassName.get(JFXooTable.class)) {

            }
        }
        return mb.build();
    }

    private List<FieldSpec> props() {
        List<FieldSpec> fss = new ArrayList<>();
        fss.add(FieldSpec.builder(GRID_PANE, "grid", PRIVATE).build());
        fs.forEach(f -> {
            if (f.control == JFXOO_TABLE) {
                TypeName _type = typeArgs(TypeName.get(f.type)).get(0);
                fss.add(FieldSpec.builder(ParameterizedTypeName.get(TABLEVIEW, _type), f.inputControlName, PRIVATE).build());
            } else {
                fss.add(FieldSpec.builder(f.control, f.inputControlName, PRIVATE).build());
            }
        });
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

    private MethodSpec layout(TypeElement te) {
        // TODO: Form controls (Save, Cancel buttons)
        MethodSpec.Builder mb = MethodSpec.methodBuilder("_layout");
        mb.addModifiers(PRIVATE);

        mb.addStatement("$T $L = new $T($S)", TEXT, "heading", TEXT, te.getSimpleName().toString());
        mb.addStatement("$L.setFont($T.font($L,$T.$L,$L))", "heading", FONT, "null", FONT_WEIGHT, "NORMAL", 20);
        mb.addStatement("grid.add($L, 0, 0, 2, 1)", "heading");

        int row = 1;
        int col = 0;
        for (Field f : fs) {
            String labelName = "label_" + f.getName();
            String inputName = f.getInputControlName();
            mb.addStatement("$T $L = new $T($S)", LABEL, labelName, LABEL, labelFormat(f.getName()));
            if (f.control == DATE_PICKER) {
                mb.addStatement("$L = new $T()", inputName, DATE_PICKER);
            } else if (f.control == PASSWORD_FIELD) {
                mb.addStatement("$L = new $T()", inputName, PASSWORD_FIELD);
            } else if (f.control == TEXTAREA) {
                mb.addStatement("$L = new $T()", inputName, TEXTAREA);
            } else if (f.control == CHECKBOX) {
                mb.addStatement("$L = new $T()", inputName, CHECKBOX);
            } else if (f.control == JFXOO_TABLE) {
                TypeName _type = typeArgs(TypeName.get(f.type)).get(0);
                String simpleName = _type.toString().substring(_type.toString().lastIndexOf(".") + 1);
                mb.addStatement("$L = new $T().table()", inputName, ClassName.get(f.pkg, "JFXooTable" + simpleName));
            } else {
                mb.addStatement("$L = new $T()", inputName, TEXT_FIELD);
            }
            mb.addStatement("grid.add($L, $L, $L)", labelName, col, row);
            col++;
            mb.addStatement("grid.add($L, $L, $L)", inputName, col, row);
            row++;
            col = 0;
        }

        mb.addStatement("$T btn_save = new $T($S)", BUTTON, BUTTON, "Save");
        mb.addStatement("$T btn_cancel = new $T($S)", BUTTON, BUTTON, "Cancel");
        mb.addStatement("$T hBox_control = new $T()", HBOX, HBOX);
        mb.addStatement("hBox_control.setAlignment($T.BASELINE_RIGHT)", POS);
        mb.addStatement("hBox_control.getChildren().addAll(btn_cancel, btn_save)");
        mb.addStatement("grid.add(hBox_control, 0, $L, 2, 1)", row);
        return mb.build();
    }

    private MethodSpec constructor() {
        MethodSpec.Builder mb = MethodSpec.constructorBuilder();
        mb.addModifiers(PUBLIC);
        mb.addStatement("grid = new $T()", GRID_PANE);
        mb.addStatement("grid.setAlignment($T.CENTER)", POS);
        mb.addStatement("grid.setHgap($L)", 10);
        mb.addStatement("grid.setVgap($L)", 10);
        mb.addStatement("_layout()");
        return mb.build();
    }

    public boolean sameType(Field f, Class<?> c) {
        return types.isSameType(f.getType(), elements.getTypeElement(c.getCanonicalName()).asType());
    }

    public boolean sameType(TypeMirror t, Class<?> c) {
        return types.isSameType(t, elements.getTypeElement(c.getCanonicalName()).asType());
    }
}
