package petstore;

import helper.Data;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.io.IOException;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.*;

public class Pet {
    String url = "https://petstore.swagger.io/v2/pet"; //endereco da entidade Pet
    Data data;

    @BeforeMethod
    public void setup(){
        data = new Data();
    }

    //Incluir - Create - Post
    @Test(priority = 1)
    public void incluirPet() throws IOException {
        String jsonBody = data.lerJson("db/pet1.json");
                given()
                        .contentType("application/json")
                        .log().all()
                        .body(jsonBody)
                .when()
                        .post(url)
                .then()
                        .log().all()
                        .statusCode(200)
                        .body("name",is("xptotcs"))
                        .body("status",is("available"))
                        .body("category.name",is("AX2345LORT")) // usamos is quando ele consegue pegar direto, quando não tem []
                        .body("tags.name", contains("Semana de teste em API"))
                ; // quando ele esta dentro de [] se ultiliza contais por que ele acredita que seja uma lista
    }

    @Test(priority = 2)
    public void alterarPet() throws IOException {
        String jsonBody = data.lerJson("db/pet1.json");;

        given()
                .contentType("application/json")
                .log().all()
                .body(jsonBody)
        .when()
                .put(url)
        .then()
                .log().all()
                .statusCode(200)
                .body("name",is("natalia"))
                .body("status",is("sold"));
    }

    @Test(priority = 4)
    public void deletarPet() throws IOException {
        String petID = "197408012589";
        String jsonBody = data.lerJson("db/pet1.json");;

        given()
                .contentType("application/json")
                .log().all()
        .when()
                .delete(url + "/" + petID)
        .then()
                .log().all()
                .statusCode(200)
                .body("code",is(200))
                .body("type",is("unknown"))
                .body("message",is(petID))
        ;
    }

    @Test(priority = 3)
    public void consultarPet(){
        String petID = "197408012589";
        String token =

        given()
                .contentType("application/json")
                .log().all()
        .when()
                .get(url + "/" + petID)
        .then()
                .log().all()
                .body("name",is("natalia"))
                .body("category.name",is("AX2345LORT"))
                .body("status",is("sold"))
                .statusCode(200)
                .extract()
                .path("category.name")
        ;
        System.out.println("O token é "+token);
    }
    @Test
    public void consultarPetPorStatus(){
        String status = "available";

        given()
                .contentType("application/json")
                .log().all()

        .when()
                .get(url + "/findByStatus?status=" + status)
        .then()
                .log().all()
                .statusCode(200)
                .body("name[]",everyItem(equalTo("xptotcs")))
        ;
    }

}
