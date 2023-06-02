package ir.ac.ut.ie.Controllers;

        import ir.ac.ut.ie.DataBase;
        import ir.ac.ut.ie.Entities.User;
        import org.springframework.http.MediaType;
        import org.springframework.web.bind.annotation.RequestMapping;
        import org.springframework.web.bind.annotation.RequestMethod;
        import org.springframework.web.bind.annotation.RequestParam;
        import org.springframework.web.bind.annotation.RestController;

        import java.io.IOException;
        import java.util.concurrent.TimeUnit;

@RestController
public class UserController {
    @RequestMapping(value = "/getUser", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public User getUser(
            @RequestParam(value = "username") String username,
            @RequestParam(value = "password") String password) throws IOException, InterruptedException {
        TimeUnit.SECONDS.sleep(3);
        return DataBase.getInstance().getAuthenticatedUser(username, password);
    }
    @RequestMapping(value = "/addUser", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public User addUser(
            @RequestParam(value = "username") String name,
            @RequestParam(value = "address") String address,
            @RequestParam(value = "birth_date") String birth_date,
            @RequestParam(value = "email") String email,
            @RequestParam(value = "credit") String credit,
            @RequestParam(value = "password") String password) throws Exception {
        return DataBase.getInstance().addUser(email, password, name, address, birth_date, credit);
    }
}
