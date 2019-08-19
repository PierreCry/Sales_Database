/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller_Main;

import Modele.DataSourceFactory;
import Modele.DAO;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import org.apache.derby.tools.ij;

/**
 *
 * @author Matthias
 */
@WebListener()
public class ApplicationListener implements ServletContextListener{

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        if (!databaseExists()){
            initializeDatabase();
        }
        
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        
    }
       
    private boolean databaseExists() {
		boolean result = false;

		DAO dao = new DAO(DataSourceFactory.getDataSource());
		try {
			int nbCusto = dao.numberCustomer();
			Logger.getLogger("ProjetJEE").log(Level.INFO, "Database already exists");
			result = true;
		} catch (SQLException ex) {
			Logger.getLogger("ProjetJEE").log(Level.INFO, "Database does not exist");
		} 
		return result;
	}

	private void initializeDatabase() {
		OutputStream nowhere = new OutputStream() {
			@Override
			public void write(int b) {
			}
		};
		
		Logger.getLogger("ProjetJEE").log(Level.INFO, "Creating databse from SQL script");
		try {
			Connection connection = DataSourceFactory.getDataSource().getConnection();
			int result = ij.runScript(connection, this.getClass().getResourceAsStream("export.sql"), "UTF-8", System.out, "UTF-8");
			if (result == 0) {
				Logger.getLogger("ProjetJEE").log(Level.INFO, "Database succesfully created");
			} else {
				Logger.getLogger("ProjetJEE").log(Level.SEVERE, "Errors creating database");
			}

		} catch (UnsupportedEncodingException | SQLException e) {
			Logger.getLogger("ProjetJEE").log(Level.SEVERE, null, e);
		}

	}
}
