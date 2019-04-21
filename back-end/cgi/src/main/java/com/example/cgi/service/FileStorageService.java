package com.example.cgi.service;


import com.example.cgi.config.FileStorageProperties;
import com.example.cgi.entity.Upload;
import com.example.cgi.exceptions.BadRequestException;
import com.example.cgi.exceptions.FileStorageException;
import com.example.cgi.exceptions.PageNotFoundException;
import com.example.cgi.repository.UploadRepository;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;
    private final Set<String> whitelistedFileExtensions;
    private final UploadRepository uploadRepository;

    public FileStorageService(FileStorageProperties fileStorageProperties, UploadRepository uploadRepository) {
        // Full path to uploads directory
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();
        this.uploadRepository = uploadRepository;

        Set<String> whitelist = new HashSet<>(Arrays.asList("jpg", "png", "jpeg", "tiff"));
        this.whitelistedFileExtensions = Collections.unmodifiableSet(whitelist);

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception e) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", e);
        }
    }

    public String storeFile(MultipartFile file) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String newFileName = null;

        try {
            // Check if the file's name contains invalid characters
            if (fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }
            // Check if file extension is whitelisted
            String[] fileNameExtension = fileName.split("\\.");
            if (!whitelistedFileExtensions.contains(
                    fileNameExtension[fileNameExtension.length - 1].toLowerCase())) {
                throw new BadRequestException("Unsupported file type!");
            }

            Path targetLocation = this.fileStorageLocation.resolve(fileName);

            // if file with the name exists:
            // add an unique number to the end of the filename, otherwise file would be overwritten
            int randomNum = 0;
            while (Files.exists(targetLocation)) {
                String[] fileNameParts = fileName.split("\\.");
                newFileName = fileNameParts[0] + randomNum + "." + fileNameParts[fileNameParts.length - 1];
                randomNum += 1;
                targetLocation = this.fileStorageLocation.resolve(newFileName);
            }
            if (newFileName != null) {
                fileName = newFileName;
            }

            // only if something went wrong, will the REPLACE_EXISTING tactic be used
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new PageNotFoundException("Upload not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new PageNotFoundException("Upload not found " + fileName, ex);
        }
    }

    public Upload saveFileWithDetails(MultipartFile file) {

        String filename = this.storeFile(file);
        Resource resource = loadFileAsResource(filename);
        int width = 0;
        int height = 0;
        String category = "";

        try {
            File upload1 = resource.getFile();
            BufferedImage bimg = ImageIO.read(upload1);
            width = bimg.getWidth();
            height = bimg.getHeight();
            category = randomGuesser(String.valueOf(height));



        } catch (IOException i) {
            i.printStackTrace();
        }
        return uploadRepository.save(new Upload(filename, file.getContentType(), height, width, category));
    }

    public Optional<Upload> saveImageByUrl(String url) {
        String filename;
        int width = 0;
        int height = 0;
        String category = "";
        try {
            URL imageUrl = new URL(url);

            filename = FilenameUtils.getName(imageUrl.getPath());
            Path targetlocation = this.fileStorageLocation.resolve(filename);
                    FileUtils.copyURLToFile(new URL(url),
                    new File(targetlocation.toString()));
            Resource resource = loadFileAsResource(filename);
            File upload = resource.getFile();
            BufferedImage bimg = ImageIO.read(upload);
            width = bimg.getWidth();
            height = bimg.getHeight();
            category = randomGuesser(String.valueOf(height));
            return Optional.of(uploadRepository.save(new Upload(filename, FilenameUtils.getExtension(imageUrl.getPath()),
                    height, width, category)));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException i) {
            i.printStackTrace();
        }
        return Optional.empty();
    }

    public String randomGuesser(String filename) {
        try{

            ProcessBuilder pb =
                    new ProcessBuilder("C:\\Users\\alvar" +
                            "\\AppData\\Local\\Programs\\Python\\Python36-32\\python.exe",
                    "C:\\Users\\alvar\\IdeaProjects\\cgiProject\\back-end\\cgi" +
                            "\\src\\main\\java\\com\\example\\cgi\\service\\randomGuesser.py", filename);
            Process p = pb.start();

            p.waitFor();
            BufferedReader bri = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line;
            while ((line = bri.readLine()) != null) {
                return line;
            }
            bri.close();
            p.waitFor();
            p.destroy();



    } catch(Exception e){
        e.printStackTrace();
    }

        return "Unknown";
    }


}