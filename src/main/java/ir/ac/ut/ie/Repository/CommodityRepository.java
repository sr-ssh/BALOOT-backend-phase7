package ir.ac.ut.ie.Repository;

import ir.ac.ut.ie.Entities.Provider;
import ir.ac.ut.ie.Entities.Commodity;
import org.springframework.context.annotation.Bean;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Repository
public interface CommodityRepository extends CrudRepository<Commodity, Integer> {
    Commodity findCommodityById(int id);
    List<Commodity> findAllByOrderByReleaseDateDesc();
    List<Commodity> findAllByNameContains(String searchValue);
    List<Commodity> findAllByGenresContains(String searchValue);
    List<Commodity> findAllByReleaseDateAfter(String searchValue);
    List<Commodity> findAllByOrderByImdbRateDesc();
    List<Commodity> findAllByProvider(int cast);
    List<Commodity> findAllByIdIn(Set<Integer> movieIds);
}
