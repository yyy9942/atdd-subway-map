package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.subway.acceptance.linestep.LineRequestStep.노선_생성;
import static nextstep.subway.acceptance.sectionstep.SectionRequestStep.*;
import static nextstep.subway.acceptance.stationstep.StationRequestStep.역_생성;
import static nextstep.subway.acceptance.testenum.TestLine.신분당선;

/**
 * Feature     : 노선의 지하철역 구간 관리
 * Backgound   : 노선이 있어야하고, 지하철역이 최소 2개 이상 존재해야한다.
 */
@DisplayName("노선의 지하철역 구간 관리")
public class SectionAcceptanceTest extends AcceptanceTest {
    private Long 강남역Id;
    private Long 역삼역Id;
    private int 강남_역삼_거리;
    private Long 신분당선Id;

    @BeforeEach
    void 노선과_지하철역_미리_생성() {
        강남역Id = extractId(역_생성("강남역"));
        역삼역Id = extractId(역_생성("역삼역"));
        강남_역삼_거리 = 10;
        신분당선Id = extractId(노선_생성(신분당선));
    }

    private Long extractId(ExtractableResponse<Response> response) {
        return response.body().jsonPath().getLong("id");
    }

    /**
     * Scenario: 생성된 노선과 지하철역들을 통해 구간을 등록한다.
     * when    : 구간 등록을 요청하면
     * then    : 구간이 등록된다.
     */
    @DisplayName("구간 등록")
    @Test
    void 구간_등록() {
        // when
        ExtractableResponse<Response> postResponse = 구간_생성_요청(강남역Id, 역삼역Id, 강남_역삼_거리, 신분당선Id);

        // then
        응답_상태_검증(postResponse, HttpStatus.CREATED);
        구간_개수_검증(신분당선Id, 1);
    }

    /**
     * Scenario: 생성된 구간을 삭제한다.
     * given   : background에 맞춰 구간을 생성하고,
     * given   : 새로운 하행선을 추가하여 구간을 생성한다.
     * when    : 새로운 하행선에 대한 구간 삭제 요청을 하면
     * then    : 구간이 삭제된다.
     */
    @DisplayName("구간 삭제")
    @Test
    void 구간_삭제() {
        // given
        구간_생성_요청(강남역Id, 역삼역Id, 강남_역삼_거리, 신분당선Id);

        // given
        Long 양재역Id = extractId(역_생성("양재역"));
        int 역삼_양재_거리 = 5;
        구간_생성_요청(역삼역Id, 양재역Id, 역삼_양재_거리, 신분당선Id);

        // when
        ExtractableResponse<Response> deleteResponse = 구간_삭제_요청(신분당선Id, 양재역Id);

        // then
        응답_상태_검증(deleteResponse, HttpStatus.NO_CONTENT);
        구간_개수_검증(신분당선Id, 1);
    }
}
