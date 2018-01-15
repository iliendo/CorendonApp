/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package corendo.fys;

import java.sql.*;
import java.util.Calendar;
import java.util.Enumeration;

/**
 *
 * @author Wouter
 */
public class MyJDBC {

    private static final String DB_DEFAULT_SERVER_URL = "localhost:3306";
    private static final String DB_DEFAULT_ACCOUNT = jdbcDBconnection.dbUsername;
    private static final String DB_DEFAULT_PASSWORD = jdbcDBconnection.dbPassword;

    private final static String DB_DRIVER_URL = "com.mysql.jdbc.Driver";
    private final static String DB_DRIVER_PREFIX = "jdbc:mysql://";
    private final static String DB_DRIVER_PARAMETERS = "?useSSL=false";

    private Connection connection = null;

    // set for verbose logging of all queries
    private boolean verbose = true;

    // remembers the first error message on the connection 
    private String errorMessage = null;

    // constructors
    public MyJDBC() {
        this("information_schema", DB_DEFAULT_SERVER_URL, DB_DEFAULT_ACCOUNT, DB_DEFAULT_PASSWORD);
    }

    public MyJDBC(String dbName) {
        this(dbName, DB_DEFAULT_SERVER_URL, DB_DEFAULT_ACCOUNT, DB_DEFAULT_PASSWORD);
    }

    public MyJDBC(String dbName, String account, String password) {
        this(dbName, DB_DEFAULT_SERVER_URL, account, password);
    }

    public MyJDBC(String dbName, String serverURL, String account, String password) {
        try {
            // verify that a proper JDBC driver has been installed and linked
            if (!selectDriver(DB_DRIVER_URL)) {
                return;
            }

            if (password == null) {
                password = "";
            }

            // establish a connection to a named database on a specified server	
            String connStr = DB_DRIVER_PREFIX + serverURL + "/" + dbName + DB_DRIVER_PARAMETERS;
            log("Connecting " + connStr);
            this.connection = DriverManager.getConnection(connStr, account, password);

        } catch (SQLException eSQL) {
            error(eSQL);
            this.close();
        }
    }

    public final void close() {

        if (this.connection == null) {
            // db has been closed earlier already
            return;
        }
        try {
            this.connection.close();
            this.connection = null;
            this.log("Data base has been closed");
        } catch (SQLException eSQL) {
            error(eSQL);
        }
    }

    /**
     * *
     * elects proper loading of the named driver for database connections. This
     * is relevant if there are multiple drivers installed that match the JDBC
     * type
     *
     * @param driverName the name of the driver to be activated.
     * @return indicates whether a suitable driver is available
     */
    private Boolean selectDriver(String driverName) {
        try {
            Class.forName(driverName);
            // Put all non-prefered drivers to the end, such that driver selection hits the first
            Enumeration<Driver> drivers = DriverManager.getDrivers();
            while (drivers.hasMoreElements()) {
                Driver d = drivers.nextElement();
                if (!d.getClass().getName().equals(driverName)) {   // move the driver to the end of the list
                    DriverManager.deregisterDriver(d);
                    DriverManager.registerDriver(d);
                }
            }
        } catch (ClassNotFoundException | SQLException ex) {
            error(ex);
            return false;
        }
        return true;
    }

    /**
     * *
     * Executes a DDL, DML or DCL query that does not yield a result set
     *
     * @param sql the full sql text of the query.
     * @return the number of rows that have been impacted, -1 on error
     */
    public int executeUpdateQuery(String sql) {
        try {
            Statement s = this.connection.createStatement();
            log(sql);
            int n = s.executeUpdate(sql);
            s.close();
            return (n);
        } catch (SQLException ex) {
            // handle exception
            error(ex);
            return -1;
        }
    }

