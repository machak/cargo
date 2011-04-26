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

import com.intellij.openapi.components.BaseComponent;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.pom.PomModule;

import org.picocontainer.PicoContainer;

/**
 * MockModule.
 * <p/>
 *
 * @version $Id: CargoTaskTest.java 414 2005-07-20 09:34:44Z vmassol $
 */
public class MockModule implements Module {

    private ModuleType moduleType;
    private String name;
    private Project project;

    public VirtualFile getModuleFile() {
        return null;
    }

    public String getModuleFilePath() {
        return null;
    }

    public void setModuleType(ModuleType moduleType) {
        this.moduleType = moduleType;
    }

    public ModuleType getModuleType() {
        return moduleType;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean isDisposed() {
        return false;
    }

    public boolean isSavePathsRelative() {
        return false;
    }

    public void setSavePathsRelative(boolean b) {
    }

    public void setOption(String optionName, String optionValue) {
    }

    public String getOptionValue(String optionName) {
        return null;
    }

    public Object getComponent(Class interfaceClass) {
        return null;
    }

    public Object getComponent(Class interfaceClass, Object defaultImplementationIfAbsent) {
        return null;
    }

    public Class[] getComponentInterfaces() {
        return new Class[0];
    }

    public boolean hasComponent(Class interfaceClass) {
        return false;
    }

    public Object[] getComponents(Class baseInterfaceClass) {
        return new Object[0];
    }

    public Object getUserData(Key key) {
        return null;
    }

    public void putUserData(Key key, Object value) {
    }

    /* ==== Methods new in 5.0 ==== */

    public PomModule getPom() {
        return null;
    }

    public BaseComponent getComponent(String name) {
        return null;
    }

    public PicoContainer getPicoContainer() {
        return null;
    }

}
