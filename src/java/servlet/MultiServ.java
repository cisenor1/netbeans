/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Craig Isenor
 */
public class MultiServ extends HttpServlet {
    
    private enum States {CHECK_LOGIN, DO_TABLE,DO_ANOTHER};
    /** Title of the webpage*/
    String titleString;
    /** username */
    String usernameString;
    String passwordString;
    

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    
    @Override
    public void init(){
        
        titleString = this.getInitParameter("title");
        usernameString = this.getInitParameter("username");
        passwordString = this.getInitParameter("password");
        
        
        
    }
    protected void doLogin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            out.println("<html>");
            out.println("<head>");
            out.println("<title>MultiServ</title>");            
            out.println("</head>");
            out.println("<body>"
                    + "<form method='post'>"
                    + "<label>UserName: </label>" 
                    + "<input type='text' name='username'><br>"
                    + "<label>Password: </label>"
                    + "<input type='password' name='password'><br>"
                    + "<input type='submit' value='submit'>"
                    + "<input type='hidden' name='step' value='CHECK_LOGIN'>"
                    + "</form>");
            out.println("</body>");
            out.println("</html>");
        } finally {            
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doLogin(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void stateMachine(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //stateMachine(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
