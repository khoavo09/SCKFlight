## SCKFlight

A three-persons team project for `CS157A - Database Management Systems` in _San Jose State University_. This project is a window-based, GUI
application that allows users to create and manage airlines reservations and information.

<h1 align="center">
  <img src="https://i.imgur.com/MeMjoOU.png" alt="one" />
</h1>

## Languages/Technologies
* Java
* Java Foundation Classes (AWT and Swing)
* MySQL
* JDBC

## Background and Overview

Depending on whether the user logs in as a normal
user or an admin, the app will display options to perform CRUD operations for airlines reservations 
and display flight information and statistics.

### Users

Upon signing up, users can either choose to be normal users or admins. Normal users have the ability to make, cancel, 
and show current reservations. They are able view and reserve from the schedule of flights and sort them based on time 
and destination.

* **Allow public user to view schedule of flights and sort by time**

```sql
SELECT airlineName, departAirport, arriveAirport, departTime, arriveTime  
FROM Flight, Schedule  
WHERE Flight.flightID = Schedule.flightID;
```

* **Allow public user to view status of his/her current reservations.**
```sql
SELECT class, type, price, airlineName, departAirport, arrivalAirport, departTime, arrivalTime
FROM Reservation, Flight, Schedule
WHERE Reservation.flightID = Flight.flightID AND Reservation.flightID = Schedule.flightID;
```

### Admins

Admins on the other hand can view all users' reservations and either modify or delete them. They can also add new data or
modify existing data for flights,airlines, and airports and the ability to mark flights as delayed or canceled. Additionally, they
have the option to view various statistics of recent flights over a given time period.

* **Allow administrator to view total value of all flight reservations sold in the last month.**
```sql
SELECT sum(price)
FROM Reservation
WHERE timestamp <= 154140154
GROUP BY price;
```

* **Allow administrator to view average number of unsold seats by airline for flights in the last month.**
```sql
SELECT airlineName, avg(availableSeats)
FROM Flight
WHERE flightID IN (SELECT flightID FROM Reservation WHERE timestamp <= 154140154;)
GROUP BY airlineName;
```

* **Allow administrator to view frequent fliers: Find users with high amount (> 10) of reservations over the last month.**
```sql
SELECT name, email
FROM Customer
WHERE reservationID IN (SELECT reservationID FROM Reservation WHERE timestamp <= 154140154;)
GROUP BY email
HAVING count(*) >= 10;
```

* **Allow administrator to cancel flights.**
```sql
DELETE FROM Flight
WHERE flightID IN (
  SELECT flightID 
  FROM Schedule 
  WHERE arriveTime < 1541141369 and status <> "Delayed";);
```

The following images show the differences in options between a normal user and an admin. This is the option to view all reservations, and
depending on whether the user is an admin, they have the ability to delete a reservation.

<h1 align="center">
  <img src="https://i.imgur.com/kpjYAvR.png" alt="two" />
</h1>

<h1 align="center">
  <img src="https://i.imgur.com/eju0e4C.png" alt="three" />
</h1>

<h1 align="center">
  <img src="https://i.imgur.com/esUyIQW.png" alt="three" />
</h1>

## JDBC

The database schema, written using MySQL, is connected through the main Java application using **J**ava **D**ata**b**ase **C**onnectivity
through **DriverManager**.
The process is as follows: We create a String object that contains a SQL query. We then pass that String object into a **Statement** (or
**PreparedStatement**) object to be executed. The Statement object will return the results and we store that results inside the **ResultSet**
object. We then are able to retrieve the data from mySQL and use that data as necessary. For each SQL query we write in Java, we 
would append it alongside with an **ActionListener**. Here is a code snippet detailing all these steps:

```java
viewSchedule.addActionListener(new ActionListener() {
  public void actionPerformed(ActionEvent arg0) {
    String listSchedule = "SELECT Flight.flightID, airlineName, 
      model, departAirport, arriveAirport, date, TIME_FORMAT(departTime, '%h:%i %p') 
      departTime, TIME_FORMAT(arrivalTime, '%h:%i %p') arrivalTime 
      FROM FLIGHT, SCHEDULE 
      WHERE FLIGHT.flightID = SCHEDULE.flightID ORDER BY departTime;";

      Statement stmt = null;
      ResultSet rs = null;

      try {
        stmt = connection.createStatement();
        rs = stmt.executeQuery(listSchedule);
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();
        Vector<String> columnNames = new Vector<String>();
        ...
        while (rs.next()) {
            Vector<Object> vector = new Vector<Object>();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                vector.add(rs.getObject(columnIndex));
            }
            data.add(vector);
        }
        ...
      }
   }
}
```

## Team Members
* [Chris Vo](https://github.com/VoChrisK)
* [Khoa Vo](https://github.com/khoavo09)
* [Stanley Lai](https://github.com/stanleylai21)
