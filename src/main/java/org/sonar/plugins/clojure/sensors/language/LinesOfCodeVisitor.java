package org.sonar.plugins.clojure.sensors.language;

import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.AstNodeType;
import com.sonar.sslr.api.AstVisitor;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.plugins.clojure.language.ClojureGrammar;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LinesOfCodeVisitor implements AstVisitor {

    private static final Logger LOG = Loggers.get(LinesOfCodeVisitor.class);
    private Set<Integer> linesOfCode = new HashSet<>();

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

    }

    public Set<Integer> getLinesOfCode() {
        return linesOfCode;
    }

    @Override
    public void visitNode(AstNode astNode) {
        linesOfCode.add(astNode.getTokenLine());
    }

    @Override
    public void leaveNode(AstNode astNode) {

    }
}
