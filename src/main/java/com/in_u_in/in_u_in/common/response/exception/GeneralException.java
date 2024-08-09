package com.in_u_in.in_u_in.common.response.exception;

import com.in_u_in.in_u_in.common.response.code.BaseErrorCode;
import com.in_u_in.in_u_in.common.response.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GeneralException extends RuntimeException {

    private BaseErrorCode code;

    public ErrorReasonDTO getErrorReason() {
        return this.code.getReason();
    }

    public ErrorReasonDTO getErrorReasonHttpStatus(){
        return this.code.getReasonHttpStatus();
    }
}
