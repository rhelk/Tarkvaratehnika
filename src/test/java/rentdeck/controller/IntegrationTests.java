package rentdeck.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import rentdeck.dao.UserDao;
import rentdeck.model.Property;
import rentdeck.model.Users;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
//@WebMvcTest
public class IntegrationTests {

    private static String authString;

    @Autowired
    UserDao uDao;

    @Autowired
    private MockMvc mockMvc;

    // FIRST LOGIN TESTS -- Also saves Authentication token.

    @Test
    public void aloginTest() throws Exception {

        String loginJson = "{ \"username\": \"peresau\", \"password\": \"04f8996da763b7a969b1028ee3007569eaf3a635486ddab211d512c85b9df8fb\" }";

        MvcResult mvcResult = mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginJson))
                .andExpect(status().isOk())
                .andReturn();

        this.authString = mvcResult.getResponse().getHeader("Authorization");

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
        System.out.println(userjson);

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


}
