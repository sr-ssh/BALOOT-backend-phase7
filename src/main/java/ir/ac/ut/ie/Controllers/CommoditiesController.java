package ir.ac.ut.ie.Controllers;

import ir.ac.ut.ie.DataBase;
import ir.ac.ut.ie.Entities.Commodity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;


@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CommoditiesController {

    @RequestMapping(value = "/getCommodities", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Commodity> getCommodities(
            @RequestParam(value = "defaultSort") String defaultSort,
            @RequestParam(value = "searchBy", required = false) String searchBy,
            @RequestParam(value = "searchValue", required = false) String searchValue) throws IOException, InterruptedException {

        if(searchValue == null)
            searchValue ="";
        if(searchBy == null)
            searchBy ="";
        List<Commodity> commodities = DataBase.getInstance().commoditiesToShow(Boolean.parseBoolean(defaultSort), searchBy, searchValue);
        return commodities;
    }
}


