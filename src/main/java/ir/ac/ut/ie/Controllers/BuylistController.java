package ir.ac.ut.ie.Controllers;

import ir.ac.ut.ie.Entities.Commodity;
import ir.ac.ut.ie.Entities.User;
import ir.ac.ut.ie.Exceptions.StockLimitError;
import ir.ac.ut.ie.Exceptions.CommodityAlreadyExists;
import ir.ac.ut.ie.Repository.CommodityRepository;
import ir.ac.ut.ie.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServlet;
import java.io.IOException;
import java.util.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class BuylistController extends HttpServlet {
    @Autowired
    CommodityRepository commodityRepository;
    @Autowired
    UserRepository userRepository;
    private Commodity[] getBuylist(String userId) {
        Set<Integer> commodityIds = userRepository.findUserByEmail(userId).getBuyList();
        return commodityRepository.findAllByIdIn(commodityIds).toArray(new Commodity[0]);
    }

    @RequestMapping(value = "/getBuylist/{userId}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Commodity[] getUser(@PathVariable(value = "userId") String userId) throws Exception {
        return getBuylist(userId);
    }

    @RequestMapping(value = "/addToBuylist/{userId}", method = RequestMethod.POST)
    @ResponseBody
    public String addToBuylist(
            @PathVariable(value = "userId") String userId,
            @RequestParam(value = "CommodityId") Integer commodityId,
            @RequestParam(value = "inStock") Integer inStock) {
        try {
            User user = userRepository.findUserByEmail(userId);
            user.addToBuyList(commodityId, inStock);
            userRepository.save(user);
            return "Commodity Added To Buylist Successfully";
        } catch (CommodityAlreadyExists e1) {
            return e1.getMessage();
        } catch (StockLimitError e2) {
            return e2.getMessage();
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @RequestMapping(value = "/deleteFromBuylist/{userId}", method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Commodity[] deleteFromBuylist(
            @PathVariable(value = "userId") String userId,
            @RequestParam(value = "commodityId") Integer commodityId) {
        userRepository.findUserByEmail(userId).getBuyList().remove(commodityId);
        userRepository.save(userRepository.findUserByEmail(userId));
        return getBuylist(userId);
    }

    @RequestMapping(value = "/getRecommendedCommodities/{userId}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Commodity[] getRecommendedCommodities(@PathVariable(value = "userId") String userId)  {
        List <Integer> recommended_commodities = new ArrayList<>();
        List <Integer> commodityId_byScore = new ArrayList<>();
        List <Integer> scores = new ArrayList<>();
        User current_user =  userRepository.findUserByEmail(userId);
        for (Commodity commodity : commodityRepository.findAll()) {
            int category_similarity_score = 0;
            for (Integer commodityId_in_BuyList : current_user.getBuyList()) {
                Commodity commodity_in_BuyList = commodityRepository.findCommodityById(commodityId_in_BuyList);
                ArrayList <String> temp_list = new ArrayList<>(commodity.getCategories());
                temp_list.retainAll(commodity_in_BuyList.getCategories());
                category_similarity_score += temp_list.size();
            }
            scores.add((int) (3 * category_similarity_score + /*+ commodity.getImdbRate() +*/ commodity.getRating()));
            commodityId_byScore.add(commodity.getId());
            commodity.setScore((int) (3 * category_similarity_score /* commodity.getImdbRate() +*/ + commodity.getRating()));
        }

        while (commodityId_byScore.size() != 0) {
            int max_score_index = scores.indexOf(Collections.max(scores));
            recommended_commodities.add(commodityId_byScore.get(max_score_index));
            scores.remove(max_score_index);
            commodityId_byScore.remove((max_score_index));
        }
        Commodity[] finalList = new Commodity[3];
        for (int i=0; i<3; i++)
            finalList[i] = commodityRepository.findCommodityById(recommended_commodities.get(i));
        return finalList;
    }
}