    /**
     * *
     * Executes an SQL query that yields a ResultSet with the outcome of the
     * query. This outcome may be a single row with a single column in case of a
     * scalar outcome.
     *
     * @param sql the full sql text of the query.
     * @return a ResultSet object that can iterate along all rows
     * @throws SQLException
     */
    public ResultSet executeResultSetQuery(String sql) throws SQLException {
        Statement s = this.connection.createStatement();
        log(sql);
        ResultSet rs = s.executeQuery(sql);
        // cannot close the statement, because that also closes the resultset
        return rs;
    }

    /**
     * *
     * Executes query that is expected to return a single String value
     *
     * @param sql the full sql text of the query.
     * @return the string result, null if no result or error
     */
    public String executeStringQuery(String sql) {
        String result = null;
        try {
            Statement s = this.connection.createStatement();
            log(sql);
            ResultSet rs = s.executeQuery(sql);
            if (rs.next()) {
                result = rs.getString(1);
            }
            // close both statement and resultset
            s.close();
        } catch (SQLException ex) {
            error(ex);
        }

        return result;
    }

    /**
     * *
     * Executes query that is expected to return a list of String values
     *
     * @param sql the full sql text of the query.
     * @return the string result, null if no result or error
     */
    public String executeStringListQuery(String sql) {
        String result = null;
        try {
            Statement s = this.connection.createStatement();
            log(sql);
            ResultSet rs = s.executeQuery(sql);
            if (rs.next()) {
                result = rs.getString(1);
            }
            // close both statement and resultset
            s.close();
        } catch (SQLException ex) {
            error(ex);
        }

        return result;
    }

    /**
     * *
     * echoes a message on the system console, if run in verbose mode
     *
     * @param message
     */
    public void log(String message) {
        if (isVerbose()) {
            //System.out.println("MyJDBC: " + message);
        }
    }

    /**
     * *
     * echoes an exception and its stack trace on the system console. remembers
     * the message of the first error that occurs for later reference. closes
     * the connection such that no further operations are possible.
     *
     * @param e
     */
    public final void error(Exception e) {
        String msg = "MyJDBC-" + e.getClass().getName() + ": " + e.getMessage();

        // capture the message of the first error of the connection
        if (this.errorMessage == null) {
            this.errorMessage = msg;
        }
        //System.out.println(msg);
        e.printStackTrace();

        // if an error occurred, close the connection to prevent further operations
        this.close();
    }

    /**
     * *
     * builds a sample database with sample content
     *
     * @param dbName name of the sample database.
     */
    public static void createTestDatabase(String dbName) {

        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH) + 1;
        int year = cal.get(Calendar.YEAR);
        int minDrieDagen = day - 2;
        String threeDays = String.valueOf(minDrieDagen);
        String jaar = String.valueOf(year);
        String maand = String.valueOf(month);
        String datum = jaar + "-" + maand + "-" + threeDays;

        MyJDBC sysJDBC = new MyJDBC("information_schema");
        sysJDBC.executeUpdateQuery("CREATE DATABASE IF NOT EXISTS " + dbName);
        sysJDBC.close();

        // create or truncate Airport table in the Airline database
        //System.out.println("Creating the Airport table...");
        MyJDBC myJDBC = new MyJDBC(dbName);

        myJDBC.executeUpdateQuery("CREATE TABLE IF NOT EXISTS`status` (\n"
                + "  `Status_id` int(11) NOT NULL AUTO_INCREMENT,\n"
                + "  `Status` varchar(45) DEFAULT NULL,\n"
                + "  PRIMARY KEY (`Status_id`)\n"
                + ")");

        myJDBC.executeUpdateQuery("CREATE TABLE IF NOT EXISTS `size` (\n"
                + "  `Size_id` int(11) NOT NULL AUTO_INCREMENT,\n"
                + "  `Size` varchar(45) DEFAULT NULL,\n"
                + "  PRIMARY KEY (`Size_id`)\n"
                + ")");

        myJDBC.executeUpdateQuery("CREATE TABLE IF NOT EXISTS `maincolor` (\n"
                + "  `MainColor_id` int(11) NOT NULL AUTO_INCREMENT,\n"
                + "  `Color` varchar(45) DEFAULT NULL,\n"
                + "  PRIMARY KEY (`MainColor_id`)\n"
                + ")");

