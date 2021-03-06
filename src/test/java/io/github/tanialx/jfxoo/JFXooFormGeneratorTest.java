package io.github.tanialx.jfxoo;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.JavaFileObjects;
import io.github.tanialx.jfxoo.processor.JFXooProc;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import javax.tools.JavaFileObject;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Objects;

import static com.google.testing.compile.CompilationSubject.assertThat;
import static com.google.testing.compile.Compiler.javac;
import static javax.tools.StandardLocation.SOURCE_OUTPUT;

public class JFXooFormGeneratorTest {

    private JavaFileObject fromResource(final String name) {
        return JavaFileObjects.forResource(
                Objects.requireNonNull(getClass().getClassLoader().getResource(name)));
    }

    @Test
    public void sourcesGenerated() {
        Compilation compilation = javac().withProcessors(new JFXooProc())
                .compile(fromResource("Book.java"), fromResource("Review.java"));

        // Each source file is successfully compiled
        assertThat(compilation).succeeded();
        // Generated file package is present and has the correct content
        assertThat(compilation)
                .generatedFile(SOURCE_OUTPUT,
                        "io.github.tanialx.jfxoo",
                        "JFXooCreatorImpl.java")
                .hasSourceEquivalentTo(fromResource("ExpectedJFXooCreatorImpl.java"));
        assertThat(compilation)
                .generatedFile(SOURCE_OUTPUT,
                "io.github.tanialx.jfxoo.test",
                        "JFXooFormBook.java")
                .hasSourceEquivalentTo(fromResource("ExpectedJFXooFormBook.java"));
        assertThat(compilation)
                .generatedFile(SOURCE_OUTPUT,
                        "io.github.tanialx.jfxoo.test",
                        "JFXooTableBook.java")
                .hasSourceEquivalentTo(fromResource("ExpectedJFXooTableBook.java"));
    }
}
