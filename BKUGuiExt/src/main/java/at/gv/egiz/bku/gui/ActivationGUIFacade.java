/*
 * Copyright 2008 Federal Chancellery Austria and
 * Graz University of Technology
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package at.gv.egiz.bku.gui;

import at.gv.egiz.bku.gui.*;
import java.awt.event.ActionListener;

/**
 *
 * @author Clemens Orthacker <clemens.orthacker@iaik.tugraz.at>
 */
public interface ActivationGUIFacade extends BKUGUIFacade {

  public void showActivationProgressDialog(int currentStep, int maxProgress, ActionListener cancelListener, String cancelCommand);

  public void incrementProgress();

  public void showIdleDialog(ActionListener cancelListener, String cancelCommand);

}