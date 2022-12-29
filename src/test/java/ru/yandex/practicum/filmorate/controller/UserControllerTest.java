package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.model.user.UserDto;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void getAllUsers_shouldReturnEmptyList() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void getAllUsers_shouldReturnUsers() throws Exception {
        List<User> users = List.of(getUser(1), getUser(2));

        when(userService.getAll()).thenReturn(users);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(users)));
    }

    @Test
    void getUser_shouldReturnUser() throws Exception {
        int id = 1;
        User user = getUser(id);

        when(userService.getById(id)).thenReturn(user);

        mockMvc.perform(get("/users/" + id))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(user)));
    }

    @Test
    void createUser_updateUser_shouldSuccessfullyCreateAndUpdateUser() throws Exception {
        int id = 1;
        UserDto userDto = getUserDto(id);
        User user = getUser(id);
        String json = objectMapper.writeValueAsString(userDto);

        when(userService.create(userDto)).thenReturn(user);

        mockMvc.perform(post("/users").contentType("application/json").content(json))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(user)));

        userDto = userDto.withName("Updated name");
        user = user.withName("Updated name");
        json = objectMapper.writeValueAsString(userDto);

        when(userService.update(userDto)).thenReturn(user);

        mockMvc.perform(put("/users").contentType("application/json").content(json))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(user)));
    }

    @ParameterizedTest
    @MethodSource("userInvalidArgumentsProvider")
    void createUser_updateUser_shouldResponseWithBadRequest_ifUserIsInvalid(UserDto userDto) throws Exception {
        String json = objectMapper.writeValueAsString(userDto);

        mockMvc.perform(post("/users").contentType("application/json").content(json))
                .andExpect(status().isBadRequest());
        mockMvc.perform(put("/users").contentType("application/json").content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteUser_shouldSuccessfullyDeleteUser() throws Exception {
        int id = 1;
        User user = getUser(id);

        when(userService.delete(id)).thenReturn(user);

        mockMvc.perform(delete("/users/" + id))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(user)));
    }

    @Test
    void getFriends_shouldReturnFriendsList() throws Exception {
        int id = 1;
        List<User> users = List.of(getUser(2), getUser(3));

        when(userService.getFriends(id)).thenReturn(users);

        mockMvc.perform(get("/users/" + id + "/friends"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(users)));
    }

    @Test
    void getCommonFriends_shouldReturnFriendsList() throws Exception {
        int user1Id = 1;
        int user2Id = 2;
        List<User> users = List.of(getUser(3), getUser(4));

        when(userService.getCommonFriends(user1Id, user2Id)).thenReturn(users);

        mockMvc.perform(get("/users/" + user1Id + "/friends/common/" + user2Id))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(users)));
    }

    private static User getUser(int id) {
        return User.builder()
                .id(id)
                .email("test@test.test")
                .login("test_login")
                .name("Test name")
                .birthday(LocalDate.EPOCH)
                .build();
    }

    private static UserDto getUserDto(int id) {
        return UserDto.builder()
                .id(id)
                .email("test@test.test")
                .login("test_login")
                .name("Test name")
                .birthday(LocalDate.EPOCH)
                .build();
    }

    private static Stream<UserDto> userInvalidArgumentsProvider() {
        UserDto userDto = getUserDto(1);

        return Stream.of(
                userDto.withEmail(null),
                userDto.withEmail(""),
                userDto.withEmail(" "),
                userDto.withEmail("notEmail"),
                userDto.withLogin(null),
                userDto.withLogin(""),
                userDto.withLogin(" "),
                userDto.withLogin("login with spaces"),
                userDto.withLogin("login with spaces"),
                userDto.withBirthday(null),
                userDto.withBirthday(LocalDate.MAX)
        );
    }
}
