package com.prototyne.service.TempService;

import com.prototyne.web.dto.TempResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TempCommandService {
    void CheckFlag(Integer flag);
    TempResponse.TempUploadDTO uploadImages(String directory, List<MultipartFile> images);
}
