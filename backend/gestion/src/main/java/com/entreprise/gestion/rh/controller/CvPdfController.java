package com.entreprise.gestion.rh.controller;

import com.entreprise.gestion.rh.service.CvPdfService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;


import java.io.ByteArrayInputStream;

@RestController
@RequestMapping("/api/public/cv")
@RequiredArgsConstructor
public class CvPdfController {

    private final CvPdfService cvPdfService;

    /**
     * Génère un CV PDF pour un candidat spécifique
     * @param candidatId ID du candidat
     * @return ResponseEntity avec le PDF en stream
     */
    //@PreAuthorize("hasRole('RH') or hasRole('ADMIN')")
    @GetMapping(value = "/{candidatId}", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> generateCvPdf(@PathVariable Integer candidatId) {
        try {
            ByteArrayInputStream bis = cvPdfService.generateCvPdf(candidatId);
            
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "inline; filename=cv_" + candidatId + ".pdf");
            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
            headers.add("Pragma", "no-cache");
            headers.add("Expires", "0");
            
            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(new InputStreamResource(bis));
                    
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    /**
     * Télécharge le CV PDF pour un candidat spécifique
     * @param candidatId ID du candidat
     * @return ResponseEntity avec le PDF en téléchargement
     */
    //@PreAuthorize("hasRole('RH') or hasRole('ADMIN')")
    @GetMapping(value = "/{candidatId}/download", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> downloadCvPdf(@PathVariable Integer candidatId) {
        try {
            ByteArrayInputStream bis = cvPdfService.generateCvPdf(candidatId);
            
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=cv_" + candidatId + ".pdf");
            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
            headers.add("Pragma", "no-cache");
            headers.add("Expires", "0");
            
            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(new InputStreamResource(bis));
                    
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }


}