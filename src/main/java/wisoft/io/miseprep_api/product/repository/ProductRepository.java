package wisoft.io.miseprep_api.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wisoft.io.miseprep_api.global.enums.Category;
import wisoft.io.miseprep_api.product.entity.Product;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategory(Category category);

    List<Product> findByNameContainingIgnoreCase(String keyword);
}
