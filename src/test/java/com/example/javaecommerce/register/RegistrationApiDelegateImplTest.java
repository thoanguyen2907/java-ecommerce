package com.example.javaecommerce.register;

import com.example.javaecommerce.IntegrationTestUtil;
import com.example.javaecommerce.model.ERole;
import com.example.javaecommerce.model.entity.RoleEntity;
import com.example.javaecommerce.model.entity.UserEntity;
import com.example.javaecommerce.model.request.UserRequest;

import com.example.javaecommerce.repository.RoleRepository;
import com.example.javaecommerce.repository.UserRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static com.example.javaecommerce.register.RegisterTestApi.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class RegistrationApiDelegateImplTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private PasswordEncoder passwordEncoder;
    @MockBean
    private RoleRepository roleRepository;
    private Long userId;
    private UserRequest userRequest;
    private UserEntity userEntity;
    private UserRequest blankData;
    private RoleEntity userRole;

    @Before
    public void setUp() {
        userId = 1L;
        userEntity = makeUserForSaving(userId);
        userRequest = prepareUserForRequesting();
        userRole = prepareRoleUser();
        blankData = prepareUserBlankDataForRequesting();
    }

    @Test
    public void givenUserRequestData_whenSendData_thenReturnsUserCreated() throws Exception {
        // Mock user existed by email or not
        Mockito.when(userRepository.existsByEmail(Mockito.anyString())).thenReturn(false);
        // Check the role existed or not
        Mockito.when(roleRepository.findByName(Mockito.eq(ERole.ROLE_USER))).thenReturn(Optional.ofNullable(userRole));
        // Mock the user repository and set user id
        Mockito.when(userRepository.save(Mockito.any())).thenAnswer(invocation -> {
            UserEntity savedUser = invocation.getArgument(0);
            savedUser.setId(userId);
            return savedUser;
        });
        // mock password is encoded
        String encodedPassword = "encodedPassword";
        Mockito.when(passwordEncoder.encode(Mockito.anyString())).thenReturn(encodedPassword);

        // mock endpoint to register and return status okay
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/signup")
                        .content(IntegrationTestUtil.asJsonString(userRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.email").value(userRequest.getEmail()));
    }

    @Test
    public void givenExistingUserEmail_whenSendData_thenReturnInternalServer() throws Exception {
        // mock the case when email existed
        Mockito.when(userRepository.existsByEmail(Mockito.anyString())).thenReturn(true);
        // perform the endpoint and return internal server
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/signup")
                        .content(IntegrationTestUtil.asJsonString(userRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());

    }
    @Test
    public void givenBlankUserData_whenSendData_thenReturnBadRequest() throws Exception {
        // perform the endpoint with blank data and return bad request
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/signup")
                        .content(IntegrationTestUtil.asJsonString(blankData))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
