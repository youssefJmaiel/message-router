package com.bankapp.messagerouter.controller;


import com.bankapp.messagerouter.entity.Partner;
import com.bankapp.messagerouter.entity.PartnerDirection;
import com.bankapp.messagerouter.entity.PartnerType;
import com.bankapp.messagerouter.entity.ProcessedFlowType;
import com.bankapp.messagerouter.service.PartnerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@WebMvcTest(PartnerController.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PartnerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PartnerService partnerService;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    public void testGetAllPartners() throws Exception {
        Partner partner = new Partner();
        partner.setAlias("Test Partner");

        when(partnerService.getAllPartners()).thenReturn(Collections.singletonList(partner));

        mockMvc.perform(get("/api/partners"))  // Assurez-vous que l'URL est correcte
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].alias").value("Test Partner"));
    }

@Test
public void testAddPartner() throws Exception {
    Partner partner = new Partner();
    partner.setAlias("New Partner");
    partner.setType(PartnerType.MESSAGE);
    partner.setDirection(PartnerDirection.INBOUND);
    partner.setProcessedFlowType(ProcessedFlowType.MESSAGE);
    partner.setDescription("Valid Description");

    // Mock the service call
    when(partnerService.savePartner(any(Partner.class))).thenReturn(partner);

    mockMvc.perform(post("/api/partners")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(partner)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.alias").value("New Partner"))
            .andExpect(jsonPath("$.type").value("MESSAGE"))
            .andExpect(jsonPath("$.direction").value("INBOUND"))
            .andExpect(jsonPath("$.processedFlowType").value("MESSAGE"))
            .andExpect(jsonPath("$.description").value("Valid Description"));
}


    @Test
    public void deletePartner_shouldReturnNoContent_whenSuccessful() throws Exception {
        Long partnerId = 1L;
        Partner partner = new Partner();
        partner.setId(partnerId);
        // Mocking the service to return a partner
        when(partnerService.findPartnerById(partnerId)).thenReturn(Optional.of(partner));

        mockMvc.perform(delete("/api/partners/{id}", partnerId))
                .andExpect(status().isNoContent());  // Expect 204 No Content
    }

    @Test
    public void deletePartner_shouldReturnNotFound_whenPartnerNotFound() throws Exception {
        Long partnerId = 1L;

        // Simulate that the partner doesn't exist
        when(partnerService.findPartnerById(partnerId)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/partners/{id}", partnerId))
                .andExpect(status().isNotFound());  // Expect 404 Not Found
    }


}
