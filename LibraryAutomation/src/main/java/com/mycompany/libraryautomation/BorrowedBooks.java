/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.libraryautomation;

import java.sql.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

/**
 *
 * @author emre2
 */
public class BorrowedBooks extends JFrame {

    private JList<String> bookList;
    private static String username;
    private JButton returnButton;
    private JButton backButton;

    public BorrowedBooks(String username) {
        this.username = username;
        List<String> books = getBooksFromDatabase();
        bookList = new JList<>(books.toArray(new String[0]));
        bookList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        bookList.setFixedCellHeight(25);

        JScrollPane scrollPane = new JScrollPane(bookList);
        scrollPane.setBounds(50, 50, 300, 150);
        add(scrollPane);

        returnButton = new JButton("Return Book");
        returnButton.setBounds(50, 220, 120, 25);
        add(returnButton);

        returnButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedBook = bookList.getSelectedValue();
                if (selectedBook != null) {
                    returnBook(selectedBook);
                } else {
                    JOptionPane.showMessageDialog(null, "Please select a book to return");
                }
            }
        });

        backButton = new JButton("Back");
        backButton.setBounds(230, 220, 120, 25);
        add(backButton);

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                UserFrame userFrame = new UserFrame(username);
                userFrame.setVisible(true);
            }
        });

        initComponents();
    }

    private List<String> getBooksFromDatabase() {
        List<String> books = new ArrayList<>();
        try {
            // Veritabanı bağlantısını al
            Connection connection = DataBaseHelper.getConnection();

            // SQL sorgusu
            String sql = "SELECT borrowed_books FROM users WHERE username = ?";

            // PreparedStatement oluştur
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);

            // Sorguyu çalıştır ve sonucu al
            ResultSet resultSet = statement.executeQuery();

            // Kullanıcıya ait borrowed_books verisini al
            if (resultSet.next()) {
                String borrowedBooks = resultSet.getString("borrowed_books");

                if (borrowedBooks != null && !borrowedBooks.isEmpty()) {
                    // Kitapları listeye ekle
                    String[] bookArray = borrowedBooks.split(";");
                    for (String book : bookArray) {
                        String trimmedBook = book.trim(); // Boşlukları temizle
                        if (!trimmedBook.isEmpty()) {
                            books.add(trimmedBook);
                        }
                    }
                } else {
                    // Herhangi bir kitap yoksa
                    books.add("Henüz hiç kitap ödünç almamışsınız.");
                }
            } else {
                // Kullanıcı bulunamadı
                books.add("Kullanıcı adına uygun bir kayıt bulunamadı.");
            }

            // Kaynakları serbest bırak
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Veritabanına bağlanırken bir hata oluştu: " + ex.getMessage());
        }
        return books;
    }

    private void returnBook(String selectedBook) {
    try {
        // Tarihi çıkararak sadece kitap adını al
        String[] parts = selectedBook.split("\\(");
        String bookTitle = parts[0].trim();

        // Veritabanı bağlantısını al
        Connection connection = DataBaseHelper.getConnection();

        // SQL sorgusu
        String updateUserSql = "UPDATE users SET borrowed_books = ? WHERE username = ?";
        String updateBookSql = "UPDATE books SET borrowed = 0 WHERE title = ?";

        // PreparedStatement oluştur
        PreparedStatement updateUserStatement = connection.prepareStatement(updateUserSql);
        PreparedStatement updateBookStatement = connection.prepareStatement(updateBookSql);

        // Kullanıcının mevcut borrowed_books listesini alın
        String getUserBooksSql = "SELECT borrowed_books FROM users WHERE username = ?";
        PreparedStatement getUserBooksStatement = connection.prepareStatement(getUserBooksSql);
        getUserBooksStatement.setString(1, username);
        ResultSet resultSet = getUserBooksStatement.executeQuery();

        if (resultSet.next()) {
            String borrowedBooks = resultSet.getString("borrowed_books");
            if (borrowedBooks != null) {
                // Kitap listesinden geri dönen kitabı çıkar
                borrowedBooks = borrowedBooks.replace(", " + selectedBook, "").trim();
                borrowedBooks = borrowedBooks.replace(selectedBook, "").trim();

                // Kullanıcının borrowed_books listesini güncelle
                updateUserStatement.setString(1, borrowedBooks);
                updateUserStatement.setString(2, username);

                // Parametreleri ayarla
                updateBookStatement.setString(1, bookTitle);

                // Sorguları çalıştır
                int userUpdateCount = updateUserStatement.executeUpdate();
                int bookUpdateCount = updateBookStatement.executeUpdate();

                // Güncelleme başarılıysa
                if (userUpdateCount > 0 && bookUpdateCount > 0) {
                    JOptionPane.showMessageDialog(null, "Book returned successfully");
                    // JList'i güncelle
                    DefaultListModel<String> model = new DefaultListModel<>();
                    bookList.setModel(model); // Yeni bir model oluşturarak eski modeli değiştirir
                    List<String> updatedBooks = getBooksFromDatabase();
                    for (String book : updatedBooks) {
                        model.addElement(book);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Error: Unable to return book");
                }
            }
        }

        // Kaynakları serbest bırak
        resultSet.close();
        getUserBooksStatement.close();
        updateUserStatement.close();
        updateBookStatement.close();
        connection.close();

    } catch (SQLException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(null, "Error: Unable to return book");
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
            java.util.logging.Logger.getLogger(BorrowedBooks.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(BorrowedBooks.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(BorrowedBooks.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(BorrowedBooks.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new BorrowedBooks(username).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
