import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import netscape.javascript.JSObject;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

public class BookStoreApiTest {
@Test
    public void apiTest(){
    RestAssured.baseURI ="https://bookstore.toolsqa.com/";
    Response response = RestAssured
            .given()
            .when()
            .get("BookStore/v1/Books")
            .then()
            .extract().response();
            Assert.assertEquals(response.getStatusCode(), 200, "Status code should be 200");

    String author = response.jsonPath().getString("books[0].author");
    String publisher = response.jsonPath().getString("books[0].publisher");
    Assert.assertEquals(author, "Richard E. Silverman", "Author should be Richard E. Silverman");
    Assert.assertEquals(publisher, "O'Reilly Media", "Publisher should be O'Reilly Media");

}
@Test
    public void postApiTest(){
    RestAssured.baseURI = "https://bookstore.toolsqa.com/";
    JSONObject requestBody = new JSONObject();
    Faker faker = new Faker();
    String firstName = faker.name().firstName();
    requestBody.put("userName", firstName);
    requestBody.put("password", "FirsTest123#");
    Response response = RestAssured
            .given()
            .header("accept","application/json")
            .header("Content-Type", "application/json")
            .body(requestBody.toString())
            .when()
            .post("Account/v1/User")
            .then()
            .extract().response();
    Assert.assertEquals(response.getStatusCode(), 201, "Status code should be 200");
    String responseBody = response.getBody().asString();
    Assert.assertTrue(responseBody.contains("userID"),"Response Body Doesn't contains userID");
}
    @Test
    public void invalidPostApiTest(){
        RestAssured.baseURI = "https://bookstore.toolsqa.com/";
        JSONObject requestBody = new JSONObject();
        Faker faker = new Faker();
        String firstName = faker.name().firstName();
        requestBody.put("userName", firstName);
        requestBody.put("password", "test");
        Response response = RestAssured
                .given()
                .header("accept","application/json")
                .header("Content-Type", "application/json")
                .body(requestBody.toString())
                .when()
                .post("Account/v1/User")
                .then()
                .extract().response();
        Assert.assertEquals(response.getStatusCode(), 400, "Status code should be 200");
        String responseBody = response.getBody().asString();
        String errorMessage = "Passwords must have at least one non alphanumeric character, one digit ('0'-'9'), one uppercase ('A'-'Z'), one lowercase ('a'-'z'), one special character and Password must be eight characters or longer.";
        String getErrorMessage = response.jsonPath().getString("message");

        Assert.assertEquals(getErrorMessage, errorMessage);

    }
}
