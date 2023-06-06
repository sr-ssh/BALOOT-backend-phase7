package ir.ac.ut.ie.Controllers;

import ir.ac.ut.ie.DataBase;
import ir.ac.ut.ie.Entities.Provider;
import ir.ac.ut.ie.Entities.Commodity;
import ir.ac.ut.ie.Repository.ProviderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ProviderController {
    @Autowired
    ProviderRepository providerRepository;

    @RequestMapping(value = "/getProviderCommodities/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Commodity[] getProviderCommodities(@PathVariable(value = "id") Integer id) throws Exception {
        return DataBase.getInstance().getCommoditiesFromProvider(id).toArray(new Commodity[0]);
    }

    @RequestMapping(value = "/getProvider/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Provider getProvider(@PathVariable(value = "id") Integer id) throws Exception {
        return providerRepository.findProviderById(id);
    }
}
