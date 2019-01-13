package org.sonar.plugins.clojure.language;

import org.junit.Before;
import org.junit.Test;

import static org.sonar.sslr.tests.Assertions.assertThat;
import static org.sonar.plugins.clojure.language.ClojureGrammar.LIST;

public class ListTest extends ClojureRuleTest {

    @Override
    @Before
    public void init() {
        p.setRootRule(g.rule(LIST));
    }

    @Test
    public void emptyList() {
        assertThat(p)
                .matches("()");

    }

    @Test
    public void nonEmptyList() {
        assertThat(p)
                .matches("(a b c)")
        .matches("(a (a) b (c (d i) )())")
        .matches("  (defn bar-inside-foo\n" +
                "    []\n" +
                "    true)\n" );
    }
}
