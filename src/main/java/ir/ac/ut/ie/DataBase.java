package ir.ac.ut.ie;

import com.fasterxml.jackson.databind.ObjectMapper;
import ir.ac.ut.ie.Entities.Provider;
import ir.ac.ut.ie.Entities.Comment;
import ir.ac.ut.ie.Entities.Commodity;
import ir.ac.ut.ie.Entities.Discount;
import ir.ac.ut.ie.Entities.User;
import ir.ac.ut.ie.Repository.*;
import ir.ac.ut.ie.Utilities.HashCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.*;
import java.net.*;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@Component
public class DataBase {
    private static DataBase instance;
    static private ObjectMapper mapper;
    static private String host;
    private static CommodityRepository commodityRepository = null;
    private static ProviderRepository providerRepository = null;
    private static UserRepository userRepository = null;
    private static CommentRepository commentRepository = null;
    private static DiscountRepository discountRepository = null;
    @Autowired
    public DataBase(CommodityRepository commodityRepository, ProviderRepository providerRepository, UserRepository userRepository, CommentRepository commentRepository, DiscountRepository discountRepository) {
        DataBase.commodityRepository = commodityRepository;
        DataBase.providerRepository = providerRepository;
        DataBase.userRepository = userRepository;
        DataBase.commentRepository = commentRepository;
        DataBase.discountRepository = DataBase.discountRepository;
    }

    public static DataBase getInstance() {
        if (instance == null)
            instance = new DataBase(commodityRepository, providerRepository, userRepository, commentRepository, discountRepository);
        return instance;
    }

    static private String getConnection(String path) throws IOException {
        URL url = new URL(host + path);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        InputStream inputStream = connection.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line = reader.readLine();
        return line;
    }

    @PostConstruct
    public void initialize() {
        mapper = new ObjectMapper();
        host = "http://138.197.181.131:5000";
        setInformation();
    }

    private void setInformation() {
        try {
            setProvidersList();
            setCommoditiesList();
            setUsersList();
            setCommentsList();
            setDiscountsList();
//            setActorsList();
//            setMoviesList();
//            setUsersList();
//            setCommentsList();
            setProviderCommoditesProvide();

        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }

    private void setCommoditiesList() throws Exception {
        String data = getConnection("/api/v2/commodities");
        Commodity[] commoditiesList = mapper.readValue(data, Commodity[].class);
        commodityRepository.saveAll(Arrays.asList(commoditiesList));
    }

    private void setProvidersList() throws Exception {
        String data = getConnection("/api/v2/providers");
        Provider[] providersList = mapper.readValue(data, Provider[].class);
        providerRepository.saveAll(Arrays.asList(providersList));
    }

    private void setProviderCommoditesProvide() {
        for (Provider provider : providerRepository.findAll()) {
            provider.setCommoditiesProvide((ArrayList<Commodity>) commodityRepository.findAllByProvider(provider.getId()));
            providerRepository.save(provider);
        }
    }

    public List<Commodity> getCommoditiesFromProvider(Integer providerId) {
        return commodityRepository.findAllByProvider(providerId);
    }

    private void setUsersList() throws Exception {
        String data = getConnection("/api/users");
        User[] usersList = mapper.readValue(data, User[].class);
        for (User user : usersList)
            user.setPassword(HashCreator.getInstance().getMD5Hash(user.getPassword()));
        userRepository.saveAll(Arrays.asList(usersList));
    }

    private void setCommentsList() throws Exception {
        String data = getConnection("/api/comments");
        Comment[] commentsList = mapper.readValue(data, Comment[].class);
        commentRepository.saveAll(Arrays.asList(commentsList));

        for (Comment comment : commentRepository.findAll()) {
            comment.setUsername(userRepository.findUserByEmail(comment.getUserEmail()).getEmail());
            Commodity commentCommodity = commodityRepository.findCommodityById(comment.getCommodityId());
            commentCommodity.addComment(comment);
            commodityRepository.save(commentCommodity);
            commentRepository.save(comment);
        }
    }

    private void setDiscountsList() throws Exception {
        String data = getConnection("/api/discount");
        Discount[] discountList = mapper.readValue(data, Discount[].class);
        discountRepository.saveAll(Arrays.asList(discountList));
    }

    public Commodity getCommodityById(Integer id) throws Exception {
        return commodityRepository.findCommodityById(id);
    }

    public List<Commodity> commoditiesToShow(boolean defaultSort, String searchBy, String searchValue) {
        List<Commodity> searchedCommoditiesList = setSearchedCommodities(searchValue, searchBy);
        if (defaultSort) return searchedCommoditiesList;
        else {
            List<Commodity> tempCommodities = commodityRepository.findAllByOrderByReleaseDateDesc();
            tempCommodities.retainAll(searchedCommoditiesList);
            return tempCommodities;
        }
    }

    public List<Commodity> setSearchedCommodities(String searchValue, String searchBy) {
        List<Commodity> searchedCommoditiesList = new ArrayList<>();
        if (searchBy.equals("genre"))
            searchedCommoditiesList = commodityRepository.findAllByGenresContains(searchValue);
        if (searchBy.equals("name")) searchedCommoditiesList = commodityRepository.findAllByNameContains(searchValue);
        if (searchBy.equals("releaseDate"))
            searchedCommoditiesList = commodityRepository.findAllByReleaseDateAfter(searchValue);
        if (searchBy.equals("")) searchedCommoditiesList = commodityRepository.findAllByOrderByImdbRateDesc();
        return searchedCommoditiesList;
    }

    public User getAuthenticatedUser(String username, String password) {
        User user = userRepository.findUserByEmailAndPassword(username, password);
        if (user != null) return user;
        User errorUser = new User();
        errorUser.setUsername("error");
        return errorUser;
    }

    public User addUser(String email, String password, String username, String address, String birth_date, String credit) throws ParseException, NoSuchAlgorithmException {
        if (userRepository.findUserByEmail(username) != null) {
            User errorUser = new User();
            errorUser.setUsername("error");
            return errorUser;
        }
        Date birthDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(birth_date);
        String hashPassword = HashCreator.getInstance().getMD5Hash(password);
        User user = new User(email, hashPassword, username, address, birthDate, Integer.parseInt(credit));
        userRepository.save(user);
        return user;
    }

    public User addUserWithGithub(String email, String username, String password, String address, String birth_date, Integer credit) throws Exception {
        User findUser = userRepository.findUserByEmail(username);

        Date birthDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(birth_date);
        Calendar c = Calendar.getInstance();
        c.setTime(birthDate);
        c.add(Calendar.YEAR, -18);
        Date newBirthDate = c.getTime();


        if (findUser != null) {
            findUser.setPassword(password);
            findUser.setPassword(email);
            findUser.setUsername(username);
            findUser.setAddress(address);
            findUser.setBirthDate(newBirthDate);
            findUser.setCredit(credit);
            userRepository.save(findUser);
            return findUser;
        }

        User user = new User(email, password, username, address, newBirthDate, credit);
        userRepository.save(user);
        return user;
    }

    public Comment addComment(String userEmail, Integer commodityId, String text) {
        String username = userRepository.findUserByEmail(userEmail).getUsername();
        Comment comment = new Comment(userEmail, commodityId, text, username);
        commodityRepository.findCommodityById(commodityId).addComment(comment);
        commentRepository.save(comment);
        commodityRepository.save(commodityRepository.findCommodityById(commodityId));
        return comment;
    }
}