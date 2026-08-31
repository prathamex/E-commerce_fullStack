package com.cws.shop.controller;

import com.cws.shop.dto.request.GenerateNewLicenseRequest;
import com.cws.shop.dto.response.GenerateNewLicenseResponse;
import com.cws.shop.model.ProductLicenseStatus;
import com.cws.shop.service.GenerateNewLicenseService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GenerateNewLicenseController.class)
public class GenerateNewLicenseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private GenerateNewLicenseService generateNewLicenseService;

    @Test
    public void whenMobileInvalid_thenReturnsBadRequest() throws Exception {
        GenerateNewLicenseRequest req = new GenerateNewLicenseRequest();
        req.setProductId(1L);
        req.setLicenseType(null);
        req.setCustomerName("John Doe");
        req.setCustomerEmail("john@example.com");
        req.setMobileNumber("abc-123"); // invalid
        req.setActivationLimit(1);
        req.setActivationDate(LocalDate.now());
        req.setExpiryDate(LocalDate.now().plusDays(30));

        mockMvc.perform(post("/admin/licenses/generate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation Failed"))
                .andExpect(jsonPath("$.errors.mobileNumber").exists());
    }

    @Test
    public void whenMobileValid_thenReturnsCreated() throws Exception {
        GenerateNewLicenseRequest req = new GenerateNewLicenseRequest();
        req.setProductId(1L);
        req.setLicenseType(null);
        req.setCustomerName("John Doe");
        req.setCustomerEmail("john@example.com");
        req.setMobileNumber("9876543210"); // valid
        req.setActivationLimit(1);
        req.setActivationDate(LocalDate.now());
        req.setExpiryDate(LocalDate.now().plusDays(30));

        GenerateNewLicenseResponse resp = new GenerateNewLicenseResponse(
                "Product A",
                "KEY-123",
                "John Doe",
                ProductLicenseStatus.ACTIVE,
                LocalDate.now().plusDays(30)
        );

        when(generateNewLicenseService.generateLicense(any(GenerateNewLicenseRequest.class)))
                .thenReturn(resp);

        mockMvc.perform(post("/admin/licenses/generate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.licenseKey").value("KEY-123"));
    }
}
