package eu.burakkocak.vetsandpetsservice.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.burakkocak.vetsandpetsservice.api.dto.ApiPetRequest;
import eu.burakkocak.vetsandpetsservice.api.dto.ApiPetResponse;
import eu.burakkocak.vetsandpetsservice.auth.config.security.JwtTokenFilter;
import eu.burakkocak.vetsandpetsservice.enums.PetType;
import eu.burakkocak.vetsandpetsservice.service.PetService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(PetController.class)
@AutoConfigureMockMvc(addFilters = false)
class PetControllerTest {

    private static final String USER_ROLE = "USER";
    private static final String ADMIN_ROLE = "ADMIN";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PetService petService;

    @MockBean JwtTokenFilter jwtTokenFilter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @WithMockUser(username = "john", roles = USER_ROLE)
    void testGetPetsForUser() throws Exception {
        // Mock data
        ApiPetResponse apiPetResponse1 = new ApiPetResponse();
        ApiPetResponse apiPetResponse2 = new ApiPetResponse();
        List<ApiPetResponse> apiPetResponses = List.of(apiPetResponse1, apiPetResponse2);
        PageImpl<ApiPetResponse> expectedPage = new PageImpl<>(apiPetResponses);

        // Stub the service method call
        when(petService.getPetList(any(Pageable.class))).thenReturn(expectedPage);

        // Call the controller method
        mockMvc.perform(get("/user/pets")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)));

        // Verify the service method is called
        verify(petService, times(1)).getPetList(any(Pageable.class));
    }

    @Test
    @WithMockUser(roles = ADMIN_ROLE)
    void testGetPetsForAdmin() throws Exception {
        // Mock data
        ApiPetResponse apiPetResponse1 = new ApiPetResponse();
        ApiPetResponse apiPetResponse2 = new ApiPetResponse();
        List<ApiPetResponse> apiPetResponses = List.of(apiPetResponse1, apiPetResponse2);
        PageImpl<ApiPetResponse> expectedPage = new PageImpl<>(apiPetResponses);

        // Stub the service method call
        when(petService.getPetList(any(Pageable.class))).thenReturn(expectedPage);

        // Call the controller method
        mockMvc.perform(get("/user/pets")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)));

        // Verify the service method is called
        verify(petService, times(1)).getPetList(any(Pageable.class));
    }

    @Test
    @WithMockUser(roles = "USER")
    void testAddPet() throws Exception {
        ApiPetRequest request = new ApiPetRequest();
        request.setName("Buddy");
        request.setType(PetType.DOG);

        // Stub the service method call
        doNothing().when(petService).addPetForUserAccount(request);

        // Call the controller method
        mockMvc.perform(post("/user/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk());

        // Verify the service method call
        verify(petService, times(1)).addPetForUserAccount(request);
    }

    @Test
    @WithMockUser
    void testUpdatePet() throws Exception {
        // Set up mock request body
        UUID petId = UUID.randomUUID();
        ApiPetRequest request = new ApiPetRequest();
        request.setId(petId);
        request.setName("Max");
        request.setType(PetType.DOG);

        // Call the controller method
        mockMvc.perform(put("/user/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk());

        // Verify that the service method was called with the correct arguments
        verify(petService, times(1)).updatePetOnUserAccount(request);
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void removePetById_withUser() throws Exception {
        UUID petId = UUID.randomUUID();

        doNothing().when(petService).removePetById(petId);

        mockMvc.perform(delete("/user/pets/" + petId))
                .andExpect(status().isNoContent());

        verify(petService, times(1)).removePetById(petId);
    }

    @Test
    @WithMockUser(username = "testadmin", roles = "ADMIN")
    void removePetById_withAdmin() throws Exception {
        UUID petId = UUID.randomUUID();

        doNothing().when(petService).removePetById(petId);

        mockMvc.perform(delete("/user/pets/" + petId))
                .andExpect(status().isNoContent());

        verify(petService, times(1)).removePetById(petId);
    }

}
