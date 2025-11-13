    /*
     * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
     * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
     */
    package deu.se.raspberrypi.dto;

    import jakarta.validation.constraints.Email;
    import jakarta.validation.constraints.NotBlank;
    import jakarta.validation.constraints.Size;
    import lombok.AllArgsConstructor;
    import lombok.Getter;
    import lombok.NoArgsConstructor;
    import lombok.Setter;

    /**
     * /member/signup 요청 바인딩용 DTO - 클라이언트 → 서버로 전달되는 회원가입 폼 값 바인딩 - Bean
     * Validation으로 서버 측 유효성 검사 수행
     *
     * @author Haruki
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public class SignupRequestDto {

        @Email(message = "이메일 형식이 아닙니다.")
        @NotBlank
        private String email;

        @NotBlank
        @Size(min = 4, max = 20, message = "비밀번호는 4자 이상이어야 합니다.")
        private String password;

        @NotBlank
        @Size(min = 4, max = 20)
        private String confirmPassword;

        @NotBlank
        @Size(min = 2, max = 10, message = "닉네임은 2~10자입니다.")
        private String nickname;

    }
