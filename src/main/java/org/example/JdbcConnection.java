package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class JdbcConnection {
    public static void main(String[] args) {
        final String url = "jdbc:mysql://127.0.0.1:3306/exampledb";  // Ensure the correct port is specified
        final String username = "root";
        final String password = "Admin@123";

        try {
            // Establish the connection
            Connection con = DriverManager.getConnection(url, username, password);

            Scanner scn = new Scanner(System.in);
            System.out.println("1. Create User with Static Values\n" +
                    "2. Create User with Dynamic Values\n" +
                    "3. Read User\n" +
                    "4. Update User\n" +
                    "5. Delete User");
            int value = scn.nextInt();

            switch (value) {
                case 1:
                    createUserWithStaticValues(con);
                    break;
                case 2:
                    createUserWithDynamicValues(con, scn);
                    break;
                case 3:
                    readUsers(con);
                    break;
                case 4:
                    updateUser(con, scn);
                    break;
                case 5:
                    deleteUser(con, scn);
                    break;
                default:
                    System.out.println("Select a valid choice!");
            }

            // Close the connection
            con.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createUserWithStaticValues(Connection con) throws SQLException {
        String query = "INSERT INTO users (firstname, lastname, email, age) VALUES ('John', 'Doe', 'johndoe@example.com', 30)";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.executeUpdate();
            System.out.println("User created with static values.");
        }
    }

    private static void createUserWithDynamicValues(Connection con, Scanner scn) throws SQLException {
        System.out.print("Enter First Name: ");
        String firstName = scn.next();
        System.out.print("Enter Last Name: ");
        String lastName = scn.next();
        System.out.print("Enter Email: ");
        String email = scn.next();
        System.out.print("Enter Age: ");
        int age = scn.nextInt();

        String query = "INSERT INTO users (firstname, lastname, email, age) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, firstName);
            stmt.setString(2, lastName);
            stmt.setString(3, email);
            stmt.setInt(4, age);
            stmt.executeUpdate();
            System.out.println("User created with dynamic values.");
        }
    }

    private static void readUsers(Connection con) throws SQLException {
        String query = "SELECT * FROM users";
        try (PreparedStatement pstmt = con.prepareStatement(query); ResultSet resultSet = pstmt.executeQuery()) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String firstname = resultSet.getString("firstname");
                String lastname = resultSet.getString("lastname");
                String email = resultSet.getString("email");
                int age = resultSet.getInt("age");
                System.out.println("ID: " + id + ", Name: " + firstname + " " + lastname + ", Email: " + email + ", Age: " + age);
            }
        }
    }

    private static void updateUser(Connection con, Scanner scn) throws SQLException {
        System.out.print("Enter User ID to update: ");
        int id = scn.nextInt();
        System.out.print("Enter New First Name: ");
        String firstName = scn.next();
        System.out.print("Enter New Last Name: ");
        String lastName = scn.next();
        System.out.print("Enter New Email: ");
        String email = scn.next();
        System.out.print("Enter New Age: ");
        int age = scn.nextInt();

        String query = "UPDATE users SET firstname=?, lastname=?, email=?, age=? WHERE id=?";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, firstName);
            stmt.setString(2, lastName);
            stmt.setString(3, email);
            stmt.setInt(4, age);
            stmt.setInt(5, id);
            stmt.executeUpdate();
            System.out.println("User updated.");
        }
    }

    private static void deleteUser(Connection con, Scanner scn) throws SQLException {
        System.out.print("Enter User ID to delete: ");
        int id = scn.nextInt();

        String query = "DELETE FROM users WHERE id=?";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("User deleted.");
        }
    }
}
