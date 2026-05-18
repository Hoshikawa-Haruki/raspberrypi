package deu.se.raspberrypi.config;

/**
 * 파일 업로드 관련 설정값을 application.properties에서 읽어 바인딩하는 설정 클래스
 *
 * prefix = "file" → application.properties의 file.* 키와 자동 매핑됨
 * DataSize 타입을 사용하면 "10MB", "30MB" 형태로 properties에 작성 가능 (가독성 향상)
 *
 * 2026.05.14.
 *
 * @author Haruki
 */
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.convert.DataSizeUnit;
import org.springframework.stereotype.Component;
import org.springframework.util.unit.DataSize;
import org.springframework.util.unit.DataUnit;

@Component
@ConfigurationProperties(prefix = "file")
@Getter
@Setter
public class FileProperties {

    // application.properties: file.upload-dir
    private String uploadDir;

    // application.properties: file.temp-dir
    private String tempDir;

    /**
     * 게시글 본문 인라인 이미지 총 용량 제한
     * application.properties: file.max-inline-image-size=10MB
     * 단위 미지정 시 기본값 BYTES로 처리되므로 @DataSizeUnit으로 기본 단위 지정
     */
    @DataSizeUnit(DataUnit.MEGABYTES)
    private DataSize maxInlineImageSize = DataSize.ofMegabytes(10);

    /**
     * 게시글 첨부파일 총 용량 제한
     * application.properties: file.max-attachment-size=30MB
     */
    @DataSizeUnit(DataUnit.MEGABYTES)
    private DataSize maxAttachmentSize = DataSize.ofMegabytes(30);
}
