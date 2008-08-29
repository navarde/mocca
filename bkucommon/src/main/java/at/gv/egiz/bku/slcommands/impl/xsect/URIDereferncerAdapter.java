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
package at.gv.egiz.bku.slcommands.impl.xsect;

import iaik.xml.crypto.utils.URIDereferencerImpl;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.xml.crypto.Data;
import javax.xml.crypto.OctetStreamData;
import javax.xml.crypto.URIDereferencer;
import javax.xml.crypto.URIReference;
import javax.xml.crypto.URIReferenceException;
import javax.xml.crypto.XMLCryptoContext;

import at.gv.egiz.bku.utils.urldereferencer.StreamData;
import at.gv.egiz.bku.utils.urldereferencer.URLDereferencer;
import at.gv.egiz.bku.utils.urldereferencer.URLDereferencerContext;

/**
 * An URIDereferencer implementation that uses an {@link URLDereferencer} to
 * dereference.
 * 
 * @author mcentner
 */
public class URIDereferncerAdapter implements URIDereferencer {

  /**
   * The context for dereferencing.
   */
  protected URLDereferencerContext urlDereferencerContext;

  /**
   * Creates a new URIDereferencerAdapter instance with the given
   * <code>urlDereferencerContext</code>.
   * 
   * @param urlDereferencerContext the context to be used for dereferencing
   */
  public URIDereferncerAdapter(URLDereferencerContext urlDereferencerContext) {
    super();
    this.urlDereferencerContext = urlDereferencerContext;
  }

  /* (non-Javadoc)
   * @see javax.xml.crypto.URIDereferencer#dereference(javax.xml.crypto.URIReference, javax.xml.crypto.XMLCryptoContext)
   */
  @Override
  public Data dereference(URIReference uriReference, XMLCryptoContext context)
      throws URIReferenceException {
    
    String uriString = uriReference.getURI();
    if (uriString == null) {
      return null;
    }
    
    URI uri;
    try {
      uri = new URI(uriString);
    } catch (URISyntaxException e) {
      throw new URIReferenceException(e.getMessage(), e);
    }
    
    if (uri.isAbsolute()) {

      URLDereferencer dereferencer = URLDereferencer.getInstance();
      StreamData streamData;
      try {
        streamData = dereferencer.dereference(uriString, urlDereferencerContext);
      } catch (IOException e) {
        throw new URIReferenceException(e.getMessage(), e);
      }
      return new OctetStreamData(streamData.getStream(), uriString, streamData.getContentType());
      
    } else {
      
      URIDereferencer uriDereferencer = context.getURIDereferencer();
      if (uriDereferencer == null || uriDereferencer == this) {
        uriDereferencer = new URIDereferencerImpl();
      }
        
      return uriDereferencer.dereference(uriReference, context);
      
    }
    
  }

}
