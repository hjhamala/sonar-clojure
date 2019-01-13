package org.sonar.plugins.clojure.language;




import com.sonar.sslr.api.Grammar;
import org.sonar.sslr.grammar.GrammarRuleKey;
import org.sonar.sslr.grammar.LexerfulGrammarBuilder;

import static com.sonar.sslr.api.GenericTokenType.IDENTIFIER;
import static org.sonar.plugins.clojure.language.ClojureLexer.Literals.INTEGER;
import static org.sonar.plugins.clojure.language.ClojureLexer.Literals.STRING;
import static org.sonar.plugins.clojure.language.ClojureLexer.Punctuators.*;

public enum ClojureGrammar implements GrammarRuleKey {
    VECTOR,
    MAP,
    PAIR,
    VALUE,
    LIST,
    CLOJURE;

    public static Grammar create() {
        LexerfulGrammarBuilder b = LexerfulGrammarBuilder.create();

        b.rule(MAP).is(BRACE_L, b.optional(PAIR, b.zeroOrMore(PAIR)), BRACE_R);
        b.rule(PAIR).is(VALUE, VALUE);
        b.rule(LIST).is(PAREN_L,  b.optional(VALUE, b.zeroOrMore(VALUE)), PAREN_R);
        b.rule(VECTOR).is(SQUARE_BRACE_L, b.optional(VALUE, b.zeroOrMore(VALUE)), SQUARE_BRACE_R);

        b.rule(VALUE).is(b.firstOf(IDENTIFIER,STRING,INTEGER, MAP, VECTOR, LIST));
        b.rule(CLOJURE).is(b.zeroOrMore(VALUE));

        return b.build();
    }

}
