DROP DATABASE IF EXISTS SCKFlightDatabase;
CREATE DATABASE SCKFlightDatabase;
USE SCKFlightDatabase;

DROP TABLE IF EXISTS Plane;
CREATE TABLE Plane
(
 model VARCHAR(15),
 seatCapacity INT CHECK(seatCapacity > 0),
 PRIMARY KEY (model)
);

DROP TABLE IF EXISTS Airport;
CREATE TABLE Airport
(
 code VARCHAR(3),
 airportName VARCHAR(50),
 city VARCHAR(20),
 state VARCHAR(20),
 PRIMARY KEY (code)
);

DROP TABLE IF EXISTS Flight;
CREATE TABLE Flight
(
 flightID INT AUTO_INCREMENT,
 airlineName VARCHAR(30),
 model VARCHAR(15),
 departAirport VARCHAR(3),
 arriveAirport VARCHAR(3),
 availableSeats INT CHECK(availableSeats < PLANE(seatCapacity)),
 PRIMARY KEY (flightID),
 FOREIGN KEY (model) REFERENCES PLANE(model) ON UPDATE CASCADE ON DELETE CASCADE,
 FOREIGN KEY (departAirport) REFERENCES AIRPORT(code) ON UPDATE CASCADE ON DELETE CASCADE,
 FOREIGN KEY (arriveAirport) REFERENCES AIRPORT(code) ON UPDATE CASCADE ON DELETE CASCADE
);
ALTER TABLE FLIGHT AUTO_INCREMENT = 1;

DROP TABLE IF EXISTS Schedule;
CREATE TABLE Schedule
(
 scheduleID INT AUTO_INCREMENT,
 flightID INT,
 departTime TIME,
 arrivalTime TIME,
 date DATE,
 status VARCHAR(10),
 PRIMARY KEY (scheduleID),
 FOREIGN KEY (flightID) REFERENCES FLIGHT(flightID) ON UPDATE CASCADE ON DELETE CASCADE
);
ALTER TABLE SCHEDULE AUTO_INCREMENT = 1;

DROP TABLE IF EXISTS Reservation;
CREATE TABLE Reservation
(
 reservationID INT AUTO_INCREMENT,
 flightID INT,
 class VARCHAR(15),
 type VARCHAR(15),
 updatedOn DATE,
 price INT,
 PRIMARY KEY (reservationID, flightID),
 FOREIGN KEY (flightID) REFERENCES Flight(flightID) ON UPDATE CASCADE ON DELETE CASCADE
);
ALTER TABLE RESERVATION AUTO_INCREMENT = 1;

DROP TABLE IF EXISTS USER;
CREATE TABLE USER
(
email VARCHAR(30),
password VARCHAR(20) NOT NULL,
userType varchar(5),
PRIMARY KEY (email)
);

DROP TABLE IF EXISTS CUSTOMER;
CREATE TABLE CUSTOMER
(
 cID INT AUTO_INCREMENT UNIQUE,
 name VARCHAR(30),
 reservationID INT,
 email VARCHAR(30),
 PRIMARY KEY (cID, email),
 FOREIGN KEY (reservationID) REFERENCES Reservation(reservationID) ON UPDATE CASCADE ON DELETE CASCADE,
 FOREIGN KEY (email) REFERENCES USER(email) ON UPDATE CASCADE ON DELETE CASCADE
);
ALTER TABLE CUSTOMER AUTO_INCREMENT = 1;

DROP TABLE IF EXISTS Archive;
CREATE TABLE Archive
(
 reservationID INT,
 flightID INT,
 class VARCHAR(15),
 type VARCHAR(15),
 price INT
);

DELIMITER //
DROP PROCEDURE IF EXISTS ReservationArchive;
CREATE PROCEDURE ReservationArchive(IN cutoffDate DATE)
BEGIN
    INSERT INTO Archive
    SELECT reservationID, flightID, class, type, price
    FROM Reservation
    WHERE updatedOn < cutoffDate;
    
    DELETE FROM Reservation
    WHERE updatedOn < cutoffDate;
END//


CREATE TRIGGER AfterReservation AFTER INSERT ON Reservation
FOR EACH ROW
BEGIN
    UPDATE Flight
    SET availableSeats = availableSeats - 1
    WHERE flightID = NEW.flightID;
END//

CREATE TRIGGER CheckSeats BEFORE UPDATE ON Flight
FOR EACH ROW
BEGIN
	IF OLD.model <> NEW.model THEN
		SET NEW.availableSeats = (SELECT seatCapacity FROM Plane WHERE model = NEW.model);
	END IF;
END//
DELIMITER ;

insert into user values("jsmith@smth.co","somegoodpw","user");
insert into user values("cgibson@smth.co","admin1","admin");
insert into user values("aromero@smth.co","user2","user");
insert into user values("svelez@smth.co","user3","user");
insert into user values("frhodes@smth.co","user4","user");
insert into user values("jyork@smth.co","user5","user");

insert into plane values("Boeing 777-300","550");
insert into plane values("Boeing 777-200","440");
insert into plane values("Airbus A340-300","295");
insert into plane values("Airbus A340-500","400");
insert into plane values("Airbus A340-900","500");
insert into plane values("Boeing 747-400","500");

