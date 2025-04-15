//Student.java
public class Student {
    private int studentID;
    private String name;
    private String department;
    private double marks;

    public Student(int studentID, String name, String department, double marks) {
        this.studentID = studentID;
        this.name = name;
        this.department = department;
        this.marks = marks;
    }

    // Getters and Setters...
}


//StudentController.java
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentController {
    private Connection conn;

    public StudentController() throws SQLException {
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/yourDatabase", "yourUsername", "yourPassword");
    }

    public void addStudent(Student s) throws SQLException {
        String sql = "INSERT INTO Student VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, s.getStudentID());
            ps.setString(2, s.getName());
            ps.setString(3, s.getDepartment());
            ps.setDouble(4, s.getMarks());
            ps.executeUpdate();
        }
    }

    public List<Student> getAllStudents() throws SQLException {
        List<Student> list = new ArrayList<>();
        ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM Student");
        while (rs.next()) {
            list.add(new Student(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getDouble(4)));
        }
        return list;
    }

    public void updateStudent(Student s) throws SQLException {
        String sql = "UPDATE Student SET Name=?, Department=?, Marks=? WHERE StudentID=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, s.getName());
            ps.setString(2, s.getDepartment());
            ps.setDouble(3, s.getMarks());
            ps.setInt(4, s.getStudentID());
            ps.executeUpdate();
        }
    }

    public void deleteStudent(int studentID) throws SQLException {
        String sql = "DELETE FROM Student WHERE StudentID=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentID);
            ps.executeUpdate();
        }
    }

  //StudentView.java
  import java.util.Scanner;
import java.util.List;

public class StudentView {
    public static void main(String[] args) throws Exception {
        StudentController controller = new StudentController();
        Scanner sc = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n1. Add\n2. View\n3. Update\n4. Delete\n5. Exit");
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("ID: "); int id = sc.nextInt(); sc.nextLine();
                    System.out.print("Name: "); String name = sc.nextLine();
                    System.out.print("Dept: "); String dept = sc.nextLine();
                    System.out.print("Marks: "); double marks = sc.nextDouble();
                    controller.addStudent(new Student(id, name, dept, marks));
                    break;
                case 2:
                    List<Student> list = controller.getAllStudents();
                    for (Student s : list)
                        System.out.println(s.getStudentID() + " " + s.getName() + " " + s.getDepartment() + " " + s.getMarks());
                    break;
                case 3:
                    System.out.print("ID: "); id = sc.nextInt(); sc.nextLine();
                    System.out.print("New Name: "); name = sc.nextLine();
                    System.out.print("New Dept: "); dept = sc.nextLine();
                    System.out.print("New Marks: "); marks = sc.nextDouble();
                    controller.updateStudent(new Student(id, name, dept, marks));
                    break;
                case 4:
                    System.out.print("Enter ID to delete: "); id = sc.nextInt();
                    controller.deleteStudent(id);
                    break;
            }
        } while (choice != 5);
    }
}

}
