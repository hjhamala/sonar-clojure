package org.sonar.plugins.clojure.language;

import com.sonar.sslr.api.Grammar;
import com.sonar.sslr.impl.Parser;
import com.sonar.sslr.impl.ast.AstWalker;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThat;
import static org.sonar.plugins.clojure.language.ClojureGrammar.CLOJURE;
import static org.sonar.sslr.tests.Assertions.assertThat;

public class LinesOfCodeVisitorTest {

    static String readFile(String path, Charset encoding)
            throws IOException
    {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }



    @Test
    public void calculateLinesOfCode() throws IOException {
        final Parser<Grammar> p = ClojureParser.create();
        final Grammar g = p.getGrammar();
        p.setRootRule(g.rule(CLOJURE));
        LinesOfCodeVisitor codeVisitor = new LinesOfCodeVisitor();
        AstWalker walker = new AstWalker(codeVisitor);
        walker.walkAndVisit(p.parse(readFile("src/test/resources/core.clj", UTF_8)));
        Set<Integer> linesWithCode = codeVisitor.getLinesOfCode();
        Set<Integer> expected = new HashSet<>();
        expected.add(1);
        expected.add(2);
        expected.add(3);
        expected.add(6);
        expected.add(7);
        expected.add(8);

        assertEquals(linesWithCode, expected);
        assertEquals(linesWithCode.size(),6);


    }
}
