import java.sql.*;
import java.util.Scanner;

public class ProductCRUD {
    static Connection conn;

    public static void main(String[] args) {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/yourDatabase", "yourUsername", "yourPassword");
            Scanner sc = new Scanner(System.in);
            int choice;

            do {
                System.out.println("\n1. Add Product\n2. View Products\n3. Update Product\n4. Delete Product\n5. Exit");
                choice = sc.nextInt();
                sc.nextLine(); // consume newline

                switch (choice) {
                    case 1: addProduct(sc); break;
                    case 2: viewProducts(); break;
                    case 3: updateProduct(sc); break;
                    case 4: deleteProduct(sc); break;
                }
            } while (choice != 5);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void addProduct(Scanner sc) throws SQLException {
        System.out.print("Enter Product Name: ");
        String name = sc.nextLine();
        System.out.print("Enter Price: ");
        double price = sc.nextDouble();
        System.out.print("Enter Quantity: ");
        int qty = sc.nextInt();

        String sql = "INSERT INTO Product(ProductName, Price, Quantity) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false);
            ps.setString(1, name);
            ps.setDouble(2, price);
            ps.setInt(3, qty);
            ps.executeUpdate();
            conn.commit();
            System.out.println("Product added successfully!");
        } catch (SQLException e) {
            conn.rollback();
            e.printStackTrace();
        }
    }

    static void viewProducts() throws SQLException {
        ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM Product");
        System.out.println("ID\tName\tPrice\tQuantity");
        while (rs.next()) {
            System.out.println(rs.getInt("ProductID") + "\t" +
                    rs.getString("ProductName") + "\t" +
                    rs.getDouble("Price") + "\t" +
                    rs.getInt("Quantity"));
        }
    }

    static void updateProduct(Scanner sc) throws SQLException {
        System.out.print("Enter ProductID to update: ");
        int id = sc.nextInt();
        sc.nextLine();
        System.out.print("New Name: ");
        String name = sc.nextLine();
        System.out.print("New Price: ");
        double price = sc.nextDouble();
        System.out.print("New Quantity: ");
        int qty = sc.nextInt();

        String sql = "UPDATE Product SET ProductName=?, Price=?, Quantity=? WHERE ProductID=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false);
            ps.setString(1, name);
            ps.setDouble(2, price);
            ps.setInt(3, qty);
            ps.setInt(4, id);
            ps.executeUpdate();
            conn.commit();
            System.out.println("Product updated.");
        } catch (SQLException e) {
            conn.rollback();
            e.printStackTrace();
        }
    }

    static void deleteProduct(Scanner sc) throws SQLException {
        System.out.print("Enter ProductID to delete: ");
        int id = sc.nextInt();
        String sql = "DELETE FROM Product WHERE ProductID=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false);
            ps.setInt(1, id);
            ps.executeUpdate();
            conn.commit();
            System.out.println("Product deleted.");
        } catch (SQLException e) {
            conn.rollback();
            e.printStackTrace();
        }
    }
}
