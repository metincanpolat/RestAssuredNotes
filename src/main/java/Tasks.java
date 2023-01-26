import POJO.ToDo;
import io.restassured.http.ContentType;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class Tasks {
    /** Task 1
     * create a request to https://jsonplaceholder.typicode.com/todos/2
     * expect status 200
     * Converting Into POJO
     */
    @Test
    public void task1()
    {
        ToDo todo=
                given()
                        .when()
                        .get("https://jsonplaceholder.typicode.com/todos/2")
                        .then()
                        .log().body()
                        .statusCode(200)
                        .extract().as(ToDo.class)
                ;
        System.out.println("todo = " + todo);
    }

    @Test
    public void task2()
    {
        /**
         * Task 2
         * create a request to https://httpstat.us/203
         * expect status 203
         * expect content type TEXT
         */

                given()
                        .when()
                        .get("https://httpstat.us/203")
                        .then()
                        .log().body()
                        .statusCode(203)
                        .contentType(ContentType.TEXT)
                ;
    }
    @Test
    public void task3()
    {
        /**
         * Task 3
         * create a request to https://httpstat.us/203
         * expect status 203
         * expect content type TEXT
         * expect BODY to be equal to "203 Non-Authoritative Information"
         */
// ? 1.Yöntem
        given()
                .when()
                .get("https://httpstat.us/203")
                .then()
                .log().body()
                .statusCode(203)
                .contentType(ContentType.TEXT)
                .body(equalTo("203 Non-Authoritative Information"))
        ;

        // ?2.Yöntem
        String bodyText=
        given()
                .when()
                .get("https://httpstat.us/203")
                .then()
                .log().body()
                .statusCode(203)
                .contentType(ContentType.TEXT)
                .extract().body().asString()
        ;
        Assert.assertTrue(bodyText.equalsIgnoreCase("203 Non-Authoritative Information"));
    }
    /**
     * Task 4
     * create a request to https://jsonplaceholder.typicode.com/todos/2
     * expect status 200
     * expect content type JSON
     * expect title in response body to be "quis ut nam facilis et officia qui"
     */

    @Test
    public void task4()
    {

        given()
                .when()
                .get("https://jsonplaceholder.typicode.com/todos/2")
                .then()
                .log().body()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("title",equalTo("quis ut nam facilis et officia qui"))
        ;
    }
    /**
     * Task 5
     * create a request to https://jsonplaceholder.typicode.com/todos/2
     * expect status 200
     * expect content type JSON
     * expect response completed status to be false
     */
    @Test
    public void task5()
    {
        given()
                .when()
                .get("https://jsonplaceholder.typicode.com/todos/2")
                .then()
                .log().body()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("completed",equalTo(false)) // hamcrest yöntemi
        ;
        // ? 2.Yöntem
        Boolean completed =
        given()
                .when()
                .get("https://jsonplaceholder.typicode.com/todos/2")
                .then()
                .log().body()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract().path("completed") // extract ve testNg Assertion yöntemi.
        ;
        Assert.assertFalse(completed);
    }




}
