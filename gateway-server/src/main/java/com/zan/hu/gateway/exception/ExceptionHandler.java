package com.zan.hu.gateway.exception;

/**
 * @version 1.0
 * @Author hupeng
 * @Date 2019-05-12 12:50
 * @Description todo
 **/
public class ExceptionHandler extends RuntimeException {

    public ExceptionHandler(String message) {
        super(message);
    }


    public ExceptionHandler(String message, Throwable throwable) {
        super(message, throwable);
    }

}
