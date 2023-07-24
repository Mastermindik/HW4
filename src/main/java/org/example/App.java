package org.example;

import java.sql.*;
import java.util.Scanner;

public class App {
    static Connection conn;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        DbProperties props = new DbProperties();
        try {
            conn = DriverManager.getConnection(props.getUrl(), props.getRoot(), props.getPassword());
            initDb();
            while (true) {
                System.out.println("1: add apartment");
                System.out.println("2: delete apartment");
                System.out.println("3: change apartment");
                System.out.println("4: view apartments");
                System.out.print("-> ");
                String q = sc.nextLine();
                if (q.equals("1")) {
                    addApartment(sc);
                } else if (q.equals("2")) {
                    deleteApartment(sc);
                } else if (q.equals("3")) {
                    changeApartment(sc);
                } else if (q.equals("4")) {
                    viewApartments();
                } else {
                    return;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void initDb() throws SQLException {
        DatabaseMetaData metaData = conn.getMetaData();

        ResultSet resultSet = metaData.getTables(null, null, "Apartments", null);
        if (!resultSet.next()) {
            try (Statement st = conn.createStatement()) {
//                st.execute("DROP TABLE IF EXISTS Apartments");
                st.execute("CREATE TABLE Apartments (id INT NOT NULL PRIMARY KEY AUTO_INCREMENT," +
                        " district VARCHAR(20) NOT NULL, address VARCHAR(40) NOT NULL UNIQUE, area DOUBLE NOT NULL," +
                        " rooms INT NOT NULL, price DOUBLE NOT NULL)");
            }
        }
    }

    public static void addApartment(Scanner sc) throws SQLException {
        System.out.println("Enter district: ");
        String district = sc.nextLine();
        System.out.println("Enter address: ");
        String address = sc.nextLine();
        System.out.println("Enter area: ");
        double area = Double.parseDouble(sc.nextLine());
        System.out.println("Enter number of rooms: ");
        int rooms = Integer.parseInt(sc.nextLine());
        System.out.println("Enter price in USD: ");
        double price = Double.parseDouble(sc.nextLine());

        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO Apartments (district, address, area," +
                " rooms,price) VALUES ( ?,?,?,?,? )");) {
            ps.setString(1, district);
            ps.setString(2, address);
            ps.setDouble(3, area);
            ps.setInt(4, rooms);
            ps.setDouble(5, price);
            ps.executeUpdate();
        }
    }

    public static void deleteApartment(Scanner sc) throws SQLException {
        System.out.println("Enter address: ");
        String address = sc.nextLine();
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM Apartments WHERE address = ?")) {
            ps.setString(1, address);
            ps.executeUpdate();
        }
    }

    public static void changeApartment(Scanner sc) throws SQLException {
        System.out.println("Enter address: ");
        String address = sc.nextLine();
        System.out.println("Enter new price: ");
        double price = Double.parseDouble(sc.nextLine());
        try (PreparedStatement ps = conn.prepareStatement("UPDATE Apartments SET price = ? WHERE address = ?")) {
            ps.setDouble(1, price);
            ps.setString(2, address);
            ps.executeUpdate();
        }
    }

    public static void viewApartments() throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM Apartments");
             ResultSet rs = ps.executeQuery()) {
            ResultSetMetaData rsmd = rs.getMetaData();
            for (int i = 1; i <= rsmd.getColumnCount(); i++)
                System.out.print(rsmd.getColumnName(i) + "\t\t");
            System.out.println();

            while (rs.next()) {
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    System.out.print(rs.getString(i) + "\t\t");
                }
                System.out.println();
            }
        }
    }


}
