/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.order;

import dal.OrdersDBContext;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Orders;

/**
 *
 * @author Admin
 */
public class SearchOrderServlet extends HttpServlet {

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
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        
        String raw_orderID = request.getParameter("orderID");
        String raw_customerID = request.getParameter("customerID");
        String raw_customerName = request.getParameter("customerName");
        String raw_dateFrom =  request.getParameter("dateFrom");
        String raw_dateTo = request.getParameter("dateTo");
        String raw_username = request.getParameter("username");
        
        if(raw_orderID == null ||  raw_orderID.isEmpty()){
            raw_orderID = "-1";
        }
        
        if(raw_customerID == null ||  raw_customerID.isEmpty()){
            raw_customerID = "-1";
        }
        
        if(raw_customerName == null || raw_customerName.isEmpty()){
            raw_customerName = "";
        }
        
        if(raw_username == null || raw_username.isEmpty()){
            raw_username = "";
        }
                
        int orderID = Integer.parseInt(raw_orderID);
        int customerID = Integer.parseInt(raw_customerID);
        String customerName = raw_customerName;
        java.sql.Date dateFrom = (raw_dateFrom == null || raw_dateFrom.isEmpty()) 
                ? null : java.sql.Date.valueOf(raw_dateFrom);
        java.sql.Date dateTo = (raw_dateTo == null || raw_dateTo.isEmpty()) 
                ? null : java.sql.Date.valueOf(raw_dateTo);
        String username = raw_username;
        
        int pagesize = 10;
        int pageindex;
        String raw_page = request.getParameter("page");

        if (raw_page == null || raw_page.isEmpty()) {
            pageindex = 1;
        } else {
            try {
                pageindex = Integer.parseInt(raw_page);
            } catch (Exception e) {
                pageindex = 1;
            }
        }
        OrdersDBContext orderDB = new OrdersDBContext();
        ArrayList<Orders> orders = orderDB.getOrders(pagesize, pageindex, orderID, 
                customerID, customerName, dateFrom, dateTo, username);
        
        int totalRecord = orders.size();
        int totalPage = (totalRecord % pagesize == 0)?totalRecord / pagesize : totalRecord / pagesize + 1;
        
        
        // set max datefrom
        String dateFromset = "";
        if(dateFrom == null){
            try {
                dateFromset = dateFormat.format(dateTo);
            } catch (Exception e) {
                dateFromset = "2010-01-01";
            }
        }
        request.setAttribute("dateFromset", dateFromset);
        Date currentDate = new Date();
        request.setAttribute("currentDate", dateFormat.format(currentDate));
        
        request.setAttribute("orders", orders);
        request.setAttribute("totalPage", totalPage);
        request.setAttribute("page", pageindex);
        request.getRequestDispatcher("../view/order/search.jsp").forward(request, response);
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