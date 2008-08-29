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

package at.gv.egiz.stal.service;

import javax.xml.ws.WebFault;

/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.3-b02-
 * Generated source version: 2.1
 * 
 */
@WebFault(name = "GetHashDataInputFault", targetNamespace = "http://www.egiz.gv.at/stal")
public class GetHashDataInputFault
    extends Exception
{

    /**
     * Java type that goes as soapenv:Fault detail element.
     * 
     */
    private GetHashDataInputFaultType faultInfo;

    /**
     * 
     * @param message
     * @param faultInfo
     */
    public GetHashDataInputFault(String message, GetHashDataInputFaultType faultInfo) {
        super(message);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @param message
     * @param faultInfo
     * @param cause
     */
    public GetHashDataInputFault(String message, GetHashDataInputFaultType faultInfo, Throwable cause) {
        super(message, cause);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @return
     *     returns fault bean: at.gv.egiz.stal.GetHashDataInputFaultType
     */
    public GetHashDataInputFaultType getFaultInfo() {
        return faultInfo;
    }

}
