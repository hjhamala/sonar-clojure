package org.sonar.plugins.clojure.language;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.sonar.plugins.clojure.language.ClojureGrammar.CLOJURE;
import static org.sonar.plugins.clojure.language.ClojureGrammar.LIST;
import static org.sonar.sslr.tests.Assertions.assertThat;

public class ClojureTest extends ClojureRuleTest {

    private static final Logger LOG = Loggers.get(ClojureTest.class);

    static String readFile(String path, Charset encoding)
            throws IOException
    {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

    public static String source = "(ns clojure-sonar-example.core)\n" +
            "(defn foo\n" +
            "  [x]\n" +
            "  \"This is not a place to put docstring\"\n" +
            "  (when (> x 0))\n" +
            "  (defn bar-inside-foo\n" +
            "    []\n" +
            "    true)\n" +
            "  nil)\n" +
            "(defn true [] 1)\n" +
            "(defn []\n" +
            "  false)";

    @Override
    @Before
    public void init() {
        p.setRootRule(g.rule(CLOJURE));
    }

    @Test
    public void sourceIsClojure() throws IOException {
        assertThat(p)
                .matches(source);
        assertThat(p)
                .matches(readFile("src/test/resources/core.clj", UTF_8));
        String boa = readFile("src/test/resources/boa.clj", UTF_8);

        assertThat(p).matches("(def char-value \\c)");
        assertThat(p).matches(boa);
        assertThat(p).matches(":e");
        assertThat(p).matches("(log/debug {:message \"DB violation\" :e e})");

    }


}
