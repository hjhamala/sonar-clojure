package org.sonar.plugins.clojure.sensors.language;

import com.sonar.sslr.api.Token;
import com.sonar.sslr.api.Trivia;
import org.sonar.plugins.clojure.language.ClojureLexer;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CommentLineCalculator {
    private  List<Token> tokens;

    public CommentLineCalculator(String contents){
        this.tokens = ClojureLexer.create().lex(contents);
    }

    public Set<Integer> calculateCommentLines(){
        Set<Integer> lines = new HashSet<>();
        for (Token t: tokens){
            if (t.hasTrivia()){
                for (Trivia trivia: t.getTrivia()){
                    if (trivia.isComment()){
                        lines.add(trivia.getToken().getLine());
                    };
                }
            };
        }
        return lines;
    }

    public Set<Integer> calculateLinesWithOnlyComments( Set<Integer> linesWithCode){
        Set<Integer> commentLines = calculateCommentLines();
        commentLines.removeAll(linesWithCode);
        return commentLines;

    }
}
