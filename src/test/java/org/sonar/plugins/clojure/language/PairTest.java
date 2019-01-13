package org.sonar.plugins.clojure.language;

import org.junit.Before;
import org.junit.Test;

import static org.sonar.plugins.clojure.language.ClojureGrammar.MAP;
import static org.sonar.plugins.clojure.language.ClojureGrammar.PAIR;
import static org.sonar.sslr.tests.Assertions.assertThat;

public class PairTest extends ClojureRuleTest {

    @Before
    @Override
    public void init() {
        p.setRootRule(g.rule(PAIR));
    }

    @Test
    public void pairHaveTwoElements() {
        assertThat(p)
                .matches("a b");
    }

    @Test
    public void oneElementIsNotAPair() {
        assertThat(p)
                .notMatches("a");
    }

    @Test
    public void threeOrMoreElementsIsNotPairs() {
        assertThat(p)
                .notMatches("a b c")
                .notMatches("a b c d");
    }
}