insert into airport values("SJC","San Jose International Airport","San Jose","California");
insert into airport values("MCO","Orlando International Airport","Orlando","Florida");
insert into airport values("LAX","Los Angeles International Airport","Los Angeles","California");
insert into airport values("SFO","San Francisco International Airport","San Francisco","California");
insert into airport values("ORD","O'Hare International Airport","Chicago","Illinois");
insert into airport values("JFK","John F. Kennedy International Airport","New York","NY");
insert into airport values("PHX","Phoenix Sky Harbor International Airport","Phoenix","Arizona");
insert into airport values("MIA","Miami International Airport","Miami","Florida");
insert into airport values("DTW","Detroit Metropolitan Airport","Detroit","Michigan");
insert into airport values("LAS","McCarran International Airport","Las Vegas","Nevada");

insert into flight values (100,"Asiana Airlines","Boeing 777-200","SJC","MCO",120);
insert into flight values (101,"JetBlue Airways","Airbus A340-300","JFK","MCO",360);
insert into flight values (102,"Air Canada","Boeing 777-200","SJC","MCO",222);
insert into flight values (103,"JetBlue Airways","Boeing 777-200","SJC","LAX",430);
insert into flight values (104,"Asiana Airlines","Airbus A340-900","PHX","MIA",290);
insert into flight values (105,"Delta Airlines","Airbus A340-300","LAS","MCO",200);
insert into flight values (106,"United Airlines","Boeing 747-400","SFO","MCO",150);
insert into flight values (107,"JetBlue Airways","Airbus A340-900","SFO","MIA",68);
insert into flight values (108,"American Airlines","Boeing 747-400","LAS","LAX",150);
insert into flight values (109,"Delta Airlines","Boeing 747-400","PHX","SFO",189);
insert into flight values (110,"JetBlue Airways","Boeing 777-300","SFO","JFK",257);
insert into flight values (111,"American Airlines","Boeing 747-400","MIA","JFK",98);
insert into flight values (112,"United Airlines","Airbus A340-500","LAS","DTW",27);

insert into reservation values(20,100,"Eco","Non-Stop",'2017-07-14',320);
insert into reservation values(20,101,"First Class","Non-Stop",'2017-07-15',932);
insert into reservation values(21,102,"Eco","Non-Stop",'2017-07-16',295);
insert into reservation values(22,102,"Eco","Non-Stop",'2017-07-17',230);
insert into reservation values(23,103,"First Class","Non-Stop",'2017-07-18',890);
insert into reservation values(24,104,"Eco","Non-Stop",'2017-07-19',275);
insert into reservation values(25,106,"Eco","Non-Stop",'2017-07-11',283);
insert into reservation values(26,108,"Eco","Non-Stop",'2017-07-12',370);
insert into reservation values(27,108,"Eco","Non-Stop",'2017-07-13',255);
insert into reservation values(28,111,"Eco","Non-Stop",'2017-07-14',284);
insert into reservation values(29,105,"First Class","Non-Stop",'2017-07-15',885);
insert into reservation values(30,107,"First Class","Non-Stop",'2017-07-16',939);
insert into reservation values(31,111,"First Class","Non-Stop",'2017-07-17',923);
insert into reservation values(32,103,"First Class","Non-Stop",'2017-07-18',911);
insert into reservation values(33,102,"First Class","Non-Stop",'2017-07-19',956);
insert into reservation values(34,112,"First Class","Non-Stop",'2017-07-11',924);
insert into reservation values(35,106,"First Class","Non-Stop",'2017-07-12',912);
insert into reservation values(36,111,"First Class","Non-Stop",'2017-07-13',879);
insert into reservation values(37,110,"First Class","Non-Stop",'2017-07-14',934);

insert into customer values(1,"John Smith",22,"jsmith@smth.co");
insert into customer values(2,"John Smith",24,"jsmith@smth.co");
insert into customer values(3,"John Smith",26,"jsmith@smth.co");
insert into customer values(4,"John Smith",27,"jsmith@smth.co");
insert into customer values(5,"Cristopher Gibson",25,"cgibson@smth.co");
insert into customer values(6,"Cristopher Gibson",23,"cgibson@smth.co");
insert into customer values(7,"Addison Romero",29,"aromero@smth.co");
insert into customer values(8,"Stephanie Velez",28,"svelez@smth.co");
insert into customer values(9,"Stephanie Velez",30,"svelez@smth.co");
insert into customer values(10,"Jared Rhodes",32,"frhodes@smth.co");
insert into customer values(11,"Jared Rhodes",31,"frhodes@smth.co");
insert into customer values(12,"Jaydan York",34,"jyork@smth.co");

insert into schedule values (1,100,'2:00','4:30','2019-09-10',"On Time");
insert into schedule values (2,101,'12:00','23:30','2019-09-11',"On Time");
insert into schedule values (3,102,'2:10','7:30','2019-09-12',"On Time");
insert into schedule values (4,103,'3:50','15:30','2019-09-13',"On Time");
insert into schedule values (5,104,'2:20','4:30','2019-09-14',"On Time");
insert into schedule values (6,105,'1:00','6:20','2019-09-15',"On Time");
insert into schedule values (7,106,'8:00','15:30','2019-09-16',"On Time");
insert into schedule values (8,107,'9:20','19:30','2019-09-17',"On Time");
insert into schedule values (9,108,'3:20','22:20','2019-09-18',"On Time");
insert into schedule values (10,109,'2:40','10:30','2019-09-19',"On Time");
insert into schedule values (11,110,'1:20','23:10','2019-09-20',"On Time");
insert into schedule values (12,111,'3:20','12:30','2019-09-21',"On Time");
insert into schedule values (13,112,'2:00','14:30','2019-09-22',"On Time");