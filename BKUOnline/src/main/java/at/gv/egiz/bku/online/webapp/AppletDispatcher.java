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

package at.gv.egiz.bku.online.webapp;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * prevent applet caching, 
 * could be removed once applet is loaded via jnlp
 *
 * @author Clemens Orthacker <clemens.orthacker@iaik.tugraz.at>
 */
public class AppletDispatcher extends HttpServlet {

  protected final static Log log = LogFactory.getLog(AppletDispatcher.class);

  public static final String DISPATCH_CTX = "dispatch/";
  public static final String RAND_PREFIX = "__";
  public static final String RAND_ATTRIBUTE = "rand";
  public static final Pattern ctxPattern = Pattern.compile(DISPATCH_CTX);
  public static final Pattern archivePattern = Pattern.compile(RAND_PREFIX + "[a-zA-Z0-9]*\\.jar$");

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

//      String suffix = (String) request.getSession().getAttribute(RAND_CTX_ATTRIBUTE);
//      log.trace("expecting random suffix " + suffix);
      
      String uri = request.getRequestURI();
      uri = ctxPattern.matcher(uri).replaceAll("");
//      uri = uri.replaceAll(suffix, ""); //only the applet jar requests contains the randCtx
      uri = archivePattern.matcher(uri).replaceAll(".jar");

      if (log.isTraceEnabled()) {
        log.trace("dispatching request URI " + request.getRequestURI() +
                " to " + uri);
      }
      
      RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(uri);
      dispatcher.forward(request, response);
    } 

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    } 

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
