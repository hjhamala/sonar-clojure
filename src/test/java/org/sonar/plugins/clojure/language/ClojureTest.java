package org.sonar.plugins.clojure.language;

import org.junit.Before;
import org.junit.Test;

import static org.sonar.plugins.clojure.language.ClojureGrammar.CLOJURE;
import static org.sonar.plugins.clojure.language.ClojureGrammar.LIST;
import static org.sonar.sslr.tests.Assertions.assertThat;

public class ClojureTest extends ClojureRuleTest {

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
    public void sourceIsClojure() {
        assertThat(p)
                .matches(source);

    }


}
