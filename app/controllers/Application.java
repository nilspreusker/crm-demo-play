package controllers;

import java.io.IOException;
import java.util.List;

import javax.persistence.Query;

import models.Customer;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

public class Application extends Controller {

    public static Result index() {
        return ok(index.render());
    }

    // TODO: add search params
    @Transactional(readOnly = true)
    public Result findCustomers() {
        Query query = JPA.em().createQuery("SELECT e FROM Customer e");
        @SuppressWarnings("unchecked")
        List<Customer> customers = query.getResultList();
        ObjectMapper mapper = new ObjectMapper();
        return ok(mapper.convertValue(customers, JsonNode.class));
    }

    @Transactional
    public Result saveCustomer() {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = request().body().asJson();
        try {
            Customer customer = mapper.readValue(json, Customer.class);
            JPA.em().persist(customer);
            response().setHeader(LOCATION, request().host() + request().uri() + "/" + customer.getId());
            return created();
        } catch (JsonParseException e) {
            return badRequest("The JSON you provided seems to be invalid, please provide valid JSON ("
                    + e.getMessage() + ").");
        } catch (JsonMappingException e) {
            return badRequest("The JSON you provided was not understood and could not be interpreted as a customer ("
                    + e.getMessage() + ").");
        } catch (IOException e) {
            return badRequest("Couldn't detect a valid JSON input");
        }
    }

    @Transactional(readOnly = true)
    public Result findCustomerById(Long customerId) {
        ObjectMapper mapper = new ObjectMapper();
        Customer customer = JPA.em().find(Customer.class, customerId);
        JsonNode jsonNode = mapper.convertValue(customer, JsonNode.class);
        return ok(jsonNode);
    }

    @Transactional
    public Result updateCustomer(Long customerId) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = request().body().asJson();
        try {
            Customer customer = mapper.readValue(json, Customer.class);
            JPA.em().merge(customer);
            return ok();
        } catch (JsonParseException e) {
            return badRequest("The JSON you provided seems to be invalid, please provide valid JSON ("
                    + e.getMessage() + ").");
        } catch (JsonMappingException e) {
            return badRequest("The JSON you provided was not understood and could not be interpreted as a customer ("
                    + e.getMessage() + ").");
        } catch (IOException e) {
            return badRequest("Couldn't detect a valid JSON input");
        }
    }

    @Transactional
    public Result deleteCustomer(Long customerId) {
        Customer customer = JPA.em().find(Customer.class, customerId);
        if (customer != null) {
            JPA.em().remove(customer);
        }
        return ok();
    }
}
