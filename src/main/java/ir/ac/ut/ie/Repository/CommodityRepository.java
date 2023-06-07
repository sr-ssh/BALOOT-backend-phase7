package ir.ac.ut.ie.Repository;

import ir.ac.ut.ie.Entities.Commodity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface CommodityRepository extends CrudRepository<Commodity, Integer> {
    Commodity findCommodityById(int id);
    List<Commodity> findAllByOrderByReleaseDateDesc();
    List<Commodity> findAllByNameContains(String searchValue);
    List<Commodity> findAllByCategoriesContains(String searchValue);
    List<Commodity> findAllByReleaseDateAfter(String searchValue);
    List<Commodity> findAllByOrderByRatingDesc();
    List<Commodity> findAllByProviderId(int cast);
    List<Commodity> findAllByIdIn(Set<Integer> movieIds);
}
