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

import com.intellij.openapi.application.ApplicationManager;

import org.codehaus.cargo.intellijidea.AbstractCargoConfigurationType;


/**
 * Specific Oc4j9x configuration type.
 *
 * @version $Id: CargoTask.java 391 2005-07-04 13:07:42Z vmassol $
 */
public class Oc4j9xConfigurationType extends AbstractCargoConfigurationType {

    /**
     * No arg constructor.
     */
    public Oc4j9xConfigurationType() {
        super(Oc4j9xIntegration.class);
    }


    /**
     * Factory method.
     *
     * @return instance
     */
    public static Oc4j9xConfigurationType getInstance() {
        return ((Oc4j9xConfigurationType) (ApplicationManager.getApplication()).getComponent(
                Oc4j9xConfigurationType.class));
    }
}
