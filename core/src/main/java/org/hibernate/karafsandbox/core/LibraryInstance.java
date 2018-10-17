/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.karafsandbox.core;

import java.util.Map;

public class LibraryInstance {

	private final String name;
	private final Map<String, String> properties;

	LibraryInstance(String name, Map<String, String> properties) {
		this.name = name;
		this.properties = properties;
	}

	public String getName() {
		return name;
	}

	public String getProperty(String name) {
		return properties.get( name );
	}

}
