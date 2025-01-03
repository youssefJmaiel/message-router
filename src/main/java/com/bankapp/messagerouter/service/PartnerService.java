package com.bankapp.messagerouter.service;

import com.bankapp.messagerouter.entity.Partner;
import com.bankapp.messagerouter.error.PartnerNotFoundException;
import com.bankapp.messagerouter.repository.PartnerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PartnerService {

    private final PartnerRepository partnerRepository;

    public PartnerService(PartnerRepository partnerRepository) {
        this.partnerRepository = partnerRepository;
    }

    public List<Partner> getAllPartners() {
        return partnerRepository.findAll();
    }

    public Partner savePartner(Partner partner) {
        return partnerRepository.save(partner);
    }

//    public void deletePartner(Long id) {
//        partnerRepository.deleteById(id);
//    }
    public void deletePartner(Long id) {
        if (partnerRepository.existsById(id)) {
            partnerRepository.deleteById(id);
        } else {
            throw new RuntimeException("Partner not found");
        }
    }

    public Partner editPartner(Long id, Partner updatedPartner) {
        return partnerRepository.findById(id).map(existingPartner -> {
            // Update fields
            existingPartner.setAlias(updatedPartner.getAlias());
            existingPartner.setType(updatedPartner.getType());
            existingPartner.setDirection(updatedPartner.getDirection());
            existingPartner.setApplication(updatedPartner.getApplication());
            existingPartner.setProcessedFlowType(updatedPartner.getProcessedFlowType());
            existingPartner.setDescription(updatedPartner.getDescription());
            // Save and return the updated partner
            return partnerRepository.save(existingPartner);
        }).orElseThrow(() -> new PartnerNotFoundException("Partner with ID " + id + " not found."));
    }
    public Optional<Partner> findPartnerById(Long id) {
        return partnerRepository.findById(id);  // Ensure this returns an Optional
    }
}
