/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.karafsandbox.core.internal.configuration.model;

import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "configuration")
@XmlType(propOrder = { "name", "properties" })
public class LibraryConfiguration {
    private String name;
	private List<LibraryConfigurationProperty> properties;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlElementWrapper(name = "properties")
	@XmlElement(name = "property")
	public List<LibraryConfigurationProperty> getProperties() {
		return properties;
	}

	public void setProperties(List<LibraryConfigurationProperty> properties) {
		this.properties = properties;
	}
}
