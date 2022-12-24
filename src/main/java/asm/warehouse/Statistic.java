package asm.warehouse;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import asm.utils.Collection;

public class Statistic {
    protected Collection<Product> products = new Collection<Product>();

    public enum Sort {
        DEFAULT,
        ID_ASC,
        ID_DESC,
        NAME_ASC,
        NAME_DESC,
        QUANTITY_ASC,
        QUANTITY_DESC,
        PRICE_ASC,
        PRICE_DESC,
        CREATED_AT_ASC,
        CREATED_AT_DESC,
        UPDATED_AT_ASC,
        UPDATED_AT_DESC
    }

    public Statistic() {}

    public Statistic(Collection<Product> products) {
        this.products = products;
    }

    public Collection<Product> search(String query) {
        return products.filter(
            product -> product.name.contains(query) || product.description.contains(query)
        );
    }

    public Collection<Product> sort(Sort type) {
        Collection<Product> products = this.products.clone();

        switch (type) {
            case ID_ASC: products.sort((p1, p2) -> p1.id.compareTo(p2.id)); break;
            case ID_DESC: products.sort((p1, p2) -> p2.id.compareTo(p1.id)); break;
            case NAME_ASC: products.sort((p1, p2) -> p1.name.compareTo(p2.name)); break;
            case NAME_DESC: products.sort((p1, p2) -> p2.name.compareTo(p1.name)); break;
            case QUANTITY_ASC: products.sort((p1, p2) -> p1.quantity.compareTo(p2.quantity)); break;
            case QUANTITY_DESC: products.sort((p1, p2) -> p2.quantity.compareTo(p1.quantity)); break;
            case PRICE_ASC: products.sort((p1, p2) -> p1.price.compareTo(p2.price)); break;
            case PRICE_DESC: products.sort((p1, p2) -> p2.price.compareTo(p1.price)); break;
            case CREATED_AT_ASC: products.sort((p1, p2) -> p1.created_at.compareTo(p2.created_at)); break;
            case CREATED_AT_DESC: products.sort((p1, p2) -> p2.created_at.compareTo(p1.created_at)); break;
            case UPDATED_AT_ASC: products.sort((p1, p2) -> p1.updated_at.compareTo(p2.updated_at)); break;
            case UPDATED_AT_DESC: products.sort((p1, p2) -> p2.updated_at.compareTo(p1.updated_at)); break;

            default: break;
        }

        return products;
    }

    public boolean has(Product product) {
        return products.find(p -> p.id.equals(product.id)) != null;
    }

    public boolean has(String id) {
        return products.find(p -> p.id.equals(id)) != null;
    }

    public Product get(Product product) {
        return products.find(p -> p.id.equals(product.id));
    }

    public Product get(String id) {
        return products.find(p -> p.id == id);
    }

    public int totalQuantity() {
        return products.reduce(0, (sum, p) -> sum + p.quantity);
    }

    public double totalValue() {
        return products.reduce(0.0, (sum, p) -> sum + p.price * p.quantity);
    }

    public int totalQuantity(String id) {
        Product product = get(id);

        if (product == null) {
            return 0;
        }

        return product.quantity;
    }

    public double totalValue(String id) {
        Product product = get(id);

        if (product == null) {
            return 0.0;
        }

        return product.price * product.quantity;
    }

    public Collection<Product> items() {
        return products;
    }

    public String serialize() {
        Collection<String> lines = products.map((p, i) -> p.toString());

        String out = "";

        for (String line : lines.all()) {
            out = out.concat(line + "\n");
        }

        return out.trim();
    }

    public static Statistic fromString(String input) {
        Collection<Product> products = new Collection<Product>();

        if (input == null || input.length() == 0) {
            return new Statistic(products);
        }

        for (String line : input.split("\n")) {
            if (line.trim().isEmpty()) {
                continue;
            }

            products.push(Product.fromString(line));
        }

        return new Statistic(products);
    }

    public void saveTo(String path) {
        try {
            saveTo(new File(path));
        }

        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Statistic loadFrom(String path) {
        try {
            return loadFrom(new File(path));
        }

        catch (Exception e) {
            return new Statistic();
        }
    }

        public void saveTo(File file) throws IOException, FileNotFoundException, UnsupportedEncodingException {

            if (file.exists() == false) file.createNewFile();

            PrintWriter writer = new PrintWriter(file, "UTF-8");

            writer.write(serialize());

            writer.close();
        }

        public static Statistic loadFrom(File file) throws IOException, FileNotFoundException, UnsupportedEncodingException {
            return fromString(new String(java.nio.file.Files.readAllBytes(file.toPath()), "UTF-8"));
        }
}
