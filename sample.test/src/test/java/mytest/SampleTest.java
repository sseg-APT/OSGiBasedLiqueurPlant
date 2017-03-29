package mytest;

/**
 * Created by bocha on 30/3/2017.
 *
 */

import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.Configuration;
import org.ops4j.pax.exam.junit.ExamReactorStrategy;
import org.ops4j.pax.exam.junit.JUnit4TestRunner;
import org.ops4j.pax.exam.spi.reactors.AllConfinedStagedReactorFactory;
import org.osgi.framework.BundleContext;

import javax.inject.Inject;

import static org.ops4j.pax.exam.CoreOptions.*;


@RunWith(JUnit4TestRunner.class)
@ExamReactorStrategy(AllConfinedStagedReactorFactory.class)
public class SampleTest {

    @Inject
    BundleContext bundleContext;


    @Configuration
    public Option[] config() {

        return options(
                cleanCaches(),
                mavenBundle("org.apache.felix", "org.apache.felix.framework"),
                junitBundles()
        );
    }

    @Test
    public void getHelloService() {
        System.out.println("********** Hello Test *************");
    }
}
