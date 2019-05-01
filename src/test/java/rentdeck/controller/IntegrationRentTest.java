package rentdeck.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import rentdeck.dao.PropertyDao;
import rentdeck.dao.RentDao;
import rentdeck.dao.UserDao;
import rentdeck.model.DateRanges;
import rentdeck.model.Rent;

import javax.transaction.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@ActiveProfiles("test")
@Transactional
public class IntegrationRentTest {

    private static String authString;

    @Autowired
    RentDao rentDao;

    @Autowired
    PropertyDao propertyDao;

    @Autowired
    UserDao userDao;

    @Autowired
    public JavaMailSender emailSender;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void aFirstSetupTest() throws Exception {

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
    public void allRentEventsIntegrationTest() throws Exception {

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/rent", 1L)
                .header("Authorization", authString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        List<Rent> rents = Arrays.asList(new ObjectMapper()
                .readValue(mvcResult.getResponse().getContentAsString(), Rent[].class));

        assertThat(rents.size()).isEqualTo(1);
        assertThat(rents.get(0).getRenter_username().equalsIgnoreCase("peresau"));
        assertThat(rents.get(0).getRent_id()).isEqualTo(1);
    }

    @Test
    public void allRentEventsUnAuthorizedTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/rent", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void initiateRentIntegrationTest() throws Exception {

        String json = "{\"start\":\"3019-05-21\",\"end\":\"3019-06-30\"}";

        MvcResult mvcResult = mockMvc.perform(post("/api/rent/to_rent/{property_id}", 4L)
                .header("Authorization", authString)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andReturn();

        Rent rent = new ObjectMapper()
                .readValue(mvcResult.getResponse().getContentAsString(), Rent.class);

        assertThat(rent.getRent_id()).isEqualTo(2L);
        assertThat(rent.getRenter_username().equalsIgnoreCase("peresau"));
        assertThat(rent.getState()).isEqualTo(Rent.State.TO_RENT);

    }

    @Test
    public void initiateRentNoPropertyTest() throws Exception {

        String json = "{\"start\":\"3019-05-21\",\"end\":\"3019-06-30\"}";

        mockMvc.perform(post("/api/rent/to_rent/{property_id}", -4L)
                .header("Authorization", authString)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isNotFound());

    }

    @Test
    public void initiateRentOwnPropertyTest() throws Exception {

        String json = "{\"start\":\"3019-05-21\",\"end\":\"3019-06-30\"}";

        mockMvc.perform(post("/api/rent/to_rent/{property_id}", 3L)
                .header("Authorization", authString)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isNotFound());

    }

    @Test
    public void initiateRentWrongPropertyTest() throws Exception {

        String json = "{\"start\":\"3019-05-21\",\"end\":\"3019-06-30\"}";

        mockMvc.perform(post("/api/rent/to_rent/{property_id}", -6L)
                .header("Authorization", authString)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isNotFound());

    }

    @Test
    public void confirmRentIntegrationTest() throws Exception {

        mockMvc.perform(post("/api/rent/confirm/{rent_id}", 1L)
                .header("Authorization", authString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Rent rent = rentDao.findById(1L).get();
        assertThat(rent.getState()).isEqualTo(Rent.State.CONFIRM_RENT);

    }

    @Test
    public void denyIntegrationTest() throws Exception {

        mockMvc.perform(post("/api/rent/deny/{rent_id}", 1L)
                .header("Authorization", authString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Rent rent = rentDao.findById(1L).get();
        assertThat(rent.getState()).isEqualTo(Rent.State.DENY_RENT);
    }

    @Test
    public void confirmRentDoNotExistTest() throws Exception {

        mockMvc.perform(post("/api/rent/confirm/{rent_id}", -2L)
                .header("Authorization", authString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    @Test
    public void denyRentDoNotExistTest() throws Exception {

        mockMvc.perform(post("/api/rent/deny/{rent_id}", -2L)
                .header("Authorization", authString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    @Test
    public void futureDatesIntegrationTest() throws Exception {

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/rent/dates/{property_id}", 2L)
                .header("Authorization", authString)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        List<DateRanges> rents = Arrays.asList(new ObjectMapper()
                .readValue(mvcResult.getResponse().getContentAsString(), DateRanges[].class));

        assertThat(rents.get(0).getStart().toString()).isEqualTo("2089-06-20");
    }

}
