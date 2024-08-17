package portfolio.ecommencesite.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import portfolio.ecommencesite.response.APIResponse;

import javax.naming.AuthenticationException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,APIResponse>> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException exception, WebRequest request){
        Map<String, APIResponse> response = new HashMap<>();
        System.out.println(exception.getLocalizedMessage());
        exception.getBindingResult().getAllErrors().forEach(objectError -> {
            String fieldName = ((FieldError) objectError).getField();
            String message = objectError.getDefaultMessage();
            response.put(fieldName, new APIResponse(message,false));
        });
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<APIResponse> resourceNotFoundExceptionHandler(ResourceNotFoundException exception){

        return new ResponseEntity<>(new APIResponse(exception.getMessage(), false),HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<APIResponse> apiExceptionHandler(ApiException exception){

        return new ResponseEntity<>(new APIResponse(exception.getMessage(), false),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({AuthenticationException.class})
    public ResponseEntity<?> authenticationExceptionHandler(AuthenticationException exception){
        Map<String,Object> map = new HashMap<>();
        map.put("message","Bad credential");
        map.put("status",false);
        return new ResponseEntity<Object>(map,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<APIResponse> exceptionHandler(Exception exception){

        return new ResponseEntity<>(new APIResponse(exception.getMessage(), false),HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