        myJDBC.executeUpdateQuery("CREATE TABLE IF NOT EXISTS `airport` (\n"
                + "  `Airport_id` int(11) NOT NULL AUTO_INCREMENT,\n"
                + "  `Airport_name` varchar(45) DEFAULT NULL,\n"
                + "  PRIMARY KEY (`Airport_id`)\n"
                + ")");

        myJDBC.executeUpdateQuery("CREATE TABLE IF NOT EXISTS `secondcolor` (\n"
                + "  `SecondColor_id` int(11) NOT NULL AUTO_INCREMENT,\n"
                + "  `Color` varchar(45) DEFAULT NULL,\n"
                + "  PRIMARY KEY (`SecondColor_id`)\n"
                + ")");

        myJDBC.executeUpdateQuery("CREATE TABLE IF NOT EXISTS `luggagetype` (\n"
                + "  `LuggageType_id` int(11) NOT NULL AUTO_INCREMENT,\n"
                + "  `LuggageType` varchar(45) DEFAULT NULL,\n"
                + "  PRIMARY KEY (`LuggageType_id`)\n"
                + ")");

        myJDBC.executeUpdateQuery("CREATE TABLE IF NOT EXISTS `function` (\n"
                + "  `Function_id` int(11) NOT NULL AUTO_INCREMENT,\n"
                + "  `Function_name` varchar(45) DEFAULT NULL,\n"
                + "  PRIMARY KEY (`Function_id`))");

        myJDBC.executeUpdateQuery("CREATE TABLE IF NOT EXISTS `passenger` (\n"
                + "  `Passenger_id` int(11) NOT NULL AUTO_INCREMENT,\n"
                + "  `Firstname` varchar(45) DEFAULT NULL,\n"
                + "  `Lastname` varchar(45) DEFAULT NULL,\n"
                + "  `Email` varchar(45) DEFAULT NULL,\n"
                + "  `PhoneNr` varchar(45) DEFAULT NULL,\n"
                + "  `Address` varchar(45) DEFAULT NULL,\n"
                + "  `Zipcode` varchar(45) DEFAULT NULL,\n"
                + "  `City` varchar(45) DEFAULT NULL,\n"
                + "  `Country_name` varchar(45) DEFAULT NULL,\n"
                + "  PRIMARY KEY (`Passenger_id`)\n"
                + ")");

        myJDBC.executeUpdateQuery("CREATE TABLE IF NOT EXISTS `brand` (\n"
                + "  `Brand_id` int(11) NOT NULL AUTO_INCREMENT,\n"
                + "  `Brand` varchar(45) DEFAULT NULL,\n"
                + "  PRIMARY KEY (`Brand_id`)\n"
                + ")");

        myJDBC.executeUpdateQuery("CREATE TABLE IF NOT EXISTS `country` (\n"
                + "  `Country_id` int(11) NOT NULL AUTO_INCREMENT,\n"
                + "  `Country_name` varchar(45) DEFAULT NULL,\n"
                + "  PRIMARY KEY (`Country_id`)\n"
                + ")");

        myJDBC.executeUpdateQuery("CREATE TABLE IF NOT EXISTS `weight` (\n"
                + "  `Weight_id` int(11) NOT NULL AUTO_INCREMENT,\n"
                + "  `Weight` varchar(45) DEFAULT NULL,\n"
                + "  PRIMARY KEY (`Weight_id`)\n"
                + ")");

