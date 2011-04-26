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
package org.codehaus.cargo.cli;

import java.util.Map;

import org.codehaus.cargo.container.Container;
import org.codehaus.cargo.container.jetty.Jetty4xEmbeddedContainer;
import org.codehaus.cargo.generic.DefaultContainerFactory;

import junit.framework.TestCase;

/**
 * CLIConfigurationBindingTest.
 * <p/>
 *
 * @version $Id: CargoTask.java 391 2005-07-04 13:07:42Z vmassol $
 */
public class CLIConfigurationBindingTest extends TestCase {
    public void testNakedJettyContainer() {
        Container jettyContainer = new DefaultContainerFactory().createContainer(Jetty4xEmbeddedContainer.ID);
        Map cliProperties = CLIConfigurationBinding.createCLIProperties(jettyContainer);

        Container newJettyContainer = CLIConfigurationBinding.createConfiguredContainer(cliProperties);
        assertEquals(jettyContainer.getId(), newJettyContainer.getId());
        assertEquals(jettyContainer.getHomeDir(), newJettyContainer.getHomeDir());
        assertEquals(jettyContainer.getName(), newJettyContainer.getName());
        assertEquals(jettyContainer.getOutput(), newJettyContainer.getOutput());
        assertEquals(jettyContainer.getSystemProperties(), newJettyContainer.getSystemProperties());
        assertEquals(jettyContainer.getTimeout(), newJettyContainer.getTimeout());
        assertEquals(jettyContainer.isAppend(), newJettyContainer.isAppend());
        assertEquals(jettyContainer.getExtraClasspath(), newJettyContainer.getExtraClasspath());
    }

    public void testWriteReadJettyContainer() {
        Container jettyContainer = new DefaultContainerFactory().createContainer(Jetty4xEmbeddedContainer.ID);
        Map cliProperties = CLIConfigurationBinding.createCLIProperties(jettyContainer);
        Container newJettyContainer = CLIConfigurationBinding.createConfiguredContainer(cliProperties);
        Map newCLIProperties = CLIConfigurationBinding.createCLIProperties(newJettyContainer);
        assertEquals(cliProperties, newCLIProperties);
    }

    private static boolean assertEquals(String[] s1, String[] s2) {
        boolean equal = false;
        if (s1 == s2) {
            equal = true;
        } else if (s1 == null) {
            equal = s2.length == 0;
        } else if (s2 == null) {
            equal = s1.length == 0;
        } else if (s1.length == s2.length) {
            for (int i = 0; i < s1.length; i++) {
                if (!s1[i].equals(s2[i])) {
                    return false;
                }
            }
            equal = true;
        }
        return equal;
    }

}
