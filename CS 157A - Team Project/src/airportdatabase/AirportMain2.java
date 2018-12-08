import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Vector;
import java.util.Date;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class AirportMain {

	// JDBC driver name and database URL
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	static final String DB_URL = "jdbc:mysql://localhost/SCKFlightDatabase";

	//  Database credentials
	static final String USER = "root";
	static final String PASS = "laishingling"; //replace with your password
	
	static String EMAIL = "";
	static String USERPASS = "";
	   
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
    
		//GUI declarations
		JFrame frame = new JFrame();
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
				    EMAIL = text1.getText();
					USERPASS = text2.getText();
					String userType;
					String check = "SELECT * FROM USER WHERE email = ? AND password = ?;";
							
					try {
						PreparedStatement pre = connect.prepareStatement(check);
						pre.setString(1, EMAIL);
						pre.setString(2, USERPASS);
						rs2 = pre.executeQuery();
						if(!rs2.next())
							flag.setVisible(true);
						else {
							userType = rs2.getString("userType");
							userType = userType.toLowerCase();
							
							frame.dispose();
							frame2.dispose();
							mainMenu(connect, userType);
							
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
						userType = "admin";
					else
						userType = "user";

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
	
	public static void mainMenu(Connection connection, String userType) {
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
		JButton viewStatistics = new JButton("View Statistics");
		JButton modifySchedule = new JButton("Modify Schedule");
		JButton addNewItem = new JButton ("Add New Item");
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		JLabel loggedIn = new JLabel();
		loggedIn.setFont(new Font("Helvetica", Font.BOLD, 24));
		
		createReservation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg1) {
				
				JFrame frame = new JFrame();
				JPanel panel = new JPanel();
				panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
				JLabel title = new JLabel("Please select a flight:");
				JButton okButton = new JButton("OK");
				JButton cancelButton = new JButton("Cancel");
				title.setFont(new Font("Helvetica", Font.BOLD, 36));
				String listSchedule = "SELECT Flight.flightID, airlineName, model, departAirport, arriveAirport, date, TIME_FORMAT(departTime, '%h:%i %p') departTime, TIME_FORMAT(arrivalTime, '%h:%i %p') arrivalTime FROM FLIGHT, SCHEDULE WHERE FLIGHT.flightID = SCHEDULE.flightID ORDER BY departTime;";
				Statement stmt = null;
				ResultSet rs = null;
				try {
					stmt = connection.createStatement();
					rs = stmt.executeQuery(listSchedule);
					ResultSetMetaData metaData = rs.getMetaData();
				    int columnCount = metaData.getColumnCount();
				    Vector<String> columnNames = new Vector<String>();
				    columnNames.add("ID");
				    columnNames.add("Airline");
				    columnNames.add("Airplane Model");
				    columnNames.add("Departure Airport");
				    columnNames.add("Arrival Airport");
				    columnNames.add("Date");
				    columnNames.add("Departure Time");
				    columnNames.add("Arrival Time");
					Vector<Vector<Object>> data = new Vector<Vector<Object>>();
				    while (rs.next()) {
				        Vector<Object> vector = new Vector<Object>();
				        for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
				            vector.add(rs.getObject(columnIndex));
				        }
				        data.add(vector);
				    }
				    DefaultTableModel myModel = new DefaultTableModel(data, columnNames) {
				    	@Override
				        public boolean isCellEditable(int row, int column) {
				           return false;
				        }
				    };
				    JTable table = new JTable(myModel);
				    JScrollPane scrollPane = new JScrollPane(table);
				    table.setFillsViewportHeight(true);
				    
				    okButton.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent arg0) {
						int flightID = (int) table.getValueAt(table.getSelectedRow(), 0);
						JFrame innerFrame = new JFrame();
						JPanel innerPanel = new JPanel();
						innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.PAGE_AXIS));
						JLabel title = new JLabel("Please choose from the following:");
						JLabel namePrompt = new JLabel("Enter name:");
						JLabel priceLabel = new JLabel("Price: ");
						JTextField nameTextField = new JTextField(20);
						JRadioButton classButton1 = new JRadioButton("First Class");
						classButton1.setActionCommand("First Class");
						classButton1.addActionListener(new ActionListener() {
					        @Override
					        public void actionPerformed(ActionEvent e) {
					            priceLabel.setText("Price: $900");

					        }
					    });
						JRadioButton classButton2 = new JRadioButton("Business");
						classButton2.setActionCommand("Business");
						classButton2.addActionListener(new ActionListener() {
					        @Override
					        public void actionPerformed(ActionEvent e) {
					            priceLabel.setText("Price: $500");

					        }
					    });
						JRadioButton classButton3 = new JRadioButton("Economy");
						classButton3.setActionCommand("Economy");
						classButton3.addActionListener(new ActionListener() {
					        @Override
					        public void actionPerformed(ActionEvent e) {
					            priceLabel.setText("Price: $300");

					        }
					    });
						ButtonGroup classGroup = new ButtonGroup();
					    classGroup.add(classButton1);
					    classGroup.add(classButton2);
					    classGroup.add(classButton3);
					    JRadioButton typeButton1 = new JRadioButton("Non-stop");
					    typeButton1.setActionCommand("Non-stop");
					    JRadioButton typeButton2 = new JRadioButton("Connecting");
					    typeButton2.setActionCommand("Connecting");
					    ButtonGroup typeGroup = new ButtonGroup();
					    typeGroup.add(typeButton1);
					    typeGroup.add(typeButton2);
						JButton innerOkButton = new JButton("Ok");
						JButton innerCancelButton = new JButton("Cancel");
						innerPanel.add(title);
						innerPanel.add(namePrompt);
						innerPanel.add(nameTextField);
						innerPanel.add(classButton1);
						innerPanel.add(classButton2);
						innerPanel.add(classButton3);
						innerPanel.add(typeButton1);
						innerPanel.add(typeButton2);
						innerPanel.add(priceLabel);
						innerPanel.add(innerOkButton);
						innerPanel.add(innerCancelButton);
						innerFrame.add(innerPanel);
						innerFrame.pack();
						innerFrame.setLocationRelativeTo(null);
						innerFrame.setVisible(true);
						innerFrame.setResizable(true);
						
						innerOkButton.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent arg0) {
								int price = Integer.parseInt(priceLabel.getText().substring(8, priceLabel.getText().length()));
								String resInsert = "insert into reservation(flightID, class, type, updatedOn, price) values (?, ?, ?, CURDATE(), ?);";
								String cusInsert = "insert into customer(name, reservationID, email) values(?, ?, ?);";
								PreparedStatement pre;
								PreparedStatement pre2;
								try {
									pre = connection.prepareStatement(resInsert);
									pre.setInt(1, flightID);
									pre.setString(2, classGroup.getSelection().getActionCommand());
									pre.setString(3, typeGroup.getSelection().getActionCommand());
									pre.setInt(4, price);
									pre.executeUpdate();
									Statement stmt2 = connection.createStatement();
									ResultSet rs2 = stmt2.executeQuery("select max(reservationID) from reservation;");
									rs2.next();
									int reservationID = rs2.getInt("max(reservationID)");
									pre2 = connection.prepareStatement(cusInsert);
									pre2.setString(1, nameTextField.getText());
									pre2.setInt(2, reservationID);
									pre2.setString(3, EMAIL);
									pre2.executeUpdate();
									
									System.out.println("Confirmed reservation.");
									innerFrame.dispose();
									frame.dispose();
								} catch (SQLException e) {
									e.printStackTrace();
								}
							}
						});
						
						innerCancelButton.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent arg0) {
								innerFrame.dispose();
							}
						});
						
						}
					});
					
					cancelButton.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent arg0) {
							frame.dispose();
						}
					});
				    
				    panel.add(title);
				    panel.add(scrollPane);
				    panel.add(okButton);
				    panel.add(cancelButton);
					frame.add(panel);
					frame.pack();
					frame.setLocationRelativeTo(null);
					frame.setVisible(true);
					frame.setResizable(true);
				} catch(SQLException se){
						se.printStackTrace();
						}
			}
		});
		
		cancelReservation.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				JFrame cancelFrame = new JFrame();
				JPanel cancelPanel = new JPanel();
				cancelPanel.setLayout(new BoxLayout(cancelPanel, BoxLayout.PAGE_AXIS));
				JLabel nameLabel = new JLabel("Select a reservation:");
				String cancelQuery = "select Reservation.reservationID, airlineName, class, departAirport, arriveAirport, date, TIME_FORMAT(departTime, '%h:%i %p') departTime, TIME_FORMAT(arrivalTime, '%h:%i %p') arrivalTime, type, price from flight, reservation, schedule where reservation.flightID = flight.flightID and reservation.flightID = schedule.flightID and reservationID in (select reservationID from customer where email = '"+EMAIL+"') order by date;";
				if(userType.equals("admin"))
					cancelQuery = "select Reservation.reservationID, name, airlineName, class, departAirport, arriveAirport, date, TIME_FORMAT(departTime, '%h:%i %p') departTime, TIME_FORMAT(arrivalTime, '%h:%i %p') arrivalTime, type, price from flight, reservation, schedule, customer where customer.reservationID = reservation.reservationID and reservation.flightID = flight.flightID and reservation.flightID = schedule.flightID order by date;";
				Statement stmt = null;
				ResultSet rs = null;
				try {
					stmt = connection.createStatement();
					rs = stmt.executeQuery(cancelQuery);
					ResultSetMetaData metaData = rs.getMetaData();
				    int columnCount = metaData.getColumnCount();
				    Vector<String> columnNames = new Vector<String>();
				    columnNames.add("ID");
				    columnNames.add("Airline");
				    columnNames.add("Class");
				    columnNames.add("Departure Airport");
				    columnNames.add("Arrival Airport");
				    columnNames.add("Date");
				    columnNames.add("Departure Time");
				    columnNames.add("Arrival Time");
				    columnNames.add("Type");
				    columnNames.add("Price");
					Vector<Vector<Object>> data = new Vector<Vector<Object>>();
				    while (rs.next()) {
				        Vector<Object> vector = new Vector<Object>();
				        for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
				            vector.add(rs.getObject(columnIndex));
				        }
				        data.add(vector);
				    }
				    DefaultTableModel myModel = new DefaultTableModel(data, columnNames) {
				    	@Override
				        public boolean isCellEditable(int row, int column) {
				           return false;
				        }
				    };
				    JTable cancelTable = new JTable(myModel);
				    JScrollPane cancelScrollPane = new JScrollPane(cancelTable);
				    cancelTable.setFillsViewportHeight(true);
				    JButton cancelOKButton = new JButton("Ok");
				    cancelOKButton.addActionListener(new ActionListener() {
				    	public void actionPerformed(ActionEvent arg0) {
				    		String resDelete = "delete from reservation where reservationID = ?";
				    		PreparedStatement pre;
							try {
								int reservationID = (int) cancelTable.getValueAt(cancelTable.getSelectedRow(), 0);
								pre = connection.prepareStatement(resDelete);
								pre.setInt(1, reservationID);
								pre.executeUpdate();
								System.out.println("Confirmed cancellation.");
								cancelFrame.dispose();
								frame.dispose();
							} catch (SQLException e) {
								e.printStackTrace();		
				    	}
					}
				    });
				    JButton cancelCancelButton = new JButton("Cancel");
				    cancelCancelButton.addActionListener(new ActionListener() {
				    	public void actionPerformed(ActionEvent arg0) {
				    		cancelFrame.dispose();
				    	}
				    });
				    cancelPanel.add(nameLabel);
				    cancelPanel.add(cancelScrollPane);
				    cancelPanel.add(cancelOKButton);
				    cancelPanel.add(cancelCancelButton);
				    cancelFrame.add(cancelPanel);
				    cancelFrame.pack();
					cancelFrame.setLocationRelativeTo(null);
					cancelFrame.setVisible(true);
					cancelFrame.setResizable(true);
				    
			} catch (SQLException s) {
				s.printStackTrace();}
			}
		});
		
		viewSchedule.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFrame viewFrame = new JFrame();
				JPanel viewPanel = new JPanel();
				viewPanel.setLayout(new BoxLayout(viewPanel, BoxLayout.PAGE_AXIS));
				JLabel viewTitle = new JLabel("Schedule");
				String listSchedule = "SELECT Flight.flightID, airlineName, model, departAirport, arriveAirport, date, TIME_FORMAT(departTime, '%h:%i %p') departTime, TIME_FORMAT(arrivalTime, '%h:%i %p') arrivalTime FROM FLIGHT, SCHEDULE WHERE FLIGHT.flightID = SCHEDULE.flightID ORDER BY departTime;";
				Statement stmt = null;
				ResultSet rs = null;
				try {
					stmt = connection.createStatement();
					rs = stmt.executeQuery(listSchedule);
					ResultSetMetaData metaData = rs.getMetaData();
				    int columnCount = metaData.getColumnCount();
				    Vector<String> columnNames = new Vector<String>();
				    columnNames.add("ID");
				    columnNames.add("Airline");
				    columnNames.add("Airplane Model");
				    columnNames.add("Departure Airport");
				    columnNames.add("Arrival Airport");
				    columnNames.add("Date");
				    columnNames.add("Departure Time");
				    columnNames.add("Arrival Time");
					Vector<Vector<Object>> data = new Vector<Vector<Object>>();
				    while (rs.next()) {
				        Vector<Object> vector = new Vector<Object>();
				        for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
				            vector.add(rs.getObject(columnIndex));
				        }
				        data.add(vector);
				    }
				    DefaultTableModel myModel = new DefaultTableModel(data, columnNames) {
				    	@Override
				        public boolean isCellEditable(int row, int column) {
				           return false;
				        }
				    };
				    JTable viewTable = new JTable(myModel);
				    JScrollPane viewScrollPane = new JScrollPane(viewTable);
				    viewTable.setFillsViewportHeight(true);
				    viewPanel.add(viewTitle);
				    viewPanel.add(viewScrollPane);
				    viewFrame.add(viewPanel);
				    viewFrame.pack();
					viewFrame.setLocationRelativeTo(null);
					viewFrame.setVisible(true);
					viewFrame.setResizable(true);
			} catch(SQLException se) {
				se.printStackTrace();
				}
			
			}
		});
		
		viewReservation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame viewResFrame = new JFrame();
				JPanel viewResPanel = new JPanel();
				viewResPanel.setLayout(new BoxLayout(viewResPanel, BoxLayout.PAGE_AXIS));
				JLabel nameLabel = new JLabel("Reservations");
				String viewQuery = "select Reservation.reservationID, name, airlineName, class, departAirport, arriveAirport, date, TIME_FORMAT(departTime, '%h:%i %p') departTime, TIME_FORMAT(arrivalTime, '%h:%i %p') arrivalTime, type, price from flight, reservation, schedule, customer where customer.reservationID = reservation.reservationID and reservation.flightID = flight.flightID and reservation.flightID = schedule.flightID and Reservation.reservationID in (select reservationID from customer where email = '"+EMAIL+"') order by date;";
				if(userType.equals("admin"))
					viewQuery = "select Reservation.reservationID, name, airlineName, class, departAirport, arriveAirport, date, TIME_FORMAT(departTime, '%h:%i %p') departTime, TIME_FORMAT(arrivalTime, '%h:%i %p') arrivalTime, type, price from flight, reservation, schedule, customer where customer.reservationID = reservation.reservationID and reservation.flightID = flight.flightID and reservation.flightID = schedule.flightID order by date;";
				Statement stmt = null;
				ResultSet rs = null;
				try {
					stmt = connection.createStatement();
					rs = stmt.executeQuery(viewQuery);
					ResultSetMetaData metaData = rs.getMetaData();
				    int columnCount = metaData.getColumnCount();
				    Vector<String> columnNames = new Vector<String>();
				    columnNames.add("ID");
				    columnNames.add("Name");
				    columnNames.add("Airline");
				    columnNames.add("Class");
				    columnNames.add("Departure Airport");
				    columnNames.add("Arrival Airport");
				    columnNames.add("Date");
				    columnNames.add("Departure Time");
				    columnNames.add("Arrival Time");
				    columnNames.add("Type");
				    columnNames.add("Price");
					Vector<Vector<Object>> data = new Vector<Vector<Object>>();
				    while (rs.next()) {
				        Vector<Object> vector = new Vector<Object>();
				        for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
				            vector.add(rs.getObject(columnIndex));
				        }
				        data.add(vector);
				    }
				    DefaultTableModel myModel = new DefaultTableModel(data, columnNames) {
				    	@Override
				        public boolean isCellEditable(int row, int column) {
				           return false;
				        }
				    };
				    JTable viewResTable = new JTable(myModel);
				    JScrollPane viewResScrollPane = new JScrollPane(viewResTable);
				    viewResTable.setFillsViewportHeight(true);
				    viewResPanel.add(nameLabel);
				    viewResPanel.add(viewResScrollPane);
				    viewResFrame.add(viewResPanel);
				    viewResFrame.pack();
					viewResFrame.setLocationRelativeTo(null);
					viewResFrame.setVisible(true);
					viewResFrame.setResizable(true);
			} catch(SQLException se) {
				se.printStackTrace();
				}
			}
		});
		
		modifyInformation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame modFrame = new JFrame();
				JPanel modPanel = new JPanel();
				modPanel.setLayout(new BoxLayout(modPanel, BoxLayout.PAGE_AXIS));
				JLabel emailLabel = new JLabel("Email: "+EMAIL);
				JButton changeEmail = new JButton("Change");
				JLabel passLabel = new JLabel("Password: "+USERPASS);
				JButton changePass = new JButton("Change");
				changeEmail.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						JFrame emailFrame = new JFrame();
						JPanel emailPanel = new JPanel();
						emailPanel.setLayout(new BoxLayout(emailPanel, BoxLayout.PAGE_AXIS));
						JLabel newEmail = new JLabel("New Email: ");
						JTextField newEmailField = new JTextField(EMAIL);
						JLabel passConfirm = new JLabel("Password");
						JTextField passConfirmField = new JTextField(20);
						JButton emailOK = new JButton("Ok");
						JButton emailCancel = new JButton("Cancel");
						
						emailOK.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								if (passConfirmField.getText().equals(USERPASS)) {
								String emailQuery = "update user set email = ? where email = ?";
								PreparedStatement pre;
								try {
									pre = connection.prepareStatement(emailQuery);
									pre.setString(1, newEmailField.getText());
									pre.setString(2, EMAIL);
									pre.executeUpdate();								
									System.out.println("Confirmed update of email.");
									EMAIL = newEmailField.getText();
									emailFrame.dispose();
									modFrame.dispose();
								} catch (SQLException ex) {
									ex.printStackTrace();
								}}
								else System.out.println("Invalid password");
							}
						});
						emailCancel.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								emailFrame.dispose();
							}
						});
						emailPanel.add(newEmail);
						emailPanel.add(newEmailField);
						emailPanel.add(passConfirm);
						emailPanel.add(passConfirmField);
						emailPanel.add(emailOK);
						emailPanel.add(emailCancel);
						emailFrame.add(emailPanel);
						emailFrame.pack();
						emailFrame.setLocationRelativeTo(null);
						emailFrame.setVisible(true);
						emailFrame.setResizable(true);
					}
				});
				changePass.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						JFrame passFrame = new JFrame();
						JPanel passPanel = new JPanel();
						passPanel.setLayout(new BoxLayout(passPanel, BoxLayout.PAGE_AXIS));
						JLabel newPass = new JLabel("New Password: ");
						JTextField newPassField = new JTextField(20);
						JLabel oldPass = new JLabel("Old Password: ");
						JTextField oldPassField = new JTextField(20);
						JButton passOK = new JButton("Ok");
						JButton passCancel = new JButton("Cancel");
						
						passOK.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								if (oldPassField.getText().equals(USERPASS)) {
								String passQuery = "update user set password = ? where password = ?";
								PreparedStatement pre;
								try {
									pre = connection.prepareStatement(passQuery);
									pre.setString(1, newPassField.getText());
									pre.setString(2, USERPASS);
									pre.executeUpdate();								
									System.out.println("Confirmed update of password.");
									USERPASS = newPassField.getText();
									passFrame.dispose();
									modFrame.dispose();
								} catch (SQLException ex) {
									ex.printStackTrace();
								}}
								else System.out.println("Invalid password");
							}
						});
						passCancel.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								passFrame.dispose();
							}
						});
						passPanel.add(newPass);
						passPanel.add(newPassField);
						passPanel.add(oldPass);
						passPanel.add(oldPassField);
						passPanel.add(passOK);
						passPanel.add(passCancel);
						passFrame.add(passPanel);
						passFrame.pack();
						passFrame.setLocationRelativeTo(null);
						passFrame.setVisible(true);
						passFrame.setResizable(true);
					}
				});
				modPanel.add(emailLabel);
				modPanel.add(changeEmail);
				modPanel.add(passLabel);
				modPanel.add(changePass);
				modFrame.add(modPanel);
				modFrame.pack();
				modFrame.setLocationRelativeTo(null);
				modFrame.setVisible(true);
				modFrame.setResizable(true);
				
			}
		});
		
		viewStatistics.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame viewStatsFrame = new JFrame();
				JPanel viewStatsPanel = new JPanel();
				viewStatsPanel.setLayout(new BoxLayout(viewStatsPanel, BoxLayout.PAGE_AXIS));
				JButton viewSales = new JButton("View Sales of All Flights For Past Month");
				JButton viewUnsold = new JButton("View Unsold Seats");
				JButton viewFrequent = new JButton("View Frequent Fliers");
				viewSales.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						JFrame salesFrame = new JFrame();
						JPanel salesPanel = new JPanel();
						salesPanel.setLayout(new BoxLayout(salesPanel, BoxLayout.PAGE_AXIS));
						String salesQuery = "select airlineName, sum(price) from reservation, flight where reservation.flightID = flight.flightID and updatedOn <= CURDATE() and updatedOn >= DATE_SUB(NOW(), INTERVAL 1 MONTH) group by airlineName;";
						Statement salesStmt = null;
						ResultSet salesRs = null;
						try {
							salesStmt = connection.createStatement();
							salesRs = salesStmt.executeQuery(salesQuery);
							ResultSetMetaData metaData = salesRs.getMetaData();
						    int columnCount = metaData.getColumnCount();
						    Vector<String> columnNames = new Vector<String>();
						    columnNames.add("Airline");
						    columnNames.add("Amount");
							Vector<Vector<Object>> data = new Vector<Vector<Object>>();
						    while (salesRs.next()) {
						        Vector<Object> vector = new Vector<Object>();
						        for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
						            vector.add(salesRs.getObject(columnIndex));
						        }
						        data.add(vector);
						    }
						    DefaultTableModel myModel = new DefaultTableModel(data, columnNames) {
						    	@Override
						        public boolean isCellEditable(int row, int column) {
						           return false;
						        }
						    };
						    JTable salesTable = new JTable(myModel);
						    JScrollPane salesScrollPane = new JScrollPane(salesTable);
						    salesTable.setFillsViewportHeight(true);
						    salesPanel.add(salesScrollPane);
						    salesFrame.add(salesPanel);
							salesFrame.pack();
							salesFrame.setLocationRelativeTo(null);
							salesFrame.setVisible(true);
							salesFrame.setResizable(true);
					} catch (SQLException se) {
						se.printStackTrace();}
					}
				});
				viewUnsold.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						JFrame unsoldFrame = new JFrame();
						JPanel unsoldPanel = new JPanel();
						unsoldPanel.setLayout(new BoxLayout(unsoldPanel, BoxLayout.PAGE_AXIS));
						String unsoldQuery = "select airlineName, round(avg(availableSeats)) from flight where flightid in (select flightid from reservation where updatedon <= curdate() and updatedon >= date_sub(now(), interval 1 month)) group by airlinename;";
						Statement unsoldStmt = null;
						ResultSet unsoldRs = null;
						try {
							unsoldStmt = connection.createStatement();
							unsoldRs = unsoldStmt.executeQuery(unsoldQuery);
							ResultSetMetaData metaData = unsoldRs.getMetaData();
						    int columnCount = metaData.getColumnCount();
						    Vector<String> columnNames = new Vector<String>();
						    columnNames.add("Airline");
						    columnNames.add("Average Number of Unsold Seats");
							Vector<Vector<Object>> data = new Vector<Vector<Object>>();
						    while (unsoldRs.next()) {
						        Vector<Object> vector = new Vector<Object>();
						        for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
						            vector.add(unsoldRs.getObject(columnIndex));
						        }
						        data.add(vector);
						    }
						    DefaultTableModel myModel = new DefaultTableModel(data, columnNames) {
						    	@Override
						        public boolean isCellEditable(int row, int column) {
						           return false;
						        }
						    };
						    JTable unsoldTable = new JTable(myModel);
						    JScrollPane unsoldScrollPane = new JScrollPane(unsoldTable);
						    unsoldTable.setFillsViewportHeight(true);
						    unsoldPanel.add(unsoldScrollPane);
						    unsoldFrame.add(unsoldPanel);
							unsoldFrame.pack();
							unsoldFrame.setLocationRelativeTo(null);
							unsoldFrame.setVisible(true);
							unsoldFrame.setResizable(true);
					} catch (SQLException se) {
						se.printStackTrace();}
					}
				});
				viewFrequent.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						JFrame frequentFrame = new JFrame();
						JPanel frequentPanel = new JPanel();
						frequentPanel.setLayout(new BoxLayout(frequentPanel, BoxLayout.PAGE_AXIS));
						String frequentQuery = "select email from customer where reservationID in (select reservationID from reservation where updatedon <= curdate() and updatedon >= date_sub(now(), interval 24 month)) group by email having count(*) >= 4;";
						Statement frequentStmt = null;
						ResultSet frequentRs = null;
						try {
							frequentStmt = connection.createStatement();
							frequentRs = frequentStmt.executeQuery(frequentQuery);
							ResultSetMetaData metaData = frequentRs.getMetaData();
						    int columnCount = metaData.getColumnCount();
						    Vector<String> columnNames = new Vector<String>();
						    columnNames.add("Email");
							Vector<Vector<Object>> data = new Vector<Vector<Object>>();
						    while (frequentRs.next()) {
						        Vector<Object> vector = new Vector<Object>();
						        for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
						            vector.add(frequentRs.getObject(columnIndex));
						        }
						        data.add(vector);
						    }
						    DefaultTableModel myModel = new DefaultTableModel(data, columnNames) {
						    	@Override
						        public boolean isCellEditable(int row, int column) {
						           return false;
						        }
						    };
						    JTable frequentTable = new JTable(myModel);
						    JScrollPane frequentScrollPane = new JScrollPane(frequentTable);
						    frequentTable.setFillsViewportHeight(true);
						    frequentPanel.add(frequentScrollPane);
						    frequentFrame.add(frequentPanel);
						    frequentFrame.pack();
						    frequentFrame.setLocationRelativeTo(null);
						    frequentFrame.setVisible(true);
						    frequentFrame.setResizable(true);
					} catch (SQLException se) {
						se.printStackTrace();}
					}
				});
				viewStatsPanel.add(viewSales);
				viewStatsPanel.add(viewUnsold);
				viewStatsPanel.add(viewFrequent);
				viewStatsFrame.add(viewStatsPanel);
				viewStatsFrame.pack();
				viewStatsFrame.setLocationRelativeTo(null);
				viewStatsFrame.setVisible(true);
				viewStatsFrame.setResizable(true);
			}
		});
		
		modifySchedule.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame modSFrame = new JFrame();
				JPanel modSPanel = new JPanel();
				modSPanel.setLayout(new BoxLayout(modSPanel, BoxLayout.PAGE_AXIS));
				JLabel modSTitle = new JLabel("Choose a flight: ");
				String modSSchedule = "SELECT Flight.flightID, airlineName, model, departAirport, arriveAirport, date, TIME_FORMAT(departTime, '%h:%i %p') departTime, TIME_FORMAT(arrivalTime, '%h:%i %p') arrivalTime FROM FLIGHT, SCHEDULE WHERE FLIGHT.flightID = SCHEDULE.flightID ORDER BY departTime;";
				Statement modSStmt = null;
				ResultSet modSRs = null;
				try {
					modSStmt = connection.createStatement();
					modSRs = modSStmt.executeQuery(modSSchedule);
					ResultSetMetaData metaData = modSRs.getMetaData();
				    int columnCount = metaData.getColumnCount();
				    Vector<String> columnNames = new Vector<String>();
				    columnNames.add("ID");
				    columnNames.add("Airline");
				    columnNames.add("Airplane Model");
				    columnNames.add("Departure Airport");
				    columnNames.add("Arrival Airport");
				    columnNames.add("Date");
				    columnNames.add("Departure Time");
				    columnNames.add("Arrival Time");
					Vector<Vector<Object>> data = new Vector<Vector<Object>>();
				    while (modSRs.next()) {
				        Vector<Object> vector = new Vector<Object>();
				        for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
				            vector.add(modSRs.getObject(columnIndex));
				        }
				        data.add(vector);
				    }
				    DefaultTableModel myModel = new DefaultTableModel(data, columnNames) {
				    	@Override
				        public boolean isCellEditable(int row, int column) {
				           return false;
				        }
				    };
				    JTable modSTable = new JTable(myModel);
				    JScrollPane modSScrollPane = new JScrollPane(modSTable);
				    modSTable.setFillsViewportHeight(true);
				    JButton modSOK = new JButton("Ok");
				    modSOK.addActionListener(new ActionListener() {
				    	public void actionPerformed(ActionEvent a) {
				    		int selectedRow = modSTable.getSelectedRow();
				    		int flightID = (int) modSTable.getValueAt(selectedRow, 0);
				    		String selectedAirline = (String) modSTable.getValueAt(selectedRow, 1);
				    		String selectedModel = (String) modSTable.getValueAt(selectedRow, 2);
				    		String selectedDepartAir = (String) modSTable.getValueAt(selectedRow, 3);
				    		String selectedArrivalAir = (String) modSTable.getValueAt(selectedRow, 4);
				    		Object selectedDate = modSTable.getValueAt(selectedRow, 5);
				    		String selectedDate2 = selectedDate.toString();
				    		String selectedDepartTime = (String) modSTable.getValueAt(selectedRow, 6);
				    		String selectedArrivalTime = (String) modSTable.getValueAt(selectedRow, 7);
				    		JFrame modSOKFrame = new JFrame();
							JPanel modSOKPanel = new JPanel();
							modSOKPanel.setLayout(new BoxLayout(modSOKPanel, BoxLayout.PAGE_AXIS));
							JLabel modSOK1 = new JLabel("Airline: ");
							JLabel modSOK2 = new JLabel("Airplane Model: ");
							JLabel modSOK3 = new JLabel("Departure Airport: ");
							JLabel modSOK4 = new JLabel("Arrival Airport: ");
							JLabel modSOK5 = new JLabel("Date: ");
							JLabel modSOK6 = new JLabel("Departure Time: ");
							JLabel modSOK7 = new JLabel("Arrival Time: ");
							JTextField modSText1 = new JTextField(selectedAirline);
							JTextField modSText2 = new JTextField(selectedModel);
							JTextField modSText3 = new JTextField(selectedDepartAir);
							JTextField modSText4 = new JTextField(selectedArrivalAir);
							JTextField modSText5 = new JTextField(selectedDate2);
							JTextField modSText6 = new JTextField(selectedDepartTime);
							modSText6.setText(modSText6.getText().substring(0, 5)+":00");
							JTextField modSText7 = new JTextField(selectedArrivalTime);
							modSText7.setText(modSText7.getText().substring(0, 5)+":00");
							JButton modSInnerOK = new JButton("Ok");
							modSInnerOK.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent ae) {
									try {
										Statement batchStmt = connection.createStatement();
										if (!modSText1.getText().equals(selectedAirline)) {
											batchStmt.addBatch("update flight set airlineName ='"+modSText1.getText()+"' where flightID = "+flightID);
										}
										if (!modSText2.getText().equals(selectedModel)) {
											batchStmt.addBatch("update flight set model ='"+modSText2.getText()+"' where flightID = "+flightID);
										}
										if (!modSText3.getText().equals(selectedDepartAir)) {
											batchStmt.addBatch("update flight set departairport ='"+modSText3.getText()+"' where flightID = "+flightID);
										}
										if (!modSText4.getText().equals(selectedArrivalAir)) {
											batchStmt.addBatch("update flight set arriveairport ='"+modSText4.getText()+"' where flightID = "+flightID);
										}
										if (!modSText5.getText().equals(selectedDate2)) {
											batchStmt.addBatch("update schedule set date ='"+modSText5.getText()+"' where flightID = "+flightID);
										}
										if (!modSText6.getText().equals(selectedDepartTime)) {
											batchStmt.addBatch("update schedule set departtime ='"+modSText6.getText()+"' where flightID = "+flightID);
										}
										if (!modSText7.getText().equals(selectedArrivalTime)) {
											batchStmt.addBatch("update schedule set arrivaltime ='"+modSText7.getText()+"' where flightID = "+flightID);
										}
										batchStmt.executeBatch();
										System.out.println("Updates confirmed.");
										modSOKFrame.dispose();
									} catch (SQLException e) {
										e.printStackTrace();
									}
								}
							});
							JButton modSInnerCancel = new JButton("Cancel");
							modSInnerCancel.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent ae) {
									modSOKFrame.dispose();
								}
							});
							modSOKPanel.add(modSOK1);
							modSOKPanel.add(modSText1);
							modSOKPanel.add(modSOK2);
							modSOKPanel.add(modSText2);
							modSOKPanel.add(modSOK3);
							modSOKPanel.add(modSText3);
							modSOKPanel.add(modSOK4);
							modSOKPanel.add(modSText4);
							modSOKPanel.add(modSOK5);
							modSOKPanel.add(modSText5);
							modSOKPanel.add(modSOK6);
							modSOKPanel.add(modSText6);
							modSOKPanel.add(modSOK7);
							modSOKPanel.add(modSText7);
							modSOKPanel.add(modSInnerOK);
							modSOKPanel.add(modSInnerCancel);
							modSOKFrame.add(modSOKPanel);
							modSOKFrame.pack();
							modSOKFrame.setLocationRelativeTo(null);
							modSOKFrame.setVisible(true);
							modSOKFrame.setResizable(true);
				    	}
				    });
				    JButton modSCancel = new JButton("Cancel");
				    modSCancel.addActionListener(new ActionListener() {
				    	public void actionPerformed(ActionEvent ae) {
				    		modSFrame.dispose();
				    	}
				    });
				    modSPanel.add(modSTitle);
				    modSPanel.add(modSScrollPane);
				    modSPanel.add(modSOK);
				    modSPanel.add(modSCancel);
				    modSFrame.add(modSPanel);
				    modSFrame.pack();
					modSFrame.setLocationRelativeTo(null);
					modSFrame.setVisible(true);
					modSFrame.setResizable(true);
				    
			} catch(SQLException sql) {
				sql.printStackTrace();}
			}
		});
		
		addNewItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ea) {
				JFrame addNewFrame = new JFrame();
				JPanel addNewPanel = new JPanel();
				addNewPanel.setLayout(new BoxLayout(addNewPanel, BoxLayout.PAGE_AXIS));
				JButton addFlight = new JButton("Add Flight");
				JButton addPlane = new JButton("Add Plane");
				JButton addAirport = new JButton("Add Airport");
				addFlight.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent ae) {
						JFrame addFlightFrame = new JFrame();
						JPanel addFlightPanel = new JPanel();
						addFlightPanel.setLayout(new BoxLayout(addFlightPanel, BoxLayout.PAGE_AXIS));
						JLabel addFlightLabel1 = new JLabel("Airline Name");
						JLabel addFlightLabel2 = new JLabel("Airplane Model");
						JLabel addFlightLabel3 = new JLabel("Departure Airport");
						JLabel addFlightLabel4 = new JLabel("Arrival Airport");
						JLabel addFlightLabel5 = new JLabel("Available Seats");
						JTextField addFlightField1 = new JTextField(20);
						JTextField addFlightField2 = new JTextField(20);
						JTextField addFlightField3 = new JTextField(3);
						JTextField addFlightField4 = new JTextField(3);
						JTextField addFlightField5 = new JTextField(20);
						JButton addFlightOK = new JButton("Ok");
						addFlightOK.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent aea) {
								String addFlightInsert = "insert into flight(airlinename, model, departairport, arriveairport, availableseats) values(?,?,?,?,?)";
								PreparedStatement pre;
								try {
									pre = connection.prepareStatement(addFlightInsert);
									pre.setString(1, addFlightField1.getText());
									pre.setString(2, addFlightField2.getText());
									pre.setString(3, addFlightField3.getText());
									pre.setString(4, addFlightField4.getText());
									pre.setInt(5, Integer.parseInt(addFlightField5.getText()));
									pre.executeUpdate();									
									System.out.println("Confirmed new flight.");
									addFlightFrame.dispose();
								} catch (SQLException e) {
									e.printStackTrace();
								}
							}
						});
						JButton addFlightCancel = new JButton("Cancel");
						addFlightCancel.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent aea) {
								addFlightFrame.dispose();
							}
						});
						addFlightPanel.add(addFlightLabel1);
						addFlightPanel.add(addFlightField1);
						addFlightPanel.add(addFlightLabel2);
						addFlightPanel.add(addFlightField2);
						addFlightPanel.add(addFlightLabel3);
						addFlightPanel.add(addFlightField3);
						addFlightPanel.add(addFlightLabel4);
						addFlightPanel.add(addFlightField4);
						addFlightPanel.add(addFlightLabel5);
						addFlightPanel.add(addFlightField5);
						addFlightPanel.add(addFlightOK);
						addFlightPanel.add(addFlightCancel);
						addFlightFrame.add(addFlightPanel);
						addFlightFrame.pack();
						addFlightFrame.setLocationRelativeTo(null);
						addFlightFrame.setVisible(true);
						addFlightFrame.setResizable(true);
					}
				});
				addPlane.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent ae) {
						JFrame addPlaneFrame = new JFrame();
						JPanel addPlanePanel = new JPanel();
						addPlanePanel.setLayout(new BoxLayout(addPlanePanel, BoxLayout.PAGE_AXIS));
						JLabel addPlaneLabel1 = new JLabel("Airplane Model");
						JLabel addPlaneLabel2 = new JLabel("Seat Capacity");
						JTextField addPlaneField1 = new JTextField(20);
						JTextField addPlaneField2 = new JTextField(20);
						JButton addPlaneOK = new JButton("Ok");
						addPlaneOK.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent aea) {
								String addPlaneInsert = "insert into plane values(?,?)";
								PreparedStatement pre;
								try {
									pre = connection.prepareStatement(addPlaneInsert);
									pre.setString(1, addPlaneField1.getText());
									pre.setInt(2, Integer.parseInt(addPlaneField2.getText()));
									pre.executeUpdate();									
									System.out.println("Confirmed new plane.");
									addPlaneFrame.dispose();
								} catch (SQLException e) {
									e.printStackTrace();
								}
							}
						});
						JButton addPlaneCancel = new JButton("Cancel");
						addPlaneCancel.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent aea) {
								addPlaneFrame.dispose();
							}
						});
						addPlanePanel.add(addPlaneLabel1);
						addPlanePanel.add(addPlaneField1);
						addPlanePanel.add(addPlaneLabel2);
						addPlanePanel.add(addPlaneField2);
						addPlanePanel.add(addPlaneOK);
						addPlanePanel.add(addPlaneCancel);
						addPlaneFrame.add(addPlanePanel);
						addPlaneFrame.pack();
						addPlaneFrame.setLocationRelativeTo(null);
						addPlaneFrame.setVisible(true);
						addPlaneFrame.setResizable(true);
					}
				});
				addAirport.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent ae) {
						JFrame addAirportFrame = new JFrame();
						JPanel addAirportPanel = new JPanel();
						addAirportPanel.setLayout(new BoxLayout(addAirportPanel, BoxLayout.PAGE_AXIS));
						JLabel addAirportLabel1 = new JLabel("Airport Code");
						JLabel addAirportLabel2 = new JLabel("Airport Name");
						JLabel addAirportLabel3 = new JLabel("City");
						JLabel addAirportLabel4 = new JLabel("State");
						JTextField addAirportField1 = new JTextField(3);
						JTextField addAirportField2 = new JTextField(50);
						JTextField addAirportField3 = new JTextField(20);
						JTextField addAirportField4 = new JTextField(20);
						JButton addAirportOK = new JButton("Ok");
						addAirportOK.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent aea) {
								String addAirportInsert = "insert into airport values(?,?,?,?)";
								PreparedStatement pre;
								try {
									pre = connection.prepareStatement(addAirportInsert);
									pre.setString(1, addAirportField1.getText());
									pre.setString(2, addAirportField2.getText());
									pre.setString(3, addAirportField3.getText());
									pre.setString(4, addAirportField4.getText());
									pre.executeUpdate();									
									System.out.println("Confirmed new airport.");
									addAirportFrame.dispose();
								} catch (SQLException e) {
									e.printStackTrace();
								}
							}
						});
						JButton addAirportCancel = new JButton("Cancel");
						addAirportCancel.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent aea) {
								addAirportFrame.dispose();
							}
						});
						addAirportPanel.add(addAirportLabel1);
						addAirportPanel.add(addAirportField1);
						addAirportPanel.add(addAirportLabel2);
						addAirportPanel.add(addAirportField2);
						addAirportPanel.add(addAirportLabel3);
						addAirportPanel.add(addAirportField3);
						addAirportPanel.add(addAirportLabel4);
						addAirportPanel.add(addAirportField4);
						addAirportPanel.add(addAirportOK);
						addAirportPanel.add(addAirportCancel);
						addAirportFrame.add(addAirportPanel);
						addAirportFrame.pack();
						addAirportFrame.setLocationRelativeTo(null);
						addAirportFrame.setVisible(true);
						addAirportFrame.setResizable(true);
					}
				});
				addNewPanel.add(addFlight);
				addNewPanel.add(addPlane);
				addNewPanel.add(addAirport);
				addNewFrame.add(addNewPanel);
				addNewFrame.pack();
				addNewFrame.setLocationRelativeTo(null);
				addNewFrame.setVisible(true);
				addNewFrame.setResizable(true);
			}
		});
		
		if(userType.equals("admin"))
			loggedIn.setText("Logged in as admin.");
		else
			loggedIn.setText("Logged in as user.");
		
		panel.add(createReservation, gbc);
		panel.add(cancelReservation, gbc);
		panel.add(viewSchedule, gbc);
		panel.add(viewReservation, gbc);
		panel.add(modifyInformation, gbc);
		
		if(userType.equals("admin")) {
			panel.add(viewStatistics, gbc);
			panel.add(modifySchedule, gbc);
			panel.add(addNewItem, gbc);
		}
		
		frame.add(title);
		frame.add(loggedIn);
		frame.add(panel);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setResizable(false);
	}
}
