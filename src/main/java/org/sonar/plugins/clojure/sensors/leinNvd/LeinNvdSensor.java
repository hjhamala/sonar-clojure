package org.sonar.plugins.clojure.sensors.leinNvd;


import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.batch.sensor.issue.NewIssue;
import org.sonar.api.batch.sensor.issue.NewIssueLocation;
import org.sonar.api.rule.RuleKey;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.plugins.clojure.language.ClojureLanguage;
import org.sonar.plugins.clojure.rules.ClojureLintRulesDefinition;
import org.sonar.plugins.clojure.sensors.CommandRunner;
import org.sonar.plugins.clojure.sensors.CommandStreamConsumer;
import org.sonar.plugins.clojure.sensors.cloverage.CloverageMetricParser;
import org.sonar.plugins.clojure.sensors.cloverage.CoverageReport;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class LeinNvdSensor implements Sensor {

    private static final Logger LOG = Loggers.get(LeinNvdSensor.class);

    private static final String COMMAND = "nvd";
    private static final String LEIN_COMMAND = "lein";

    private CommandRunner commandRunner;

    public LeinNvdSensor(CommandRunner commandRunner) {
        this.commandRunner = commandRunner;
    }

    static String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }


    private InputFile getFile(String filePath, FileSystem fileSystem) {
        return fileSystem.inputFile(
                fileSystem.predicates().and(
                        fileSystem.predicates().hasRelativePath(filePath),
                        fileSystem.predicates().hasType(InputFile.Type.MAIN)));
    }


    @Override
    public void describe(SensorDescriptor descriptor) {
        descriptor.name("SonarClojureLeinNvd")
                .onlyOnLanguage(ClojureLanguage.KEY)
                .global();
    }

    @Override
    public void execute(SensorContext context) {
        LOG.info("Running Lein NVD");
        this.commandRunner.run(LEIN_COMMAND, COMMAND, "check");

        try {
            String json = readFile("target/nvd/dependency-check-report.json", Charset.forName("UTF-8"));
            List<Vulnerability> vulnerabilities = LeinNvdParser.parseJson(json);
            saveVulnerabilities(vulnerabilities, context);
        } catch (IOException e) {
            LOG.warn("Lein NVD dependency report does not exists or cannot be read");

        }
    }

    private void saveVulnerabilities(List<Vulnerability> vulnerabilities, SensorContext context) {
        InputFile file = getFile("project.clj", context.fileSystem());

        for (Vulnerability v :
                vulnerabilities) {
            LOG.debug("Processing vulnerability: " +v.toString());
            RuleKey ruleKey = RuleKey.of(ClojureLintRulesDefinition.REPOSITORY_KEY, "nvd-" + v.getSeverity().toLowerCase());
            NewIssue newIssue = context.newIssue().forRule(ruleKey);
            NewIssueLocation primaryLocation = newIssue
                    .newLocation()
                    .on(file)
                    .message(v.getName()
                            + ";" + v.getCwe()
                            + ";" + v.getFileName())
                    .at(file.selectLine(1));
            newIssue.at(primaryLocation);
            newIssue.save();
        }
    }

}