/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.karafsandbox.core.internal.configuration;

import java.io.IOException;
import java.io.InputStream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.hibernate.karafsandbox.core.internal.configuration.model.LibraryConfiguration;

public class LibraryConfigurationLoader {

	private final JAXBContext context;

	public LibraryConfigurationLoader() throws IOException {
		try {
			context = JAXBContext.newInstance( LibraryConfiguration.class );
		}
		catch (JAXBException e) {
			throw unexpectedException( e );
		}
	}

	public LibraryConfiguration load(InputStream stream) throws IOException {
		try {
			Unmarshaller um = context.createUnmarshaller();
			return (LibraryConfiguration) um.unmarshal( stream );
		}
		catch (JAXBException e) {
			throw unexpectedException( e );
		}
	}

	private static IOException unexpectedException(JAXBException e) {
		return new IOException( "Unexpected exception: " + e.getMessage(), e );
	}

}
