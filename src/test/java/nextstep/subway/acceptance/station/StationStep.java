package nextstep.subway.acceptance.station;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.station.domain.dto.StationRequest;

@Service
public class StationStep {
    private static final String NAME_FORMAT = "%d역";

    private int dummyCounter = 0;

    public ExtractableResponse<Response> 지하철역_생성_요청(StationRequest request) {
        return RestAssured.given().log().all()
                          .body(request)
                          .contentType(MediaType.APPLICATION_JSON_VALUE)
                          .when()
                          .post("/stations")
                          .then().log().all()
                          .extract();
    }

    public ExtractableResponse<Response> 지하철역_생성_요청() {
        return 지하철역_생성_요청(dummyRequest());
    }

    public StationRequest dummyRequest() {
        return new StationRequest(nextName());
    }

    public synchronized String nextName() {
        return String.format(NAME_FORMAT, dummyCounter++);
    }
}
