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

import com.intellij.j2ee.appServerIntegrations.ApplicationServerHelper;
import com.intellij.j2ee.appServerIntegrations.ApplicationServerInfo;
import com.intellij.j2ee.appServerIntegrations.ApplicationServerPersistentData;
import com.intellij.j2ee.appServerIntegrations.ApplicationServerPersistentDataEditor;
import com.intellij.j2ee.appServerIntegrations.CantFindApplicationServerJarsException;

/**
 * Manages a couple of configuration aspects for the integrated application
 * server. These are:
 * <ul>
 * <li>default Name of the app server</li>
 * <li>default libraries</li>
 * <li>persistent data editor</li>
 * <li>empty persistent data object factory</li>
 * </ul>
 *
 * @version $Id: CargoTask.java 391 2005-07-04 13:07:42Z vmassol $
 */
public class CargoApplicationServerHelper implements ApplicationServerHelper {

    /**
     * Empty file array.
     */
    private static final File[] EMPTY_FILE_ARRAY = new File[0];
    /**
     * The parent CargoManager.
     */
    private AbstractCargoAppServerIntegration cargoAppServerIntegration;

    /**
     * Constructor.
     *
     * @param cargoAppServerIntegration the cargoManager that owns this helper
     */
    public CargoApplicationServerHelper(
            AbstractCargoAppServerIntegration cargoAppServerIntegration) {
        this.cargoAppServerIntegration = cargoAppServerIntegration;
    }

    /**
     * The AppServerIntegration this helper belongs to.
     *
     * @return AppServerIntegration
     */
    public AbstractCargoAppServerIntegration getCargoAppServerIntegration() {
        return cargoAppServerIntegration;
    }

    /**
     * Returns an {@link ApplicationServerInfo} object.
     *
     * @param persistentData persistent data this app server is configured with (home dir etc.)
     * @return an ApplicationServerInfo object
     * @throws CantFindApplicationServerJarsException
     *          if configuration paths or files
     *          cannot be found
     */
    public ApplicationServerInfo getApplicationServerInfo(
            ApplicationServerPersistentData persistentData)
            throws CantFindApplicationServerJarsException {
        CargoContainerModel cargoContainerModel = (CargoContainerModel) persistentData;
        final String homeDirString = cargoContainerModel.getHomeDir();
        if (homeDirString == null || homeDirString.length() == 0) {
            throw new CantFindApplicationServerJarsException(
                    "No home directory set.");
        }
        File homeDir = new File(homeDirString).getAbsoluteFile();
        if (!homeDir.exists() || !homeDir.isDirectory()) {
            throw new CantFindApplicationServerJarsException(
                    "Can't find directory '" + homeDir.getAbsolutePath() + '\'');
        }
        return new ApplicationServerInfo(getDefaultLibraries(cargoContainerModel),
                cargoAppServerIntegration.getPresentableName());

    }

    /**
     * Returns an array of jars that are default libraries for the container.
     * Note: if these jars do not contain servlet classes and/or ejb classes
     * the configuration will be marked as faulty in the IntelliJIdea API.
     * <p/>
     * This method is meant to be overwritten by conatiner specific subclasses.
     *
     * @param cargoContainerModel cargoPersistentData, can be used to get the container home
     *                            directory
     * @return the default implementation returns an empty file array
     */
    protected File[] getDefaultLibraries(CargoContainerModel cargoContainerModel) {
        return EMPTY_FILE_ARRAY;
    }

    /**
     * Creates an empty persistent configuration data object.
     *
     * @return persistent data object
     * @see CargoContainerModel
     */
    public ApplicationServerPersistentData createPersistentDataEmptyInstance() {
        return new CargoContainerModel();
    }

    /**
     * Creates a persistent data editor.
     *
     * @return {@link CargoContainerEditor}
     */
    public ApplicationServerPersistentDataEditor createConfigurable() {
        return new CargoContainerEditor();
    }
}
