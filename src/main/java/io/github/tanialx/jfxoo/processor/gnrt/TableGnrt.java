package io.github.tanialx.jfxoo.processor.gnrt;

import com.squareup.javapoet.*;
import io.github.tanialx.jfxoo.JFXooTable;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.github.tanialx.jfxoo.processor.CLSName.*;
import static io.github.tanialx.jfxoo.processor.gnrt.Helper.isFromType;
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.PUBLIC;

public class TableGnrt {

    private Types types;
    private Elements elements;

    public TableGnrt(ProcessingEnvironment procEnv) {
        this.types = procEnv.getTypeUtils();
        this.elements = procEnv.getElementUtils();
    }

    public JavaFile run(TypeElement te) {
        final String pkg = elements.getPackageOf(te).toString();
        final String _class = "JFXooTable" + te.getSimpleName();

        return JavaFile.builder(
                        pkg,
                        TypeSpec.classBuilder(_class)
                                .addModifiers(PUBLIC)
                                .addSuperinterface(ParameterizedTypeName.get(
                                        ClassName.get(JFXooTable.class),
                                        TypeName.get(te.asType())))
                                .addField(HBOX, "control", PRIVATE)
                                .addField(ParameterizedTypeName.get(TABLEVIEW, TypeName.get(te.asType())), "table", PRIVATE)
                                .addMethod(constructor(te))
                                .addMethod(table(te))
                                .addMethod(control())
                                .addMethod(data(te))
                                .build())
                .indent("    ")
                .build();
    }

    private MethodSpec control() {
        MethodSpec.Builder mb = MethodSpec.methodBuilder("control");
        mb.addAnnotation(Override.class);
        mb.returns(HBOX);
        mb.addModifiers(PUBLIC);
        mb.addStatement("return control");
        return mb.build();
    }

    private MethodSpec table(TypeElement te) {
        MethodSpec.Builder mb = MethodSpec.methodBuilder("table");
        mb.addAnnotation(Override.class);
        mb.returns(ParameterizedTypeName.get(TABLEVIEW, TypeName.get(te.asType())));
        mb.addModifiers(PUBLIC);
        mb.addStatement("return table");
        return mb.build();
    }

    private MethodSpec data(TypeElement te) {
        MethodSpec.Builder mb = MethodSpec.methodBuilder("data");
        mb.addAnnotation(Override.class);
        mb.returns(ParameterizedTypeName.get(ClassName.get("javafx.collections", "ObservableList"), TypeName.get(te.asType())));
        mb.addModifiers(PUBLIC);
        mb.addStatement("return table.getItems()");
        return mb.build();
    }

    private MethodSpec constructor(TypeElement te) {
        MethodSpec.Builder mb = MethodSpec.constructorBuilder();
        mb.addStatement("table = new $T<>()", TABLEVIEW);
        List<String> cols = new ArrayList<>();
        ElementFilter.fieldsIn(te.getEnclosedElements()).forEach(v -> {
            if (isFromType(TypeName.get(v.asType()), ClassName.get(List.class))) return;
            String name = v.getSimpleName().toString();
            String colVar = String.format("c_%s", v.getSimpleName().toString());
            String getter = String.format("get%s", Character.toUpperCase(name.charAt(0)) + name.substring(1));
            mb.addStatement("$T<$T, $T> $L = new $T<>($S)", TABLE_COLUMN, te.asType(), v.asType(), colVar, TABLE_COLUMN, Helper.labelFormat(name));
            mb.addStatement("$L.setCellValueFactory(p -> new $T<>(p.getValue().$L()))", colVar, SIMPLE_OBJECT_PROPERTY, getter);
            cols.add(colVar);
        });
        mb.addStatement("table.getColumns().addAll($T.asList($L))", Arrays.class, String.join(",", cols));
        mb.addStatement("table.setColumnResizePolicy($T.$L)", TABLEVIEW, "CONSTRAINED_RESIZE_POLICY");
        mb.addStatement("table.setEditable(false)");
        mb.addStatement("control = new $T()", HBOX);
        mb.addStatement("$T btnADD = new $T($S)", BUTTON, BUTTON, "Add");
        mb.addStatement("control.getChildren().add(btnADD)");
        mb.addModifiers(PUBLIC);
        return mb.build();
    }
}
