package org.codehaus.cargo.intellijidea;

import java.io.File;

import com.intellij.j2ee.appServerIntegrations.ApplicationServerInfo;
import com.intellij.j2ee.appServerIntegrations.CantFindApplicationServerJarsException;
import com.intellij.openapi.module.ModuleType;

import junit.framework.TestCase;

/**
 * CargoApplicationServerHelperTest.
 * <p/>
 *
 * @version $Id: CargoTaskTest.java 414 2005-07-20 09:34:44Z vmassol $
 */
public class CargoApplicationServerHelperTest extends TestCase {
    private CargoApplicationServerHelper cargoApplicationServerHelper;
    private MockJettyIntegration mockJettyIntegration;

    protected void setUp() {
        if (ModuleType.WEB == null) {
            ModuleType.WEB = MockModuleType.WEB;
        }
        mockJettyIntegration = new MockJettyIntegration();
        cargoApplicationServerHelper
                = (CargoApplicationServerHelper) mockJettyIntegration.getApplicationServerHelper();
    }

    public void testGetApplicationServerInfoWithNonExistantHomeDir() {
        final CargoContainerModel cargoContainerModel
                = (CargoContainerModel) cargoApplicationServerHelper
                .createPersistentDataEmptyInstance();
        final String homeDir = "/" + System.currentTimeMillis();
        assertFalse(new File(homeDir).exists());
        cargoContainerModel.setHomeDir(homeDir);
        try {
            cargoApplicationServerHelper.getApplicationServerInfo(cargoContainerModel);
            fail("Expected CantFindApplicationServerJarsException as the home dir does not exist.");
        } catch (CantFindApplicationServerJarsException e) {
            // expected this.
        }
    }

    public void testGetApplicationServerInfoWithNullHomeDir() {
        final CargoContainerModel cargoContainerModel
                = (CargoContainerModel) cargoApplicationServerHelper
                .createPersistentDataEmptyInstance();
        cargoContainerModel.setHomeDir(null);
        try {
            cargoApplicationServerHelper.getApplicationServerInfo(cargoContainerModel);
            fail("Expected CantFindApplicationServerJarsException as the home dir does not exist"
                    + "(it is null).");
        } catch (CantFindApplicationServerJarsException e) {
            // expected this.
        }
    }

    public void testGetApplicationServerInfoWithEmptyHomeDir() {
        final CargoContainerModel cargoContainerModel
                = (CargoContainerModel) cargoApplicationServerHelper
                .createPersistentDataEmptyInstance();
        cargoContainerModel.setHomeDir("");
        try {
            cargoApplicationServerHelper.getApplicationServerInfo(cargoContainerModel);
            fail("Expected CantFindApplicationServerJarsException as the home dir does not exist"
                    + "(it is an empty String).");
        } catch (CantFindApplicationServerJarsException e) {
            // expected this.
        }
    }

    public void testGetApplicationServerInfo() throws CantFindApplicationServerJarsException {
        final CargoContainerModel cargoContainerModel
                = (CargoContainerModel) cargoApplicationServerHelper
                .createPersistentDataEmptyInstance();
        cargoContainerModel.setHomeDir("/");
        ApplicationServerInfo applicationServerInfo
                = cargoApplicationServerHelper.getApplicationServerInfo(cargoContainerModel);
        assertNotNull(applicationServerInfo.getDefaultLibraries());
        assertEquals(0, applicationServerInfo.getDefaultLibraries().length);
        assertNotNull(applicationServerInfo.getDefaultName());
    }
}
