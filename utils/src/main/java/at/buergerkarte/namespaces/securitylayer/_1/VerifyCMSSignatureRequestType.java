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

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for VerifyCMSSignatureRequestType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="VerifyCMSSignatureRequestType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="DateTime" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="CMSSignature" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/>
 *         &lt;element name="DataObject" type="{http://www.buergerkarte.at/namespaces/securitylayer/1.2#}CMSDataObjectOptionalMetaType" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="Signatories" type="{http://www.buergerkarte.at/namespaces/securitylayer/1.2#}SignatoriesType" default="1" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "VerifyCMSSignatureRequestType", propOrder = {
    "dateTime",
    "cmsSignature",
    "dataObject"
})
public class VerifyCMSSignatureRequestType {

    @XmlElement(name = "DateTime")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dateTime;
    @XmlElement(name = "CMSSignature", required = true)
    protected byte[] cmsSignature;
    @XmlElement(name = "DataObject")
    protected CMSDataObjectOptionalMetaType dataObject;
    @XmlAttribute(name = "Signatories")
    protected List<String> signatories;

    /**
     * Gets the value of the dateTime property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateTime() {
        return dateTime;
    }

    /**
     * Sets the value of the dateTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateTime(XMLGregorianCalendar value) {
        this.dateTime = value;
    }

    /**
     * Gets the value of the cmsSignature property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getCMSSignature() {
        return cmsSignature;
    }

    /**
     * Sets the value of the cmsSignature property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setCMSSignature(byte[] value) {
        this.cmsSignature = ((byte[]) value);
    }

    /**
     * Gets the value of the dataObject property.
     * 
     * @return
     *     possible object is
     *     {@link CMSDataObjectOptionalMetaType }
     *     
     */
    public CMSDataObjectOptionalMetaType getDataObject() {
        return dataObject;
    }

    /**
     * Sets the value of the dataObject property.
     * 
     * @param value
     *     allowed object is
     *     {@link CMSDataObjectOptionalMetaType }
     *     
     */
    public void setDataObject(CMSDataObjectOptionalMetaType value) {
        this.dataObject = value;
    }

    /**
     * Gets the value of the signatories property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the signatories property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSignatories().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getSignatories() {
        if (signatories == null) {
            signatories = new ArrayList<String>();
        }
        return this.signatories;
    }

}
