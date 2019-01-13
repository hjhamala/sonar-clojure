package org.sonar.plugins.clojure.language;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.Assert.assertEquals;


public class CommentLineCalculatorTest {
    static String readFile(String path, Charset encoding)
            throws IOException
    {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

    private CommentLineCalculator calc;

    @Before
    public void setUp() throws IOException {
        String contents = readFile("src/test/resources/core.clj", UTF_8);
        calc = new CommentLineCalculator(contents);
    }

    @Test
    public void calculateComments(){
        Set<Integer> comments = calc.calculateCommentLines();
        Set<Integer> expected = new HashSet<>();
        expected.add(5);
        expected.add(7);
        assertEquals(comments, expected);
    }

    @Test
    public void calculateDifference() {
        Set<Integer> codeLines = new HashSet<>();
        codeLines.add(1);
        codeLines.add(2);
        codeLines.add(3);
        codeLines.add(6);
        codeLines.add(7);
        codeLines.add(8);
        Set<Integer> difference = calc.calculateLinesWithOnlyComments(codeLines);
        Set<Integer> expected = new HashSet<>();
        expected.add(5);
        assertEquals(difference, expected);
    }
}
