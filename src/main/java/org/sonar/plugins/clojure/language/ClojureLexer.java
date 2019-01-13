package org.sonar.plugins.clojure.language;
import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.TokenType;
import com.sonar.sslr.impl.Lexer;
import com.sonar.sslr.impl.channel.BlackHoleChannel;
import com.sonar.sslr.impl.channel.IdentifierAndKeywordChannel;
import com.sonar.sslr.impl.channel.PunctuatorChannel;

import static com.sonar.sslr.impl.channel.RegexpChannelBuilder.commentRegexp;
import static com.sonar.sslr.impl.channel.RegexpChannelBuilder.regexp;

public final class ClojureLexer {

    private ClojureLexer() {
    }

    public static enum Literals implements TokenType {

        INTEGER,STRING;

        @Override
        public String getName() {
            return name();
        }

        @Override
        public String getValue() {
            return name();
        }

        @Override
        public boolean hasToBeSkippedFromAst(AstNode node) {
            return false;
        }

    }

    public static enum Punctuators implements TokenType {

        PAREN_L("("), PAREN_R(")"),
        BRACE_L("{"), BRACE_R("}"),
        SQUARE_BRACE_L("["), SQUARE_BRACE_R("]");

        private final String value;

        private Punctuators(String value) {
            this.value = value;
        }

        @Override
        public String getName() {
            return name();
        }

        @Override
        public String getValue() {
            return value;
        }

        @Override
        public boolean hasToBeSkippedFromAst(AstNode node) {
            return false;
        }

    }

    public static enum Keywords implements TokenType {

        TRUE("true"),
        FALSE("false"), NIL("nil");

        private final String value;

        private Keywords(String value) {
            this.value = value;
        }

        @Override
        public String getName() {
            return name();
        }

        @Override
        public String getValue() {
            return value;
        }

        @Override
        public boolean hasToBeSkippedFromAst(AstNode node) {
            return false;
        }

        public static String[] keywordValues() {
            Keywords[] keywordsEnum = Keywords.values();
            String[] keywords = new String[keywordsEnum.length];
            for (int i = 0; i < keywords.length; i++) {
                keywords[i] = keywordsEnum[i].getValue();
            }
            return keywords;
        }

    }

    public static Lexer create() {
        return Lexer.builder()
                .withFailIfNoChannelToConsumeOneCharacter(true)
                .withChannel(new IdentifierAndKeywordChannel("[^0-9;\\[\\]\\]{}\\(\\)\\s][^;\\[\\]\\]{}\\(\\)\\s]*", true, Keywords.values()))
                .withChannel(regexp(Literals.STRING, "\"([^\"\\\\]|\\\\([\"\\\\/bfnrt]|u[0-9a-fA-F]{4}))*+\""))
                .withChannel(regexp(Literals.INTEGER, "[0-9]+"))
                .withChannel(commentRegexp(";.*"))
                .withChannel(new PunctuatorChannel(Punctuators.values()))
                .withChannel(new BlackHoleChannel("[ \n\r\t\f,]*+"))
                .build();
    }

}