        myJDBC.executeUpdateQuery("CREATE TABLE IF NOT EXISTS `luggage_import` (\n"
                + "  `luggage_id` varchar(45) NOT NULL,\n"
                + "  `DateFound` varchar(45) DEFAULT NULL,\n"
                + "  `TimeFound` varchar(45) DEFAULT NULL,\n"
                + "  `LuggageType` varchar(45) DEFAULT NULL,\n"
                + "  `Brand` varchar(45) DEFAULT NULL,\n"
                + "  `ArrivedFlight` varchar(45) DEFAULT NULL,\n"
                + "  `LuggageTag` varchar(45) DEFAULT NULL,\n"
                + "  `LocationFound` varchar(45) DEFAULT NULL,\n"
                + "  `MainColor` varchar(45) DEFAULT NULL,\n"
                + "  `SecondColor` varchar(45) DEFAULT NULL,\n"
                + "  `Size` varchar(45) DEFAULT NULL,\n"
                + "  `Weight` varchar(45) DEFAULT NULL,\n"
                + "  `PassengerName` varchar(45) DEFAULT NULL,\n"
                + "  `City` varchar(45) DEFAULT NULL,\n"
                + "  `OtherCharacteristics` varchar(45) DEFAULT NULL,\n"
                + "  PRIMARY KEY (`luggage_id`)\n"
                + ")");

        myJDBC.executeUpdateQuery("CREATE TABLE IF NOT EXISTS `location_airport` (\n"
                + "  `Location_Airport_id` int(11) NOT NULL AUTO_INCREMENT,\n"
                + "  `Location_name` varchar(45) DEFAULT NULL,\n"
                + "  `Airport_id` int(11) NOT NULL,\n"
                + "  `Country_id` int(11) NOT NULL,\n"
                + "  PRIMARY KEY (`Location_Airport_id`,`Airport_id`,`Country_id`)\n"
                + "  )");

        myJDBC.executeUpdateQuery("ALTER TABLE location_airport ADD FOREIGN KEY ( Airport_id ) REFERENCES airport( Airport_id )");

        myJDBC.executeUpdateQuery("CREATE TABLE IF NOT EXISTS `luggage` (\n"
                + "  `Luggage_id` int(11) NOT NULL AUTO_INCREMENT,\n"
                + "  `DateFound` DATE NULL,\n"
                + "  `TimeFound` varchar(45) DEFAULT NULL,\n"
                + "  `LuggageType_id` int(11) NOT NULL,\n"
                + "  `Brand_id` int(11) NOT NULL,\n"
                + "  `MainColor_id` int(11) NOT NULL,\n"
                + "  `Status_id` int(11) NOT NULL,\n"
                + "  `Size_id` int(11) NOT NULL,\n"
                + "  `Weight_id` int(11) NOT NULL,\n"
                + "  `SecondColor_id` int(11) NOT NULL,\n"
                + "  `LuggageTag` varchar(45) DEFAULT NULL,\n"
                + "  `Image` varchar(200) DEFAULT NULL,\n"
                + "  `Location_Airport_id` int(11) NOT NULL,\n"
                + "  `Airport_id` int(11) NOT NULL,\n"
                + "  `Passenger_id` int(11) NOT NULL,\n"
                + "  `Flight` varchar(45) DEFAULT NULL,\n"
                + "  `Features` varchar(150) DEFAULT NULL,\n"
                + "  `OnWorkStatus` varchar(45) DEFAULT NULL,\n"
                + "  PRIMARY KEY (`Luggage_id`,`LuggageType_id`,`Brand_id`,`MainColor_id`,`Status_id`,`Size_id`,`Weight_id`,`SecondColor_id`,`Location_Airport_id`,`Airport_id`,`Passenger_id`)\n"
                + ")");

