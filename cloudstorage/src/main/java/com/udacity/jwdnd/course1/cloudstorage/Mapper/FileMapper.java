package com.udacity.jwdnd.course1.cloudstorage.Mapper;

import com.udacity.jwdnd.course1.cloudstorage.Model.File;
import java.util.List;
import org.apache.ibatis.annotations.*;

@Mapper
public interface FileMapper {
    @Select("SELECT * FROM FILES WHERE filename = #{fileName}")
    File findFileByFileName(String fileName);

    @Select("SELECT filename FROM FILES WHERE userid = #{userId}")
    List<String> findFileNamesByUserId(Integer userId);

    @Insert("INSERT INTO FILES (filename, contenttype, filesize, userid, filedata) " +
            "VALUES(#{fileName}, #{contentType}, #{fileSize}, #{userId}, #{fileData})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    int insert(File file);

    @Delete("DELETE FROM FILES WHERE filename = #{fileName}")
    void deleteFileByFileName(String fileName);
}
