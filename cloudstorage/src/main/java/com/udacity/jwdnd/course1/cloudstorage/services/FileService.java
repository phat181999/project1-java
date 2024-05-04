package com.udacity.jwdnd.course1.cloudstorage.services;


import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.udacity.jwdnd.course1.cloudstorage.Mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.Mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.Model.File;

import java.io.IOException;
import java.util.List;

@Service
public class FileService {
    private final FileMapper fileMapper;
    private final UserMapper userMapper;

    public FileService(FileMapper fileMapper, UserMapper userMapper) {
        this.fileMapper = fileMapper;
        this.userMapper = userMapper;
    }

    public List<String> getFileListings(Integer userId) {
        return fileMapper.findFileNamesByUserId(userId);
    }

    public int addFile(MultipartFile multipartFile, String username) throws IOException {
        byte[] fileData = multipartFile.getBytes();
        String fileName = multipartFile.getOriginalFilename();
        String contentType = multipartFile.getContentType();
        String fileSize = String.valueOf(multipartFile.getSize());
        Integer userId = userMapper.findUserIdByUsername(username).getUserId();
        File file = new File(0, fileName, contentType, fileSize, userId, fileData);
        return fileMapper.insert(file);
    }

    public File getFile(String fileName) {
        return fileMapper.findFileByFileName(fileName);
    }

    public void deleteFile(String fileName) {
        fileMapper.deleteFileByFileName(fileName);
    }
}
