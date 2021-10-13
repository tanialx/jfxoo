package io.github.tanialx.jfxoo.processor;

import com.squareup.javapoet.JavaFile;
import io.github.tanialx.jfxoo.annotation.JFXooForm;
import io.github.tanialx.jfxoo.processor.gnrt.CreatorGnrt;
import io.github.tanialx.jfxoo.processor.gnrt.FormGnrt;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JFXooProc extends AbstractProcessor {

    private final List<JavaFile> fs = new ArrayList<>();

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        /*
         * Generate code for JFXoo supported annotations
         * - JFXooForm
         * and write to 'generated' output path
         */
        if (!roundEnv.processingOver()) {
            final CreatorGnrt creatorGnrt = new CreatorGnrt(processingEnv);
            roundEnv.getElementsAnnotatedWith(JFXooForm.class)
                    .stream()
                    .filter(e -> e.getKind() == ElementKind.CLASS)
                    .map(e -> (TypeElement) e)
                    .forEach(te -> {
                        creatorGnrt.add(te);
                        fs.add(new FormGnrt(processingEnv).run(te));
                    });
            fs.add(creatorGnrt.run());
        } else {
            fs.forEach(f -> {
                try {
                    f.writeTo(this.processingEnv.getFiler());
                } catch (IOException ex) {
                    throw new RuntimeException();
                }
            });
        }
        return false;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> set = new HashSet<>();
        set.add(JFXooForm.class.getCanonicalName());
        return set;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
