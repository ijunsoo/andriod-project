package com.oil.station.global.utill.exception;



import com.oil.station.global.utill.init.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CustomException extends RuntimeException {
    private final ErrorCode errorCode;

    public CustomException(com.oil.station.global.utill.init.ErrorCode errorCode) {

    }
}