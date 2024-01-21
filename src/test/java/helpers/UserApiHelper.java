package helpers;

import models.UserBodyModel;
import models.UserCreateResponseModel;

import static io.restassured.RestAssured.given;
import static specs.SpecUser.successfulUserCreateResponse;
import static specs.SpecUser.userRequestSpec;

public class UserApiHelper {

    public static UserCreateResponseModel createNewUser (String userEndPoint) {
        UserBodyModel userData = new UserBodyModel();
        userData.setJob("QA");
        userData.setName("Menskaia");

        UserCreateResponseModel model =
                given(userRequestSpec)
                        .body(userData)
                .when()
                        .post(userEndPoint)
                .then()
                        .spec(successfulUserCreateResponse)
                        .extract().as(UserCreateResponseModel.class);

                return model;
    }
}
