package search_resources;

import bot.PS5HunterBot;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class SonyStoreSearcher {

    public static void search() {

        Response responsePS5 = given()
                .when()
                .get("https://store.sony.ru/product/konsol-playstation-5-digital-edition-317400/")
                .then()
                .log().all()
                .extract()
                .response();

        String stringResponsePS5 = responsePS5.asString();

        if (stringResponsePS5.contains("http://schema.org/InStock")) {
            System.out.println("PS5 В НАЛИЧИИ");
            PS5HunterBot.FOUND = true;
        } else if (stringResponsePS5.contains("http://schema.org/OutOfStock")) {
            System.out.println("PS5 НЕТ В НАЛИЧИИ");
        } else {
            System.out.println("ЧТО-ТО ПОШЛО НЕ ТАК");
        }
    }
}
