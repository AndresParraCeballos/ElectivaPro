/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlet;

import ControladorJPA.FacturaJpaController;
import ControladorJPA.FormaspagosJpaController;
import ControladorJPA.UsuarioJpaController;
import implementacionDAO.FacturaImpl;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import Entidades.Factura;
import Entidades.Formaspagos;
import Entidades.Usuarios;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelo.Usuario;

/**
 *
 * @author Andre
 */
public class FacturaServlet extends HttpServlet {

     private String fecha;
    private int idVendedor;
    private int idComprador;
    private int formapago;
    private Factura factura;
    private Date date;
    private FacturaJpaController impl;
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            
            factura = new Factura();
            impl = new FacturaJpaController();
            date = new Date();
            this.fecha = date.toString();
            this.idVendedor =Integer.parseInt(request.getParameter("iduser"));
            this.idComprador = Integer.parseInt(request.getParameter("iduser"));
            this.formapago = Integer.parseInt(request.getParameter("fpago"));
            
            Usuarios comprador = new Usuarios();
            UsuarioJpaController con = new UsuarioJpaController();
            comprador = con.findUsuario(idComprador);
            Usuarios vendedor = new Usuarios();
            vendedor = con.findUsuario(idVendedor);
            factura.setFacFecha(this.fecha);
            factura.setIdUsuariovendedor(vendedor);
            factura.setIdUsuariocomprador(comprador);
            factura.setFacEstado("X");  
            
            Formaspagos formapago = new Formaspagos();
            FormaspagosJpaController contr = new FormaspagosJpaController();
            formapago = contr.findFormaspagos(this.formapago);
            factura.setFormaspagosIdformaspagos(formapago);
            
            
            boolean bol;
            try {
                bol = impl.create(factura);
                if (!bol) {
                out.println("<h1>no sirve</h1>");
                }else{
                    response.sendRedirect("pages/productos.jsp?idComprador="+this.idComprador);
                }
            } catch (Exception ex) {
                Logger.getLogger(FacturaServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
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
     *
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
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}








