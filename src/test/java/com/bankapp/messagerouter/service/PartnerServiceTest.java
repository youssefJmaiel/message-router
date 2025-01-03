package com.bankapp.messagerouter.service;


import com.bankapp.messagerouter.entity.Partner;
import com.bankapp.messagerouter.entity.PartnerDirection;
import com.bankapp.messagerouter.entity.PartnerType;
import com.bankapp.messagerouter.entity.ProcessedFlowType;
import com.bankapp.messagerouter.repository.PartnerRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class PartnerServiceTest {

    private final PartnerRepository partnerRepository = mock(PartnerRepository.class);
    private final PartnerService partnerService = new PartnerService(partnerRepository);

    @Test
    public void testGetAllPartners() {
        Partner partner = new Partner();
        partner.setAlias("Test Partner");

        when(partnerRepository.findAll()).thenReturn(Collections.singletonList(partner));

        List<Partner> partners = partnerService.getAllPartners();

        assertEquals(1, partners.size());
        assertEquals("Test Partner", partners.get(0).getAlias());
        verify(partnerRepository, times(1)).findAll();
    }

    @Test
    public void testSavePartner() {
        Partner partner = new Partner();
        partner.setAlias("New Partner");

        when(partnerRepository.save(Mockito.any(Partner.class))).thenReturn(partner);

        Partner savedPartner = partnerService.savePartner(partner);

        assertEquals("New Partner", savedPartner.getAlias());
        verify(partnerRepository, times(1)).save(partner);
    }


    @Test
    void deletePartner_shouldCallDeleteMethod_whenPartnerExists() {
        Long partnerId = 1L;

        // Simulate that the partner exists in the repository
        when(partnerRepository.existsById(partnerId)).thenReturn(true);

        // Call the delete method on the service
        partnerService.deletePartner(partnerId);

        // Verify that the repository's deleteById method is called with the correct partnerId
        verify(partnerRepository, times(1)).deleteById(partnerId);
    }

    @Test
    void deletePartner_shouldThrowException_whenPartnerDoesNotExist() {
        Long partnerId = 1L;

        // Simulate that the partner does not exist in the repository
        when(partnerRepository.existsById(partnerId)).thenReturn(false);

        // Call the delete method and verify it throws the expected exception
        Exception exception = assertThrows(RuntimeException.class, () -> partnerService.deletePartner(partnerId));

        // Verify that the exception message matches the expected
        assertEquals("Partner not found", exception.getMessage());

        // Verify that deleteById is never called
        verify(partnerRepository, times(0)).deleteById(partnerId);
    }

    @Test
    void testEditPartner() {
        Long partnerId = 1L;

        Partner existingPartner = new Partner();
        existingPartner.setId(partnerId);
        existingPartner.setAlias("Alias1");
        existingPartner.setType(PartnerType.MESSAGE);
        existingPartner.setDirection(PartnerDirection.INBOUND);
        existingPartner.setApplication("App1");
        existingPartner.setProcessedFlowType(ProcessedFlowType.MESSAGE);
        existingPartner.setDescription("Description1");

        Partner updatedPartner = new Partner();
        updatedPartner.setAlias("NewAlias");
        updatedPartner.setType(PartnerType.NOTIFICATION);
        updatedPartner.setDirection(PartnerDirection.OUTBOUND);
        updatedPartner.setApplication("NewApp");
        updatedPartner.setProcessedFlowType(ProcessedFlowType.ALERTING);
        updatedPartner.setDescription("Updated Description");

        when(partnerRepository.findById(partnerId)).thenReturn(Optional.of(existingPartner));
        when(partnerRepository.save(any(Partner.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Partner result = partnerService.editPartner(partnerId, updatedPartner);

        assertEquals(updatedPartner.getAlias(), result.getAlias());
        assertEquals(updatedPartner.getType(), result.getType());
        assertEquals(updatedPartner.getDirection(), result.getDirection());
        assertEquals(updatedPartner.getApplication(), result.getApplication());
        assertEquals(updatedPartner.getProcessedFlowType(), result.getProcessedFlowType());
        assertEquals(updatedPartner.getDescription(), result.getDescription());

        verify(partnerRepository).findById(partnerId);
        verify(partnerRepository).save(any(Partner.class));
    }
}
