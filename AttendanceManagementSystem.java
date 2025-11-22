import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AttendanceManagementSystem extends JFrame {
    
    // SQLite database file - will be created automatically in the same folder
    private static final String DB_URL = "jdbc:sqlite:attendance.db";
    
    // UI Components
    private JTextField txtStudentId, txtStudentName, txtSearchId;
    private JComboBox<String> cmbStatus;
    private JTable attendanceTable;
    private DefaultTableModel tableModel;
    private JLabel lblDate;
    
    public AttendanceManagementSystem() {
        setupUI();
        createDatabaseAndTable();
        loadAttendanceData();
    }
    
    private void setupUI() {
        setTitle("Attendance Management System");
        setSize(1000, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        
        // Modern color scheme
        Color primaryColor = new Color(41, 128, 185);
        Color backgroundColor = new Color(236, 240, 241);
        Color panelColor = Color.WHITE;
        
        getContentPane().setBackground(backgroundColor);
        
        // Top Panel - Header
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(primaryColor);
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel lblTitle = new JLabel("Attendance Management System", JLabel.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(Color.WHITE);
        
        lblDate = new JLabel(LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy")));
        lblDate.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblDate.setForeground(Color.WHITE);
        lblDate.setHorizontalAlignment(JLabel.CENTER);
        
        topPanel.add(lblTitle, BorderLayout.CENTER);
        topPanel.add(lblDate, BorderLayout.SOUTH);
        
        // Center Panel - Input Form
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(backgroundColor);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(panelColor);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            BorderFactory.createEmptyBorder(30, 40, 30, 40)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Student ID
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lblStudentId = new JLabel("Student ID:");
        lblStudentId.setFont(new Font("Segoe UI", Font.BOLD, 14));
        formPanel.add(lblStudentId, gbc);
        
        gbc.gridx = 1; gbc.gridy = 0;
        txtStudentId = new JTextField(20);
        txtStudentId.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtStudentId.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        formPanel.add(txtStudentId, gbc);
        
        // Student Name
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel lblStudentName = new JLabel("Student Name:");
        lblStudentName.setFont(new Font("Segoe UI", Font.BOLD, 14));
        formPanel.add(lblStudentName, gbc);
        
        gbc.gridx = 1; gbc.gridy = 1;
        txtStudentName = new JTextField(20);
        txtStudentName.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtStudentName.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        formPanel.add(txtStudentName, gbc);
        
        // Status
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel lblStatus = new JLabel("Status:");
        lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 14));
        formPanel.add(lblStatus, gbc);
        
        gbc.gridx = 1; gbc.gridy = 2;
        String[] statuses = {"Present", "Absent", "Late"};
        cmbStatus = new JComboBox<>(statuses);
        cmbStatus.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        formPanel.add(cmbStatus, gbc);
        
        // Buttons Panel
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBackground(panelColor);
        
        JButton btnAdd = createStyledButton("Add Attendance", new Color(46, 204, 113));
        JButton btnUpdate = createStyledButton("Update", new Color(52, 152, 219));
        JButton btnDelete = createStyledButton("Delete", new Color(231, 76, 60));
        JButton btnClear = createStyledButton("Clear", new Color(149, 165, 166));
        
        btnAdd.addActionListener(e -> addAttendance());
        btnUpdate.addActionListener(e -> updateAttendance());
        btnDelete.addActionListener(e -> deleteAttendance());
        btnClear.addActionListener(e -> clearFields());
        
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);
        
        formPanel.add(buttonPanel, gbc);
        
        centerPanel.add(formPanel);
        
        // Bottom Panel - Table
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.setBackground(backgroundColor);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        
        // Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        searchPanel.setBackground(backgroundColor);
        
        JLabel lblSearch = new JLabel("Search by ID:");
        lblSearch.setFont(new Font("Segoe UI", Font.BOLD, 14));
        txtSearchId = new JTextField(15);
        txtSearchId.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtSearchId.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        JButton btnSearch = createStyledButton("Search", new Color(155, 89, 182));
        JButton btnRefresh = createStyledButton("Refresh All", new Color(52, 152, 219));
        
        btnSearch.addActionListener(e -> searchAttendance());
        btnRefresh.addActionListener(e -> loadAttendanceData());
        
        searchPanel.add(lblSearch);
        searchPanel.add(txtSearchId);
        searchPanel.add(btnSearch);
        searchPanel.add(btnRefresh);
        
        // Table
        String[] columns = {"ID", "Student ID", "Student Name", "Date", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        attendanceTable = new JTable(tableModel);
        attendanceTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        attendanceTable.setRowHeight(30);
        attendanceTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        attendanceTable.getTableHeader().setBackground(new Color(52, 73, 94));
        attendanceTable.getTableHeader().setForeground(Color.WHITE);
        attendanceTable.setSelectionBackground(new Color(52, 152, 219));
        attendanceTable.setSelectionForeground(Color.WHITE);
        attendanceTable.setGridColor(new Color(189, 195, 199));
        
        attendanceTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = attendanceTable.getSelectedRow();
                txtStudentId.setText(tableModel.getValueAt(row, 1).toString());
                txtStudentName.setText(tableModel.getValueAt(row, 2).toString());
                cmbStatus.setSelectedItem(tableModel.getValueAt(row, 4).toString());
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(attendanceTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199)));
        
        bottomPanel.add(searchPanel, BorderLayout.NORTH);
        bottomPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Add panels to frame
        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(130, 35));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }
    
    private void createDatabaseAndTable() {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            Statement stmt = conn.createStatement();
            
            // Create table if not exists
            String createTableSQL = "CREATE TABLE IF NOT EXISTS attendance (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "student_id TEXT NOT NULL," +
                    "student_name TEXT NOT NULL," +
                    "date TEXT NOT NULL," +
                    "status TEXT NOT NULL)";
            
            stmt.executeUpdate(createTableSQL);
            stmt.close();
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Database Error: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void addAttendance() {
        String studentId = txtStudentId.getText().trim();
        String studentName = txtStudentName.getText().trim();
        String status = cmbStatus.getSelectedItem().toString();
        
        if (studentId.isEmpty() || studentName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            String sql = "INSERT INTO attendance (student_id, student_name, date, status) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, studentId);
            pstmt.setString(2, studentName);
            pstmt.setString(3, LocalDate.now().toString());
            pstmt.setString(4, status);
            
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Attendance added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearFields();
            loadAttendanceData();
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error adding attendance: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateAttendance() {
        int selectedRow = attendanceTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a record to update!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int id = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
        String studentId = txtStudentId.getText().trim();
        String studentName = txtStudentName.getText().trim();
        String status = cmbStatus.getSelectedItem().toString();
        
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            String sql = "UPDATE attendance SET student_id=?, student_name=?, status=? WHERE id=?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, studentId);
            pstmt.setString(2, studentName);
            pstmt.setString(3, status);
            pstmt.setInt(4, id);
            
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Attendance updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearFields();
            loadAttendanceData();
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error updating attendance: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void deleteAttendance() {
        int selectedRow = attendanceTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a record to delete!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this record?", 
                "Confirm Delete", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            int id = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
            
            try (Connection conn = DriverManager.getConnection(DB_URL)) {
                String sql = "DELETE FROM attendance WHERE id=?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, id);
                pstmt.executeUpdate();
                
                JOptionPane.showMessageDialog(this, "Attendance deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                clearFields();
                loadAttendanceData();
                
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error deleting attendance: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void loadAttendanceData() {
        tableModel.setRowCount(0);
        
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            String sql = "SELECT * FROM attendance ORDER BY date DESC, id DESC";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                Object[] row = {
                    rs.getInt("id"),
                    rs.getString("student_id"),
                    rs.getString("student_name"),
                    rs.getString("date"),
                    rs.getString("status")
                };
                tableModel.addRow(row);
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void searchAttendance() {
        String searchId = txtSearchId.getText().trim();
        if (searchId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a student ID to search!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        tableModel.setRowCount(0);
        
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            String sql = "SELECT * FROM attendance WHERE student_id LIKE ? ORDER BY date DESC";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "%" + searchId + "%");
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Object[] row = {
                    rs.getInt("id"),
                    rs.getString("student_id"),
                    rs.getString("student_name"),
                    rs.getString("date"),
                    rs.getString("status")
                };
                tableModel.addRow(row);
            }
            
            if (tableModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "No records found!", "Information", JOptionPane.INFORMATION_MESSAGE);
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error searching: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void clearFields() {
        txtStudentId.setText("");
        txtStudentName.setText("");
        cmbStatus.setSelectedIndex(0);
        txtSearchId.setText("");
        attendanceTable.clearSelection();
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            AttendanceManagementSystem app = new AttendanceManagementSystem();
            app.setVisible(true);
        });
    }
}