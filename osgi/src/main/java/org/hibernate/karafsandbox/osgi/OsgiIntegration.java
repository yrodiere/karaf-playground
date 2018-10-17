/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.karafsandbox.osgi;

import java.io.IOException;

import org.hibernate.karafsandbox.core.LibraryBootstrapper;
import org.hibernate.karafsandbox.core.LibraryInstance;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

public class OsgiIntegration {

	private final LibraryBootstrapper bootstrapper = new LibraryBootstrapper();

	public LibraryInstance bootstrapWithoutOsgiClassLoader() throws IOException {
		return bootstrapper.bootstrap( getClass().getClassLoader() );
	}

	public LibraryInstance bootstrapWithOsgiClassLoader(Bundle requestingBundle) throws IOException {
		final OsgiClassLoader osgiClassLoader = new OsgiClassLoader();
		// First, add the client bundle that's requesting the OSGi services.
		osgiClassLoader.addBundle( requestingBundle );
		// Then, automatically add the core bundle
		osgiClassLoader.addBundle( FrameworkUtil.getBundle( LibraryInstance.class ) );

		return bootstrapper.bootstrap( osgiClassLoader );
	}

}
