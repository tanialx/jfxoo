package io.github.tanialx.jfxoo.processor;

import com.squareup.javapoet.JavaFile;
import io.github.tanialx.jfxoo.annotation.JFXooForm;
import io.github.tanialx.jfxoo.processor.gnrt.CreatorGnrt;
import io.github.tanialx.jfxoo.processor.gnrt.FormGnrt;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class JFXooProc extends AbstractProcessor {

    private final List<TypeElement> tes = new ArrayList<>();
    private CreatorGnrt creatorGnrt;
    private FormGnrt formGnrt;
    private boolean CREATOR_WRITTEN = false;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        creatorGnrt = new CreatorGnrt(processingEnv);
        formGnrt = new FormGnrt(processingEnv);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        /*
         * Generate code for JFXoo supported annotations
         * - JFXooForm
         * and write to 'generated' output path
         */
        if (!roundEnv.processingOver()) {
            tes.addAll(roundEnv.getElementsAnnotatedWith(JFXooForm.class)
                    .stream()
                    .filter(e -> e.getKind() == ElementKind.CLASS)
                    .map(e -> (TypeElement) e)
                    .collect(Collectors.toList()));
        } else {
            tes.forEach(te -> {
                this.output(formGnrt.run(te));
                creatorGnrt.add(te);
            });
            if (!CREATOR_WRITTEN && this.output(creatorGnrt.run())) {
                CREATOR_WRITTEN = true;
            }
        }
        return false;
    }

    private boolean output(JavaFile f) {
        try {
            f.writeTo(this.processingEnv.getFiler());
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
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
