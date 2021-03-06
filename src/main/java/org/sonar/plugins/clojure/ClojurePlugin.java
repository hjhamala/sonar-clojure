package org.sonar.plugins.clojure;


import org.sonar.api.Plugin;
import org.sonar.plugins.clojure.language.ClojureLanguage;
import org.sonar.plugins.clojure.language.ClojureSensor;
import org.sonar.plugins.clojure.language.ClojureSonarWayProfile;
import org.sonar.plugins.clojure.rules.ClojureLintRulesDefinition;
import org.sonar.plugins.clojure.sensors.ancient.AncientSensor;
import org.sonar.plugins.clojure.sensors.cloverage.CloverageSensor;
import org.sonar.plugins.clojure.sensors.eastwood.EastwoodSensor;
import org.sonar.plugins.clojure.sensors.CommandRunner;
import org.sonar.plugins.clojure.sensors.kibit.KibitSensor;
import org.sonar.plugins.clojure.sensors.leinNvd.LeinNvdSensor;
import org.sonar.plugins.clojure.settings.ClojureProperties;
import org.sonar.plugins.clojure.sensors.eastwood.NativeClojureSensor;

public class ClojurePlugin implements Plugin {

    public void define(Context context) {
        SonarProjectProperties props = new SonarProjectProperties();
        context.addExtension(ClojureLanguage.class);
        context.addExtension(ClojureSonarWayProfile.class);
        context.addExtension(ClojureLintRulesDefinition.class);
        context.addExtension(CommandRunner.class);

        if (!props.isEastwoodDisabled()){
            context.addExtension(EastwoodSensor.class);
        }
        if (!props.isKibitDisabled()){
            context.addExtension(KibitSensor.class);
        }
        if (!props.isCloverageDisabled()){
            context.addExtension(CloverageSensor.class);
        }
        if (!props.isLeinNVDDisabled()){
            context.addExtension(LeinNvdSensor.class);
        }

        if (!props.isAncientDisabled()){
            context.addExtension(AncientSensor.class);
        }
        context.addExtension(ClojureProperties.getProperties());
        context.addExtension(ClojureSensor.class);
    }
}