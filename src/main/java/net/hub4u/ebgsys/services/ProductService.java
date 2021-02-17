package net.hub4u.ebgsys.services;

import net.hub4u.ebgsys.entities.Product;

import java.util.List;

public interface ProductService {

    Product createProduct(Product product);

    List<Product> fetchAllProducts();

    void deleteProduct(Long id);

    Product fetchProduct(Long id);

}
