/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.karafsandbox.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.stream.Collectors;

import org.hibernate.karafsandbox.core.internal.configuration.LibraryConfigurationLoader;
import org.hibernate.karafsandbox.core.internal.configuration.model.LibraryConfiguration;
import org.hibernate.karafsandbox.core.internal.configuration.model.LibraryConfigurationProperty;

public class LibraryBootstrapper {

	private static final String CONFIGURATION_PATH = "configuration.xml";

	public LibraryInstance bootstrap(ClassLoader classLoader) throws IOException {
		LibraryConfigurationLoader loader = new LibraryConfigurationLoader();
		try ( InputStream inputStream = classLoader.getResourceAsStream( CONFIGURATION_PATH ) ) {
			if ( inputStream == null ) {
				throw new IOException( "Could not find '" + CONFIGURATION_PATH + "'" );
			}
			LibraryConfiguration configuration = loader.load( inputStream );

			return new LibraryInstance(
					configuration.getName(),
					configuration.getProperties().stream()
							.collect( Collectors.toMap( LibraryConfigurationProperty::getName, LibraryConfigurationProperty::getValue ) )
			);
		}
	}

}
