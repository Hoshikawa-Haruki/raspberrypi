package deu.se.raspberrypi.controller.advice;

/**
 * 전역 예외 처리 핸들러
 * 파일 업로드 용량 초과 등 공통 예외를 잡아 에러 페이지로 안내
 *
 * 2026.05.14.
 *
 * @author Haruki
 */
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ModelAndView handleMaxUploadSizeExceeded(MaxUploadSizeExceededException e) {
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("errorMessage", "첨부파일 총 용량은 30MB를 초과할 수 없습니다.");
        return mav;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ModelAndView handleIllegalArgument(IllegalArgumentException e) {
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("errorMessage", e.getMessage());
        return mav;
    }
}
