package OopsProject2;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Scanner;
import javax.swing.*;

public class StudentDatabaseGUI extends JFrame {

    private JTextArea outputTextArea;
    private JTextField inputTextField;
    private StudentRecordManagement recordManagement;

    public StudentDatabaseGUI() {
        recordManagement = new StudentRecordManagement();

        // Create the text area to display output
        outputTextArea = new JTextArea(20, 40);

        // Create the text field for input
        inputTextField = new JTextField(40);

        // Redirect System.out to the text area
        PrintStream printStream = new PrintStream(new CustomOutputStream(outputTextArea));
        System.setOut(printStream);
        System.setErr(printStream);

        // Create buttons
        JButton addButton = new JButton("Add");
        JButton viewButton = new JButton("View");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");

        // Add the text area, text field, and buttons to the GUI
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(outputTextArea), BorderLayout.CENTER);
        panel.add(inputTextField, BorderLayout.SOUTH);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(addButton);
        buttonPanel.add(viewButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

        panel.add(buttonPanel, BorderLayout.NORTH);

        getContentPane().add(panel);

        // Set up the GUI
        setTitle("Student Database Management");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        // Add a listener to the text field to process user input
        inputTextField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String input = inputTextField.getText();
                System.out.println("User input: " + input);

                // Process user input
                processUserInput(input);

                inputTextField.setText("");
            }
        });

        // Add listeners to the buttons
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openAddDialog();
            }
        });

        viewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                recordManagement.display();
            }
        });

        updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openUpdateDialog();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openDeleteDialog();
            }
        });
    }

    // Process user input from the text field
    private void processUserInput(String input) {
        Scanner scanner = new Scanner(input);
        String command = scanner.next();

        switch (command) {
            case "add":
                int idNumber = scanner.nextInt();
                int contactNumber = scanner.nextInt();
                String name = scanner.nextLine().trim();
                Record record = new Record(name, idNumber, contactNumber);
                recordManagement.add(record);
                break;
            case "delete":
                int deleteIdNumber = scanner.nextInt();
                recordManagement.delete(deleteIdNumber);
                break;
            case "find":
                int findIdNumber = scanner.nextInt();
                recordManagement.find(findIdNumber);
                break;
            default:
                System.out.println("Invalid command");
        }
    }

    // Open a dialog to add a new student record
    private void openAddDialog() {
        JTextField idNumberField = new JTextField(10);
        JTextField contactNumberField = new JTextField(10);
        JTextField nameField = new JTextField(20);

        JPanel panel = new JPanel(new GridLayout(3, 2));
        panel.add(new JLabel("ID Number:"));
        panel.add(idNumberField);
        panel.add(new JLabel("Contact Number:"));
        panel.add(contactNumberField);
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        int result = JOptionPane.showConfirmDialog(null, panel, "Add Record", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                int idNumber = Integer.parseInt(idNumberField.getText());
                int contactNumber = Integer.parseInt(contactNumberField.getText());
                String name = nameField.getText();
                Record record = new Record(name, idNumber, contactNumber);
                recordManagement.add(record);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input");
            }
        }
    }

    // Open a dialog to update an existing student record
    private void openUpdateDialog() {
        JTextField idNumberField = new JTextField(10);

        JPanel panel = new JPanel(new GridLayout(1, 2));
        panel.add(new JLabel("ID Number:"));
        panel.add(idNumberField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Update Record", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                int idNumber = Integer.parseInt(idNumberField.getText());

                Scanner inputScanner = new Scanner(System.in);
                recordManagement.update(idNumber, inputScanner);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input");
            }
        }
    }

    // Open a dialog to delete an existing student record
    private void openDeleteDialog() {
        JTextField idNumberField = new JTextField(10);

        JPanel panel = new JPanel(new GridLayout(1, 2));
        panel.add(new JLabel("ID Number:"));
        panel.add(idNumberField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Delete Record", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                int idNumber = Integer.parseInt(idNumberField.getText());
                recordManagement.delete(idNumber);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input");
            }
        }
    }

    public static void main(String[] args) {
        new StudentDatabaseGUI();
    }

    // Custom output stream that writes to the text area
    private static class CustomOutputStream extends OutputStream {
        private JTextArea textArea;

        public CustomOutputStream(JTextArea textArea) {
            this.textArea = textArea;
        }

        @Override
        public void write(int b) throws IOException {
            textArea.append(String.valueOf((char) b));
            textArea.setCaretPosition(textArea.getDocument().getLength());
        }
    }
}