        myJDBC.executeUpdateQuery("ALTER TABLE luggage ADD FOREIGN KEY ( Weight_id ) REFERENCES weight( Weight_id )");
        myJDBC.executeUpdateQuery("ALTER TABLE luggage ADD FOREIGN KEY ( SecondColor_id ) REFERENCES secondcolor( SecondColor_id )");
        //myJDBC.executeUpdateQuery("ALTER TABLE luggage ADD FOREIGN KEY ( Location_Airport_id, Airport_id) REFERENCES location_airport( Location_Airport_id, Airport_id )");
        myJDBC.executeUpdateQuery("ALTER TABLE luggage ADD FOREIGN KEY ( LuggageType_id ) REFERENCES luggagetype( LuggageType_id )");
        myJDBC.executeUpdateQuery("ALTER TABLE luggage ADD FOREIGN KEY ( Brand_id ) REFERENCES brand( Brand_id )");
        myJDBC.executeUpdateQuery("ALTER TABLE luggage ADD FOREIGN KEY ( MainColor_id ) REFERENCES maincolor( MainColor_id )");
        myJDBC.executeUpdateQuery("ALTER TABLE luggage ADD FOREIGN KEY ( Status_id ) REFERENCES status( Status_id )");
        myJDBC.executeUpdateQuery("ALTER TABLE luggage ADD FOREIGN KEY ( Size_id ) REFERENCES size( Size_id )");

        myJDBC.executeUpdateQuery("CREATE TABLE IF NOT EXISTS `damagedluggage` (\n"
                + "  `damagedLuggage_id` int(11) NOT NULL AUTO_INCREMENT,\n"
                + "  `Date` DATE NULL,\n"
                + "  `Time` varchar(45) DEFAULT NULL,\n"
                + "  `LuggageTag` varchar(45) DEFAULT NULL,\n"
                + "  `Image` longblob,\n"
                + "  `Flight` varchar(45) DEFAULT NULL,\n"
                + "  `Passenger_id` int(11) NOT NULL,\n"
                + "  `LuggageType_id` int(11) NOT NULL,\n"
                + "  `Airport_id` int(11) NOT NULL,\n"
                + "  `MainColor_id` int(11) NOT NULL,\n"
                + "  `Brand_id` int(11) NOT NULL,\n"
                + "  `Size_id` int(11) NOT NULL,\n"
                + "  `Weight_id` int(11) NOT NULL,\n"
                + "  `SecondColor_id` int(11) NOT NULL,\n"
                + "  PRIMARY KEY (`damagedLuggage_id`,`Passenger_id`,`LuggageType_id`,`Airport_id`,`MainColor_id`,`Brand_id`,`Size_id`,`Weight_id`,`SecondColor_id`)\n"
                + ")");

        myJDBC.executeUpdateQuery("ALTER TABLE damagedluggage ADD FOREIGN KEY ( Weight_id ) REFERENCES weight( Weight_id )");
        myJDBC.executeUpdateQuery("ALTER TABLE damagedluggage ADD FOREIGN KEY ( SecondColor_id ) REFERENCES secondcolor( SecondColor_id )");
        myJDBC.executeUpdateQuery("ALTER TABLE damagedluggage ADD FOREIGN KEY ( LuggageType_id ) REFERENCES luggagetype( LuggageType_id )");
        myJDBC.executeUpdateQuery("ALTER TABLE damagedluggage ADD FOREIGN KEY ( Brand_id ) REFERENCES brand( Brand_id )");
        myJDBC.executeUpdateQuery("ALTER TABLE damagedluggage ADD FOREIGN KEY ( MainColor_id ) REFERENCES maincolor( MainColor_id )");
        myJDBC.executeUpdateQuery("ALTER TABLE damagedluggage ADD FOREIGN KEY ( Size_id ) REFERENCES size( Size_id )");
        myJDBC.executeUpdateQuery("ALTER TABLE damagedluggage ADD FOREIGN KEY ( Airport_id ) REFERENCES airport( Airport_id )");
        myJDBC.executeUpdateQuery("ALTER TABLE damagedluggage ADD FOREIGN KEY ( Passenger_id ) REFERENCES passenger( Passenger_id )");

        myJDBC.executeUpdateQuery("CREATE TABLE IF NOT EXISTS `employee` (\n"
                + "  `Employee_id` int(11) NOT NULL AUTO_INCREMENT,\n"
                + "  `Firstname` varchar(45) DEFAULT NULL,\n"
                + "  `Lastname` varchar(45) DEFAULT NULL,\n"
                + "  `Email` varchar(70) DEFAULT NULL,\n"
                + "  `Password` varchar(45) DEFAULT NULL,\n"
                + "  `Function_id` int(11) NOT NULL,\n"
                + "  `Country_id` int(11) NOT NULL,\n"
                + "  PRIMARY KEY (`Employee_id`,`Function_id`,`Country_id`),\n"
                + "  KEY `foreign_key_idx` (`Function_id`),\n"
                + "  KEY `foreign_key1_idx` (`Country_id`)\n"
                + ")");

