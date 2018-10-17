/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.karafsandbox.osgi.testpackage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.ops4j.pax.exam.CoreOptions.options;
import static org.ops4j.pax.exam.CoreOptions.when;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.configureConsole;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.debugConfiguration;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.editConfigurationFileExtend;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.editConfigurationFilePut;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.features;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.karafDistributionConfiguration;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.keepRuntimeFolder;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.logLevel;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Locale;
import java.util.Properties;
import javax.inject.Inject;

import org.hibernate.karafsandbox.core.LibraryInstance;
import org.hibernate.karafsandbox.osgi.OsgiIntegration;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import org.apache.karaf.features.BootFinished;
import org.apache.karaf.features.FeaturesService;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.karaf.options.LogLevelOption;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;

/**
 * Tests for hibernate-osgi running within a Karaf container via PaxExam.
 *
 * @author Steve Ebersole
 * @author Brett Meyer
 */
@RunWith( PaxExam.class )
@ExamReactorStrategy( PerClass.class )
public class OsgiIntegrationTest {

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Prepare the Karaf container

	@Configuration
	public Option[] config() throws Exception {
		final Properties paxExamEnvironment = loadPaxExamEnvironmentProperties();

		boolean debug = Boolean.getBoolean( "osgi.debug" );

		return options(
				when( debug ).useOptions( debugConfiguration( "5005", true ) ),
				karafDistributionConfiguration()
						.frameworkUrl(
								paxExamEnvironment.getProperty( "org.ops4j.pax.exam.container.karaf.distroUrl" )
						)
						.karafVersion( paxExamEnvironment.getProperty( "org.ops4j.pax.exam.container.karaf.version" ) )
						.name( "Apache Karaf" )
						.unpackDirectory( new File(
								paxExamEnvironment.getProperty( "org.ops4j.pax.exam.container.karaf.unpackDir" )
						) )
						.useDeployFolder( false ),
				editConfigurationFileExtend(
						"etc/org.ops4j.pax.url.mvn.cfg",
						"org.ops4j.pax.url.mvn.repositories",
						"https://repository.jboss.org/nexus/content/groups/public/"
				),
				configureConsole().ignoreLocalConsole().ignoreRemoteShell(),
				when( debug ).useOptions( keepRuntimeFolder() ),
				logLevel( LogLevelOption.LogLevel.INFO ),
				// also log to the console, so that the logs are writtten to the test output file
				editConfigurationFilePut(
						"etc/org.ops4j.pax.logging.cfg",
						"log4j2.rootLogger.appenderRef.Console.filter.threshold.level",
						"TRACE" // Means "whatever the root logger level is"
				),

				features( featureXmlUrl( paxExamEnvironment ), "karafsandbox-core" )
		);
	}

	private static Properties loadPaxExamEnvironmentProperties() throws IOException {
		Properties props = new Properties();
		props.load( OsgiIntegrationTest.class.getResourceAsStream( "/pax-exam-environment.properties" ) );
		return props;
	}

	private static String featureXmlUrl(Properties paxExamEnvironment) throws MalformedURLException {
		return new File( paxExamEnvironment.getProperty( "org.hibernate.karaf-sandbox.test.karafFeatureFile" ) ).toURI().toURL()
				.toExternalForm();
	}


	@BeforeClass
	public static void setLocaleToEnglish() {
		Locale.setDefault( Locale.ENGLISH );
	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// The tests

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Inject
	private FeaturesService featuresService;

	@Inject
	private BootFinished bootFinished;

	@Inject
	private BundleContext bundleContext;

	@Test
	public void testActivation() throws Exception {
		assertTrue( featuresService.isInstalled( featuresService.getFeature( "karafsandbox-core" ) ) );
		assertActiveBundle( "org.hibernate.karafsandbox.core" );
	}

	@Test
	public void testWithoutOsgiClassLoader() throws IOException {
		OsgiIntegration integration = new OsgiIntegration();

		expectedException.expect( IOException.class );
		expectedException.expectMessage( "Could not find 'configuration.xml'" );
		integration.bootstrapWithoutOsgiClassLoader();
	}

	@Test
	public void testWithOsgiClassLoader() throws IOException {
		OsgiIntegration integration = new OsgiIntegration();
		LibraryInstance instance = integration.bootstrapWithOsgiClassLoader( bundleContext.getBundle() );
		assertNotNull( instance );
		assertEquals( "My configuration", instance.getName() );
		assertEquals( "osgi value 1", instance.getProperty( "property1" ) );
		assertEquals( "osgi value 2", instance.getProperty( "property2" ) );
	}

	private void assertActiveBundle(String symbolicName) {
		for ( Bundle bundle : bundleContext.getBundles() ) {
			if ( bundle.getSymbolicName().equals( symbolicName ) ) {
				Assert.assertEquals(
						symbolicName + " was found, but not in an ACTIVE state.", Bundle.ACTIVE, bundle.getState() );
				return;
			}
		}
		Assert.fail( "Could not find bundle: " + symbolicName );
	}
}