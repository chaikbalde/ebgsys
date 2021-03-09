package net.hub4u.ebgsys.services;

import net.hub4u.ebgsys.entities.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    Product createProduct(Product product);

    List<Product> fetchAllProducts();

    Product deleteProduct(Long id);

    Product fetchProduct(Long id);

    Product fetchProductByReference(String reference);

    Product updateProduct(Product product);

}
