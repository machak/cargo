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
package org.codehaus.cargo.intellijidea.jo;

import java.io.File;
import java.io.FileFilter;

import org.codehaus.cargo.intellijidea.AbstractCargoAppServerIntegration;
import org.codehaus.cargo.intellijidea.CargoApplicationServerHelper;
import org.codehaus.cargo.intellijidea.CargoContainerModel;

/**
 * jo! specific ApplicationServerHelper.
 *
 * @version $Id: CargoTask.java 391 2005-07-04 13:07:42Z vmassol $
 */
public class Jo1xApplicationServerHelper extends CargoApplicationServerHelper {

    /**
     * Constructor.
     *
     * @param cargoAppServerIntegration the cargoManager that owns this helper
     */
    public Jo1xApplicationServerHelper(
            AbstractCargoAppServerIntegration cargoAppServerIntegration) {
        super(cargoAppServerIntegration);
    }

    /**
     * Returns an array of jars that are default libraries for the container.
     * Note: if these jars do not contain servlet classes and/or ejb classes
     * the configuration will be marked as faulty in the IntelliJIdea API.
     *
     * @param cargoContainerModel cargoPersistentData, can be used to get the container home
     *                            directory
     * @return all jars and zips in jo's system library folder
     */
    protected File[] getDefaultLibraries(CargoContainerModel cargoContainerModel) {
        File homeDir = new File(cargoContainerModel.getHomeDir()).getAbsoluteFile();
        return new File(homeDir, "system").listFiles(new FileFilter() {
            public boolean accept(File pathname) {
                return pathname.getName().endsWith(".jar") || pathname.getName().endsWith(".zip");
            }
        });
    }

}
