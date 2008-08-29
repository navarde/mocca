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
//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-520 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2008.07.25 at 10:41:37 AM GMT 
//


package at.buergerkarte.namespaces.securitylayer._1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CreateHashInfoRequestType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CreateHashInfoRequestType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="HashData" type="{http://www.buergerkarte.at/namespaces/securitylayer/1.2#}HashDataType"/>
 *         &lt;element name="HashAlgorithm" type="{http://www.w3.org/2001/XMLSchema}anyURI"/>
 *         &lt;element name="FriendlyName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="RespondHashData" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CreateHashInfoRequestType", propOrder = {
    "hashData",
    "hashAlgorithm",
    "friendlyName"
})
public class CreateHashInfoRequestType {

    @XmlElement(name = "HashData", required = true)
    protected HashDataType hashData;
    @XmlElement(name = "HashAlgorithm", required = true)
    @XmlSchemaType(name = "anyURI")
    protected String hashAlgorithm;
    @XmlElement(name = "FriendlyName")
    protected String friendlyName;
    @XmlAttribute(name = "RespondHashData", required = true)
    protected boolean respondHashData;

    /**
     * Gets the value of the hashData property.
     * 
     * @return
     *     possible object is
     *     {@link HashDataType }
     *     
     */
    public HashDataType getHashData() {
        return hashData;
    }

    /**
     * Sets the value of the hashData property.
     * 
     * @param value
     *     allowed object is
     *     {@link HashDataType }
     *     
     */
    public void setHashData(HashDataType value) {
        this.hashData = value;
    }

    /**
     * Gets the value of the hashAlgorithm property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHashAlgorithm() {
        return hashAlgorithm;
    }

    /**
     * Sets the value of the hashAlgorithm property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHashAlgorithm(String value) {
        this.hashAlgorithm = value;
    }

    /**
     * Gets the value of the friendlyName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFriendlyName() {
        return friendlyName;
    }

    /**
     * Sets the value of the friendlyName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFriendlyName(String value) {
        this.friendlyName = value;
    }

    /**
     * Gets the value of the respondHashData property.
     * 
     */
    public boolean isRespondHashData() {
        return respondHashData;
    }

    /**
     * Sets the value of the respondHashData property.
     * 
     */
    public void setRespondHashData(boolean value) {
        this.respondHashData = value;
    }

}
