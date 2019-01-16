package org.sonar.plugins.clojure.language;

import com.sonar.sslr.api.Grammar;
import com.sonar.sslr.api.Rule;
import com.sonar.sslr.impl.Parser;

public abstract class ClojureRuleTest {

    protected final Parser<Grammar> p = ClojureParser.create();
    protected final Grammar g = p.getGrammar();

    public final Rule getTestedRule() {
        return p.getRootRule();
    }

    public abstract void init();

}


