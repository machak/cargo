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

import java.io.File;
import java.io.IOException;

import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.options.ConfigurationException;

import junit.framework.TestCase;

/**
 * CargoContainerEditorTest.
 *
 * @version $Id: CargoTaskTest.java 414 2005-07-20 09:34:44Z vmassol $
 */
public class CargoContainerEditorTest extends TestCase {
    private CargoContainerEditor cargoContainerEditor;

    protected void setUp() throws Exception {
        if (ModuleType.WEB == null) {
            ModuleType.WEB = MockModuleType.WEB;
        }
        cargoContainerEditor = new CargoContainerEditor();
    }

    public void testNullHomeDir() {
        final CargoContainerModel cargoContainerModel = new CargoContainerModel();
        cargoContainerModel.setHomeDir(null);
        cargoContainerEditor.resetEditorFrom(cargoContainerModel);
        try {
            cargoContainerEditor.applyEditorTo(cargoContainerModel);
            fail("Expected ConfigurationException as the home dir is null");
        } catch (ConfigurationException e) {
            // expected this
        }
    }

    public void testEmptyStringHomeDir() {
        final CargoContainerModel cargoContainerModel = new CargoContainerModel();
        cargoContainerModel.setHomeDir("");
        cargoContainerEditor.resetEditorFrom(cargoContainerModel);
        try {
            cargoContainerEditor.applyEditorTo(cargoContainerModel);
            fail("Expected ConfigurationException as the home dir is an empty String");
        } catch (ConfigurationException e) {
            // expected this
        }
    }

    public void testNonExistentHomeDir() {
        final CargoContainerModel cargoContainerModel = new CargoContainerModel();
        final String homeDir = "/" + System.currentTimeMillis();
        assertFalse(new File(homeDir).exists());
        cargoContainerModel.setHomeDir(homeDir);
        cargoContainerEditor.resetEditorFrom(cargoContainerModel);
        try {
            cargoContainerEditor.applyEditorTo(cargoContainerModel);
            fail("Expected ConfigurationException as the home dir does not exist");
        } catch (ConfigurationException e) {
            // expected this
        }
    }

    public void testSetHomeDir() throws ConfigurationException, IOException {
        final CargoContainerModel cargoContainerModel = new CargoContainerModel();
        final String homeDir = new File(System.getProperty("user.dir")).getAbsolutePath();
        assertTrue(new File(homeDir).exists());
        cargoContainerModel.setHomeDir(homeDir);
        cargoContainerEditor.resetEditorFrom(cargoContainerModel);
        final CargoContainerModel newCargoContainerModel = new CargoContainerModel();
        cargoContainerEditor.applyEditorTo(newCargoContainerModel);
        assertEquals(homeDir, newCargoContainerModel.getHomeDir());
    }
}
