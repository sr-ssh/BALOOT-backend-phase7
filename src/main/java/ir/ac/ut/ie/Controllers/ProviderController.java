package ir.ac.ut.ie.Controllers;

import ir.ac.ut.ie.DataBase;
import ir.ac.ut.ie.Entities.Commodity;
import ir.ac.ut.ie.Entities.Provider;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;


@RestController
public class ProviderController {
    @RequestMapping(value = "/getProviderCommodities/{id}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Commodity[] getProviderCommodities(@PathVariable(value = "id") Integer id) throws Exception {
        TimeUnit.SECONDS.sleep(3);
        return DataBase.getInstance().getCommoditiesFromProvider(id).toArray(new Commodity[0]);
    }

    @RequestMapping(value = "/getProvider/{id}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Provider getProvider(@PathVariable(value = "id") Integer id) throws Exception {
        TimeUnit.SECONDS.sleep(3);
        return DataBase.getInstance().getProviderById(id);
    }
}
