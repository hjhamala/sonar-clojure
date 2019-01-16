package org.sonar.plugins.clojure.sensors.language;


import com.sonar.sslr.api.Grammar;
import com.sonar.sslr.impl.Parser;
import com.sonar.sslr.impl.ast.AstWalker;
import org.sonar.api.batch.fs.FilePredicate;
import org.sonar.api.batch.fs.FilePredicates;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.measures.Metric;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.plugins.clojure.language.*;
import org.sonar.plugins.clojure.sensors.CommandRunner;

import java.io.IOException;
import java.io.Serializable;
import java.util.Set;

import static org.sonar.api.measures.CoreMetrics.COMMENT_LINES;
import static org.sonar.api.measures.CoreMetrics.NCLOC;
import static org.sonar.plugins.clojure.language.ClojureGrammar.CLOJURE;

public class ClojureLanguageSensor implements Sensor {

    private static final Logger LOG = Loggers.get(ClojureLanguageSensor.class);

    private CommandRunner commandRunner;

    private  static  <T extends Serializable> void saveMeasure (SensorContext context, T value, Metric<T> metric, InputFile inputFile){
        context.<T>newMeasure()
                .withValue(value)
                .forMetric(metric)
                .on(inputFile)
                .save();
    }

    public void saveMetrics(SensorContext context, InputFile inputFile){
        final Parser<Grammar> p = ClojureParser.create();
        final Grammar g = p.getGrammar();
        p.setRootRule(g.rule(CLOJURE));

        try {
            LinesOfCodeVisitor codeVisitor = new LinesOfCodeVisitor();
            CommentLineCalculator calculator = new CommentLineCalculator(inputFile.contents());
            HighlighterVisitor highlight = new HighlighterVisitor(context,inputFile);

            AstWalker walker = new AstWalker(codeVisitor,highlight);

            walker.walkAndVisit(p.parse(inputFile.contents()));
            Set<Integer> linesWithCode = codeVisitor.getLinesOfCode();
            saveMeasure(context, linesWithCode.size(),NCLOC, inputFile);
            saveMeasure(context, calculator.calculateLinesWithOnlyComments(linesWithCode).size(),COMMENT_LINES, inputFile);

        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    @Override
    public void describe(SensorDescriptor descriptor) {
        descriptor.name("SonarClojureTokenizerAndHighLighter")
                .onlyOnLanguage(ClojureLanguage.KEY)
                .global();
    }
    @Override
    public void execute(SensorContext context) {
        LOG.info("Running tokenizer and highlighter");
        FilePredicates predicates = context.fileSystem().predicates();
        FilePredicate clojure = predicates.hasLanguage(ClojureLanguage.KEY);
        FilePredicate main = predicates.hasType(InputFile.Type.MAIN);

        Iterable<InputFile> sources = context.fileSystem().inputFiles(predicates.and(clojure,main));
        for (InputFile source:sources) {
            LOG.info(source.toString());
            saveMetrics(context,source);
        }


    }


}