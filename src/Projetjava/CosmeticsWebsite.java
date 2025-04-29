package Projetjava;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class CosmeticsWebsite {
	static String URL="jdbc:mysql://localhost/cosmeticswebsite";
	static String USER="root";
	static String PASS="";
	
	public static Connection getConnection()
	{
	try {
		return DriverManager.getConnection(URL,USER,PASS);
			} catch (SQLException ex) {
		throw new RuntimeException("Error connecting to the database", ex);
	}}
    
	
    public static void main(String[] args) {
    	try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Couldn't set system look and feel.");
        }

        
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame(); 
            loginFrame.setVisible(true);
        });
    }

}
