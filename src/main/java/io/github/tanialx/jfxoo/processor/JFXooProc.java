package io.github.tanialx.jfxoo.processor;

import io.github.tanialx.jfxoo.annotation.JFXooForm;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import java.util.HashSet;
import java.util.Set;

public class JFXooProc extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        /*
         * Generate code for JFXoo supported annotations
         * - JFXooForm
         * and write to 'generated' output path
         */
        roundEnv.getElementsAnnotatedWith(JFXooForm.class)
                .stream()
                .filter(e -> e.getKind() == ElementKind.CLASS)
                .forEach(e -> {
                   // TODO: Generate code for JFXooForm
                });
        return true;
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
