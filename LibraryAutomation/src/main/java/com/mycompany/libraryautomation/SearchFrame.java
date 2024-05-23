/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.libraryautomation;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.time.LocalDate;

public class SearchFrame extends javax.swing.JFrame {

    private JTextField searchField;
    private JButton borrowButton;
    private JButton backButton;
    private JTable booksTable;
    private DefaultTableModel tableModel;
    String username;

    public SearchFrame() {

    }

    public SearchFrame(String username) {
        this.username = username;
        initializeComponents();
    }

    private void initializeComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Search Books");

        // Search field
        searchField = new JTextField();
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                searchBooks();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                searchBooks();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                searchBooks();
            }
        });

        // Borrow button
        borrowButton = new JButton("Borrow");
        borrowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRowIndex = booksTable.getSelectedRow();
                if (selectedRowIndex != -1) {
                    String bookTitle = (String) tableModel.getValueAt(selectedRowIndex, 1);
                    boolean isBorrowed = (boolean) tableModel.getValueAt(selectedRowIndex, 3);
                    if (!isBorrowed) {
                        borrowBook(bookTitle, username);
                    } else {
                        JOptionPane.showMessageDialog(null, "This book is already borrowed.");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Please select a book to borrow");
                }
            }
        });

        // Back button
        backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new UserFrame(username).setVisible(true);
            }
        });

        // Table
        booksTable = new JTable();
        tableModel = new DefaultTableModel();
        tableModel.addColumn("ID");
        tableModel.addColumn("Title");
        tableModel.addColumn("Author");
        tableModel.addColumn("Status");
        booksTable.setModel(tableModel);

        // Layout
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(searchField)
                                        .addComponent(booksTable, GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(borrowButton)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(backButton)))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(searchField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(booksTable, GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(borrowButton)
                                        .addComponent(backButton))
                                .addContainerGap())
        );

        pack();
    }

    private void searchBooks() {
        String searchText = searchField.getText().trim().toLowerCase();
        tableModel.setRowCount(0);

        try {
            Connection connection = DataBaseHelper.getConnection();
            String sql = "SELECT * FROM books WHERE LOWER(title) LIKE ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, "%" + searchText + "%");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("book_id");
                String title = resultSet.getString("title");
                String author = resultSet.getString("author");
                boolean borrowed = resultSet.getBoolean("borrowed");
                String status = borrowed ? "Ödünç Verildi" : "Mevcut";

                tableModel.addRow(new Object[]{id, title, author, borrowed});
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Veritabanına bağlanırken hata oluştu!", "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void borrowBook(String bookTitle, String username) {
        try {
            Connection connection = DataBaseHelper.getConnection();
            LocalDate today = LocalDate.now();
            String dateString = today.toString();
            String bookEntry = bookTitle + " (" + dateString + ")";
            String updateSql = "UPDATE books SET borrowed = 1 WHERE title = ?";
            PreparedStatement updateStatement = connection.prepareStatement(updateSql);
            updateStatement.setString(1, bookTitle);
            int rowsUpdated = updateStatement.executeUpdate();
            if (rowsUpdated > 0) {
                String updateUserSql = "UPDATE users SET borrowed_books = CONCAT(IFNULL(borrowed_books, ''), ?) WHERE username = ?";
                PreparedStatement updateUserStatement = connection.prepareStatement(updateUserSql);
                updateUserStatement.setString(1, (bookEntry + "; "));
                updateUserStatement.setString(2, username);
                updateUserStatement.executeUpdate();
                updateUserStatement.close();
                JOptionPane.showMessageDialog(null, "You borrowed: " + bookTitle);
                searchBooks(); // Güncellenmiş kitap listesini yeniden ara
            } else {
                JOptionPane.showMessageDialog(null, "Error: Unable to borrow book");
            }
            updateStatement.close();
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: Unable to borrow book");
        }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(SearchFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SearchFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SearchFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SearchFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SearchFrame().setVisible(true);
            }
        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
