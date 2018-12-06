package airportdatabase;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

import javax.swing.*;

public class AirportMain2 {

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
    
		JFrame frame = new JFrame();
		frame.setSize(650,300);
		JPanel login = new JPanel();
		JButton button1 = new JButton("Log in");
		JButton button2 = new JButton("Create Account");
		
		final Connection connect = conn;
    
		button1.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			JFrame frame2 = new JFrame();
			JPanel panel = new JPanel();
			panel.setLayout(new GridLayout(3,2,-75,5));
			JLabel label1 = new JLabel("Email:  ");
			JLabel label2 = new JLabel("Password:   ");
			label1.setFont(new Font("Serif", Font.BOLD, 22));
			label2.setFont(new Font("Serif", Font.BOLD, 22));
			JTextField text1 = new JTextField(10);
			text1.setFont(new Font("Helvetica", Font.BOLD, 22));
			JTextField text2 = new JTextField(10);
			text2.setFont(new Font("Helvetica", Font.BOLD, 22));
			panel.add(label1);
			panel.add(text1);
			panel.add(label2);
			panel.add(text2);
			JButton ok = new JButton("Ok");
			JButton button4 = new JButton("Cancel");
			JLabel flag = new JLabel("Incorrect email/password. Try again");
			flag.setVisible(false);
			
