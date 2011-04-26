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

import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.options.ConfigurationException;

import org.codehaus.cargo.container.property.GeneralPropertySet;
import org.codehaus.cargo.container.property.ServletPropertySet;

import junit.framework.TestCase;

/**
 * CargoConfigurationEditorTest.
 * <p/>
 *
 * @version $Id: CargoTaskTest.java 414 2005-07-20 09:34:44Z vmassol $
 */
public class CargoConfigurationEditorTest extends TestCase {
    private CargoConfigurationEditor cargoConfigurationEditor;
    private MockJettyIntegration mockJettyIntegration;
    private MockCommonModel commonModel;
    private CargoConfigurationModel cargoConfigurationModel;

    protected void setUp() throws Exception {
        if (ModuleType.WEB == null) {
            ModuleType.WEB = MockModuleType.WEB;
        }
        mockJettyIntegration = new MockJettyIntegration();
        cargoConfigurationEditor
                = new CargoConfigurationEditor((CargoApplicationServerHelper)
                mockJettyIntegration.getApplicationServerHelper());
        commonModel = new MockCommonModel();
        cargoConfigurationModel = new CargoConfigurationModel();
        commonModel.setServerModel(cargoConfigurationModel);
    }

    public void testNullConfigDir() {
        cargoConfigurationModel.setConfigDir(null);
        cargoConfigurationEditor.resetEditorFrom(commonModel);
        try {
            cargoConfigurationEditor.applyEditorTo(commonModel);
            fail("Expected ConfigurationException as the config dir is null");
        } catch (ConfigurationException e) {
            //expected this
        }
    }

    public void testEmptyStringConfigDir() {
        cargoConfigurationModel.setConfigDir("");
        cargoConfigurationEditor.resetEditorFrom(commonModel);
        try {
            cargoConfigurationEditor.applyEditorTo(commonModel);
            fail("Expected ConfigurationException as the config dir is an empty String");
        } catch (ConfigurationException e) {
            //expected this
        }
    }


    public void testSetConfigDir() throws ConfigurationException {
        final String configDir = new File(System.getProperty("java.io.tmpdir")).getAbsoluteFile().toString();
        assertTrue(new File(configDir).exists());
        cargoConfigurationModel.setConfigDir(configDir);
        cargoConfigurationEditor.resetEditorFrom(commonModel);
        final CargoConfigurationModel newCargoConfigurationModel = this.cargoConfigurationModel;
        final MockCommonModel newCommonModel = new MockCommonModel();
        newCommonModel.setServerModel(newCargoConfigurationModel);
        cargoConfigurationEditor.applyEditorTo(newCommonModel);
        assertEquals(configDir, newCargoConfigurationModel.getConfigDir());
    }

    public void testProperties() throws ConfigurationException {
        checkSetGetProperty(GeneralPropertySet.HOSTNAME, "TestHost");
        checkSetGetProperty(GeneralPropertySet.JVMARGS, "-Djvm=arg");
        checkSetGetProperty(GeneralPropertySet.LOGGING, "medium");
        checkSetGetProperty(GeneralPropertySet.LOGGING, "low");
        checkSetGetProperty(GeneralPropertySet.LOGGING, "high");
        checkSetGetProperty(ServletPropertySet.PORT, "4567");
        checkSetGetProperty(ServletPropertySet.USERS, "name1:pwd1:role11,role1N|name2:pwd2:role21,role2N");
    }

    public void testNegativePort() {
        try {
            setGetProperty(ServletPropertySet.PORT, "-1");
            fail("Expected ConfigurationException due to negative port");
        } catch (ConfigurationException e) {
            // expected this
        }
    }

    public void testTooHighPort() {
        try {
            setGetProperty(ServletPropertySet.PORT, "666666");
            fail("Expected ConfigurationException due to too high port");
        } catch (ConfigurationException e) {
            // expected this
        }
    }

    public void testInvalidPort() {
        try {
            setGetProperty(ServletPropertySet.PORT, "BooYah!");
            fail("Expected ConfigurationException due to too invalid port");
        } catch (ConfigurationException e) {
            // expected this
        }
    }

    public void testInvalidUsers() {
        try {
            setGetProperty(ServletPropertySet.PORT, "BooYah!");
            fail("Expected ConfigurationException due to too invalid users");
        } catch (ConfigurationException e) {
            // expected this
        }
    }

    private void checkSetGetProperty(final String propertyKey, final String propertyValue)
            throws ConfigurationException {
        assertEquals("Failed to set property " + propertyKey + " to editor and get it back.",
                propertyValue, setGetProperty(propertyKey, propertyValue));
    }

    private String setGetProperty(final String propertyKey,
                                  final String propertyValue)
            throws ConfigurationException {
        cargoConfigurationModel.setProperty(propertyKey, propertyValue);
        // we have to have a valid config dir to test whatever else we want to test
        final String configDir = System.getProperty("java.io.tmpdir");
        assertTrue(new File(configDir).exists());
        cargoConfigurationModel.setConfigDir(configDir);
        cargoConfigurationEditor.resetEditorFrom(commonModel);
        final CargoConfigurationModel newCargoConfigurationModel = this.cargoConfigurationModel;
        final MockCommonModel newCommonModel = new MockCommonModel();
        newCommonModel.setServerModel(newCargoConfigurationModel);
        cargoConfigurationEditor.applyEditorTo(newCommonModel);
        return newCargoConfigurationModel.getProperty(propertyKey);
    }

}
