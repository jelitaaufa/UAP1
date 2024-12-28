package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

class Contact {
    String name;
    String phone;
    String imagePath;

    public Contact(String name, String phone, String imagePath) {
        this.name = name;
        this.phone = phone;
        this.imagePath = imagePath;
    }
}
public class ContactManager extends JFrame {
    private ArrayList<Contact> contacts = new ArrayList<>();
    private JTextField nameField, phoneField;
    private JLabel imageLabel;
    private File selectedImageFile; // To store the selected image file
    private DefaultTableModel tableModel;
    private JTable contactTable;

    // Maximum dimensions for the image
    private static final int MAX_IMAGE_WIDTH = 100;
    private static final int MAX_IMAGE_HEIGHT = 100;

    public ContactManager() {
        setTitle("Contact Manager");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        // Create text fields with larger font
        nameField = new JTextField(20); // Increase the number of columns
        phoneField = new JTextField(20); // Increase the number of columns
        Font font = new Font("Arial", Font.PLAIN, 10); // Set a larger font
        nameField.setFont(font);
        phoneField.setFont(font);

        imageLabel = new JLabel("No Image Selected");

        // Set up the table model and JTable
        String[] columnNames = {"Name", "Phone", "Image"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 2) { // Assuming the image is in the third column
                    return ImageIcon.class; // Set the "Image" column to use ImageIcon
                }
                return String.class;
            }
        };
        contactTable = new JTable(tableModel);
        contactTable.setRowHeight(100); // Set row height to accommodate images
        contactTable.getColumnModel().getColumn(0).setPreferredWidth(200); // Set width for Name column
        contactTable.getColumnModel().getColumn(1).setPreferredWidth(100); // Set width for Phone column
        contactTable.getColumnModel().getColumn(2).setPreferredWidth(100); // Set width for Image column

        // Set a larger font for the table
        Font tableFont = new Font("Arial", Font.PLAIN, 16); // Set a larger font for the table
        contactTable.setFont(tableFont);
        contactTable.setRowHeight(100); // Optionally increase row height for better visibility


        JScrollPane scrollPane = new JScrollPane(contactTable);
        scrollPane.setPreferredSize(new Dimension(750, 400)); // Set preferred size for the scroll pane

        JButton addButton = new JButton("Add Contact");
        JButton chooseImageButton = new JButton("Choose Image");
        JButton deleteButton = new JButton("Delete Contact");

        addButton.addActionListener(e -> addContact());
        chooseImageButton.addActionListener(e -> uploadImage());
        deleteButton.addActionListener(e -> deleteContact());

        add(new JLabel("Name:"));
        add(nameField);
        add(new JLabel("Phone:"));
        add(phoneField);
        add(chooseImageButton);
        add(imageLabel);
        add(addButton);
        add(deleteButton);
        add(scrollPane); // Add the table to the frame
    }

    private void uploadImage() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int returnValue = fileChooser.showOpenDialog(this);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            selectedImageFile = fileChooser.getSelectedFile();
            imageLabel.setText(selectedImageFile.getName());
            JOptionPane.showMessageDialog(this, "Image selected: " + selectedImageFile.getName());
        } else {
            JOptionPane.showMessageDialog(this, "No image selected.");
        }
    }

    private void addContact() {
        String name = nameField.getText();
        String phone = phoneField.getText();

        // Validate the name
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name cannot be empty");
            return;
        }

        // Validate the phone number
        boolean valid = true;

        if (phone.length() < 10) { // Example: Minimum length of 10 digits
            JOptionPane.showMessageDialog(this, "Phone number is not valid! Minimum 10 digits required.");
            valid = false;
        }

        if (!phone.matches("\\d+")) { // Check if the phone number contains only digits
            JOptionPane.showMessageDialog(this, "Phone number is not valid! It must contain only digits.");
            valid = false;
        }

        if (valid) {
            // Use the selected image file path if an image was uploaded
            String imagePath = selectedImageFile != null ? selectedImageFile.getAbsolutePath() : null;
            contacts.add(new Contact(name, phone, imagePath));

            // Create an ImageIcon from the selected image file
            ImageIcon imageIcon = null;
            if (selectedImageFile != null) {
                imageIcon = new ImageIcon(new ImageIcon(selectedImageFile.getAbsolutePath()).getImage().getScaledInstance(MAX_IMAGE_WIDTH, MAX_IMAGE_HEIGHT, Image.SCALE_SMOOTH));
            }

            // Add the contact to the table model
            tableModel.addRow(new Object[]{name, phone, imageIcon}); // Add contact to the table display
            JOptionPane.showMessageDialog(this, "Contact added!");


            nameField.setText("");
            phoneField.setText("");
            imageLabel.setText("No Image Selected");
            imageLabel.setIcon(null); // Reset image display
            selectedImageFile = null; // Reset selected image file
        }
    }
    private void deleteContact() {
        // Get the selected row
        int selectedRow = contactTable.getSelectedRow();

        // Check if a row is selected
        if (selectedRow != -1) {
            // Remove the contact from the contacts list
            contacts.remove(selectedRow);
            // Remove the row from the table model
            tableModel.removeRow(selectedRow);
            JOptionPane.showMessageDialog(this, "Contact deleted!");
        } else {
            JOptionPane.showMessageDialog(this, "Please select a contact to delete.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ContactManager manager = new ContactManager();
            manager.setVisible(true);
        });
    }
}