package asm;

import asm.frames.CreateProductDialog;
import asm.frames.EditProductDialog;
import asm.frames.ManagerFrame;
import asm.warehouse.Product;

import javax.swing.JFrame;

public class WarehouseScreen extends JFrame {
    final public static WarehouseScreen frame = new WarehouseScreen();

    final public ManagerFrame managerFrame = new ManagerFrame();
    public void showCreateDialog() {
        CreateProductDialog dialog = new CreateProductDialog();

        dialog.setVisible(true);
    }

    public void showEditDialog(Product product) {
        EditProductDialog dialog = new EditProductDialog(product);

        dialog.setVisible(true);
    }
}
