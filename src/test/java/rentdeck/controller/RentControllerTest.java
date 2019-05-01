package rentdeck.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import rentdeck.dao.PropertyDao;
import rentdeck.dao.RentDao;
import rentdeck.dao.UserDao;
import rentdeck.model.DateRanges;
import rentdeck.model.Property;
import rentdeck.model.Rent;
import rentdeck.model.Users;


import java.security.Principal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringRunner.class)
@WebMvcTest(value = RentController.class, secure = false)
public class RentControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    PropertyDao propertyDao;

    @MockBean
    UserDao userDao;

    @MockBean
    RentDao rentDao;

    @MockBean
    public JavaMailSender emailSender;

    private Rent mockedRent;

    private DateRanges mockedRanges = Mockito.mock(DateRanges.class);

    private Principal mockedPrincipal = Mockito.mock(Principal.class);

    private Users mockedUser = Mockito.mock(Users.class);

    private Property mockedProperty;

    public RentControllerTest() {

        mockedRent = new Rent();
        mockedRent.setRenter_username("A");
        mockedRent.setState(Rent.State.TO_RENT);
        mockedRent.setProperty(new Property());
        mockedRent.setOwner_id(1L);
        mockedRent.setStart(Date.valueOf("2019-05-21"));
        mockedRent.setEnd(Date.valueOf("2019-06-30"));

        mockedProperty = new Property();
        mockedProperty.setAddress("abba 1");
        mockedProperty.setPrice(300L);
        mockedProperty.setProperty_id(-1L);
        mockedProperty.setUsers(new Users());
        mockedProperty.getUsers().setUser_id(-2L);
        mockedProperty.getUsers().setUsername("testing");

    }

    @Test
    public void getAllRentRequestsTest() throws Exception {

        when(mockedPrincipal.getName()).thenReturn("asha");
        when(userDao.findByUsername("asha")).thenReturn(mockedUser);
        when(mockedUser.getUser_id()).thenReturn(1L);
        when(mockedUser.getUsername()).thenReturn("testing");

        when(rentDao.findAllRent(1L, "testing"))
                .thenReturn(Arrays.asList(mockedRent, mockedRent));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/rent")
                .principal(mockedPrincipal)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void initiateRentTest() throws Exception {

        String json = "{\"start\":\"2019-05-21\",\"end\":\"2019-06-30\"}";

        when(mockedPrincipal.getName()).thenReturn("asha");

        when(propertyDao.findById(1L)).thenReturn(java.util.Optional.of(mockedProperty));

        mockedRent.setRent_id(-1L);
        when(rentDao.save(any(Rent.class))).thenReturn(mockedRent);

        mockMvc.perform(post("/api/rent/to_rent/{property_id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .principal(mockedPrincipal))
                .andExpect(status().isOk())
                .andExpect(jsonPath("rent_id").value(-1L));
    }

    @Test
    public void datesLockedTest() throws Exception {
        when(rentDao.findAllLaterThanToday(1L))
                .thenReturn(Arrays.asList(mockedRent, mockedRent, mockedRent));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/rent/dates/{property_id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .principal(mockedPrincipal))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));

    }

    @Test
    public void rentConfirmedTest() throws Exception {

        Optional<Rent> capsuledRent = Optional.of(mockedRent);
        when(rentDao.findById(1L)).thenReturn(capsuledRent);

        when(mockedPrincipal.getName()).thenReturn("asha");
        when(userDao.findByUsername("asha")).thenReturn(mockedUser);
        when(mockedUser.getUser_id()).thenReturn(1L);
        when(mockedUser.getUsername()).thenReturn("testing");

        when(rentDao.findDateConflicts(any(Date.class), any(Date.class)))
                .thenReturn(new ArrayList<>());

        when(rentDao.save(any(Rent.class))).thenReturn(mockedRent);

        mockMvc.perform(post("/api/rent/confirm/{rent_id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .principal(mockedPrincipal))
                .andExpect(status().isOk());

    }

    @Test
    public void rentDenyTest() throws Exception {

        Optional<Rent> capsuledRent = Optional.of(mockedRent);
        when(rentDao.findById(1L)).thenReturn(capsuledRent);

        when(mockedPrincipal.getName()).thenReturn("asha");
        when(userDao.findByUsername("asha")).thenReturn(mockedUser);
        when(mockedUser.getUser_id()).thenReturn(1L);
        when(mockedUser.getUsername()).thenReturn("testing");

        when(rentDao.save(any(Rent.class))).thenReturn(mockedRent);

        mockMvc.perform(post("/api/rent/deny/{rent_id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .principal(mockedPrincipal))
                .andExpect(status().isOk());

    }


}
