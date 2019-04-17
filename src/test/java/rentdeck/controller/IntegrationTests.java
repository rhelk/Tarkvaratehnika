package rentdeck.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import rentdeck.dao.PropertyDao;
import rentdeck.dao.UserDao;
import rentdeck.model.Property;
import rentdeck.model.Users;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
//@WebMvcTest
@ActiveProfiles("test")
@Transactional
public class IntegrationTests {

    private static String authString;

    @Autowired
    UserDao uDao;

    @Autowired
    PropertyDao propertyDao;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void AuthCheckFailureTest() throws Exception {
        mockMvc.perform(get("/api/user/authCheck"))
                .andExpect(status().is(401));

    }

    @Test
    public void RentNoAuthTest() throws Exception {
        mockMvc.perform(post("/api/property/rent")
                .contentType(MediaType.APPLICATION_JSON)
                .content("1"))
                .andExpect(status().is(401));
    }

    // FIRST LOGIN TESTS -- Also saves Authentication token.

    @Test
    public void aloginTest() throws Exception {

        String loginJson = "{ \"username\": \"peresau\", \"password\": \"04f8996da763b7a969b1028ee3007569eaf3a635486ddab211d512c85b9df8fb\" }";

        MvcResult mvcResult = mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginJson))
                .andExpect(status().isOk())
                .andReturn();

        authString = mvcResult.getResponse().getHeader("Authorization");

        assertThat(authString).isNotNull();
        assertThat(authString.substring(0,6)).isEqualTo("Bearer");

    }

    @Test
    public void loginWrongPasswordTest() throws Exception {
        String loginJson = "{ \"username\": \"peresau\", \"password\": \"04f8996da763b7a669b1028ee3007569eaf3a635486ddab211d512c85b9df8fb\" }";

        MvcResult mvcResult = mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginJson))
                .andExpect(status().is(401))
                .andReturn();

        String checking = mvcResult.getResponse().getHeader("Authorization");
        assertThat(checking).isNull();
    }

    @Test
    public void loginWrongUsernameTest() throws Exception {
        String loginJson = "{ \"username\": \"perssau\", \"password\": \"04f8996da763b7a969b1028ee3007569eaf3a635486ddab211d512c85b9df8fb\" }";

        MvcResult mvcResult = mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginJson))
                .andExpect(status().is(401))
                .andReturn();

        String checking = mvcResult.getResponse().getHeader("Authorization");
        assertThat(checking).isNull();
    }

    @Test
    public void authCheckIntegrationTest() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/api/user/authCheck")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", authString))
                .andExpect(status().isOk())
                .andReturn();

        Users user = new ObjectMapper().readValue(mvcResult.getResponse().getContentAsString(), Users.class);
    }

    @Test
    public void userByIdTest() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/user/get/{id}", 1L)
                .header("Authorization", authString)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        Users users = new ObjectMapper().readValue(mvcResult.getResponse().getContentAsString(), Users.class);
        assertThat(users.getUsername()).isEqualTo("peresau");
    }

    @Test
    public void userByWrongIdTest() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/user/get/{id}", 25L)
                .header("Authorization", authString)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.NOT_FOUND.value()))
                .andReturn();
    }

    @Test
    public void registerUserTest() throws Exception {

        String userjson = "{ \"first_name\" : \"Petter\", \"last_name\" : \"Iss\", \"username\" : \"a@a\", \"password\": \"d74ff0ee8da3b9806b18c877dbf29bbde50b5bd8e4dad7a3a725000feb82e8f1\"}";
//        System.out.println(userjson);

        MvcResult mvcResult = mockMvc.perform(post("/api/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userjson))
                .andExpect(status().isOk())
                .andReturn();

        Users users = new ObjectMapper().readValue(mvcResult.getResponse().getContentAsString(), Users.class);

        assertThat(users.getUser_id()).isEqualTo(3);
        assertThat(users.getAuthorityList()).isNull();
        assertThat(users.getPassword()).isNull();
    }

    @Test
    public void propertyByIdTest() throws Exception {

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/property/get/{id}", 1L)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        Property property = new ObjectMapper().readValue(mvcResult.getResponse().getContentAsString(), Property.class);
        assertThat(property.getProperty_id()).isEqualTo(1L);
        assertThat(property.getPrice()).isEqualTo(200);

    }

    @Test
    public void propertyByWrongIdTest() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/property/get/{id}", 25L)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.NOT_FOUND.value()))
                .andReturn();
    }

    @Test
    public void getAllPropertiesTest() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/property/all")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        List<Property> properties = Arrays.asList(new ObjectMapper()
                .readValue(mvcResult.getResponse().getContentAsString(), Property[].class));

        assertThat(properties.size()).isEqualTo(4);
        assertThat(properties.get(3).getProperty_id()).isEqualTo(4);
        assertThat(properties.get(2).getPic_url()).isNotNull();

    }

    @Test
    public void validSearchPriceRangeTest() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/property/prices")
                .param("start", "200")
                .param("end", "400")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        List<Property> properties = Arrays.asList(new ObjectMapper()
                .readValue(mvcResult.getResponse().getContentAsString(), Property[].class));

        assertThat(properties.size()).isEqualTo(2);
        assertThat(properties.stream().allMatch(a -> a.getPrice() >= 200 && a.getPrice() <= 400)).isTrue();
    }

    @Test
    public void searchPriceRangeZeroTest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/property/prices")
                .param("start", "330")
                .param("end", "370")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));

    }

    @Test
    public void searchPriceRangeWrongWayTest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/property/prices")
                .param("start", "400")
                .param("end", "200")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));

    }

    @Test
    public void queryByExampleSingleFieldTest() throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        List<Property> results = null;

        //Price check
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/property/search")
                .param("price", "100")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        results = Arrays.asList(mapper.readValue(mvcResult.getResponse().getContentAsString(), Property[].class));

        assertThat(results.size()).isEqualTo(1);
        assertThat(results.get(0).getPrice()).isEqualTo(100);
        assertThat(results.get(0).getProperty_id()).isEqualTo(2);


        // Bed_Count
        mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/property/search")
                .param("bed_count", "2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        results = Arrays.asList(mapper.readValue(mvcResult.getResponse().getContentAsString(), Property[].class));

        assertThat(results.size()).isEqualTo(4);
        assertThat(results.get(0).getBed_count()).isEqualTo(2);
        assertThat(results.get(2).getProperty_id()).isEqualTo(3);

        //room_count
        mockMvc.perform(MockMvcRequestBuilders.get("/api/property/search")
                .param("room_count", "6")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));

        mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/property/search")
                .param("room_count", "3")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        results = Arrays.asList(mapper.readValue(mvcResult.getResponse().getContentAsString(), Property[].class));

        assertThat(results.size()).isEqualTo(3);

    }

    @Test
    public void insertPropertyTest() throws Exception {

        Property property = new Property();
        property.setPrice(300L);
        property.setBed_count(7);
        property.setRoom_count(5);
        property.setPic_url("url");
        property.setTitle("Something");

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        String json = mapper.writeValueAsString(property);

        System.out.println(json);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/secure/property/add")
                .header("Authorization", authString)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andReturn();

        Property result = mapper.readValue(mvcResult.getResponse().getContentAsString(), Property.class);

        assertThat(result.getProperty_id()).isEqualTo(5L);
        assertThat(result.getUsers().getUser_id()).isEqualTo(1);

        // After asserting values add them to property so can do FieldByField comparison
        property.setProperty_id(5L);
        property.setUsers(result.getUsers());

        assertThat(result).isEqualToComparingFieldByField(property);

    }

    @Test
    public void rentIntegerationTest() throws Exception{

        int before = propertyDao.findByVisibility(Property.Visibility.VISIBLE).size();

        mockMvc.perform(post("/api/property/rent")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", authString)
                .content("1"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        int after = propertyDao.findByVisibility(Property.Visibility.VISIBLE).size();

        assertThat(before).isEqualTo(after + 1);

    }

    @Test
    public void rentWrongIdTest() throws Exception {

        int before = propertyDao.findByVisibility(Property.Visibility.VISIBLE).size();

        mockMvc.perform(post("/api/property/rent")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", authString)
                .content("-1"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));

        int after = propertyDao.findByVisibility(Property.Visibility.VISIBLE).size();

        assertThat(before).isEqualTo(after);

    }

    @Test
    public void myPropertiesNoAuthorizationTest() throws Exception {

        mockMvc.perform(get("/api/property/mine")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void myPropertiesIntegrationTest() throws Exception {

        MvcResult mvcResult = mockMvc.perform(get("/api/property/mine")
                .header("Authorization", authString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        List<Property> properties = Arrays.asList(new ObjectMapper()
                .readValue(mvcResult.getResponse().getContentAsString(), Property[].class));

        System.out.println("Size is " + properties.size());
        properties.forEach(System.out::println);

        assertThat(properties.size()).isEqualTo(3);
        assertThat(properties.stream().allMatch(a -> a.getUsers().getUser_id() == 1)).isTrue();
    }


}
