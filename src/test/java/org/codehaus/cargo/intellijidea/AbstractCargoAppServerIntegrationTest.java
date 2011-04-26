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

import com.intellij.openapi.module.ModuleType;

import org.codehaus.cargo.container.jetty.Jetty4xEmbeddedContainer;

import junit.framework.TestCase;

/**
 * AbstractCargoAppServerIntegrationTest.
 *
 * @version $Id: CargoTaskTest.java 414 2005-07-20 09:34:44Z vmassol $
 */
public class AbstractCargoAppServerIntegrationTest extends TestCase {
    protected void setUp() throws Exception {
        if (ModuleType.WEB == null) {
            ModuleType.WEB = MockModuleType.WEB;
        }
    }

    public void testSupportedModules() {
        MockJettyIntegration mockJettyIntegration = new MockJettyIntegration();
        ModuleType[] jettyModuleTypes = mockJettyIntegration.getSupportedModuleTypes();
        assertNotNull(jettyModuleTypes);
        assertEquals(1, jettyModuleTypes.length);
        assertEquals(ModuleType.WEB, jettyModuleTypes[0]);

        MockWebLogicIntegration mockWebLogicIntegration = new MockWebLogicIntegration();
        ModuleType[] webLogicModuleTypes = mockWebLogicIntegration.getSupportedModuleTypes();
        assertNotNull(webLogicModuleTypes);
        assertEquals(2, webLogicModuleTypes.length);
        assertTrue((ModuleType.WEB == webLogicModuleTypes[0]
                && ModuleType.J2EE_APPLICATION == webLogicModuleTypes[1])
                || (ModuleType.J2EE_APPLICATION == webLogicModuleTypes[0]
                && ModuleType.WEB == webLogicModuleTypes[1]));
    }

    public void testGetContainerId() {
        MockJettyIntegration mockJettyIntegration = new MockJettyIntegration();
        assertEquals(Jetty4xEmbeddedContainer.ID, mockJettyIntegration.getContainerId());
    }

    public void testGetIcon() {
        MockJettyIntegration mockJettyIntegration = new MockJettyIntegration();
        assertNotNull(mockJettyIntegration.getIcon());
    }

    public void testGetApplicationServerHelper() {
        MockJettyIntegration mockJettyIntegration = new MockJettyIntegration();
        assertNotNull(mockJettyIntegration.getApplicationServerHelper());
    }
}
