package koodjohvi.buyit.Product;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product, String> {
    Product findByName(String name);
    Product findByUserId(String userId);
    Product findByPrice(double price);
    Product findByQuantity(int quantity);
    Product findByDescription(String description);
}
