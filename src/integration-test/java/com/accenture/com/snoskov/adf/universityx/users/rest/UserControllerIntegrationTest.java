package com.accenture.com.snoskov.adf.universityx.users.rest;

import com.accenture.com.snoskov.adf.universityx.UniversityXAuthServiceApplication;
import com.accenture.com.snoskov.adf.universityx.users.rest.model.AuthRequestDTO;
import com.accenture.com.snoskov.adf.universityx.users.rest.model.CreateUserRequestDTO;
import com.accenture.com.snoskov.adf.universityx.users.rest.model.SignUpRequestDTO;
import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { UniversityXAuthServiceApplication.class }, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class UserControllerIntegrationTest {

    private static final int PORT = 8080;

    private static final String JWT_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbl91c2VyIiwicm9sZSI6IkFETUlOIiwiZXhwIjo1OTk3MTA0MzczLCJpYXQiOjE1ODIwNjQzNzN9.qDEdYizlPs4fp9yi-NYa-Yska_cyqXkSTnrxNafgyg0";

    private static final String BASE_URL = "/apis/users";
    private static final String SIGN_UP_URL = "/sign-up";
    private static final String AUTHENTICATE_URL = "/authenticate";
    private static final String CREATE_USER = "/create";

    private static final String CONTENT_TYPE_JSON = "application/json";
    private static final String FIELD_MESSAGE = "message";

    private Header adminRoleHeader;

    @BeforeClass
    public static void initialize() {
        RestAssured.port = PORT;
    }

    @Before
    public void setup() {
        adminRoleHeader = new Header("Authorization", "Bearer " + JWT_TOKEN);
    }

    @Test
    public void testSignUp() {
        SignUpRequestDTO request = new SignUpRequestDTO();
        request.setUsername("new_user");
        request.setPassword("password1");
        request.setFirstName("Vasja");
        request.setLastName("Pupkin");
        request.setEmail("vasja@pupkin.com");
        request.setToken("wrong_token");

        Response response = RestAssured.given()
                .contentType(CONTENT_TYPE_JSON)
                .body(request)
                .post(BASE_URL + SIGN_UP_URL);

        String json = response.asString();
        JsonPath jp = new JsonPath(json);

        assertEquals(400, response.getStatusCode());
        assertEquals("Invalid token for admin user!", jp.get(FIELD_MESSAGE));
    }

    @Test
    public void testAuthenticate() {
        AuthRequestDTO request = new AuthRequestDTO();
        request.setUsername("non_existing_user");
        request.setPassword("password2");

        Response response = RestAssured.given()
                .contentType(CONTENT_TYPE_JSON)
                .body(request)
                .post(BASE_URL + AUTHENTICATE_URL);

        String json = response.asString();
        JsonPath jp = new JsonPath(json);

        assertEquals(400, response.getStatusCode());

        String message = String.format("Authentication failed for user: %s", request.getUsername());
        assertEquals(message, jp.get(FIELD_MESSAGE));
    }

    @Test
    public void testCreateUser() {
        CreateUserRequestDTO request = new CreateUserRequestDTO();
        request.setStudentId("new_user");
        request.setPassword("password3");
        request.setYearOfEntrance("2020");
        request.setProgramId(1);
        request.setFirstName("Fedor");
        request.setLastName("Sumkin");
        request.setEmail("bad-email.com");

        Response response = RestAssured.given()
                .header(adminRoleHeader)
                .contentType(CONTENT_TYPE_JSON)
                .body(request)
                .post(BASE_URL + CREATE_USER);

        String json = response.asString();
        JsonPath jp = new JsonPath(json);

        assertEquals(400, response.getStatusCode());
        assertEquals("Email looks suspicious.", jp.get(FIELD_MESSAGE));
    }

    @Test
    public void testRetrieveUserByName() {
        Response response = RestAssured.given()
                .header(adminRoleHeader)
                .contentType(CONTENT_TYPE_JSON)
                .get(BASE_URL + "/admin_user");

        String json = response.asString();
        JsonPath jp = new JsonPath(json);

        assertEquals("admin_user", jp.get("username"));
    }

    @Test
    public void testRetrieveUsers() {
        Response response = RestAssured.given()
                .header(adminRoleHeader)
                .contentType(CONTENT_TYPE_JSON)
                .get(BASE_URL + "/?id=1");

        String json = response.asString();
        assertTrue(json.contains("\"username\":\"admin_user\""));
    }

    @Test
    public void testRetrieveUserSummary() {
        Response response = RestAssured.given()
                .header(adminRoleHeader)
                .contentType(CONTENT_TYPE_JSON)
                .get(BASE_URL + "/summary/?id=1");

        String json = response.asString();
        assertTrue(json.contains("\"username\":\"admin_user\""));
    }
}