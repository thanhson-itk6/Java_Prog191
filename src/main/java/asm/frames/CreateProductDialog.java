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
        String error = validateContent();

        errorLabel.setText(error);

        if (error.isEmpty() == false) {
            return;
        }

        store();
        dispose();
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

    private String validateContent() {
        if (idInput.getText().isEmpty()) {
            return "ID is required\n";
        }

        if (WarehouseScreen.frame.managerFrame.getProducts().has(idInput.getText())) {
            return "ID already exists";
        }

        if (nameInput.getText().isEmpty()) {
            return "Name is required\n";
        }

        if (quantityInput.getText().isEmpty()) {
            return "Quantity is required\n";
        }

        if (priceInput.getText().isEmpty()) {
            return "Price is required\n";
        }

        return "";
    }
}
