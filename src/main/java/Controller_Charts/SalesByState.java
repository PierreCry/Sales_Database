package Controller_Charts;

import Modele.DataSourceFactory;
import Modele.DAO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Properties;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Matthias
 */
@WebServlet(name = "salesState", urlPatterns = {"/salesState"})
public class SalesByState extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        DAO dao = new DAO(DataSourceFactory.getDataSource());
        Properties resultat = new Properties();
        String dateDebut = request.getParameter("debut");
        String dateFin = request.getParameter("fin");
        
        try {
            resultat.put("records", dao.StateCA(dateDebut, dateFin));
        } catch (SQLException ex) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resultat.put("records", Collections.EMPTY_LIST);
            resultat.put("message", ex.getMessage());
        }
        try (PrintWriter out = response.getWriter()) {
            response.setContentType("application/json;charset=UTF-8");
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String gsonData = gson.toJson(resultat);
            out.println(gsonData);

        }
    }

   @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
