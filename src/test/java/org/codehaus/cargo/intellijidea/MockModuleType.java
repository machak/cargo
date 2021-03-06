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

import javax.swing.Icon;

import com.intellij.ide.util.projectWizard.ModuleBuilder;
import com.intellij.openapi.module.ModuleType;

/**
 * MockModuleType.
 * <p/>
 *
 * @version $Id: CargoTaskTest.java 414 2005-07-20 09:34:44Z vmassol $
 */
public class MockModuleType {
    public static final ModuleType WEB = new ModuleType("J2EE_WEB_MODULE") {

        public ModuleBuilder createModuleBuilder() {
            return null;
        }

        public String getName() {
            return null;
        }

        public String getDescription() {
            return null;
        }

        public Icon getBigIcon() {
            return null;
        }

        public Icon getNodeIcon(boolean isOpened) {
            return null;
        }
    };

    public static final ModuleType EAR = new ModuleType("J2EE_APPLICATION_MODULE") {

        public ModuleBuilder createModuleBuilder() {
            return null;
        }

        public String getName() {
            return null;
        }

        public String getDescription() {
            return null;
        }

        public Icon getBigIcon() {
            return null;
        }

        public Icon getNodeIcon(boolean isOpened) {
            return null;
        }
    };

    public static final ModuleType EJB = new ModuleType("J2EE_EJB_MODULE") {

        public ModuleBuilder createModuleBuilder() {
            return null;
        }

        public String getName() {
            return null;
        }

        public String getDescription() {
            return null;
        }

        public Icon getBigIcon() {
            return null;
        }

        public Icon getNodeIcon(boolean isOpened) {
            return null;
        }
    };

}
