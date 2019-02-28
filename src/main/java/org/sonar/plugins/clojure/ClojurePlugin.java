package org.sonar.plugins.clojure;

import org.sonar.api.Plugin;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.plugins.clojure.language.ClojureLanguage;
import org.sonar.plugins.clojure.language.ClojureSonarWayProfile;
import org.sonar.plugins.clojure.rules.ClojureLintRulesDefinition;
import org.sonar.plugins.clojure.sensors.cloverage.CloverageSensor;
import org.sonar.plugins.clojure.sensors.CommandRunner;
import org.sonar.plugins.clojure.sensors.ancient.AncientSensor;
import org.sonar.plugins.clojure.sensors.eastwood.EastwoodSensor;
import org.sonar.plugins.clojure.sensors.language.ClojureLanguageSensor;
import org.sonar.plugins.clojure.sensors.leinnvd.LeinNvdSensor;
import org.sonar.plugins.clojure.settings.ClojureProperties;

public class ClojurePlugin implements Plugin {

    private static final Logger LOG = Loggers.get(SonarProjectProperties.class);
    private final SonarProjectProperties props = new SonarProjectProperties();

    private void addExtensionIfNotDisabled(Class extension, Context context){
        if (!props.isSensorDisabled(extension)) {
            context.addExtension(extension);
        }
    }

    public void define(Context context) {
        context.addExtension(ClojureProperties.getProperties());
        context.addExtension(ClojureLanguage.class);
        context.addExtension(ClojureSonarWayProfile.class);
        context.addExtension(ClojureLintRulesDefinition.class);
        context.addExtension(CommandRunner.class);
        context.addExtension(EastwoodSensor.class);
        context.addExtension(AncientSensor.class);
        context.addExtension(CloverageSensor.class);
        context.addExtension(LeinNvdSensor.class);
        context.addExtension(ClojureLanguageSensor.class);
    }
}