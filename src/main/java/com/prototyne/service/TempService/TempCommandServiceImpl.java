package com.prototyne.service.TempService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.prototyne.apiPayload.code.status.ErrorStatus;
import com.prototyne.apiPayload.exception.handler.TempHandler;

@Service
@RequiredArgsConstructor
public class TempCommandServiceImpl implements TempCommandService {
    @Override
    public void CheckFlag(Integer flag) {
        if (flag == 1)
            throw new TempHandler(ErrorStatus.TEMP_EXCEPTION);
    }
}