package com.shopway.shopway.controllers;


import com.shopway.shopway.services.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/*
 * REST kontroler za upload fajlova na S3-kompatibilni storage.
 */
@RestController
@RequestMapping("/api/file")
@CrossOrigin(origins = "http://localhost:5137")
public class FileUpload {

    @Autowired
    FileUploadService fileUploadService;
    /*
     * Uploaduje fajl na S3 storage (Bunny CDN).
     * Endpoint je javan (videti WebSecurityConfig - "/api/file" je u permitAll()).
     *
     * @param file MultipartFile objekat - fajl koji se uploaduje
     * @param fileName naziv pod kojim će fajl biti sačuvan na storage-u
     * @return ResponseEntity sa:
     *         - HTTP statusom 201 CREATED ako je upload uspešan
     *         - HTTP statusom 500 INTERNAL_SERVER_ERROR ako je došlo do greške
     */
    @PostMapping
    public ResponseEntity<?> fileUpload(@RequestParam(value = "file", required = true) MultipartFile file, @RequestParam(value= "fileName", required = true) String fileName) {
        int statusCode = fileUploadService.uploadFile(file, fileName);
        return new ResponseEntity<>(statusCode == 201 ? HttpStatus.CREATED : HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
