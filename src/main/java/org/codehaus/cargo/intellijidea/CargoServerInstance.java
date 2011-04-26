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

import com.intellij.execution.process.OSProcessHandler;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.j2ee.run.configuration.CommonModel;
import com.intellij.j2ee.serverInstances.DefaultJ2EEServerEvent;
import com.intellij.j2ee.serverInstances.DefaultServerInstance;


/**
 * Is instantiated by {@link CargoConfigurationModel#createServerInstance()}.
 *
 * @version $Id: CargoTask.java 391 2005-07-04 13:07:42Z vmassol $
 */
public class CargoServerInstance extends DefaultServerInstance {

    /**
     * Default constructor.
     *
     * @param runConfiguration runConfiguration
     */
    public CargoServerInstance(CommonModel runConfiguration) {
        super(runConfiguration);
    }

    /**
     * Returns the {@link CargoConfigurationModel}
     *
     * @return model
     */
    public CargoConfigurationModel getCargoModel() {
        return (CargoConfigurationModel) getServerModel();
    }

    /**
     * Is called when the instance has been started.
     *
     * @param processHandler processHandler
     */
    public void start(final ProcessHandler processHandler) {
        super.start(processHandler);
        fireServerListeners(
                new DefaultJ2EEServerEvent("Started", "Cargo instance started.", true, false));

        /*
         * This is where we could hook in a JSR45PositionManager, if we are running in
         * embedded mode, i.e. without spawning another VM instance. Sample code:
         */

        /*
        final DebuggerManager debuggerManager = (DebuggerManager.getInstance(model.getProject()));
        debuggerManager.addDebugProcessListener(processHandler, new DebugProcessAdapter()
        {
            private PositionManager positionManager;

            //executed in manager thread
            public void processDetached(DebugProcess process)
            {
                super.processDetached(process);
            }

            public void processAttached(DebugProcess process)
            {
                positionManager = new JSR45PositionManager(process)
                {
                    protected String getJSPClassesPackage()
                    {
                        // TODO: This is different for every container!
                        return "org.apache.jsp";
                    }
                };
                process.appendPositionManager(positionManager);
            }
        });
        */
    }

    /**
     * Shutdown the container.
     */
    public void shutdown() {
        super.shutdown();
        final ProcessHandler processHandler = getProcessHandler();
        if (processHandler != null) {
            ((OSProcessHandler) processHandler).getProcess().destroy();
        }
    }

}
