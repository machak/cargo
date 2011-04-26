/*
 * ========================================================================
 *
 * Copyright 2004-2005 Vincent Massol.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * ========================================================================
 */
package org.codehaus.cargo.intellijidea;

import com.intellij.j2ee.appServerIntegrations.ApplicationServerPersistentData;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.WriteExternalException;

import org.jdom.Element;


/**
 * Persistent configuration data. Equivalent to container home dir
 * and properties.
 *
 * @version $Id: CargoTask.java 391 2005-07-04 13:07:42Z vmassol $
 */
public class CargoContainerModel implements ApplicationServerPersistentData {
    /**
     * Home dir.
     */
    private String homeDir;

    /**
     * Get home dir.
     *
     * @return home dir
     */
    public String getHomeDir() {
        return homeDir;
    }

    /**
     * Sets homeDir dir.
     *
     * @param homeDir homeDir dir
     */
    public void setHomeDir(String homeDir) {
        this.homeDir = homeDir;
    }

    /**
     * Reads from persistent storage.
     *
     * @param parentNode parent node
     * @throws InvalidDataException if the data is not valid
     */
    public void readExternal(Element parentNode) throws InvalidDataException {
        homeDir = getElement("homeDir", parentNode);
    }

    /**
     * Helper method to get an element.
     *
     * @param name       name
     * @param parentNode parent node
     * @return element value (text)
     */
    private String getElement(final String name, Element parentNode) {
        String res = null;
        Element homeDirElement = parentNode.getChild(name);
        if (homeDirElement != null) {
            res = homeDirElement.getText();
        }
        return res;
    }

    /**
     * Writes to persistent storage.
     *
     * @param parentNode parent node
     * @throws WriteExternalException if the data cannot be written
     */
    public void writeExternal(Element parentNode) throws WriteExternalException {
        addElement("homeDir", homeDir, parentNode);
    }

    /**
     * Helpermethod to add an Element.
     *
     * @param name       name
     * @param value      value
     * @param parentNode parent node
     */
    private void addElement(final String name, final String value, Element parentNode) {
        Element homeDirElement = new Element(name);
        homeDirElement.setText(value);
        parentNode.addContent(homeDirElement);
    }
}
