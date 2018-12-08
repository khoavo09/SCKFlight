package airportdatabase;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

import javax.swing.*;

public class OldAirportMain {

	// JDBC driver name and database URL
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	static final String DB_URL = "jdbc:mysql://localhost/SCKFlightDatabase";

	//  Database credentials
	static final String USER = "root";
	static final String PASS = "Sh@d0wmage5";
	   
	public static void main(String[] args) {
		Connection conn = null;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
		    System.out.println("Connecting to database...");
		    conn = DriverManager.getConnection(DB_URL, USER, PASS);
			      
		} catch(SQLException se){
		      se.printStackTrace();
		}catch(Exception e){
			  e.printStackTrace();
		}
		/*
		finally{
			 try{
			     if(stmt!=null)
			            stmt.close();
			      }catch(SQLException se2){
			      }// nothing we can do
			      try{
			         if(conn!=null)
			            conn.close();
			      }catch(SQLException se){
			         se.printStackTrace();
			      }
			   }
			   System.out.println("Goodbye!");
			   */
    
		JFrame frame = new JFrame();
		frame.setSize(650,300);
		JPanel panel1 = new JPanel();
		JButton button1 = new JButton("Make Reservation");
		JButton button2 = new JButton("Cancel Reservation");
		JButton button3 = new JButton("Show Current Reservation(s)");
		JButton button4 = new JButton("Show All Reservations");
		
		final Connection connect = conn;
    
	button1.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			JFrame frame2 = new JFrame();
			JPanel panel = new JPanel();
			JLabel label1 = new JLabel("Select your flight:");
			JLabel label3 = new JLabel(String.format("%-5s %-23s %-10s %-15s %-15s %-16s %-1s", "ID", "Airlines", "Model", "Depart from", "Arrive at", "Depart Time", "Arrival Time"));
			label1.setFont(new Font("Serif", Font.PLAIN, 24));
			label3.setFont(new Font("Serif", Font.PLAIN, 18));
			panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
			String available = "SELECT FLIGHT.flightID, airlineName, model, departAirport, arriveAirport, departTime, arrivalTime FROM FLIGHT, SCHEDULE WHERE FLIGHT.flightID = SCHEDULE.flightID;";
			Statement stmt = null;
			ResultSet rs = null; 
		
			try {
				stmt = connect.createStatement();
				rs = stmt.executeQuery(available);
		     
				while(rs.next()) {
					String temp = String.format("%-2s %-20s %-18s %-14s %-17s %-20s %-1s", rs.getInt("flightID"), rs.getString("airlineName"), rs.getString("model"), rs.getString("departAirport"), rs.getString("arriveAirport"), rs.getInt("departTime"), rs.getInt("arrivalTime"));
					JLabel label2 = new JLabel(temp);
					label2.setFont(new Font("Serif", Font.PLAIN, 18));
					panel.add(label2);
				}
			} catch(SQLException se){
			   se.printStackTrace();
			}
			
			JLabel label4 = new JLabel("Enter Flight ID:");
			JTextField text = new JTextField(5);
			JButton button5 = new JButton("Enter");
			
			button5.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					JFrame frame4 = new JFrame();
					JPanel panel2 = new JPanel();
					JLabel label = new JLabel("Enter Name:");
					JTextField text2 = new JTextField(5);
					JButton button = new JButton("Enter");
					
