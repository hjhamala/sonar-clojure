package org.sonar.plugins.clojure.sensors.language;

import com.sonar.sslr.api.*;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.TextPointer;
import org.sonar.api.batch.fs.TextRange;
import org.sonar.api.batch.fs.internal.DefaultTextPointer;
import org.sonar.api.batch.fs.internal.DefaultTextRange;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.highlighting.NewHighlighting;
import org.sonar.api.batch.sensor.highlighting.TypeOfText;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.plugins.clojure.language.ClojureGrammar;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

import static org.sonar.api.batch.sensor.highlighting.TypeOfText.*;
import static org.sonar.plugins.clojure.language.ClojureLexer.Punctuators.PAREN_L;

public class HighlighterVisitor implements AstVisitor {

    private static final Logger LOG = Loggers.get(HighlighterVisitor.class);
    private final SensorContext context;
    private final InputFile input;
    private boolean hasHightlight = false;
    private NewHighlighting highlighting;

    public HighlighterVisitor(SensorContext context, InputFile input) {
        this.context = context;

        this.input = input;
        this.highlighting = context.newHighlighting().onFile(input);
    }

    @Override
    public List<AstNodeType> getAstNodeTypesToVisit() {
        final List<AstNodeType> nodeTypes = new ArrayList<>();
        nodeTypes.add(ClojureGrammar.VALUE);
        return nodeTypes;
    }


    @Override
    public void visitFile(@Nullable AstNode astNode) {

    }

    @Override
    public void leaveFile(@Nullable AstNode astNode) {
        if (hasHightlight) {
            highlighting.save();
        }
    }


    public TextRange calculateRange(Token t) {
        String[] lines = t.getValue().split("\\r?\\n");
        TextPointer start = new DefaultTextPointer(t.getLine(), t.getColumn());
        TextPointer end;
        int endline = t.getLine() + lines.length - 1;

        if (endline == t.getLine()) {
            end = new DefaultTextPointer(t.getLine(), t.getColumn() + t.getValue().length());
        } else {
            end = new DefaultTextPointer(endline, lines[lines.length - 1].length());
        }

        return new DefaultTextRange(start, end);
    }

    @Override
    public void visitNode(AstNode astNode) {
        if (astNode.hasToken()) {
            Token t = astNode.getToken();
            TypeOfText type;
            switch (t.getType().getName()) {
                case "STRING":
                    hasHightlight = true;
                    type = STRING;

                    highlighting.highlight(calculateRange(t), type);
                    break;

                case "CHAR":
                    hasHightlight = true;
                    type = STRING;

                    highlighting.highlight(calculateRange(t), type);
                    break;

                case "CLOJURE_KEYWORD":
                    hasHightlight = true;
                    type = KEYWORD_LIGHT;

                    highlighting.highlight(calculateRange(t), type);
                    break;
                case "IDENTIFIER":
                    if (astNode.getPreviousAstNode().getToken().getType() == PAREN_L) {
                        hasHightlight = true;
                        type = KEYWORD;
                        highlighting.highlight(calculateRange(t), type);
                    }
                    break;
            }
        }

    }

    @Override
    public void leaveNode(AstNode astNode) {

    }
}