        myJDBC.executeUpdateQuery("CREATE TABLE IF NOT EXISTS `employee_archive` (\n"
                + "  `Employee_id` int(11) NOT NULL AUTO_INCREMENT,\n"
                + "  `Firstname` varchar(45) DEFAULT NULL,\n"
                + "  `Lastname` varchar(45) DEFAULT NULL,\n"
                + "  `Email` varchar(70) DEFAULT NULL,\n"
                + "  `Function_id` int(11) NOT NULL,\n"
                + "  `Country_id` int(11) NOT NULL,\n"
                + "  PRIMARY KEY (`Employee_id`,`Function_id`,`Country_id`),\n"
                + "  KEY `foreign_key_idx` (`Function_id`),\n"
                + "  KEY `foreign_key1_idx` (`Country_id`)\n"
                + ")");

        myJDBC.executeUpdateQuery("ALTER TABLE employee ADD FOREIGN KEY ( Function_id ) REFERENCES function( Function_id )");
        myJDBC.executeUpdateQuery("ALTER TABLE employee ADD FOREIGN KEY ( Country_id ) REFERENCES country( Country_id )");

        int rowTeller = 0;

        try {
            String query1 = "SELECT * FROM function WHERE Function_id = '1' AND Function_name = 'Balie medewerker'";
            ResultSet rs = myJDBC.executeResultSetQuery(query1);
            while (rs.next()) {
                rowTeller++;
            }
            rs.close();
        } catch (SQLException ex) {
            myJDBC.error(ex);
        }

        System.out.println("aantal rows is " + rowTeller);

