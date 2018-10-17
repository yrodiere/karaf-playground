/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.karafsandbox.core.internal.configuration.model;

import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = { "name", "value" })
public class LibraryConfigurationProperty {
	private String name;
	private String value;

	public LibraryConfigurationProperty() {
	}

	public LibraryConfigurationProperty(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
