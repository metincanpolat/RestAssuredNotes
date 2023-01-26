import POJO.Location;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class ZippoTest {

    @Test
    public void test(){

        given().
                // hazırlık işlemlerini yapacağız (token,send body, parametreler)
                when().
                // link i ve metodu veriyoruz
                then();
                // assertion ve verileri ele alma extract


    }

    @Test
    public void statusCodeTest(){

        given().
                when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body() // log().all() bütün respons u gösterir
                .statusCode(200) // status kontrolü
        ;

    }

    @Test
    public void contentTypeTest(){

        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body() // log().all() bütün respons u gösterir
                .statusCode(200) // status kontrolü
                .contentType(ContentType.JSON) // Dönen sonuç json tipinde mi
        ;

    }

    @Test
    public void checkCountryInResponseBody(){

        given().
                when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body() // log().all() bütün respons u gösterir
                .statusCode(200) // status kontrolü
                .body("country", equalTo("United States"))// body.country == United States ?   equalTo -> org.hamcrest metodlarından
          ;

    }
/*
    pm                              RestAssured
    body.country                    body("country",
    body.'post code'                body("post code",
    body.places[0].'place name'     body("places[0].'place name'")
    body.places.'place name'        body("places.'place name'")   -> bütün place name leri verir
                                    bir index verilmezse dizinin bütün elemanlarında arar
    {
        "post code": "90210",
            "country": "United States",
            "country abbreviation": "US",
            "places": [   // köşeli parantez görünce anlıyoruz ki bu bir dizi
        {
            "place name": "Beverly Hills",
                "longitude": "-118.4065",
                "state": "California",
                "state abbreviation": "CA",
                "latitude": "34.0901"
        },
        {
            "place name": "Beverly Hills",
                "longitude": "-118.4065",
                "state": "California",
                "state abbreviation": "CA",
                "latitude": "34.0901"
        }
    ]
    }

    {
        "meta": {
        "pagination": {
            "total": 4252,       body("meta.pagination.total"  yani parantezler direk noktadır. [] yok ise

*/


    @Test
    public void checkStateInResponseBody(){

        given().
                when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body() // log().all() bütün respons u gösterir
                .statusCode(200) // status kontrolü
                .body("places[0].state", equalTo("California")) //bire bir eşit mi?
        ;

    }

    @Test
    public void bodyJsonPathTest3(){

        given().
                when()
                .get("http://api.zippopotam.us/tr/01000")
                .then()
                .log().body() // log().all() bütün respons u gösterir
                .statusCode(200) // status kontrolü
                .body("places.'place name'", hasItem("Dörtağaç Köyü")) // verilen path deki liste bu item a sahip mi, contains.
                                                                            // hasItem -> org.hamcrest metodlarından
        ;

    }

    @Test
    public void bodyArrayHasSizeTest(){

        given().
                when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body() // log().all() bütün respons u gösterir
                .statusCode(200) // status kontrolü
                .body("places", hasSize(1)) // place in size ı 1 e eşit mi
        ;

    }

    @Test
    public void combiningTest(){

        given().
                when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body() // log().all() bütün respons u gösterir
                .statusCode(200) // status kontrolü
                .body("places", hasSize(1)) // place in size ı 1 e eşit mi
                .body("places.state", hasItem("California")) // verilen path deki list bu item e sahip mi
                .body("places[0].'place name'",equalTo("Beverly Hills")) // verilen path deki değer buna eşit mi
        ;

    }

    @Test
    public void pathParamTest(){

        given()
                .pathParam("Country","us") // burada url için değişken tanımladık
                .pathParam("ZipCode",90210)
                .log().uri()  // Request URI:	http://api.zippopotam.us/us/90210
                .when()
                .get("http://api.zippopotam.us/{Country}/{ZipCode}")
                .then()
                .log().body()
                .statusCode(200) // status kontrolü

        ;

    }

    @Test
    public void pathParamTest2() {
        //90210 da 90213 kadar test sonuçlarında places in size nın hepsinde 1 geldiğini test ediniz.

        for (int i = 90210; i <= 90213; i++) {
            given()
                    .pathParam("Country", "us") // burada url için değişken tanımladık
                    .pathParam("ZipCode", i)
                    .log().uri()  // Request URI:	http://api.zippopotam.us/us/90210
                    .when()
                    .get("http://api.zippopotam.us/{Country}/{ZipCode}")
                    .then()
                    .log().body() // log().all() bütün respons u gösterir
                    .statusCode(200) // status kontrolü
                    .body("places",hasSize(1))
            ;

        }
    }

    @Test
    public void queryParamTest() {


            given()
                    .param("page",1) // ?page=1 şeklinde linke ekleniyor
                    .log().uri()  // Request URI:	https://gorest.co.in/public/v1/users?page=1

                    .when()
                    .get("https://gorest.co.in/public/v1/users")

                    .then()
                    .log().body() // log().all() bütün respons u gösterir
                    .statusCode(200) // status kontrolü
                    .body("meta.pagination.page",equalTo(1))
            ;

    }
    @Test
    public void queryParamTest2() {
        // https://gorest.co.in/public/v1/users?page=3
        // bu linkteki 1 den 10 kadar sayfaları çağırdığınızda response daki donen page degerlerinin
        // çağrılan page nosu ile aynı olup olmadığını kontrol ediniz.

        for (int i = 1; i <= 10 ; i++) {
            given()
                .param("page",i)
                .log().uri()  // Request URI:	https://gorest.co.in/public/v1/users?page=1

                .when()
                .get("https://gorest.co.in/public/v1/users")

                .then()
                .log().body() // log().all() bütün respons u gösterir
                .statusCode(200) // status kontrolü
                .body("meta.pagination.page",equalTo(i))
        ;
        }

    }

    RequestSpecification requestSpec;
    ResponseSpecification responseSpec;

    @BeforeClass
    void Setup(){

        baseURI="https://gorest.co.in/public/v1";

        requestSpec = new RequestSpecBuilder()
                .log(LogDetail.URI)
                .setContentType(ContentType.JSON)
                .build();


        responseSpec = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectContentType(ContentType.JSON)
                .log(LogDetail.BODY)
                .build();


    }

    @Test
    public void requestResponseSpecification(){
        //https:gorest.co.in/public/v1/users?page=3

        given()
                .param ("page",1)
                .spec(requestSpec)
                .when()
                .get("/users") // başında http olsaydı baseURI yi kullanmazdı.
                .then()
                .body("meta.pagination.page", equalTo(1))
                .spec(responseSpec)
        ;

    }

    // Json extract

    @Test
    public void extractingJsonPath()
    {
        String placeName =
                given()
                        .when()
                        .get("http://api.zippopotam.us/us/90210")
                        .then()
                        .statusCode(200)
                        //.log().body()
                        .extract().path("places[0].'place name'") // extracy her zaman sona yazıyoruz
        // extract metodu ile given ile başlayan satır,
        // bir değer döndürür hale geldi, en sonda extract olmalı
                ;
        System.out.println("placeName = " + placeName);
    }

    @Test
    public void extractingJsonPathInt()
    {
        int limit =
                given()
                        .when()
                        .get("https://gorest.co.in/public/v1/users")
                        .then()
                        //.log().body()
                        .statusCode(200)
                        .extract().path("meta.pagination.limit")
                ;
        System.out.println("limit = " + limit);
    }

    @Test
    public void extractingJsonPathList()
    {
        List<Integer> idler =
                given()
                        .when()
                        .get("https://gorest.co.in/public/v1/users")
                        .then()
                        //.log().body()
                        .statusCode(200)
                        .extract().path("data.id")
                ;

        System.out.println("idler = " + idler);
        Assert.assertTrue(idler.contains(4235));
    }

    @Test
    public void extractingJsonPathStringList()
    {
        List<String> isimler =
                given()
                        .when()
                        .get("https://gorest.co.in/public/v1/users")
                        .then()
                        //.log().body()
                        .statusCode(200)
                        .extract().path("data.name")
                ;

        System.out.println("idler = " + isimler);
        Assert.assertTrue(isimler.contains("Gautam Ahluwalia"));
    }

    @Test
    public void extractingJsonPathResponseAll()
    {
        Response response = // burada tüm response ı aldık aşağıda içerisinden istediğimiz değerleri list e aldık.
                given()
                        .when()
                        .get("https://gorest.co.in/public/v1/users")
                        .then()
                        //.log().body()
                        .statusCode(200)
                        .extract().response()
                ;

        List<Integer> idler = response.path("data.id");
        List<String> isimler = response.path("data.name");
        int limit = response.path("meta.pagination.limit");


        System.out.println("idler = " + idler);
        System.out.println("isimler = " + isimler);
        System.out.println("limit = " + limit);
        System.out.println("response.prettyPrint() = " + response.prettyPrint()); // tüm response u aynı body gibi yazdırıyoruz.

        Assert.assertTrue(isimler.contains("Aanandaswarup Pillai"));
        Assert.assertTrue(idler.contains(4139));
        Assert.assertEquals(limit,10,"test sonucu");
        System.out.println("limit = " + limit);
    }

    @Test
    public void extractingJsonPOJO() // POJO : JSON Object i // POJO (Plain Old Java Object)
    {
        Location yer=
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body()
                .extract().as(Location.class); // Location şablonuna göre

        System.out.println("yer.getPostCode() = " + yer.getPostCode());
        System.out.println("yer.getPlaces().get(0).getPlaceName() = " + yer.getPlaces().get(0).getPlaceName());


    }





}