			button4.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					frame2.dispose();
				}
			});
			
			ok.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					ResultSet rs2 = null; 
					String email = text1.getText();
					String password = text2.getText();
					String check = "SELECT COUNT(*) AS TOTAL FROM USER WHERE email = ? AND password = ?;";
							
					try {
						PreparedStatement pre = connect.prepareStatement(check);
						pre.setString(1, email);
						pre.setString(2, password);
						rs2 = pre.executeQuery();
						rs2.next();
						if(rs2.getInt("TOTAL") == 0)
							flag.setVisible(true);
						else {
							frame.dispose();
							frame2.dispose();
							mainMenu(connect);
							
						}
						
					} catch(SQLException se){
						se.printStackTrace();
					}
				}
			});
			
			JPanel buttons = new JPanel();
			buttons.add(ok);
			buttons.add(button4);
			panel.add(buttons);
			
			frame2.setLayout(new BoxLayout(frame2.getContentPane(), BoxLayout.PAGE_AXIS));
			frame2.add(panel);
			frame2.add(flag);
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
			panel.setLayout(new GridLayout(4,2,-30,10));
			JLabel nameLabel = new JLabel("Name:");
			nameLabel.setFont(new Font("Serif", Font.BOLD, 22));
			JTextField nameText = new JTextField(10);
			nameText.setFont(new Font("Helvetica", Font.BOLD, 22));
			panel.add(nameLabel);
			panel.add(nameText);
			JLabel emailLabel = new JLabel("E-Mail:");
			emailLabel.setFont(new Font("Serif", Font.BOLD, 22));
			JTextField emailText = new JTextField(10);
			emailText.setFont(new Font("Helvetica", Font.BOLD, 22));
			panel.add(emailLabel);
			panel.add(emailText);
			JLabel passwordLabel = new JLabel("Password:");
			passwordLabel.setFont(new Font("Serif", Font.BOLD, 22));
			JTextField passwordText = new JTextField(10);
			passwordText.setFont(new Font("Helvetica", Font.BOLD, 22));
			panel.add(passwordLabel);
			panel.add(passwordText);
			JRadioButton button1 = new JRadioButton("User");
			JRadioButton button2 = new JRadioButton("Admin");
			ButtonGroup group = new ButtonGroup();
			group.add(button1);
			group.add(button2);
			JPanel radioButtons = new JPanel();
			radioButtons.add(button1);
			radioButtons.add(button2);
			panel.add(radioButtons);
			JButton ok = new JButton("Ok");
			JButton cancel = new JButton("Cancel");
			
			cancel.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					frame.dispose();
				}
				
			});
			
			ok.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					ResultSet rs = null;
					String insert = "INSERT INTO USER VALUES(?, ?, ?);";
					String name = nameText.getText();
					String email = emailText.getText();
					String password = passwordText.getText();
					String userType;
					
					if(button1.isSelected())
						userType = "User";
					else if(button2.isSelected())
						userType = "Admin";
					else
						userType = "User";

					try {
						PreparedStatement pre = connect.prepareStatement(insert);
					pre.setString(1, email);
					pre.setString(2, password);
					pre.setString(3, userType);
					frame.dispose();
					pre.executeUpdate();
					} catch(SQLException se){
						 se.printStackTrace();
					}
				}
				
			});
			
			/*
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
					*/
			
			JPanel buttons = new JPanel();
			buttons.add(ok);
			buttons.add(cancel);
			panel.add(buttons);
			
			frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.PAGE_AXIS));
			frame.add(panel);
			frame.add(panel);
			frame.add(panel);
			frame.pack();
			frame.setLocationRelativeTo(null);
			frame.setVisible(true);
			frame.setResizable(false);
		}
		
	});
	
	
	
		JLabel text = new JLabel("Welcome to SCK Flights");
		text.setFont(new Font("Serif", Font.BOLD, 24));
		JPanel panel2 = new JPanel();
		panel2.add(text);
		login.add(button1);
		login.add(button2);
	frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.PAGE_AXIS));
	frame.add(panel2);
	frame.add(login);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.pack();
	frame.setLocationRelativeTo(null);
	frame.setVisible(true);
	frame.setResizable(false);
	}
	
	public static void mainMenu(Connection connect) {
		JFrame frame = new JFrame();
		frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.PAGE_AXIS));
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		JLabel title = new JLabel("Welcome to SCK Flights");
		title.setFont(new Font("Helvetica", Font.BOLD, 48));
		JButton createReservation = new JButton("Create Reservation");
		JButton cancelReservation = new JButton("Cancel Reservation");
		JButton viewSchedule = new JButton("View Schedule");
		JButton viewReservation = new JButton("View Reservation");
		JButton modifyInformation = new JButton("Modify Information");
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		
		createReservation.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFrame frame = new JFrame();
				JPanel panel = new JPanel();
				panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
				JLabel title = new JLabel("Select your flight:");
				JLabel label3 = new JLabel(String.format("%-5s %-23s %-10s %-15s %-15s %-16s %-1s", "ID", "Airlines", "Model", "Depart from", "Arrive at", "Depart Time", "Arrival Time"));
				title.setFont(new Font("Serif", Font.PLAIN, 24));
				label3.setFont(new Font("Serif", Font.PLAIN, 18));
				panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
				String availableFlights = "SELECT airlineName FROM Flight;";
				String numberOfFlights = "SELECT count(*) AS TOTAL FROM Flight;";
				Statement stmt = null;
				ResultSet rs = null; 
			
				try {
					stmt = connect.createStatement();
					rs = stmt.executeQuery(numberOfFlights);
					rs.next();
					int index = rs.getInt("TOTAL");
					JRadioButton[] buttons = new JRadioButton[index];
					ButtonGroup groups = new ButtonGroup();
					
					rs = stmt.executeQuery(availableFlights);
					rs.next();
					
					for(int i = 0; i < buttons.length; i++) {
						buttons[i] = new JRadioButton(rs.getString("airlineName"));
						rs.next();
						groups.add(buttons[i]);
						panel.add(buttons[i]);
					}
					
					for(int i = 0; i < buttons.length; i++) {
						System.out.print(i);
						if(buttons[i].isSelected())
							System.out.println("hi" + i);
					}
					
					frame.add(panel);
					
				} catch(SQLException se){
				   se.printStackTrace();
				}
				
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.pack();
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
				frame.setResizable(false);
				
			}
		});
				
		
		panel.add(createReservation, gbc);
		panel.add(cancelReservation, gbc);
		panel.add(viewSchedule, gbc);
		panel.add(viewReservation, gbc);
		panel.add(modifyInformation, gbc);
		frame.add(title);
		frame.add(panel);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setResizable(false);
	}
}