					button.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent arg0) {
							ResultSet rs2 = null; 
							int ID = Integer.parseInt(text.getText());
							String name = text2.getText();
							String reserve = "INSERT INTO RESERVATION(flightID, class, type, timestamp) VALUES(?, 'Eco', 'Non-Stop', '1541140154');";
							String customer = "INSERT INTO CUSTOMER(name, reservationID, email) VALUES(?, ?, 'somethingfornow@gmail.com');";
							
							try {
							PreparedStatement pre = connect.prepareStatement(reserve, Statement.RETURN_GENERATED_KEYS);
							pre.setInt(1, ID);
							pre.executeUpdate();
							rs2 = pre.getGeneratedKeys();
							rs2.next();
							pre = connect.prepareStatement(customer);
							pre.setString(1, name);
							pre.setInt(2, rs2.getInt(1));
							pre.executeUpdate();
						
							} catch(SQLException se){
								 se.printStackTrace();
							}
							
							frame4.dispose();
						}
		
					});
					
					panel2.add(label);
					panel2.add(text2);
					panel2.add(button);
					
					frame4.add(panel2);
					frame4.pack();
					frame4.setLocationRelativeTo(null);
					frame4.setVisible(true);
					frame4.setResizable(false);
					frame2.dispose();
				}
				
			});
			
			JPanel panel3 = new JPanel();
			panel3.add(label4);
			panel3.add(text);
			panel3.add(button5);
			
			frame2.setLayout(new BoxLayout(frame2.getContentPane(), BoxLayout.PAGE_AXIS));
			frame2.add(label1);
			frame2.add(label3);
			frame2.add(panel);
			frame2.add(panel3);
			frame2.pack();
			frame2.setLocationRelativeTo(null);
			frame2.setVisible(true);
			frame2.setResizable(false);
		}
		
	});
	
	button2.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			JFrame frame = new JFrame();
			JPanel panel = new JPanel();
			JLabel label = new JLabel("Enter Reservation ID: ");
			JButton button = new JButton("Enter");
			JTextField text = new JTextField(5);
			JPanel reservations = new JPanel();
			reservations = getReservations(reservations, connect);
			reservations.setLayout(new BoxLayout(reservations, BoxLayout.PAGE_AXIS));
			
			button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					int ID = Integer.parseInt(text.getText());
					String reserve = "DELETE FROM RESERVATION WHERE reservationID = ?;";
					try {
					PreparedStatement pre = connect.prepareStatement(reserve);
					pre.setInt(1, ID);
					pre.executeUpdate();
					} catch(SQLException se){
						 se.printStackTrace();
					}
					
					frame.dispose();
				}
				
			});
			
			panel.add(label);
			panel.add(text);
			panel.add(button);
			
			frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.PAGE_AXIS));
			frame.add(reservations);
			frame.add(panel);
			frame.pack();
			frame.setLocationRelativeTo(null);
			frame.setVisible(true);
			frame.setResizable(false);
		}
		
	});
	
	button3.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			JFrame frame = new JFrame();
			JPanel panel = new JPanel();
			JLabel label = new JLabel("Enter Name:");
			JTextField text = new JTextField(10);
			JButton button = new JButton("Enter");
			
			button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					ResultSet rs2 = null; 
					JFrame frame2 = new JFrame();
					frame2.setLayout(new BoxLayout(frame2.getContentPane(), BoxLayout.PAGE_AXIS));
					JPanel panel2 = new JPanel();
					panel2.setLayout(new BoxLayout(panel2, BoxLayout.PAGE_AXIS));
					JLabel label2 = new JLabel(String.format("%-14s %-15s %-40s %-1s", "Customer ID", "Name", "Reservation ID", "E-mail"));
					label2.setFont(new Font("Serif", Font.PLAIN, 18));
					String name = text.getText();
					String show = "SELECT * FROM CUSTOMER WHERE name = ?";
					
					try {
					PreparedStatement pre = connect.prepareStatement(show);
					pre.setString(1, name);
					rs2 = pre.executeQuery();

					while(rs2.next()) {
						JLabel label3 = new JLabel(String.format("%-17s %-22s %-10s %-1s", rs2.getInt("cID"), rs2.getString("name"), rs2.getInt("reservationID"), rs2.getString("email")));
						label3.setFont(new Font("Serif", Font.PLAIN, 24));
						panel2.add(label3);
					}
					
					} catch(SQLException se){
						 se.printStackTrace();
					}
					
					frame2.add(label2);
					frame2.add(panel2);
					frame2.pack();
					frame2.setLocationRelativeTo(null);
					frame2.setVisible(true);
					frame2.setResizable(false);
					frame.dispose();
				}
			});
			panel.add(label);
			panel.add(text);
			panel.add(button);
			//panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
			
			frame.add(panel);
			frame.pack();
			frame.setLocationRelativeTo(null);
			frame.setVisible(true);
			frame.setResizable(false);
		}
		
	});
	
	button4.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			JFrame frame = new JFrame();
			JPanel panel = new JPanel();
			panel = getReservations(panel, connect);
			panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
			
			frame.add(panel);
			frame.pack();
			frame.setLocationRelativeTo(null);
			frame.setVisible(true);
			frame.setResizable(false);
		}
		
	});
	
	JLabel text = new JLabel("Welcome to SCK Flights");
	text.setFont(new Font("Serif", Font.PLAIN, 48));
	JPanel panel2 = new JPanel();
	panel2.add(text);
	panel1.add(button1);
	panel1.add(button2);
	panel1.add(button3);
	panel1.add(button4);
	frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.PAGE_AXIS));
	frame.add(panel2);
	frame.add(panel1);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.pack();
	frame.setLocationRelativeTo(null);
	frame.setVisible(true);
	frame.setResizable(false);
	}
	
	public static JPanel getReservations(JPanel panel, Connection connect) {
		Statement stmt = null;
		ResultSet rs = null;
		String check = "SELECT * FROM RESERVATION;";
		JLabel label = new JLabel(String.format("%-20s %-15s %-20s %-17s %-1s", "Reservation ID", "Flight ID", "Class", "Type", "Timestamp"));
		panel.add(label);
		
		try {
			stmt = connect.createStatement();
			rs = stmt.executeQuery(check);
	     
			while(rs.next()) {
				String temp = String.format("%-19s %-11s %-8s %-10s %-10s", rs.getInt("reservationID"), rs.getInt("flightID"), rs.getString("class"), rs.getString("type"), rs.getInt("timestamp"));
				JLabel label2 = new JLabel(temp);
				label2.setFont(new Font("Serif", Font.PLAIN, 18));
				panel.add(label2);
			}
		} catch(SQLException se){
		   se.printStackTrace();
		}
		
		return panel;
	}
}
