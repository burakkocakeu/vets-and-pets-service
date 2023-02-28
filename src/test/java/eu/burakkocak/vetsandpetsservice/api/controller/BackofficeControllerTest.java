package eu.burakkocak.vetsandpetsservice.api.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.burakkocak.vetsandpetsservice.api.dto.ApiPetStatisticResponse;
import eu.burakkocak.vetsandpetsservice.api.dto.ApiUserRequest;
import eu.burakkocak.vetsandpetsservice.api.dto.ApiUserResponse;
import eu.burakkocak.vetsandpetsservice.auth.config.security.JwtTokenFilter;
import eu.burakkocak.vetsandpetsservice.enums.PetType;
import eu.burakkocak.vetsandpetsservice.service.AccountService;
import eu.burakkocak.vetsandpetsservice.service.PetService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(BackofficeController.class)
@AutoConfigureMockMvc(addFilters = false)
class BackofficeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @MockBean
    private PetService petService;

    @MockBean JwtTokenFilter jwtTokenFilter;

    @InjectMocks
    private BackofficeController backofficeController;

    @BeforeEach
    void setUp() {
        backofficeController = new BackofficeController(accountService, petService);
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUsers() {
        // Mock data
        Pageable pageable = Pageable.ofSize(10).withPage(0);
        List<ApiUserResponse> apiUserResponses = Collections.singletonList(new ApiUserResponse());
        Page<ApiUserResponse> expectedPage = new PageImpl<>(apiUserResponses);

        // Stub the service method call
        when(accountService.getUserList(anyBoolean(), anyString(), anyBoolean(), any(Pageable.class)))
                .thenReturn(expectedPage);

        // Call the controller method
        ResponseEntity<Page<ApiUserResponse>> response = backofficeController.getUsers(false, "John", false, pageable);

        // Check the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedPage, response.getBody());
    }

    @Test
    public void testUpdateUser() {
        // Create a sample ApiUserRequest object to pass in to the updateUser() method
        ApiUserRequest request = new ApiUserRequest();
        request.setId(UUID.randomUUID());
        request.setFullName("John Doe");

        // Call the method being tested
        backofficeController.updateUser(request);

        // Verify that the service method was called with the correct input
        verify(accountService).updateUser(request);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void removeUserById() throws Exception {
        UUID userId = UUID.randomUUID();

        doNothing().when(accountService).removeUserById(userId);

        mockMvc.perform(delete("/backoffice/user/" + userId))
                .andExpect(status().isNoContent());

        verify(accountService, times(1)).removeUserById(userId);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetPetsByTypes() throws Exception {
        List<PetType> petTypes = List.of(PetType.DOG, PetType.CAT);

        List<ApiPetStatisticResponse> expectedResponse = new ArrayList<>();
        expectedResponse.add(ApiPetStatisticResponse.builder().type(PetType.DOG).total(2).build());
        expectedResponse.add(ApiPetStatisticResponse.builder().type(PetType.CAT).total(1).build());

        when(petService.getPetsByTypes(petTypes)).thenReturn(expectedResponse);

        MvcResult result = mockMvc.perform(get("/backoffice/user/pet-statistics")
                        .param("types", "DOG,CAT"))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        List<ApiPetStatisticResponse> actualResponse = new ObjectMapper().readValue(responseBody, new TypeReference<>() {});

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetLatestUsers() throws Exception {
        Long latestUserNumber = 10L;
        List<ApiUserResponse> expectedList = List.of(new ApiUserResponse(), new ApiUserResponse());

        // Stub the service method call
        when(petService.getLatestUsers(latestUserNumber))
                .thenReturn(expectedList);

        // Call the controller method
        MvcResult mvcResult = mockMvc.perform(get("/backoffice/user/user-statistics")
                        .param("latestUserNumber", latestUserNumber.toString()))
                .andExpect(status().isOk())
                .andReturn();

        // Verify the response
        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference<List<ApiUserResponse>> typeReference = new TypeReference<>() {};
        List<ApiUserResponse> actualList = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), typeReference);
        assertEquals(expectedList, actualList);

        // Verify the service method was called with the correct arguments
        verify(petService, times(1)).getLatestUsers(latestUserNumber);
    }

}