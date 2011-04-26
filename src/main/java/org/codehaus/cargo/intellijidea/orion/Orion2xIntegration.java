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
package org.codehaus.cargo.intellijidea.orion;

import com.intellij.j2ee.appServerIntegrations.ApplicationServerHelper;
import com.intellij.openapi.application.ApplicationManager;

import org.codehaus.cargo.container.orion.Orion2xContainer;
import org.codehaus.cargo.intellijidea.AbstractCargoAppServerIntegration;

/**
 * Orion2x Integration.
 *
 * @version $Id: CargoTask.java 391 2005-07-04 13:07:42Z vmassol $
 */
public class Orion2xIntegration extends AbstractCargoAppServerIntegration {
    /**
     * Constructor passing the Orion2x id.
     */
    public Orion2xIntegration() {
        super(Orion2xContainer.ID);
    }

    /**
     * Factory method.
     *
     * @return CargoManager instance
     */
    public static AbstractCargoAppServerIntegration getInstance() {
        return (AbstractCargoAppServerIntegration) (ApplicationManager.getApplication())
                .getComponent(Orion2xIntegration.class);
    }

    /**
     * @return CargoApplicationServerHelper instance
     */
    protected ApplicationServerHelper createApplicationServerHelper() {
        return new OrionApplicationServerHelper(this);
    }


}
