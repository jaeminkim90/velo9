package teamexpress.velo9.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

//@RestControllerAdvice
//배포시 풀어서 사용할 것
public class Velo9ExceptionHandler {

	@ExceptionHandler(Exception.class)
	protected ResponseEntity<?> handleIllegalArgumentException(Exception e) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
	}
}
