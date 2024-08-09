package com.in_u_in.in_u_in.common.response.exception.handler;


import com.in_u_in.in_u_in.common.response.code.BaseErrorCode;
import com.in_u_in.in_u_in.common.response.exception.GeneralException;

public class ErrorHandler extends GeneralException {
    public ErrorHandler(BaseErrorCode code) {
        super(code);
    }
}
