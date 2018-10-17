/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.karafsandbox.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.junit.Test;

public class LibraryBootstrapperTest {

	@Test
	public void test() throws IOException {
		LibraryInstance instance = new LibraryBootstrapper().bootstrap( getClass().getClassLoader() );
		assertNotNull( instance );
		assertEquals( "My configuration", instance.getName() );
		assertEquals( "core value 1", instance.getProperty( "property1" ) );
		assertEquals( "core value 2", instance.getProperty( "property2" ) );
	}

}
