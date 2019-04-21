package com.example.cgi.controller;


import com.example.cgi.entity.Upload;
import com.example.cgi.exceptions.BadRequestException;
import com.example.cgi.repository.UploadRepository;
import com.example.cgi.service.FileStorageService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.Optional;

@RestController
public class FileController {

    private final FileStorageService fileStorageService;
    private final UploadRepository uploadRepository;


    public FileController(FileStorageService fileStorageService, UploadRepository uploadRepository) {
        this.uploadRepository = uploadRepository;
        this.fileStorageService = fileStorageService;

    }

    @PostMapping("/api/uploadFile")
    public Upload uploadFile(@RequestParam("file") MultipartFile file) {
       Upload upload = fileStorageService.saveFileWithDetails(file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/downloadFile/")
                .path(upload.getFilename())
                .toUriString();


        upload.setDownloadUri(fileDownloadUri);
        return uploadRepository.save(upload);
    }

    @PostMapping("/api/uploadFileUrl")
    public Upload uploadFileUrl(@RequestParam("url") String url) {

        Optional<Upload> upload = fileStorageService.saveImageByUrl(url);

        if (upload.isPresent()) {
            Upload realUpload = upload.get();
            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/api/downloadFile/")
                    .path(realUpload.getFilename())
                    .toUriString();
            realUpload.setDownloadUri(fileDownloadUri);
            return uploadRepository.save(realUpload);
        }
        return null;
    }


    @GetMapping("/api/downloadFile/{fileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        // Load file as Resource
        Resource resource = fileStorageService.loadFileAsResource(fileName);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            throw new BadRequestException("Unknown file type!");
        }

        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @GetMapping("/api/allUploads")
    public Iterable<Upload> allFiles() {
        return uploadRepository.findAll();
    }
}