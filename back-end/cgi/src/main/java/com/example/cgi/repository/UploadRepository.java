package com.example.cgi.repository;

import com.example.cgi.entity.Upload;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UploadRepository extends JpaRepository<Upload, Long> {


}