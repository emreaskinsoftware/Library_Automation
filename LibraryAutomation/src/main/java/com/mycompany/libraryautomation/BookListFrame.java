package com.mycompany.libraryautomation;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BookListFrame extends JFrame {

   private JList<String> bookList;
    private JScrollPane scrollPane;
    private JButton borrowButton;
    private JButton backButton;
    String username;

    public BookListFrame() {

    }

    public BookListFrame(String username) {
        this.username = username;
        setTitle("Book List");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        List<String> books = getBooksFromDatabase();

        bookList = new JList<>(books.toArray(new String[0]));
        bookList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scrollPane = new JScrollPane(bookList);
        scrollPane.setBounds(50, 50, 300, 150);
        add(scrollPane);

        borrowButton = new JButton("Borrow");
        borrowButton.setBounds(50, 220, 100, 25);
        add(borrowButton);

        backButton = new JButton("Back");
        backButton.setBounds(200, 220, 100, 25);
        add(backButton);

        borrowButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedBook = bookList.getSelectedValue();
                if (selectedBook != null) {
                    if (!isBookBorrowed(selectedBook)) {
                        borrowBook(selectedBook, username);
                        reloadBookList();
                    } else {
                        JOptionPane.showMessageDialog(null, "This book is already borrowed.");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Please select a book to borrow");
                }
            }
        });

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new UserFrame(username).setVisible(true);
            }
        });

        initComponents();
    }

    private List<String> getBooksFromDatabase() {
        List<String> books = new ArrayList<>();
        try {
            Connection connection = DataBaseHelper.getConnection();
            String sql = "SELECT title FROM Books";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String title = resultSet.getString("title");
                books.add(title);
            }
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: Unable to fetch books from database");
        }
        return books;
    }

    private boolean isBookBorrowed(String bookTitle) {
        boolean isBorrowed = false;
        try {
            Connection connection = DataBaseHelper.getConnection();
            String sql = "SELECT borrowed FROM books WHERE title = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, bookTitle);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int borrowed = resultSet.getInt("borrowed");
                isBorrowed = (borrowed == 1);
            }
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: Unable to check book status in database");
        }
        return isBorrowed;
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
                reloadBookList();
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

    private void reloadBookList() {
        List<String> books = getBooksFromDatabase();
        bookList.setListData(books.toArray(new String[0]));
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
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(BookListFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(BookListFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(BookListFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(BookListFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new BookListFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
