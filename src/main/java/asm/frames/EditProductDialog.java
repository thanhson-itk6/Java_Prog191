package asm.frames;

import asm.WarehouseScreen;
import asm.warehouse.Product;

import javax.swing.*;
import java.awt.event.*;

public class EditProductDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField idInput;
    private JTextField nameInput;
    private JTextField descInput;
    private JTextField quantityInput;
    private JTextField priceInput;
    private JTextField updatedInput;
    private JTextField createdInput;
    private JLabel errorLabel;

    private Product product;

    public EditProductDialog(Product product) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setSize(400, 300);

        setup(this.product = product);

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

    private void setup(Product product) {
        idInput.setText(product.id);
        nameInput.setText(product.name);
        descInput.setText(product.description);
        quantityInput.setText(product.quantity.toString());
        priceInput.setText(product.price.toString());


        if (product.updated_at != null) {
            updatedInput.setText(product.updated_at.toString());
        }

        if (product.created_at != null) {
            createdInput.setText(product.created_at.toString());
        }
    }

    private void onOK() {
        String error = validateContent();

        errorLabel.setText(error);

        if (error.isEmpty() == false) {
            return;
        }

        product.name = nameInput.getText();
        product.description = descInput.getText();
        product.quantity = Integer.parseInt(quantityInput.getText());
        product.price = Double.parseDouble(priceInput.getText());

        product.touch();

        WarehouseScreen.frame.managerFrame.updateTable();

        dispose();
    }

    private String validateContent() {
        String error = "";

        if (nameInput.getText().isEmpty()) {
            error = "Name is required\n";
        }

        if (descInput.getText().isEmpty()) {
            error = "Description is required\n";
        }

        if (quantityInput.getText().isEmpty()) {
            error = "Quantity is required\n";
        }

        if (priceInput.getText().isEmpty()) {
            error = "Price is required\n";
        }

        return error;
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
}
