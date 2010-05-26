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
package at.gv.egiz.bku.smccstal;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.gv.egiz.bku.gui.BKUGUIFacade;
import at.gv.egiz.smcc.SignatureCard;
import at.gv.egiz.stal.STALRequest;
import at.gv.egiz.stal.STALResponse;

public abstract class AbstractRequestHandler implements SMCCSTALRequestHandler,
    ActionListener {
  private final Logger log = LoggerFactory.getLogger(AbstractRequestHandler.class);

  protected SignatureCard card;
  protected BKUGUIFacade gui;
  protected String actionCommand;
  protected boolean actionPerformed = false;

  @Override
  public abstract STALResponse handleRequest(STALRequest request) throws InterruptedException;
  
  @Override
  public void init(SignatureCard sc, BKUGUIFacade gui) {
    if ((sc == null) || (gui == null)) {
      throw new NullPointerException("Parameter must not be set to null");
    }
    this.card = sc;
    this.gui = gui;
  }
  
  protected synchronized void waitForAction() throws InterruptedException {
    try {
      while (!actionPerformed) {
        wait();
      }
    } catch (InterruptedException e) {
      log.error("interrupt in waitForAction");
      throw e;
    }
    actionPerformed = false;
  }

  private synchronized void actionPerformed() {
    actionPerformed = true;
    notifyAll();
  }
  
  @Override
  public void actionPerformed(ActionEvent e) {
    actionCommand = e.getActionCommand();
    actionPerformed();
  }

}
