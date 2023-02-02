package subway.domain.line;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;

public class LineUnitTest {
    public static ExtractableResponse<Response> 지하철노선을_생성한다(Map<String, Object> param) {
        지하철구간을_생성한다(param);

        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(param)
                .when().post("/lines")
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> 지하철구간을_생성한다(Map<String, Object> param) {
        Map<String, Object> sectionParam = new HashMap<>();
        sectionParam.put("upStationId", param.get("upStationId"));
        sectionParam.put("downStationId", param.get("downStationId"));
        sectionParam.put("distance", param.get("distance"));

        var response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(sectionParam)
                .when().post("/sections")
                .then().log().all()
                .extract();

        return response;
    }

    public static ExtractableResponse<Response> 지하철노선_목록을_조회한다() {
        return RestAssured.given().log().all()
                .when().get("/lines")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철노선을_조회한다(int id) {
        return RestAssured.given().log().all()
                .when().get("/lines/{id}",id)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철노선을_수정한다(int id, Map<String, String> param1) {
        return RestAssured.given().log().all()
                .body(param1)
                .contentType(ContentType.JSON)
                .when().put("/lines/{id}", id)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철노선을_삭제한다(int id) {
        return RestAssured.given().log().all()
                .when().delete("/lines/{id}",id)
                .then().log().all()
                .extract();
    }
}
