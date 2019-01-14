package org.sonar.plugins.clojure.language;

import com.sonar.sslr.impl.Lexer;
import org.junit.Test;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Pattern;

import static com.sonar.sslr.api.GenericTokenType.IDENTIFIER;
import static com.sonar.sslr.test.lexer.LexerMatchers.hasComment;
import static com.sonar.sslr.test.lexer.LexerMatchers.hasToken;
import static com.sonar.sslr.test.minic.MiniCLexer.Punctuators.SEMICOLON;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.Assert.assertThat;
import static org.sonar.api.web.Criterion.EQ;
import static org.sonar.plugins.clojure.language.ClojureLexer.Keywords.FALSE;
import static org.sonar.plugins.clojure.language.ClojureLexer.Keywords.NIL;
import static org.sonar.plugins.clojure.language.ClojureLexer.Keywords.TRUE;
import static org.sonar.plugins.clojure.language.ClojureLexer.Literals.INTEGER;
import static org.sonar.plugins.clojure.language.ClojureLexer.Punctuators.*;

public class ClojureLexerTest {

    private static final Logger LOG = Loggers.get(ClojureLexerTest.class);
    Lexer lexer = ClojureLexer.create();

    static String readFile(String path, Charset encoding)
            throws IOException
    {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

    @Test
    public void testRegexp() throws IOException {
        String regexp = readFile("src/test/resources/boa.clj", UTF_8);
        LOG.info(regexp);

        assertThat(lexer.lex("abc0"), hasToken("abc0", IDENTIFIER));
        assertThat(lexer.lex("abc_0"), hasToken("abc_0", IDENTIFIER));
        assertThat(lexer.lex("i"), hasToken("i", IDENTIFIER));
    }

    @Test
    public void lexString() {

        assertThat(lexer.lex("abc0"), hasToken("abc0", IDENTIFIER));
        assertThat(lexer.lex("abc_0"), hasToken("abc_0", IDENTIFIER));
        assertThat(lexer.lex("i"), hasToken("i", IDENTIFIER));
    }

    @Test
    public void lexIdentifiers() {
        assertThat(lexer.lex("abc"), hasToken("abc", IDENTIFIER));
        assertThat(lexer.lex("abc0"), hasToken("abc0", IDENTIFIER));
        assertThat(lexer.lex("abc_0"), hasToken("abc_0", IDENTIFIER));
        assertThat(lexer.lex("i"), hasToken("i", IDENTIFIER));
    }

    @Test
    public void lexIntegers() {
        assertThat(lexer.lex("0"), hasToken("0", INTEGER));
        assertThat(lexer.lex("000"), hasToken("000", INTEGER));
        assertThat(lexer.lex("1234"), hasToken("1234", INTEGER));
    }

    @Test
    public void lexKeywords() {
        assertThat(lexer.lex("true"), hasToken(TRUE));
        assertThat(lexer.lex("false"), hasToken(FALSE));
        assertThat(lexer.lex("nil"), hasToken(NIL));
    }

    @Test
    public void lexComments() {
        assertThat(lexer.lex(";foo"), hasComment(";foo"));
        assertThat(lexer.lex("(def a);bar"), hasComment(";bar"));
        assertThat(lexer.lex(";Here the comment is foundedaadiot"), hasComment(";Here the comment is foundedaadiot"));
    }

    @Test
    public void lexPunctuators() {
        assertThat(lexer.lex("("), hasToken(PAREN_L));
        assertThat(lexer.lex(")"), hasToken(PAREN_R));
        assertThat(lexer.lex("{"), hasToken(BRACE_L));
        assertThat(lexer.lex("}"), hasToken(BRACE_R));
    }
}
