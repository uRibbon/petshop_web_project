package com.dog.shop.exception;

import com.dog.shop.errorcode.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CommonException extends RuntimeException {
	/**
	 * httpStatus를 추가해서 상황에 적절한 에러 상태를 전송
	 */
	private final ErrorCode error;
	private HttpStatus httpStatus;
	
	public CommonException(ErrorCode error, HttpStatus httpStatus) {
        this.error = error;
        this.httpStatus = httpStatus;
    }
	
}
