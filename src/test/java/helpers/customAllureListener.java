package helpers;

import io.qameta.allure.restassured.AllureRestAssured;

public class customAllureListener {
        private static final AllureRestAssured FILTER = new AllureRestAssured();

        public static AllureRestAssured withCustomTemplates() {
            FILTER.setRequestTemplate("request.ftl");
            FILTER.setResponseTemplate("response.ftl");
            return FILTER;
        }
}
