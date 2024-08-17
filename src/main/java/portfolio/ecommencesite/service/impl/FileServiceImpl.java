package portfolio.ecommencesite.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import portfolio.ecommencesite.service.FileService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {
    @Override
    public String uploadImage(String path, MultipartFile image) throws IOException {
        //file name of current / original file
        String originalFileName = image.getOriginalFilename()   ;
        //Generate a unique file name
        String randomId = UUID.randomUUID().toString();
        //Check if path exist and create
        String filename = randomId.concat(originalFileName.substring(originalFileName.lastIndexOf('.')));
        String filePath = path + File.separator +filename;
        //it might not run on other operating system when hard coded
        File folder = new File(path);
        if(!folder.exists()){
            folder.mkdir();
        }
        Files.copy(image.getInputStream(), Paths.get(filePath));

        //Upload to server
        return filename;

    }
}
