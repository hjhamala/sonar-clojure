package org.sonar.plugins.clojure.language;

import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.Grammar;
import com.sonar.sslr.impl.Parser;
import org.sonar.api.internal.apachecommons.io.FileUtils;

import java.io.File;

public class ClojureParser {

    private static final Parser<Grammar> P = ClojureParser.create();

    private ClojureParser() {
    }

    public static Parser<Grammar> create() {
        return Parser.builder(ClojureGrammar.create()).withLexer(ClojureLexer.create()).build();
    }

    public static AstNode parseFile(String filePath) {
        File file = FileUtils.toFile(ClojureParser.class.getResource(filePath));
        if (file == null || !file.exists()) {
            throw new AssertionError("The file \"" + filePath + "\" does not exist.");
        }

        return P.parse(file);
    }

    public static AstNode parseString(String source) {
        return P.parse(source);
    }
}
