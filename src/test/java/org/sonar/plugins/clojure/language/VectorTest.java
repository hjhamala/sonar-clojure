package org.sonar.plugins.clojure.language;

import org.junit.Before;
import org.junit.Test;

import static org.sonar.plugins.clojure.language.ClojureGrammar.MAP;
import static org.sonar.plugins.clojure.language.ClojureGrammar.VECTOR;
import static org.sonar.sslr.tests.Assertions.assertThat;

public class VectorTest extends ClojureRuleTest {

    @Before
    @Override
    public void init() {
        p.setRootRule(g.rule(VECTOR));
    }

    @Test
    public void vectorCanBeEmpty() {
        assertThat(p)
                .matches("[]");

    }

    @Test
    public void vectorHaveOneOrMoreElements() {
        assertThat(p)
                .matches("[a]")
                .matches("[a b]")
                .matches("[a [a]]")
                .matches("[a {a 1}]")
                ;
    }


}
