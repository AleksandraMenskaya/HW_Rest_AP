package tests;

import helpers.UserApiHelper;
import models.*;
import org.junit.jupiter.api.*;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static specs.Spec.successfulGetRequest;
import static specs.SpecLogin.loginRequestSpec;
import static specs.SpecLogin.successfulLoginResponse;
import static specs.SpecUser.successfulUserUpdateResponse;
import static specs.SpecUser.userRequestSpec;

public class ApiTest extends TestBase {
    String userEndPoint = "/api/users/";
    @Test
    @DisplayName("Получение пользователя по id")
    void getListUsers(){

        UsersResponseModel response = step("Делаем запрос на получение данных пользователя", ()->
                given(successfulGetRequest)
                        .when()
                        .get()
                        .then()
                        .spec(successfulLoginResponse)
                        .extract().as(UsersResponseModel.class));

        step("Проверяем из data поля id, email, first_name, last_name", () -> {
                assertEquals(2, response.getData().getId());
                assertEquals("janet.weaver@reqres.in", response.getData().getEmail());
                assertEquals("Janet", response.getData().getFirst_name());
                assertEquals("Weaver", response.getData().getLast_name());
                assertEquals("https://reqres.in/img/faces/2-image.jpg", response.getData().getAvatar());
        });
        step("Проверяем из support поля url, text", () -> {
                assertEquals("https://reqres.in/#support-heading", response.getSupport().getUrl());
                assertEquals("To keep ReqRes free, contributions towards server costs are appreciated!", response.getSupport().getText());
        });
    }

    @Test
    @DisplayName("Создание нового пользователя")
    void createUser () {
        UserCreateResponseModel model = step("Делаем запрос на создание нового пользователя", ()->
                UserApiHelper.createNewUser(userEndPoint)
        );

        step("Проверяем в ответе поля Name, Job, CreatedAt", () -> {
            assertEquals("Menskaia", model.getName());
            assertEquals("QA", model.getJob());
            assertNotNull(model.getCreatedAt());
        });
    }

    @Test
    @DisplayName("Редактирования данных пользователя")
    void UpdateUser () {
        UserBodyModel userData = new UserBodyModel();
        userData.setJob("QA engineer");
        userData.setName("MenskaiaAY");

        String userId = step("Делаем запрос на создание нового пользователя", ()->
                UserApiHelper.createNewUser(userEndPoint).getId()
        );

        UserUpdateResponseModel response = step("Делаем запрос на обновления данных пользователя", ()->
        given(userRequestSpec)
                .pathParam("userId", userId)
                .body(userData)
        .when()
                .put(userEndPoint + "{userId}")
        .then()
                .spec(successfulUserUpdateResponse)
                .extract().as(UserUpdateResponseModel.class));

        step("Проверяем в ответе поля Name, Job, updatedAt", () -> {
            assertEquals("MenskaiaAY", response.getName());
            assertEquals("QA engineer", response.getJob());
            assertNotNull(response.getUpdatedAt());
        });
    }

    @Test
    @DisplayName("Успешная регистрация")
    void registerSuccessful () {
        LoginBodyModel authData = new LoginBodyModel();
        authData.setEmail("eve.holt@reqres.in");
        authData.setPassword("pistol");

        LoginResponseModel response = step("Делаем запрос на регистрацию", ()->
                given(loginRequestSpec)
                        .body(authData)
                .when()
                        .post()
                .then()
                        .spec(successfulLoginResponse)
                        .extract().as(LoginResponseModel.class));

        step("Проверяем в ответе поля токен", ()-> {
                assertEquals("QpwL5tke4Pnpja7X4", response.getToken());
        });
    }

    @Test
    @DisplayName("Успешная авторизация")
    void loginSuccessful () {
        LoginBodyModel authData = new LoginBodyModel();
        authData.setEmail("eve.holt@reqres.in");
        authData.setPassword("pistol");

        LoginResponseModel response = step("Делаем запрос на авторизацию", ()->
                given(loginRequestSpec)
                            .body(authData)
                .when()
                            .post()
                .then()
                            .spec(successfulLoginResponse)
                            .extract().as(LoginResponseModel.class));

        step("Проверяем в ответе поля токен", ()-> {
            assertEquals("QpwL5tke4Pnpja7X4", response.getToken());
        });
    }
}
