package rentdeck.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import rentdeck.dao.PropertyDao;
import rentdeck.dao.UserDao;
import rentdeck.model.Property;

import java.security.Principal;
import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = PropertyController.class, secure = false)
public class PropertyControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    PropertyDao propertyDao;

    @MockBean
    UserDao userDao;

    @Test
    public void getPropertyByIdTest() throws Exception {

        Property mockedProperty = new Property();
        mockedProperty.setAddress("abba 1");
        mockedProperty.setPrice(300L);
        mockedProperty.setProperty_id(-1L);

        when(propertyDao.findById(-1L)).thenReturn(Optional.of(mockedProperty));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/property/get/{id}", -1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("address").value("abba 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("property_id").value(-1));

    }

    @Test
    public void getAllPropertyTest() throws Exception {

        Property mockedProperty = new Property();
        mockedProperty.setAddress("abba 1");
        mockedProperty.setPrice(300L);
        mockedProperty.setProperty_id(-1L);

        when(propertyDao.findAll()).thenReturn(Arrays.asList(mockedProperty, mockedProperty, mockedProperty));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/properties")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    public void addPropertyTest() throws Exception {

        Property mockedProperty = new Property();
        mockedProperty.setAddress("abba 1");
        mockedProperty.setPrice(300L);
        mockedProperty.setProperty_id(-1L);

        String propJson = "{\"property_id\":-1,\"title\":null,\"description\":null,\"address\":\"abba 1\",\"pic_url\":null,\"room_count\":null,\"bed_count\":null,\"users\":null,\"price\":300}";

        Principal mockedPrincipal = Mockito.mock(Principal.class);
        when(mockedPrincipal.getName()).thenReturn("asha");

        when(propertyDao.save(any(Property.class))).thenReturn(mockedProperty);

        mockMvc.perform(post("/api/property/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(propJson)
                .principal(mockedPrincipal))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("property_id").value(-1));
    }

}