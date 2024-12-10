package com.electronistore.service;

import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;


@Service
public class ImageServiceImpl implements ImageService {

    Logger logger = LoggerFactory.getLogger(ImageServiceImpl.class);
    @Override
    public String uploadImage(MultipartFile file, String path) throws IOException {

        String originalFilename = file.getOriginalFilename();
        logger.info("Original File name: {}", originalFilename);
        // Random Filename
        String randomFileName = UUID.randomUUID().toString();
        // Find extension from user given image filename like png, jpg, jpeg etc.
        assert originalFilename != null;
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileWithExtension = randomFileName + extension;
        String fullPathWithFilename = path + File.separator + fileWithExtension;


        if (extension.equalsIgnoreCase(".png") || extension.equalsIgnoreCase(".jpg") || extension.equalsIgnoreCase(".jpeg")){

            //File save
            File folder = new File(path);

            if(!folder.exists()){
                // create folder
                folder.mkdirs();
            }
            // upload
            Files.copy(file.getInputStream(), Paths.get(fileWithExtension));
            return fileWithExtension;
        }
        else{
            throw new BadRequestException("File with this "+ extension + "not allowed");
        }

    }

    @Override
    public InputStream getResource(String path, String name) throws FileNotFoundException {

        String fullPath = path + File.separator + name;

        FileInputStream inputStream = new FileInputStream(fullPath);
        return inputStream;
    }
}
