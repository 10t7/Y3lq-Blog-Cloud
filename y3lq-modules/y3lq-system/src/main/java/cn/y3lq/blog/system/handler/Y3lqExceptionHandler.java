package cn.y3lq.blog.system.handler;

import cn.y3lq.blog.common.core.constant.HttpStatus;
import cn.y3lq.blog.common.core.domain.AjaxResult;
import cn.y3lq.blog.common.core.domain.R;
import cn.y3lq.blog.common.core.exception.ServiceException;
import com.fasterxml.jackson.core.JsonParseException;
import org.aspectj.weaver.loadtime.Aj;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;

/**
 * @author: Y3lq
 * @description: 异常处理类
 */
@ControllerAdvice(basePackages = "cn.y3lq.blog.system")
@ResponseBody
public class Y3lqExceptionHandler {

    /**
     * 处理转换异常
     */
    @ExceptionHandler(HttpMessageConversionException.class)
    public AjaxResult handlerConversionException(HttpMessageConversionException e) {
        return AjaxResult.error(e.getMessage());
    }

    /**
     * 处理JsonParseException
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public AjaxResult handlerJsonParseException() {
        return AjaxResult.error("请求JSON格式错误");
    }

    /**
     * 处理参数校验异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public AjaxResult handlerValidException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        HashMap<String, String> errorMap = new HashMap<>();
        for (FieldError fieldError : fieldErrors) {
            errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return AjaxResult.error(HttpStatus.VALID, "校验异常").put("error", errorMap);
    }

    /**
     * 处理业务异常
     */
    @ExceptionHandler(ServiceException.class)
    public AjaxResult handlerServiceException(ServiceException e) {
        Integer code = e.getCode();
        if (code == null) {
            return AjaxResult.error(e.getMessage());
        }
        return AjaxResult.error(code, e.getMessage());
    }

    /**
     * 处理未知运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    public AjaxResult handlerRuntimeException(RuntimeException e) {
        return AjaxResult.error(e.getMessage());
    }

    /**
     * 处理系统异常
     */
    @ExceptionHandler(Exception.class)
    public AjaxResult handlerException(Exception e) {
        return AjaxResult.error(e.getMessage());
    }

}
