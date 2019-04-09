package rentdeck.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Example;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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

    Property mockedProperty;

    public PropertyControllerTest() {
        mockedProperty = new Property();
        mockedProperty.setAddress("abba 1");
        mockedProperty.setPrice(300L);
        mockedProperty.setProperty_id(-1L);

    }

    @Test
    public void getPropertyByIdTest() throws Exception {

        when(propertyDao.findById(-1L)).thenReturn(Optional.of(mockedProperty));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/property/get/{id}", -1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("address").value("abba 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("property_id").value(-1));

    }

    @Test
    public void getAllPropertyTest() throws Exception {

        when(propertyDao.findByVisibility(Property.Visibility.VISIBLE)).thenReturn(Arrays.asList(mockedProperty, mockedProperty, mockedProperty));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/property/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    public void addPropertyTest() throws Exception {

        String propJson = "{\"property_id\":-1,\"title\":\"testing\",\"description\":null,\"address\":\"abba 1\",\"pic_url\":\"somethingsae\",\"room_count\":9,\"bed_count\":9,\"users\":null,\"price\":700}";

        Principal mockedPrincipal = Mockito.mock(Principal.class);
        when(mockedPrincipal.getName()).thenReturn("asha");

        System.out.println(propJson);
        System.out.println(new ObjectMapper().readValue(propJson, Property.class));

        when(propertyDao.save(any(Property.class))).thenReturn(mockedProperty);

        mockMvc.perform(post("/api/secure/property/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(propJson)
                .principal(mockedPrincipal))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("property_id").value(-1));
    }

    @Test
    public void searchPropertiesTest() throws Exception {

        when(propertyDao.findAll(any(Example.class))).thenReturn(Arrays.asList(mockedProperty, mockedProperty, mockedProperty));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/property/search")
                .param("county", "Tallinn")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    public void searchByPriceTest() throws Exception {
        when(propertyDao.searchByPrice(anyLong(), anyLong())).thenReturn(Arrays.asList(mockedProperty, mockedProperty, mockedProperty));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/property/prices")
                .param("start", "200")
                .param("end", "400")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));

    }

    @Test
    public void rentTest() throws Exception {
        when(propertyDao.setHidden(1L)).thenReturn(1);

        Principal mockedPrincipal = Mockito.mock(Principal.class);
        when(mockedPrincipal.getName()).thenReturn("asha");


        mockMvc.perform(post("/api/property/rent")
                .contentType(MediaType.APPLICATION_JSON)
                .principal(mockedPrincipal)
                .content("1"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    public void myPropertiesTest() throws Exception {

        Principal mockedPrincipal = Mockito.mock(Principal.class);
        when(mockedPrincipal.getName()).thenReturn("asha");

        when(propertyDao.findAll(any(Example.class)))
                .thenReturn(Arrays.asList(mockedProperty, mockedProperty, mockedProperty));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/property/mine")
                .principal(mockedPrincipal)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }

}
