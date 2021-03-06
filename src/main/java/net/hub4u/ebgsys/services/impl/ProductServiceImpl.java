package net.hub4u.ebgsys.services.impl;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.hub4u.ebgsys.entities.Product;
import net.hub4u.ebgsys.repositories.ProductRepository;
import net.hub4u.ebgsys.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductRepository productRepository;

    @Override
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public List<Product> fetchAllProducts() {
        Iterable<Product>  productIterable = productRepository.findAll();
        List<Product> products   = new ArrayList<>();
        productIterable.forEach(products::add);
        return products;
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public Product fetchProduct(Long id) {
        return productRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("fetchProduct() - Failed finding Product with Id: " + id) );
    }

    @Override
    public Product fetchProductByReference(String reference) {
        return productRepository.findByReference(reference).orElseThrow(
                () -> new IllegalArgumentException("fetchProductByReference() - Failed finding Product with reference: " + reference) );
    }

    @Override
    public Product updateProduct(Product product) {

        Product currentProduct = fetchProduct(product.getId());

        currentProduct.setReference(product.getReference());
        currentProduct.setCost(product.getCost());
        currentProduct.setSaleProducts(product.getSaleProducts());
        currentProduct.setDescription(product.getDescription());
        currentProduct.setGrossPrice(product.getGrossPrice());
        currentProduct.setName(product.getName());
        currentProduct.setUnitPrice(product.getUnitPrice());
        currentProduct.setNextReferenceView(product.getNextReferenceView());

        return productRepository.save(currentProduct);
    }
}
