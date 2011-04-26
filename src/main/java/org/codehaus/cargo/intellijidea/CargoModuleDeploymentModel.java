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

import com.intellij.j2ee.deployment.DeploymentModel;
import com.intellij.j2ee.j2eeDom.J2EEModuleProperties;
import com.intellij.j2ee.run.configuration.CommonModel;

/**
 * Module deployment model. This is manipulated by {@link CargoWAREditor}.
 *
 * @version $Id: CargoTask.java 391 2005-07-04 13:07:42Z vmassol $
 */
public class CargoModuleDeploymentModel extends DeploymentModel {
    /**
     * Context path. Has to be public for JDOMExternalizable mechanism.
     */
    public String contextPath = "/";

    /**
     * Constructor.
     *
     * @param commonModel      commonModel
     * @param moduleProperties moduleProperties
     */
    public CargoModuleDeploymentModel(CommonModel commonModel,
                                      J2EEModuleProperties moduleProperties) {
        super(commonModel, moduleProperties);
    }

    /**
     * Get context path.
     *
     * @return context path
     */
    public String getContextPath() {
        return contextPath;
    }

    /**
     * Sets the context path.
     *
     * @param contextPath context path
     */
    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }
}
