package teamexpress.velo9.common.exception;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class Velo9ExceptionHandler {

	@ExceptionHandler(Exception.class)
	protected ResponseEntity<?> handleIllegalArgumentException(Exception e) {
		return ResponseEntity.badRequest().body(e.getMessage());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex){
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors()
			.forEach(error -> errors.put(((FieldError) error).getField(), error.getDefaultMessage()));
		return ResponseEntity.badRequest().body(errors);
	}
}
