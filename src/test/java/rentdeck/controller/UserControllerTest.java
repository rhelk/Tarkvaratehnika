package rentdeck.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import rentdeck.dao.UserDao;
import rentdeck.model.Users;

import java.security.Principal;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@WebMvcTest(value = UserController.class, secure = false)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserDao userDao;

    @Test
    public void getUserByIdTest() throws Exception {

        Users mockedUser = new Users();
        mockedUser.setUser_id(1L);
        Mockito.when(userDao.findById(1L)).thenReturn(Optional.of(mockedUser));
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/get/{id}", 1L)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(System.out::println)
                .andExpect(MockMvcResultMatchers.jsonPath("user_id").value(1));
    }

    @Test
    public void addUserTest() throws Exception {

        Users user = new Users();
        user.setUser_id(-1L);
//        user.setFirst_name("Tim");
//        user.setLast_name("Drake");
//        user.setPassword("notrealone");
//        user.setUsername("Robin");

//        System.out.println(user.toString());
//        System.out.println(objectMapper.writeValueAsString(user));

        String userJson = "{\"user_id\":null,\"first_name\":\"Tim\",\"last_name\":\"Drake\",\"username\":\"Robin\",\"password\":\"notrealone\"}";

//        Users mockUsers = Mockito.mock(Users.class);

        Mockito.when(userDao.save(any(Users.class))).thenReturn(user);

        MvcResult result = mockMvc.perform(post("/api/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isOk())
                .andReturn();

//        assertEquals(result.getResponse().getContentAsString().substring(11, 14), "-1,");
        assertThat(result.getResponse().getContentAsString().substring(11, 14)).isEqualTo("-1,");

    }

    @Test
    public void authCheckTest() throws Exception{

        Users user = new Users();
        user.setUser_id(-1L);

        Principal mockedPrincipal = Mockito.mock(Principal.class);

        Mockito.when(mockedPrincipal.getName()).thenReturn("asha");
        Mockito.when(userDao.findByUsername("asha")).thenReturn(user);

        MvcResult mvcResult = mockMvc.perform(get("/api/user/authCheck")
                .contentType(MediaType.APPLICATION_JSON)
                .principal(mockedPrincipal))
                .andExpect(status().isOk())
                .andReturn();

        Users resultUser = new ObjectMapper().readValue(mvcResult.getResponse().getContentAsString(), Users.class);

        assertThat(resultUser.getUser_id()).isEqualTo(user.getUser_id());
    }

}
