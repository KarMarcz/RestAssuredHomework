package RestApiTestForHomework;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.junit.BeforeClass;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.core.IsEqual.equalTo;

public class RestApiTest {

    private static RequestSpecification requestSpec;

    @BeforeClass
    public static void createRequestSpecification() {
        requestSpec = new RequestSpecBuilder().
                setBaseUri("https://jsonplaceholder.typicode.com").
                build();
    }

    @Test
    public void getAllPosts() {
        given().
                spec(requestSpec).
        when().
                get("/posts").
        then().
                statusCode(200);
    }

    @Test
    public void getAllCommentsForSpecificPost() {
        given().
                spec(requestSpec).
        when().
                get("/posts/1/comments").
        then().
                statusCode(200);
    }

    @Test
    public void createNewPost() {
        given().
                spec(requestSpec).
                basePath("/posts").
                contentType("application/json").
                body("{\"userId\": 1,\"title\": \"test api for this site\",\"body\": \"Bez testowania nie ma życia XD\"}").
        when().
                post().
        then().
                statusCode(201);
    }

    @Test
    public void deletePost() {
        given().
                spec(requestSpec).
                basePath("/posts/100").
        when().
                delete().
        then().
                statusCode(200);
    }

    @Test
    public void contentEncodingIsGzip() {
        given().
                spec(requestSpec).
        when().
                get("/posts").
        then().
                header("Content-Encoding", equalTo("gzip"));
    }

    @Test
    public void sizeOfResponseShouldBe100() {
        given().
                spec(requestSpec).
        when().
                get("/posts").
        then().
                assertThat().
                body("size()", is(100));
    }

    @Test
    public void userWith235051337ZipCode() {
        given().
                spec(requestSpec).
        when().
                get("/users").
        then().
                contentType("application/json").
                body("address.findAll {zip -> zip.zipcode == \"23505-1337\"}.asBoolean()", equalTo(true));
    }

    @Test
    public void usersWithWebsiteEndingWith_ORG() {
        given().
                spec(requestSpec).
        when().
                get("/users").
        then().
                contentType("application/json").
                body("findAll {web -> web.website.endsWith(\".org\")}.size()", equalTo(2));
    }
}
