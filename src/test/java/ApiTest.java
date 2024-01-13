import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.*;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.text.IsEmptyString.emptyString;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class ApiTest extends TestBase {
    String userEndPoint = "/api/users/";
    String registerEndPoint = "/api/register/";
    String loginEndPoint = "/api/login/";
    private static String userId;
    @Test
    @DisplayName("Получение списка пользователей с указанием номера страницы")
    void getListUsers(){
        String numberList = "2";
        get(userEndPoint + "?page=" + numberList)
        .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("data", not(emptyString()))
                .body("support.url", is("https://reqres.in/#support-heading"))
                .body("support.text", is("To keep ReqRes free, contributions towards server costs are appreciated!"));
    }

    @Test
    @Order(1)
    @DisplayName("Создание нового пользователя")
    void createUser () {
        String userData = "{\"name\": \"Menskaia\", \"job\": \"QA\"}";
        Response response = given()
                .body(userData)
                .contentType(JSON)
        .when()
                .post(userEndPoint)
        .then()
                .log().status()
                .log().body()
                .statusCode(201)
                .body("name", is("Menskaia"))
                .body("job", is("QA"))
                .body("id", is(notNullValue()))
                .extract()
                .response();
                userId = response.path("id");
    }

    @Test
    @Order(2)
    @DisplayName("Редактирования данных пользователя")
    void UpdateUser () {

        String userData = "{\"name\": \"MenskaiaAY\", \"job\": \"QA engineer\"}";
        given()
                .pathParam("userId", userId)
                .body(userData)
                .contentType(JSON)
                .log().uri()
        .when()
                .put(userEndPoint + "{userId}")
        .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("name", is("MenskaiaAY"))
                .body("job", is("QA engineer"));
    }

    @Test
    @DisplayName("Успешная регистрация")
    void registerSuccessful () {
        String authData = "{\"email\": \"eve.holt@reqres.in\", \"password\": \"pistol\"}";
        given()
                .body(authData)
                .contentType(JSON)
                .log().uri()
        .when()
                .post(registerEndPoint)
        .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("id", notNullValue())
                .body("token", is(notNullValue()));
    }

    @Test
    @DisplayName("Успешная авторизация")
    void loginSuccessful () {
        String authData = "{\"email\": \"eve.holt@reqres.in\", \"password\": \"pistol\"}";
        given()
                .body(authData)
                .contentType(JSON)
        .when()
                .post(loginEndPoint)
        .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("token", is("QpwL5tke4Pnpja7X4"));
    }
}
