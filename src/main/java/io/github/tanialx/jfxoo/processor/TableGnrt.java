package io.github.tanialx.jfxoo.processor;

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
import static io.github.tanialx.jfxoo.processor.Helper.isFromType;
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
                                .addField(VBOX, "node", PRIVATE)
                                .addField(HBOX, "control", PRIVATE)
                                .addField(ParameterizedTypeName.get(TABLEVIEW, TypeName.get(te.asType())), "table", PRIVATE)
                                .addMethod(constructor(te))
                                .addMethod(table(te))
                                .addMethod(control())
                                .addMethod(data(te))
                                .addMethod(node())
                                .build())
                .indent("    ")
                .build();
    }

    public MethodSpec node() {
        MethodSpec.Builder mb = MethodSpec.methodBuilder("node");
        mb.addAnnotation(Override.class);
        mb.returns(VBOX);
        mb.addModifiers(PUBLIC);
        mb.addStatement("return node");
        return mb.build();
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
        mb.addStatement("node = new $T()", VBOX);
        mb.addStatement("node.setSpacing($L)", 4);
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

        String simpleName = te.getSimpleName().toString();
        ClassName jfxooFormClassname = ClassName.get(elements.getPackageOf(te).toString(), "JFXooForm" + simpleName);

        mb.addStatement(CodeBlock.builder()
                .add("table.setRowFactory(tv -> {\n")
                .add("$T<$T> row = new $T<>();\n", TABLE_ROW, te.asType(), TABLE_ROW)
                .add("row.setOnMouseClicked(evt -> {\n")
                .add("if (!row.isEmpty() && evt.getButton() == $T.PRIMARY && evt.getClickCount() == 2) {\n", MOUSE_BUTTON)
                .add("$T selected = row.getItem();\n", te.asType())
                .add("$T s = new $T();\n", STAGE, STAGE)
                .add("$T f = new $T();\n", jfxooFormClassname, jfxooFormClassname)
                .add("f.init(selected);\n")
                .add("f.setOnSave(_f -> {\n")
                .add("int idx = table.getItems().indexOf(selected);\n")
                .add("table.getItems().remove(selected);\n")
                .add("table.getItems().add(idx, _f);\n")
                .add("s.close();\n")
                .add("});\n")
                .add("f.setOnCancel(Void -> s.close());\n")
                .add("$T scene = new $T(f.node());\n", SCENE, SCENE)
                .add("s.setScene(scene);\n")
                .add("s.setTitle(\"Edit\");\n")
                .add("s.show();\n")
                .add("}\n")
                .add("});\n")
                .add("return row;\n")
                .add("})")
                .build());

        mb.addStatement("control = new $T()", HBOX);
        mb.addStatement("control.setSpacing($L)", 4);
        mb.addStatement("$T btnADD = new $T($S)", BUTTON, BUTTON, "Add");
        mb.addStatement("$T btnEDT = new $T($S)", BUTTON, BUTTON, "Edit");
        mb.addStatement("$T btnREM = new $T($S)", BUTTON, BUTTON, "Remove");
        mb.addStatement(CodeBlock.builder()
                .add("btnADD.setOnMouseClicked(evt -> {\n")
                .add("$T s = new $T();\n", STAGE, STAGE)
                .add("$T f = new $T();\n", jfxooFormClassname, jfxooFormClassname)
                .add("f.setOnSave(_f -> {\n" +
                        "                table.getItems().add(_f);\n" +
                        "                s.close();\n" +
                        "            });\n")
                .add(" f.setOnCancel(Void -> s.close());\n")
                .add("$T scene = new $T(f.node());\n", SCENE, SCENE)
                .add("s.setScene(scene);\n")
                .add("s.setTitle($S);\n", "Add")
                .add("s.show();\n")
                .add("})")
                .build());
        mb.addStatement(CodeBlock.builder()
                .add("btnEDT.setOnMouseClicked(evt -> {\n")
                .add("$T selected = table.getSelectionModel().getSelectedItem();\n", te.asType())
                .add("if (selected == null) return;\n")
                .add("$T s = new $T();\n", STAGE, STAGE)
                .add("$T f = new $T();\n", jfxooFormClassname, jfxooFormClassname)
                .add("f.init(selected);\n")
                .add("f.setOnSave(_f -> {\n")
                .add("int idx = table.getItems().indexOf(selected);\n")
                .add("table.getItems().remove(selected);\n")
                .add("table.getItems().add(idx, _f);\n")
                .add("s.close();\n")
                .add("});\n")
                .add("f.setOnCancel(Void -> s.close());\n")
                .add("$T scene = new $T(f.node());\n", SCENE, SCENE)
                .add("s.setScene(scene);\n")
                .add("s.setTitle(\"Edit\");\n")
                .add("s.show();\n")
                .add("})")
                .build());
        mb.addStatement(CodeBlock.builder()
                .add("btnREM.setOnMouseClicked(evt -> {\n")
                .add("$T selected = table.getSelectionModel().getSelectedItem();\n", te.asType())
                .add("if (selected != null) {\n")
                .add("table.getItems().remove(selected);\n")
                .add("}\n")
                .add("})")
                .build());
        mb.addStatement("control.getChildren().addAll(btnADD, btnEDT, btnREM)");
        mb.addStatement("node.getChildren().addAll(control, table)");
        mb.addModifiers(PUBLIC);
        return mb.build();
    }
}
