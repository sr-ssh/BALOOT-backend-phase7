package ir.ac.ut.ie.Controllers;
import ir.ac.ut.ie.DataBase;
import ir.ac.ut.ie.Entities.*;
import ir.ac.ut.ie.Repository.CommodityRepository;
import ir.ac.ut.ie.Repository.RateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;


@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CommodityController {
    @Autowired
    CommodityRepository commodityRepository;
    @Autowired
    RateRepository rateRepository;
    @RequestMapping(value = "/getCommodity/{id}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Commodity getCommodity(@PathVariable(value = "id") Integer id) throws Exception {
        TimeUnit.SECONDS.sleep(3);
        return DataBase.getInstance().getCommodityById(id);
    }

//    @RequestMapping(value = "/getMovieActors/{id}", method = RequestMethod.GET,
//            produces = MediaType.APPLICATION_JSON_VALUE)
//    public Actor[] getMovieActors(@PathVariable(value = "id") Integer id) throws Exception {
//        List<Actor> actors = new ArrayList<>();
//        Movie movie = DataBase.getInstance().getMovieById(id);
//        for (Integer actorId : movie.getCast()) {
//            actors.add(DataBase.getInstance().getActorById(actorId));
//        }
//        TimeUnit.SECONDS.sleep(3);
//        return actors.toArray(new Actor[0]);
//    }

    @RequestMapping(value = "/postRate/{commodityId}", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Commodity postRate(
            @PathVariable(value = "commodityId") Integer commodityId,
            @RequestParam(value = "userId") String userId,
            @RequestParam(value = "rate") int score) throws Exception {
        Rate newRate = new Rate(userId, commodityId, score);
        Commodity commodity = commodityRepository.findCommodityById(commodityId);
        commodity.addRate(newRate, rateRepository);
        rateRepository.save(newRate);
        commodityRepository.save(commodity);
        return commodity;
    }
}
