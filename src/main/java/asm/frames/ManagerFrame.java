package asm.frames;

import asm.WarehouseScreen;
import asm.warehouse.Product;
import asm.warehouse.Statistic;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.io.File;

public class ManagerFrame extends JFrame {
    private JPanel panel;
    private JTable productTable;
    private JButton queryButton;
    private JTextField searchInput;
    private JButton deleteButton;
    private JButton createAProductButton;
    private JButton editButton;
    private JLabel indexRowLabel;
    private JLabel infoLabel;
    private JComboBox sortInput;
    private JButton clearDataButton;
    private JButton importButton;
    private JButton exportButton;

    private Statistic products = new Statistic();

    private DefaultTableModel productModel;
    private DefaultComboBoxModel<String> sortModel = new DefaultComboBoxModel<>(SortType.values());

    private int indexSelected = 0;

    private String fileData = "product.dat";

    public ManagerFrame() {
        setContentPane(panel);
        setTitle("Warehouse manager");
        sortInput.setModel(sortModel);
        registerEvents();
        loadData();
        setVisible(true);
    }

    protected void registerEvents() {
        productTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                indexSelected = productTable.rowAtPoint(e.getPoint());
                indexRowLabel.setText("Select row index: " + indexSelected);
            }
        });

        createAProductButton.addActionListener(e -> {
            WarehouseScreen.frame.showCreateDialog();
        });

        deleteButton.addActionListener(e -> {
            if (indexSelected < 0 || indexSelected >= productModel.getRowCount()) {
                return;
            }

            remove((String) productModel.getValueAt(indexSelected, 0));
        });

        editButton.addActionListener(e -> {
            if (indexSelected < 0 || indexSelected >= productModel.getRowCount()) {
                return;
            }

            Product product = products.get((String) productModel.getValueAt(indexSelected, 0));

            if (product != null) {
                WarehouseScreen.frame.showEditDialog(product);
            }
        });

        queryButton.addActionListener(e -> {
            Statistic.Sort type = SortType.enumOf((String) sortModel.getSelectedItem());

            Statistic statistic = new Statistic(products.search(searchInput.getText()));

            statistic = new Statistic(statistic.sort(type));

            display(statistic);

            if (searchInput.getText().isEmpty()) return;

            infoLabel.setText(
                "Search result for \"" + searchInput.getText() + "\" found " + statistic.items().count() + " kind of products. " + infoLabel.getText()
            );
        });

        clearDataButton.addActionListener(e -> {
            int response = JOptionPane.showConfirmDialog(
                    null,
                    "Are you sure to delete all data?",
                    "Confirm delete",
                    JOptionPane.YES_NO_OPTION
            );

            if (response == 0) {
                products = new Statistic();
                updateTable();
                saveData();
                infoLabel.setText("All data was deleted!!!");
            }
        });

        exportButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
            chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

            int response = chooser.showOpenDialog(null);

            if (response != JFileChooser.APPROVE_OPTION) {
                return;
            }

            File file = chooser.getSelectedFile();
            file = file.isDirectory() ? new File(file, fileData) : file;

            try {
                products.saveTo(file);
                infoLabel.setText("File saved to: " + file.getAbsolutePath());
            }

            catch (Exception ex) {
                infoLabel.setText("There is error when export file, please try another location!");
            }
        });

        importButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            chooser.setAcceptAllFileFilterUsed(false);
            chooser.setFileFilter(new FileNameExtensionFilter("File .dat only", "dat"));

            int response = chooser.showOpenDialog(null);

            if (response != JFileChooser.APPROVE_OPTION) {
                return;
            }

            products = Statistic.loadFrom(chooser.getSelectedFile().getAbsolutePath());
            updateTable();
            saveData();

            infoLabel.setText("Success loaded " + products.items().count() + " kind of products");
        });


    }

    protected void loadData() {
        products = Statistic.loadFrom(fileData);
        updateTable();
    }

    protected DefaultTableModel createProductModel() {
        return new DefaultTableModel(new String[] {
                "ID", "Name", "Description", "Quantity", "Price"
        }, 0);
    }

    public Statistic getProducts() {
        return products;
    }

    protected void setModel(DefaultTableModel model) {
        productModel = model;
        productTable.setModel(model);
    }

    public void display(Statistic products) {
        DefaultTableModel model = createProductModel();

        for (Product p : products.items().all()) {
            model.addRow(p.toFlat());
        }

        infoLabel.setText(
            "Total products is " + products.totalQuantity() + " items and total value is " + products.totalValue()
        );

        setModel(model);
    }

    public void add(Product product) {
        products.items().push(product);
        updateTable();
        saveData();
    }

    public Product remove(String id) {
        Product product = products.get(id);

        if (product != null) {
            products.items().remove(product);
            updateTable();
            saveData();
        }

        return product;
    }

    protected void saveData() {
        products.saveTo(fileData);
    }

    public void updateTable() {
        display(products);
    }

    protected static class SortType {
        public static String[] values() {
            Statistic.Sort[] types = Statistic.Sort.values();
            String[] values = new String[types.length];

            for (int i = 0; i < types.length ; i++) {
                values[i] = types[i].toString().toLowerCase().replace('_', ' ');
            }

            return values;
        }

        public static Statistic.Sort enumOf(String type) {
            return Statistic.Sort.valueOf(
                type.replace(' ', '_').toUpperCase()
            );
        }
    }
}
