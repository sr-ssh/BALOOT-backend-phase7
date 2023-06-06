package ir.ac.ut.ie.Entities;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import ir.ac.ut.ie.Exceptions.InvalidCommand;
import javax.persistence.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;
@Entity
@Table(name = "Actor")
public class Provider {
    @Id
    private Integer id;
    private String name;
    private Date registryDate;
    @ManyToMany
    private List<Commodity> commoditiesProvide;


    public void update(Provider updatedActor) {
        name = updatedActor.getName();
        registryDate = updatedActor.getRegistryDate();
    }

    public ObjectNode getInformation(ObjectMapper mapper) {
        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.put("providerId", id);
        objectNode.put("name", name);
        return objectNode;
    }

    public void checkForInvalidCommand() throws InvalidCommand {
        if (id==null || name==null || registryDate==null)
            throw new InvalidCommand();
    }

    public int getAge() {
        LocalDate birthDate = new java.sql.Date(this.registryDate.getTime()).toLocalDate();
        int age = Period.between(birthDate, LocalDate.now()).getYears();
        return age;
    }
    public void setCommoditiesProvide(ArrayList<Commodity> commoditiesProvide) {
        this.commoditiesProvide = new ArrayList<>();
        this.commoditiesProvide = commoditiesProvide;
    }

    public List<Commodity> getCommoditiesProvide() {
        return commoditiesProvide;
    }
    public Integer getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public Date getRegistryDate(){
        return registryDate;
    }
}
