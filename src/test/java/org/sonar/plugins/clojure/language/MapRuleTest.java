package org.sonar.plugins.clojure.language;

import org.junit.Before;
import org.junit.Test;

import static org.sonar.plugins.clojure.language.ClojureGrammar.MAP;
import static org.sonar.sslr.tests.Assertions.assertThat;

public class MapRuleTest extends ClojureRuleTest {

    @Before
    @Override
    public void init() {
        p.setRootRule(g.rule(MAP));
    }

    @Test
    public void mapCanBeEmpty() {
        assertThat(p)
                .matches("{}");

    }

    @Test
    public void mapHavePairs() {
        assertThat(p)
                .matches("{a b}")
                .matches("{" +
                        "c d " +
                        "f y" +
                        "}");
    }

    @Test
    public void mapWithIncompletePairIsInvalidMap() {
        assertThat(p)
                .notMatches("{a}");
    }
}