        if (rowTeller == 0) {
            myJDBC.executeUpdateQuery("INSERT INTO `function` VALUES (1,'Balie medewerker'),(2,'Supervisor')");
            myJDBC.executeUpdateQuery("INSERT INTO `country` VALUES (1,'The Netherlands'),(2,'Spain'),(3,'Turkey'),(4,'Germany'),(5,'England')");
            myJDBC.executeUpdateQuery("INSERT INTO `employee` VALUES (1,'1','1','1','c4ca4238a0b923820dcc509a6f75849b',1,1),(2,'2','2','2','c81e728d9d4c2f636f067f89cc14862c',2,2), (3,'Jane','Smith','Jane.Smith@corendon.nl','c4ca4238a0b923820dcc509a6f75849b',1,1), (4,'John','Doe','John.Doe@corendon.nl','c81e728d9d4c2f636f067f89cc14862c',2,2)");
            myJDBC.executeUpdateQuery("INSERT INTO `brand` VALUES (1,'N.V.T.'),(2,'Gucci'),(3,'Nike'),(4,'Adidas'),(5,'Louis Vuitton'),(6,'Jordans'),(7,'Puma'),(8,'North Face')");
            myJDBC.executeUpdateQuery("INSERT INTO `airport` VALUES (1,'N.V.T.'),(2,'Schiphol'),(3,'Eindhoven')");
            myJDBC.executeUpdateQuery("INSERT INTO `location_airport` VALUES (1,'N.V.T.',1,1),(2,'departure hall',1,1),(3,'arrival hall',1,1),(4,'toilet',1,1),(5,'belt-01',1,1),(6,'belt-02',1,1),(7,'belt-03',1,1),(8,'belt-04',1,1),(9,'belt-05',1,1),(10,'belt-06',1,1)");
            myJDBC.executeUpdateQuery("INSERT INTO `luggagetype` VALUES (1,'N.V.T.'),(2,'Suitcase'),(3,'Bagpack'),(4,'Box')");
            myJDBC.executeUpdateQuery("INSERT INTO `maincolor` VALUES (1,'N.V.T.'),(2,'Black'),(3,'White'),(4,'Voilet'),(5,'Orange'),(6,'Red'),(7,'Yellow'),(8,'Green'),(9,'Purple')");
            myJDBC.executeUpdateQuery("INSERT INTO `passenger` VALUES (1,'Dwight','Nelson','dwight.nelson69@example.com','(599)-387-9878','Fairview St','3943','New York','Amerika'),(2,'Erica','Rivera','erica.rivera37@example.com','(618)-271-8521','E Center St','5373','Amsterdam','Nederland'),(3,'Eva','Soto','eva.soto44@example.com','(712)-673-8531','Sunset Blvd 23','5307','Los Angeles','Amerika'),(4,'Donald','Daniels','donald.daniels32@example.com','(828)-376-6030','Texas Ave 2','6469','Texas','Amerika'),(5,'Wouter','Kloos','whkloos@gmail.com','0612345678','Straatweg 164','5234GZ','Rotterdam','Nederland'),(6,'Ray','Miles','ray.miles67@example.com','(550)-761-5917','Westheimer Rd 64','1424','Sydney','Australie'),(7,'Nicholas','Scott','nicholas.scott94@example.com','(438)-227-4742','Lovers Ln','2576','Amsterdam','Nederland'),(8,'Alex','Powell','alex.powell45@example.com','(572)-323-2845','Preston Rd','7748','Preston','Canada'),(9,'Curtis','Little','curtis.little78@example.com','(549)-793-5659','Elgin St','2341','Elgin','Mexico'),(10,'Julie','Silva','julie.silva11@example.com','(939)-755-3911','Pecan Acres Ln','3753','Pecan','Frankrijk'),(11,'Leon','Curtis','leon.curtis65@example.com','(345)-841-2513','Royal Ln','7090','Royal','Spanje'),(12,'Catherine','Vargas','catherine.vargas47@example.com','(983)-929-5253','Woodlane Dr','6892','Woodlane','Turkije'),(13,'Tanya','Mills','tanya.mills75@example.com','(148)-601-3303','Kraft Ave','3331','Kraft','Griekenland'),(14,'Ruby','Murphy','ruby.murphy37@example.com','(843)-882-2372','Hamilton Ave','6219','Hamilton','Italie'),(15,'Pauline','Byrd','pauline.byrd85@example.com','(536)-374-6052','Marsh Ln','1298','Marsh',NULL),(16,'Erica','Rivera','erica.rivera37@example.com','(618)-271-8521','E Center St','5373','Amsterdam','Nederland'),(17,'Eva','Soto','eva.soto44@example.com','(712)-673-8531','Sunset Blvd 23','5307','Los Angeles','Amerika'),(18,'Donald','Daniels','donald.daniels32@example.com','(828)-376-6030','Texas Ave 2','6469','Texas','Amerika'),(19,'Wouter','Kloos','whkloos@gmail.com','0612345678','Straatweg 164','5234GZ','Rotterdam','Nederland'),(20,'Dwight','Nelson','dwight.nelson69@example.com','(599)-387-9878','Fairview St','3943','New York','Amerika'),(21,'Erica','Rivera','erica.rivera37@example.com','(618)-271-8521','E Center St','5373','Amsterdam','Nederland'),(22,'Eva','Soto','eva.soto44@example.com','(712)-673-8531','Sunset Blvd 23','5307','Los Angeles','Amerika'),(23,'Donald','Daniels','donald.daniels32@example.com','(828)-376-6030','Texas Ave 2','6469','Texas','Amerika')");
            myJDBC.executeUpdateQuery("INSERT INTO `secondcolor` VALUES (1,'N.V.T.'),(2,'Red'),(3,'Yellow'),(4,'Green'),(5,'Blue'),(6,'Pink'),(7,'Black'),(8,'Brown'),(9,'White')");
            myJDBC.executeUpdateQuery("INSERT INTO `size` VALUES (1,'N.V.T.'),(2,'10x10x10'),(3,'20x20x20'),(4,'30x30x30'),(5,'40x40x40')");
            myJDBC.executeUpdateQuery("INSERT INTO `weight` VALUES (1,'N.V.T.'),(2,'5kg'),(3,'10kg'),(4,'15kg'),(5,'20kg'),(6,'25kg'),(7,'30kg')");
            myJDBC.executeUpdateQuery("INSERT INTO `status` VALUES (1,'Lost'),(2,'Found')");
            myJDBC.executeUpdateQuery("INSERT INTO `luggage` VALUES (1,'2018-01-04','12:14:36',2,2,2,2,4,4,6,'1234556','no image',1,2,1,'KL1234','',NULL),(2,'2018-01-02','12:14:36',3,4,2,2,5,5,1,'3847263','no image',1,3,2,'KL4535',NULL,NULL),(3,'2018-01-10','12:18:04',3,4,4,2,3,4,1,'23422','no image',1,2,3,'KL3542','',NULL),(4,'"+datum+"','12:18:04',2,8,2,1,5,5,9,'2384263','no image',1,2,4,'KL3452',NULL,NULL),(5,'"+datum+"','12:18:04',2,3,3,1,4,4,1,'239562394','no image',1,2,5,'KL4634',NULL,NULL),(6,'"+datum+"','12:22:14',2,2,3,1,3,6,2,'325345','no image',1,3,6,'HV2345','Very expensive luggage',NULL),(7,'"+datum+"','12:22:14',2,5,3,1,4,6,3,'5342523','no image',1,2,7,'FR2423',NULL,NULL),(8,'2018-01-04','12:14:36',2,2,2,2,4,4,6,'1234556','no image',1,2,13,'KL1234','',NULL),(9,'2018-01-02','12:14:36',3,4,2,2,5,5,1,'3847263','no image',1,3,14,'KL4535',NULL,NULL),(10,'2018-01-10','12:18:04',3,4,4,2,3,4,1,'23422','no image',1,2,15,'KL3542','',NULL),(11,'"+datum+"','12:18:04',2,8,2,2,5,5,9,'2384263','no image',1,2,16,'KL3452',NULL,NULL),(12,'"+datum+"','12:18:04',2,3,3,2,4,4,1,'239562394','no image',1,2,17,'KL4634',NULL,NULL),(13,'2018-01-08','12:18:04',2,3,3,1,4,4,1,'239562394','no image',1,2,18,'KL4634',NULL,NULL),(14,'2018-01-08','12:18:04',2,3,3,1,4,4,1,'239562394','no image',1,2,19,'KL4634',NULL,NULL),(15,'2018-01-08','12:18:04',2,3,3,1,4,4,1,'239562394','no image',1,2,20,'KL4634',NULL,NULL),(16,'2018-01-08','12:18:04',2,3,3,1,4,4,1,'239562394','no image',1,2,21,'KL4634',NULL,NULL),(17,'2018-01-08','12:18:04',2,3,3,1,4,4,1,'239562394','no image',1,2,22,'KL4634',NULL,NULL),(18,'2018-01-08','12:18:04',2,3,3,1,4,4,1,'239562394','no image',1,2,23,'KL4634',NULL,NULL);");
            myJDBC.executeUpdateQuery("INSERT INTO `damagedluggage` VALUES (1,'2018-01-01','1','1',NULL,'1',8,1,1,1,1,1,1,1),(2,'2018-01-05','1','1',NULL,'1',9,1,1,1,1,1,1,1),(3,'2018-01-10','1','1',NULL,'1',10,1,1,1,1,1,1,1),(4,'"+datum+"','1','1',NULL,'1',11,1,1,1,1,1,1,1),(5,'"+datum+"','1','1',NULL,'1',12,1,1,1,1,1,1,1)");
        }

        myJDBC.close();

    }

    public boolean isVerbose() {
        return verbose;
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

}
