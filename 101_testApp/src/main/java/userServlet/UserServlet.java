package userServlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.RequestDispatcher;


import UserDao.UserDAO;
import model.User;

/**
 * Servlet implementation class UserServlet
 */
@WebServlet("/")
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private UserDAO userDAO;

    public void init() {
        userDAO = new UserDAO();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String action = request.getServletPath();

        try {
            switch (action) {
                case "/new":
                    showNewForm(request, response);
                    break;
                case "/insert":
                    insertUser(request, response);
                    break;
                case "/delete":
                    deleteUser(request, response);
                    break;
                case "/edit":
                    showEditForm(request, response);
                    break;
                case "/update":
                    updateUser(request, response);
                    break;
                case "/logout":
                    logout(request, response);
                    break;
                case "/list":
                    listUser(request, response);
                    break;
                case "/login":
                    login(request, response);
                    break;
                default:
                	response.sendRedirect("login.jsp");
                	break;
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    private void listUser(HttpServletRequest request, HttpServletResponse response)
    throws SQLException, IOException, ServletException {
    	HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("useracountname") != null) {
            String username = (String) session.getAttribute("useracountname");
            request.setAttribute("username", username);
            
            List < User > listUser = userDAO.selectAllUsers();
            request.setAttribute("listUser", listUser);
            request.getRequestDispatcher("user-list.jsp").forward(request, response);
        } else {
            response.sendRedirect("login.jsp");
        }
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
    	HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("useracountname") != null) {
            String username = (String) session.getAttribute("useracountname");
            request.setAttribute("username", username);
            
            request.getRequestDispatcher("user-form.jsp").forward(request, response);
        } else {
            response.sendRedirect("login.jsp");
        }
    }
    	
        
    

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
    throws SQLException, ServletException, IOException {
    	HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("useracountname") != null) {
            String username = (String) session.getAttribute("useracountname");
            request.setAttribute("username", username);
            
            int id = Integer.parseInt(request.getParameter("id"));
            User existingUser = userDAO.selectUser(id);
            request.setAttribute("user", existingUser);
            request.getRequestDispatcher("user-form.jsp").forward(request, response);
        } else {
            response.sendRedirect("login.jsp");
        }
    	
    	
    }

    private void insertUser(HttpServletRequest request, HttpServletResponse response)
    throws SQLException, IOException {
    	HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("useracountname") != null) {
            String username = (String) session.getAttribute("useracountname");
            request.setAttribute("username", username);
            

            String name = request.getParameter("name");
            String email = request.getParameter("email");
            String country = request.getParameter("country");
            User newUser = new User(name, email, country);
            userDAO.insertUser(newUser);
            response.sendRedirect("list");
        } else {
            response.sendRedirect("login.jsp");
        }
    	
    }

    private void updateUser(HttpServletRequest request, HttpServletResponse response)
    throws SQLException, IOException {
    	HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("useracountname") != null) {
            String username = (String) session.getAttribute("useracountname");
            request.setAttribute("username", username);
            
            int id = Integer.parseInt(request.getParameter("id"));
            String name = request.getParameter("name");
            String email = request.getParameter("email");
            String country = request.getParameter("country");

            User book = new User(id, name, email, country);
            userDAO.updateUser(book);
            response.sendRedirect("list");
        } else {
            response.sendRedirect("login.jsp");
        }
    	
    }

    private void deleteUser(HttpServletRequest request, HttpServletResponse response)
    throws SQLException, IOException {
    	HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("useracountname") != null) {
            String username = (String) session.getAttribute("useracountname");
            request.setAttribute("username", username);
            
            int id = Integer.parseInt(request.getParameter("id"));
            userDAO.deleteUser(id);
            response.sendRedirect("list");
        } else {
            response.sendRedirect("login.jsp");
        }
    	
    }
    
    
    private void logout(HttpServletRequest request, HttpServletResponse response)
    	    throws SQLException, IOException {
    	 HttpSession session = request.getSession(false);
         if (session != null) {
             session.invalidate();
         }
         response.sendRedirect("login.jsp");
     }
    

    protected void login(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        if (userDAO.checkUserAccount(username,password)) { 
            HttpSession session = request.getSession();
            session.setAttribute("useracountname", username);
            response.sendRedirect("list");
        } else {
            response.sendRedirect("login.jsp");
        }
    }
}
