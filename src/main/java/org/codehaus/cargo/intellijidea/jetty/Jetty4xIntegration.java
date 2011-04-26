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
package org.codehaus.cargo.intellijidea.jetty;

import com.intellij.openapi.application.ApplicationManager;

import org.codehaus.cargo.container.jetty.Jetty4xEmbeddedLocalContainer;
import org.codehaus.cargo.intellijidea.AbstractCargoAppServerIntegration;

/**
 * Jetty4x Integration.
 *
 * @version $Id: CargoTask.java 391 2005-07-04 13:07:42Z vmassol $
 */
public class Jetty4xIntegration extends AbstractCargoAppServerIntegration {
    /**
     * Constructor passing the Jetty4x id.
     */
    public Jetty4xIntegration() {
        super(Jetty4xEmbeddedLocalContainer.ID);
    }

    /**
     * Factory method.
     *
     * @return CargoManager instance
     */
    public static AbstractCargoAppServerIntegration getInstance() {
        return (AbstractCargoAppServerIntegration) (ApplicationManager.getApplication())
                .getComponent(Jetty4xIntegration.class);
    }


}
