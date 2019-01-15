package org.sonar.plugins.clojure.language;
import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.Token;
import com.sonar.sslr.api.TokenType;
import com.sonar.sslr.impl.Lexer;
import com.sonar.sslr.impl.channel.BlackHoleChannel;
import com.sonar.sslr.impl.channel.IdentifierAndKeywordChannel;
import com.sonar.sslr.impl.channel.PunctuatorChannel;
import org.sonar.sslr.channel.Channel;
import org.sonar.sslr.channel.CodeReader;

import java.security.Key;

import static com.sonar.sslr.impl.channel.RegexpChannelBuilder.commentRegexp;
import static com.sonar.sslr.impl.channel.RegexpChannelBuilder.regexp;

public final class ClojureLexer {

    private ClojureLexer() {
    }

    public static enum Literals implements TokenType {

        INTEGER,STRING,CLOJURE_KEYWORD, CHAR;

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

    private static String identifierKeywordChannelRegex = "[^\\\\:\"0-9;\\[\\]\\]{}\\(\\)\\s][^\";\\[\\]\\]{}\\(\\)\\s]*+";
    private static String stringRegex = "\"([^\"\\\\]*+(\\\\[\\s\\S])?+)*+\"";
    private static String clojureKeywordRegex = ":([^;\\[\\]\\]{}\\(\\)\\s\\/\\\"])*\\/?[^;\\[\\]\\]{}\\(\\)\\s\\/\\\"]+";
    private static String integerRegex = "[0-9]+";
    private static String commentRegexp = ";.*";
    private static String blackholeRegexp = "[ \n\r\t\f]+";
    private static String charRegexp = "\\\\.";

    private  class ClojureKeywordChannel extends Channel<Lexer> {

        @Override
        public boolean consume(CodeReader codeReader, Lexer lexer) {
            if (codeReader.peek(1)[0] == ':'){


//                Token token =  Token.builder()
//                        .setType(Literals.CLOJURE_KEYWORD)
//                        .setValueAndOriginalValue(value)
//                        .setURI(lexer.getURI())
//                        .setLine(code.getPreviousCursor().getLine())
//                        .setColumn(code.getPreviousCursor().getColumn())
//                        .build();

            }
            return false;
        }
    }

    public static Lexer create() {
        return Lexer.builder()
                .withFailIfNoChannelToConsumeOneCharacter(true)
                .withChannel(new IdentifierAndKeywordChannel(identifierKeywordChannelRegex, true, Keywords.values()))
                .withChannel(regexp(Literals.STRING, stringRegex))
                .withChannel(regexp(Literals.INTEGER, integerRegex))
                .withChannel(regexp(Literals.CHAR, charRegexp))
                .withChannel(regexp(Literals.CLOJURE_KEYWORD, clojureKeywordRegex))
                .withChannel(commentRegexp(commentRegexp))
                .withChannel(new PunctuatorChannel(Punctuators.values()))
            .withChannel(new BlackHoleChannel(blackholeRegexp))
                .build();
    }

}
