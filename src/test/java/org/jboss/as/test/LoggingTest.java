package org.jboss.as.test;

import java.io.File;
import java.lang.reflect.Field;

import junit.framework.Assert;

import org.hibernate.event.internal.AbstractFlushingEventListener;
import org.hibernate.internal.CoreMessageLogger;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * This shows the minimal deps combo, which leads to debug-logging enabled 
 * for AbstractFlushingEventListener despite the root-logger is set 
 * to WARN and no intermediate categories are overridden.
 * 
 * @throws Exception
 */
@RunWith(Arquillian.class)
public class LoggingTest {
	
	@Deployment
	public static WebArchive createDeployment() {
		WebArchive archive = ShrinkWrap.create(WebArchive.class);
		
		archive.addAsWebResource("META-INF/jboss-deployment-structure.xml", "META-INF/jboss-deployment-structure.xml");

		File[] libs = Maven.resolver().loadPomFromFile("pom.xml").resolve(
							"org.jboss.seam:jboss-seam:2.2.2.Final",
							"com.sun.facelets:jsf-facelets:1.1.15.B1"
						).withoutTransitivity().asFile();

		archive.addAsLibraries(libs);
		return archive;
	}

	@Test
	public void test() throws Exception {
    	Field field = AbstractFlushingEventListener.class.getDeclaredField("LOG");
    	field.setAccessible(true);
    	CoreMessageLogger LOG = (CoreMessageLogger) field.get(null);
    	Assert.assertFalse(LOG.isDebugEnabled());
	}

}
