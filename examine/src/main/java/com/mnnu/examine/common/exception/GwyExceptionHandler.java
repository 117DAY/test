package com.mnnu.examine.common.exception;

import com.mnnu.examine.common.utils.GwyConstant;
import com.mnnu.examine.common.utils.R;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

/**
 * 统一的异常处理
 *
 * @author qiaoh
 */
@RestControllerAdvice
public class GwyExceptionHandler {

    /**
     * 数据校验的异常处理
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public R handlerValidException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        HashMap<String, String> resultMap = new HashMap<>();
        bindingResult.getFieldErrors().forEach((error) -> {
            resultMap.put(error.getField(), error.getDefaultMessage());
        });
        return R.error(GwyConstant.BizCode.VALID_EXCEPTION.getCode(), GwyConstant.BizCode.VALID_EXCEPTION.getMessage())
                .put("data", resultMap);
    }

    /**
     * socket链接时的异常处理
     *
     * @param e
     * @return
     */
    @MessageExceptionHandler(MessageException.class)
    public R handleException(MessageException e) {
        return R.error(e.getMessage());
    }

    @ExceptionHandler(GwyException.class)
    public R handleException(GwyException e) {
        return R.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(Throwable.class)
    public R handleException(Exception e) {
        return R.error(e.getMessage());
    }
}
