package me.pckv.kompis;

import me.pckv.kompis.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    private static boolean dataLoaded = false;
    @Autowired private MockMvc mvc;
    @Autowired private UserService userService;

    @Before
    public void setUp() throws Exception {
        if (!dataLoaded) {
            String newUserJson = "{\"email\": \"email\", \"password\": \"password\", \"displayName\": \"displayName\"}";

            mvc.perform(MockMvcRequestBuilders
                    .post("/users")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(newUserJson))
                    .andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.displayName").value("displayName"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));

            dataLoaded = true;
        }
    }

    private String loginAndReturnAuthorization() throws Exception {
        String loginJson = "{\"email\": \"email\", \"password\": \"password\"}";
        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .post("/users/login")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginJson))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.displayName").value("displayName"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andReturn();

        return result.getResponse().getHeader("Authorization");
    }

    @Test
    public void testLogin() throws Exception {
        String token = loginAndReturnAuthorization();
        System.out.println(token);
    }

    @Test
    public void testLoginAndGetCurrentUser() throws Exception {
        String authorization = loginAndReturnAuthorization();
        mvc.perform(MockMvcRequestBuilders
                .get("/users/current")
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", authorization))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.displayName").value("displayName"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));
    }

    @Test
    public void testGetCurrentUserWithoutLogin() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .get("/users/current")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
}
