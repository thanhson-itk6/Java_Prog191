package asm.warehouse;

import java.util.Date;

public class Product {
    public String id = "";
    public String name = "";
    public String description = "";

    public Integer quantity = 0;
    public Double price = 0.0;

    public Date created_at;
    public Date updated_at;

    public static Product fromString(String str) {
        String[] parts = str.split("\\|\\|");
        Product product = new Product();

        product.id = parts[0];
        product.name = parts[1];
        product.description = parts[2];
        product.quantity = Integer.parseInt(parts[3]);
        product.price = Double.parseDouble(parts[4]);
        product.created_at = new Date(Long.parseLong(parts[5]));
        product.updated_at = new Date(Long.parseLong(parts[6]));

        return product;
    }

    public static ProductFactory factory() {
        return new ProductFactory();
    }

    public void touch() {
        this.updated_at = new Date();
    }

    public String[] toFlat() {
        return new String[] {
            id, name, description, quantity.toString(), price.toString(), created_at.toString(), updated_at.toString()
        };
    }

    public String toString() {
        final String format = "%s||%s||%s||%s||%s||%s||%s";

        return String.format(format, id, name, description, quantity, price, created_at.getTime(), updated_at.getTime());
    }

    public static class ProductFactory {
        protected Product product = new Product();

        public ProductFactory id(String id) {
            product.id = id;
            return this;
        }

        public ProductFactory name(String name) {
            product.name = name;
            return this;
        }

        public ProductFactory description(String description) {
            product.description = description;
            return this;
        }

        public ProductFactory quantity(Integer quantity) {
            product.quantity = quantity;
            return this;
        }

        public ProductFactory price(Double price) {
            product.price = price;
            return this;
        }

        public ProductFactory created_at(Date created_at) {
            product.created_at = created_at;
            return this;
        }

        public ProductFactory updated_at(Date updated_at) {
            product.updated_at = updated_at;
            return this;
        }

        public Product build() {
            return product;
        }
    }
}
