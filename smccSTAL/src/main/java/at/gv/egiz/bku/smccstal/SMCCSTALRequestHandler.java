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


import at.gv.egiz.bku.gui.BKUGUIFacade;
import at.gv.egiz.smcc.PINProvider;
import at.gv.egiz.smcc.SignatureCard;
import at.gv.egiz.stal.STALRequest;
import at.gv.egiz.stal.STALResponse;

public interface SMCCSTALRequestHandler {
  
  public void init(SignatureCard sc, BKUGUIFacade gui);
  
  public STALResponse handleRequest(STALRequest request);
  
  public boolean requireCard();
  
  public SMCCSTALRequestHandler newInstance();

}
