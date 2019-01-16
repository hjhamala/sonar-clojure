package org.sonar.plugins.clojure.sensors.language;


import com.sonar.sslr.api.Grammar;
import com.sonar.sslr.impl.Parser;
import com.sonar.sslr.impl.ast.AstWalker;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.sonar.api.batch.fs.FilePredicate;
import org.sonar.api.batch.fs.FilePredicates;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.batch.sensor.internal.DefaultSensorDescriptor;
import org.sonar.api.measures.Metric;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.plugins.clojure.language.ClojureLanguage;
import org.sonar.plugins.clojure.language.ClojureParser;
import org.sonar.plugins.clojure.sensors.CommandRunner;
import org.sonar.plugins.clojure.sensors.eastwood.EastwoodSensor;

import java.io.IOException;
import java.io.Serializable;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.sonar.api.measures.CoreMetrics.COMMENT_LINES;
import static org.sonar.api.measures.CoreMetrics.NCLOC;
import static org.sonar.plugins.clojure.language.ClojureGrammar.CLOJURE;

public class ClojureLanguageSensorTest {

    private static final Logger LOG = Loggers.get(ClojureLanguageSensorTest.class);

    @Mock
    private CommandRunner commandRunner;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void testSensorDescriptor() {
        DefaultSensorDescriptor descriptor = new DefaultSensorDescriptor();
        new ClojureLanguageSensor().describe(descriptor);
        assertThat(descriptor.name(), is("SonarClojureTokenizerAndHighLighter"));
        assertTrue(descriptor.languages().contains("clj"));
        assertThat(descriptor.languages().size(), is(1));
    }
   @Test
    public void testExecute(){

   }


}