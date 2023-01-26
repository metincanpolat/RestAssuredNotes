package Gorest;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class GoRestUsersTest {
    int userID;
    User newUser;

    @BeforeClass
    void Setup() {

        baseURI = "https://gorest.co.in/public/v2/users";
    }

    public String getRandomName()
    {
        return RandomStringUtils.randomAlphabetic(8);
    }
    public String getRandomEmail()
    {
        return RandomStringUtils.randomAlphabetic(8).toLowerCase()+"@gmail.com";
    }


    @Test(enabled = false)
    public void createUserObject()
    {
        Map<String,String> newUser = new HashMap<>();
        newUser.put("name",getRandomName());
        newUser.put("gender","male");
        newUser.put("email",getRandomEmail());
        newUser.put("status","active");


        int userID=
        given()
                .header("Authorization","Bearer 8faf7cde9dc5815cb95944b916c8591830e749845aae3aac9035c3e7fe6852c8")
                .contentType(ContentType.JSON)
                //.body("{\"name\":\""+getRandomName()+"\", \"gender\":\"male\", \"email\":\""+getRandomEmail()+"\", \"status\":\"active\"}") // bu manuel yazılmış hali
                // üst taraf request özellikleridir : hazırlık işlmeleri POSTMAN deki Authorization ve request BODY kısmı
                .body(newUser)
                .log().uri()
                .log().body()
                .when() // request in olduğu nokta POSTMAN deki SEND butonu
                .post() // baseURI+parantez içi(https yoksa). Eğer GET,POST,... içerisine HTTPS ile başlayan bir adres varsa baseURI yi yok sayıyor.
                // respons un oluştuğu nokta CREATE işlemi POST metodu ile çağırıyoruz POSTMAN deki gibi

                // alt taraf response sonrası POSTMAN deki test penceresi
                .then()
                .log().body()
                .statusCode(201)
                .contentType(ContentType.JSON)
                .extract().path("id")
                ;

        System.out.println("userID = " + userID);
    }

    @Test(priority = 1)
    public void createUserWithObject() // yukarıdakinin aynısını class yöntemiyle yaptık.
    {
        newUser = new User();
        newUser.setName(getRandomName());
        newUser.setGender("male");
        newUser.setEmail(getRandomEmail());
        newUser.setStatus("active");


        userID=
                given()
                        .header("Authorization","Bearer 8faf7cde9dc5815cb95944b916c8591830e749845aae3aac9035c3e7fe6852c8")
                        .contentType(ContentType.JSON)
                        //.body("{\"name\":\""+getRandomName()+"\", \"gender\":\"male\", \"email\":\""+getRandomEmail()+"\", \"status\":\"active\"}")
                        // üst taraf request özellikleridir : hazırlık işlmeleri POSTMAN deki Authorization ve request BODY kısmı
                        .body(newUser)
                        .log().uri()
                        .log().body()
                        .when() // request in olduğu nokta POSTMAN deki SEND butonu
                        .post() // baseURI+parantez içi(https yoksa). Eğer GET,POST,... içerisine HTTPS ile başlayan bir adres varsa baseURI yi yok sayıyor.
                        // respons un oluştuğu nokta CREATE işlemi POST metodu ile çağırıyoruz POSTMAN deki gibi

                        // alt taraf response sonrası POSTMAN deki test penceresi
                        .then()
                        .log().body()
                        .statusCode(201)
                        .contentType(ContentType.JSON)
                        //.extract().path("id")
                        .extract().jsonPath().getInt("id") // bu şekilde de extract yaparak tipini istediğimiz tipte alabiliyoruz.
                ;
        // path : class veya tip dönüşümüne imkan veremeyen direk veriyi verir. List<String> gibi
        // jsonPath : class dönüşümüne ve tip dönüşümüne izin vererek , veriyi istediğimiz formatta verir.

        System.out.println("userID = " + userID);



    }

    @Test(dependsOnMethods = "createUserWithObject",priority = 2)
    public void getUserById() // yukarıdakinin aynısını class yöntemiyle yaptık.
    {
                given()
                        .header("Authorization","Bearer 8faf7cde9dc5815cb95944b916c8591830e749845aae3aac9035c3e7fe6852c8")
                        .pathParam("userID",userID)
                        .log().uri()

                        .when()
                        .get("{userID}")

                        .then()
                        .log().body()
                        .statusCode(200)
                        .contentType(ContentType.JSON)
                        .body("id",equalTo(userID))
        ;
    }
    @Test(dependsOnMethods = "getUserById",priority = 3)
    public void updateUserObject() // yukarıdakinin aynısını class yöntemiyle yaptık.
    {
        //newUser.setName("MetCan"); // Burada Map içerisindeki name i değiştirip User ı yeniden yolladık

        Map<String,String> updateUser = new HashMap<>(); // Burada ise sadece name i yolladık
        updateUser.put("name","MetCan");


        given()
                .header("Authorization","Bearer 8faf7cde9dc5815cb95944b916c8591830e749845aae3aac9035c3e7fe6852c8")
                .pathParam("userID",userID)
                .contentType(ContentType.JSON)
                .body(updateUser)
                .log().body()
                .log().uri()

                .when()
                .put("{userID}")

                .then()
                .log().body()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("id",equalTo(userID))
                .body("name",equalTo("MetCan"))
        ;
    }

    @Test(dependsOnMethods = "updateUserObject",priority = 4)
    public void deleteUserById()
    {

        given()
                .header("Authorization","Bearer 8faf7cde9dc5815cb95944b916c8591830e749845aae3aac9035c3e7fe6852c8")
                .pathParam("userID",userID)
                .log().uri()

                .when()
                .delete("{userID}")

                .then()
                .log().body()
                .statusCode(204)

        ;
    }

    @Test(dependsOnMethods = "deleteUserById")
    public void deleteUserByIdNegative()
    {

        given()
                .header("Authorization","Bearer 8faf7cde9dc5815cb95944b916c8591830e749845aae3aac9035c3e7fe6852c8")
                .pathParam("userID",userID)
                .log().uri()

                .when()
                .delete("{userID}")

                .then()
                .log().body()
                .statusCode(404)

        ;
    }

    @Test
    public void getUsers()
    {

        Response body =
        given()
                .header("Authorization","Bearer 8faf7cde9dc5815cb95944b916c8591830e749845aae3aac9035c3e7fe6852c8")
                .log().uri()

                .when()
                .get()

                .then()
                .log().body()
                .statusCode(200)
                .extract().response()
        ;
        int idUser3path = body.path("[2].id"); // index 0 dan başlayarak gidiyor. İçerisindeki 2. indexdeki id.
        int idUser3JsonPath = body.jsonPath().getInt("[2].id");
        System.out.println("idUser3path = " + idUser3path);
        System.out.println("idUser3JsonPath = " + idUser3JsonPath);
        
        User[] userlar = body.as(User[].class); // extract as
        System.out.println("Arrays.toString(userlar) = " + Arrays.toString(userlar));

        List<User> usersList = body.jsonPath().getList("",User.class);
        System.out.println("usersList = " + usersList);

    }

    @Test
    public void getUsersV1()
    {

        Response body =
                given()
                        .header("Authorization","Bearer 8faf7cde9dc5815cb95944b916c8591830e749845aae3aac9035c3e7fe6852c8")
                        .log().uri()

                        .when()
                        .get("https://gorest.co.in/public/v1/users")

                        .then()
                        .log().body()
                        .statusCode(200)
                        .extract().response()
                ;
        // body.as(), extract.as // tüm gelen response uygun nesneler için tüm class ların yapılması gerekiyor.

        List<User> dataUsers = body.jsonPath().getList("data",User.class);
        // JsonPath bir response içindeki bir parçayı nesneye dönüştürebiliriz.
        System.out.println("dataUsers = " + dataUsers);

        // Daha önceki örneklerde (as) Clas dönüşümleri için tüm yapıya karşılık gelen
        // gereken tüm classları yazarak dönüştürüp istediğimiz elemanlara ulaşıyorduk.
        // Burada ise(JsonPath) aradaki bir veriyi clasa dönüştürerek bir list olarak almamıza
        // imkan veren JSONPATH i kullandık.Böylece tek class ise veri alınmış oldu
        // diğer class lara gerek kalmadan

        // path : class veya tip dönüşümüne imkan veremeyen direk veriyi verir. List<String> gibi
        // jsonPath : class dönüşümüne ve tip dönüşümüne izin vererek , veriyi istediğimiz formatta verir.


    }


}

class User
{
    private int id;
    private String name;
    private String gender;
    private String email;
    private String status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", email='" + email + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
