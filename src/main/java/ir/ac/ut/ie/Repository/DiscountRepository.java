package ir.ac.ut.ie.Repository;

import ir.ac.ut.ie.Entities.Discount;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiscountRepository extends CrudRepository<Discount, Integer> {
    Discount findDiscountByDiscountCode(String discountCode);
}
