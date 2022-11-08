package ru.yandex.practicum.filmorate.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.yandex.practicum.filmorate.core.exception.ExceptionsHandler;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setMockMvc() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).setControllerAdvice(new ExceptionsHandler()).build();
    }

    @Test
    void getAll_shouldReturnEmptyList() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void getAll_shouldReturnUsers() throws Exception {
        List<User> users = List.of(getUser(1), getUser(2));
        when(userService.getAll()).thenReturn(users);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(users)));
    }

    @Test
    public void create_shouldSuccessfullyCreateAndUpdateUser() throws Exception {
        User user = getUser(1);
        String json = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/users")
                .accept("application/json")
                .contentType("application/json")
                .content(json)
        ).andExpect(status().isCreated());

        json = objectMapper.writeValueAsString(user.withName("Updated name"));

        mockMvc.perform(put("/users")
                .accept("application/json")
                .contentType("application/json")
                .content(json)
        ).andExpect(status().isOk());
    }

    @Test
    void create_shouldResponseWithBadRequest_ifUserIsInvalid() throws Exception {
        User user;
        String json;

        user = getUser(1).withName("");
        json = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/users").accept("application/json").contentType("application/json").content(json))
                .andExpect(status().isCreated());
        mockMvc.perform(put("/users").contentType("application/json").content(json))
                .andExpect(status().isOk());

        user = getUser(1).withEmail("notEmail");
        json = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/users").contentType("application/json").content(json))
                .andExpect(status().isBadRequest());
        mockMvc.perform(put("/users").contentType("application/json").content(json))
                .andExpect(status().isBadRequest());

        user = getUser(1).withLogin("");
        json = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/users").contentType("application/json").content(json))
                .andExpect(status().isBadRequest());
        mockMvc.perform(put("/users").contentType("application/json").content(json))
                .andExpect(status().isBadRequest());

        user = getUser(1).withBirthday(LocalDate.MAX);
        json = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/users").contentType("application/json").content(json))
                .andExpect(status().isBadRequest());
        mockMvc.perform(put("/users").contentType("application/json").content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void delete_shouldSuccessfullyDeleteUser() throws Exception {
        when(userService.delete(1)).thenReturn(getUser(1));

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(getUser(1))));
    }

    @Test
    public void shouldSuccessfullyAddFriendAndDeleteFriend() throws Exception {
        User user1 = getUser(1);
        User user2 = getUser(2);

        when(userService.addFriend(1, 2)).thenReturn(user1.withFriends(Set.of(2)));
        when(userService.getFriends(1)).thenReturn(List.of(user2));
        when(userService.deleteFriend(1, 2)).thenReturn(user1);

        mockMvc.perform(put("/users/1/friends/2"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(user1.withFriends(Set.of(2)))));

        mockMvc.perform(get("/users/1/friends"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(user2))));

        mockMvc.perform(delete("/users/1/friends/2"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(user1)));
    }

    private static User getUser(int id) {
        return new User(id, "email@email.email", "login", "name", LocalDate.now(), Collections.emptySet());
    }
}
