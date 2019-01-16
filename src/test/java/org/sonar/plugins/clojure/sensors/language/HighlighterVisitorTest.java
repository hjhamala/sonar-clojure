package org.sonar.plugins.clojure.sensors.language;

import com.sonar.sslr.api.GenericTokenType;
import com.sonar.sslr.api.Token;
import org.junit.Test;
import org.sonar.api.batch.fs.TextRange;
import org.sonar.api.batch.fs.internal.DefaultInputFile;
import org.sonar.api.batch.fs.internal.DefaultTextPointer;
import org.sonar.api.batch.fs.internal.DefaultTextRange;
import org.sonar.api.batch.fs.internal.TestInputFileBuilder;
import org.sonar.api.batch.sensor.internal.SensorContextTester;
import org.sonar.plugins.clojure.language.ClojureLanguage;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.Assert.assertEquals;

public class HighlighterVisitorTest {

    static String readFile(String path, Charset encoding)
            throws IOException
    {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

    @Test
    public void calculateMultiLineEnding() throws IOException, URISyntaxException {
        String multilen = readFile("src/test/resources/multi_line.clj", UTF_8);
        File f = new File("src/test/resources/multi_line.clj");
        SensorContextTester context = SensorContextTester.create(f);
        DefaultInputFile inputFile = TestInputFileBuilder.create("", "file.clj")
                .setLanguage(ClojureLanguage.KEY)
                .initMetadata(new String(Files.readAllBytes(f.toPath()), StandardCharsets.UTF_8))
                .build();
        Token t = Token.builder()
                .setColumn(1)
                .setLine(1)
                .setValueAndOriginalValue(multilen)
                .setType(GenericTokenType.LITERAL)
                .setURI(new URI("uri:123"))
                .build();
        HighlighterVisitor h = new HighlighterVisitor(context, inputFile);
        TextRange result = h.calculateRange(t);
        TextRange expected = new DefaultTextRange(new DefaultTextPointer(1, 1), new DefaultTextPointer(2, 64));
        assertEquals(result,expected);
    }
}
