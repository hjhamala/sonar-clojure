package org.sonar.plugins.clojure.language;

import com.sonar.sslr.impl.Lexer;
import org.junit.Test;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.sonar.sslr.api.GenericTokenType.IDENTIFIER;
import static com.sonar.sslr.test.lexer.LexerMatchers.hasComment;
import static com.sonar.sslr.test.lexer.LexerMatchers.hasToken;
import static org.junit.Assert.assertThat;
import static org.sonar.plugins.clojure.language.ClojureLexer.Keywords.*;
import static org.sonar.plugins.clojure.language.ClojureLexer.Literals.*;
import static org.sonar.plugins.clojure.language.ClojureLexer.Punctuators.*;

public class ClojureLexerTest {

    private static final Logger LOG = Loggers.get(ClojureLexerTest.class);
    Lexer lexer = ClojureLexer.create();

    @Test
    public void lexChar() {

        assertThat(lexer.lex("\\c"), hasToken("\\c", CHAR));
        assertThat(lexer.lex("abc \\d"), hasToken("\\d", CHAR));
        assertThat(lexer.lex("(def char-value \\c)"), hasToken("\\c", CHAR));
    }

    @Test
    public void lexIdentifiers() {
        assertThat(lexer.lex("abc"), hasToken("abc", IDENTIFIER));
        assertThat(lexer.lex("abc0"), hasToken("abc0", IDENTIFIER));
        assertThat(lexer.lex("abc_0"), hasToken("abc_0", IDENTIFIER));
        assertThat(lexer.lex("i"), hasToken("i", IDENTIFIER));
    }

    @Test
    public void lexKeyword() {
        assertThat(lexer.lex(":a"), hasToken(":a", CLOJURE_KEYWORD));
        assertThat(lexer.lex(":aa"), hasToken(":aa", CLOJURE_KEYWORD));
        assertThat(lexer.lex(":aa/b"), hasToken(":aa/b", CLOJURE_KEYWORD));
        assertThat(lexer.lex(":aa/bs\"b\""), hasToken(":aa/bs", CLOJURE_KEYWORD));
        assertThat(lexer.lex("::a/b"), hasToken("::a/b", CLOJURE_KEYWORD));
        assertThat(lexer.lex(":a\"a\""), hasToken(":a", CLOJURE_KEYWORD));
    }


    @Test
    public void lexIntegers() {
        assertThat(lexer.lex("0"), hasToken("0", INTEGER));
        assertThat(lexer.lex("000"), hasToken("000", INTEGER));
        assertThat(lexer.lex("1234"), hasToken("1234", INTEGER));
    }


    @Test
    public void lexStrings() {
        assertThat(lexer.lex("\"MIIDBTCCAe2gAwIBAgIQev76BWqjWZxChmKkGqoAfDANBgkqhkiG9w0BAQsFADAtMSswKQYDVQQDEyJhY2NvdW50cy5hY2Nlc3Njb250cm9sLndpbmRvd3MubmV0MB4XDTE4MDIxODAwMDAwMFoXDTIwMDIxOTAwMDAwMFowLTErMCkGA1UEAxMiYWNjb3VudHMuYWNjZXNzY29udHJvbC53aW5kb3dzLm5ldDCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBAMgmGiRfLh6Fdi99XI2VA3XKHStWNRLEy5Aw/gxFxchnh2kPdk/bejFOs2swcx7yUWqxujjCNRsLBcWfaKUlTnrkY7i9x9noZlMrijgJy/Lk+HH5HX24PQCDf+twjnHHxZ9G6/8VLM2e5ZBeZm+t7M3vhuumEHG3UwloLF6cUeuPdW+exnOB1U1fHBIFOG8ns4SSIoq6zw5rdt0CSI6+l7b1DEjVvPLtJF+zyjlJ1Qp7NgBvAwdiPiRMU4l8IRVbuSVKoKYJoyJ4L3eXsjczoBSTJ6VjV2mygz96DC70MY3avccFrk7tCEC6ZlMRBfY1XPLyldT7tsR3EuzjecSa1M8CAwEAAaMhMB8wHQYDVR0OBBYEFIks1srixjpSLXeiR8zES5cTY6fBMA0GCSqGSIb3DQEBCwUAA4IBAQCKthfK4C31DMuDyQZVS3F7+4Evld3hjiwqu2uGDK+qFZas/D/eDunxsFpiwqC01RIMFFN8yvmMjHphLHiBHWxcBTS+tm7AhmAvWMdxO5lzJLS+UWAyPF5ICROe8Mu9iNJiO5JlCo0Wpui9RbB1C81Xhax1gWHK245ESL6k7YWvyMYWrGqr1NuQcNS0B/AIT1Nsj1WY7efMJQOmnMHkPUTWryVZlthijYyd7P2Gz6rY5a81DAFqhDNJl2pGIAE6HWtSzeUEh3jCsHEkoglKfm4VrGJEuXcALmfCMbdfTvtu4rlsaP2hQad+MG/KJFlenoTK34EMHeBPDCpqNDz8UVNk\""), hasToken(STRING));
        assertThat(lexer.lex(":a\"a\""), hasToken("\"a\"", STRING));

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
