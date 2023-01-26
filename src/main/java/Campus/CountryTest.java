package Campus;

import Campus.Model.Country;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;


public class CountryTest {

    Cookies cookies;
    String countryID;
    String countryName;
    String countryCode;


    @BeforeClass
    public void loginCampus()
    {
        baseURI = "https://test.mersys.io/";
        // Diğer testler çalışmadan önce login olup cookies im alınması gerekiyor
        // bu yüzden before class annotation ı ekledik

        Map<String,String> credential = new HashMap<>();
        credential.put("username","turkeyts");
        credential.put("password","TechnoStudy123");
        credential.put("rememberMe","true");

        cookies =
                given()
                        .contentType(ContentType.JSON)
                        .body(credential)

                        .when()
                        .post("auth/login")

                        .then()
                        //.log().body()
                        .statusCode(200)
                        .extract().response().detailedCookies()
        ;
        //System.out.println("cookies = " + cookies);


    }
    public String getRandomName()
    {
        return RandomStringUtils.randomAlphabetic(8);
    }
    public String getRandomCode() { return RandomStringUtils.randomAlphabetic(3); }


    @Test
    public void createCountry()
    {
        Country country = new Country();
        country.setName(getRandomName());
        country.setCode(getRandomCode());

        Response countryBody =
        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(country)
                .log().body()

                .when()
                .post("school-service/api/countries")

                .then()
                .log().body()
                .statusCode(201)
                .extract().response()
        ;
        countryName = countryBody.jsonPath().getString("name");
        countryCode = countryBody.jsonPath().getString("code");
        countryID = countryBody.jsonPath().getString("id");

        System.out.println("countryName = " + countryName);
        System.out.println("countryCode = " + countryCode);
    }
    @Test(dependsOnMethods = "createCountry",priority = 1)
    public void createCountryNegative()
    {
        Map<String,String> negativeCountry = new HashMap<>();
        negativeCountry.put("name",countryName);
        negativeCountry.put("code",countryCode);

                given()
                        .cookies(cookies)
                        .contentType(ContentType.JSON)
                        .body(negativeCountry)
                        .log().body()

                        .when()
                        .post("school-service/api/countries")

                        .then()
                        .log().body()
                        .statusCode(400)
                        .body("message",containsString(countryName))
                        //.body("message",equalTo("The Country with Name \""+countryName+"\" already exists."))
                ;
    }

    @Test(dependsOnMethods = "createCountry" , priority = 2)
    public void updateCountry()
    {
        countryName = getRandomName();
        countryCode = getRandomCode();

        Country country = new Country();
        country.setName(countryName);
        country.setCode(countryCode);
        country.setId(countryID);

        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(country)
                //.log().body()

                .when()
                .put("school-service/api/countries")

                .then()
                .log().body()
                .statusCode(200)
                .body("name",equalTo(countryName))
        ;


    }
    @Test(dependsOnMethods = "updateCountry")
    public void deleteCountry()
    {

        given()
                .cookies(cookies)
                .log().uri()

                .when()
                .delete("school-service/api/countries/"+countryID)

                .then()
                .log().body()
                .statusCode(200)
        ;

    }

    @Test(dependsOnMethods = "deleteCountry")
    public void deleteCountryNegative()
    {

        given()
                .cookies(cookies)
                .log().uri()

                .when()
                .delete("school-service/api/countries/"+countryID)

                .then()
                .log().body()
                .statusCode(400)
        ;

    }
//@Test(dependsOnMethods = "deleteCountry")
//public void deleteCountryByIdNegative() {
//    given()
//            .cookies(cookies)
//            .pathParam("countryID", countryID)
//            .log().uri()
//
//            .when()
//            .delete("school-service/api/countries/{countryID}")
//
//            .then()
//            .log().body()
//            .statusCode(400)
//            .body("message", equalTo("Country not found"))
//    ;
//}

    @Test(dependsOnMethods = "deleteCountryNegative")
    public void updateDeletedCountry()
    {
        countryName = getRandomName();
        countryCode = getRandomCode();

        Country country = new Country();
        country.setName(countryName);
        country.setCode(countryCode);
        country.setId(countryID);

        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(country)
                //.log().body()

                .when()
                .put("school-service/api/countries")

                .then()
                .log().body()
                .statusCode(400)
                .body("message",equalTo("Country not found"))
        ;
    }
}
