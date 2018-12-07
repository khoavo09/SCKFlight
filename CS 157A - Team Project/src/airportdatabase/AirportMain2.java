import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Vector;

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
				String viewQuery = "select Reservation.reservationID, airlineName, class, departAirport, arriveAirport, date, TIME_FORMAT(departTime, '%h:%i %p') departTime, TIME_FORMAT(arrivalTime, '%h:%i %p') arrivalTime, type, price from flight, reservation, schedule where reservation.flightID = flight.flightID and reservation.flightID = schedule.flightID and reservationID in (select reservationID from customer where email = '"+EMAIL+"') order by date;";
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
