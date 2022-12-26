package asm.frames;

import javax.swing.*;

import asm.WarehouseScreen;
import asm.warehouse.Product;

import java.awt.event.*;
import java.util.Date;

public class CreateProductDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField idInput;
    private JTextField nameInput;
    private JTextField descInput;
    private JTextField quantityInput;
    private JTextField priceInput;
    private JLabel errorLabel;

    public CreateProductDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setSize(400, 270);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {

        try {
            StringBuilder sb = new StringBuilder();
            checkID(idInput, sb);
            checkName(nameInput, sb);
            checkPrice(priceInput, sb);
            checkQuantity(quantityInput, sb);
            if (sb.length() > 0) {
                JOptionPane.showMessageDialog(this, sb.toString(), "Invalid Data", JOptionPane.ERROR_MESSAGE);
                return;
            }


            store();

            dispose();
            JOptionPane.showMessageDialog(this, "Add Successful");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }


    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    private void store() {
        Product product = Product.factory()
                .id(idInput.getText())
                .name(nameInput.getText())
                .description(descInput.getText())
                .quantity(Integer.parseInt(quantityInput.getText()))
                .price(Double.parseDouble(priceInput.getText()))
                .created_at(new Date())
                .updated_at(new Date())
                .build();

        WarehouseScreen.frame.managerFrame.add(product);
    }

    private void checkID(JTextField field, StringBuilder sb) {

        String id = field.getText();
        if (id.isEmpty()) {
            sb.append("ID is required\n");
        }
        try {

            if (WarehouseScreen.frame.managerFrame.getProducts().has(id)) {
                sb.append("ID already exists\n");

            }
        } catch (Exception e) {


        }

    }

    private void checkName(JTextField field, StringBuilder sb) {

        try {

            String name = field.getText();
            if (name.isEmpty()) {
                sb.append("Name is required\n");
            }


        } catch (Exception e) {
            sb.append("Error name: \n");

        }

    }

    private void checkQuantity(JTextField field, StringBuilder sb) {

        String quantity = field.getText();
        if (quantity.isEmpty()) {
            sb.append("quantity is required\n");
        }

        try {
            char[] characters = quantity.toCharArray();
            for (char character : characters) {
                if (Character.isLetter(character)) {
                    sb.append("Quantity does not contain letter\n");
                    break;
                }
            }
        } catch (Exception e) {
            sb.append("Invalid Quantity(default number)\n");
        }


    }

    private void checkPrice(JTextField field, StringBuilder sb) {

//        double price = Double.parseDouble(field.getText());
        String price = field.getText();
        if (price.isEmpty()) {
            sb.append("Price is required\n");
            try {
                char[] characters = price.toCharArray();
                for (char character : characters) {
                    if (Character.isLetter(character)) {
                        sb.append("price does not contain letter\n");
                        break;
                    }
                }
//            if (price<0) {
//                sb.append("price >0\n");


            } catch (Exception e) {
                sb.append("Invalid Price(default number)\n");
            }


        }

    }
}
