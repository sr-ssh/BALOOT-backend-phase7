package ir.ac.ut.ie.Repository;

import ir.ac.ut.ie.Entities.Provider;
import ir.ac.ut.ie.Entities.Commodity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ProviderRepository extends CrudRepository<Provider, Integer> {
    Provider findProviderById(int id);
    List<Provider> findAllByCommoditiesProvide(Commodity commodity);
}
