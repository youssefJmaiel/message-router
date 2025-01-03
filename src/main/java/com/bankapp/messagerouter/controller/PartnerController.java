package com.bankapp.messagerouter.controller;

import com.bankapp.messagerouter.entity.Partner;
import com.bankapp.messagerouter.service.PartnerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/partners")
@CrossOrigin(origins = "*")
public class PartnerController {

    private final PartnerService partnerService;

    public PartnerController(PartnerService partnerService) {
        this.partnerService = partnerService;
    }

    @GetMapping
    public ResponseEntity<List<Partner>> getAllPartners() {
        // Logique pour récupérer les partenaires
        return ResponseEntity.ok(partnerService.getAllPartners());
    }


    @PostMapping
    public ResponseEntity<Partner> savePartner(@RequestBody Partner partner) {
        Partner createdPartner = partnerService.savePartner(partner);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPartner);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePartner(@PathVariable Long id) {
        Optional<Partner> partner = partnerService.findPartnerById(id);
        if (!partner.isPresent()) {
            return ResponseEntity.notFound().build();  // Return 404 if partner is not found
        }
        partnerService.deletePartner(id);
        return ResponseEntity.noContent().build();  // Return 204 No Content for successful deletion
    }

    @PutMapping("/{id}")
    public ResponseEntity<Partner> editPartner(
            @PathVariable Long id,
            @RequestBody Partner updatedPartner
    ) {
        Partner editedPartner = partnerService.editPartner(id, updatedPartner);
        return ResponseEntity.ok(editedPartner);
    }

}
