package br.teste.api;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import io.restassured.http.ContentType;
public class AuthTest {

	@Test
	public void deveAcessarSWAPI() {
		given()
		.log().all()
	.when()
		.get("https://swapi.co/api/people/1")
	.then()
		.log().all()
		.statusCode(200)
		.body("name", is("Luke Skywalker"))
	;
	}
	
	@Test
	public void naoDeveAcessarSemSenha() {
		given()
		.log().all()
	.when()
		.get("https://restapi.wcaquino.me/basicauth")
	.then()
		.log().all()
		.statusCode(401)
	;
	}
	
	@Test
	public void deveFazerAutenticacaoBasica() {
		given()
		.log().all()
	.when()
		.get("https://admin:senha@restapi.wcaquino.me/basicauth")
	.then()
		.log().all()
		.statusCode(200)
		.body("status", is("logado"))
	;
	}
	
	@Test
	public void deveFazerAutenticacaoBasica2() {
		given()
		.log().all()
		.auth().basic("admin", "senha")
	.when()
		.get("https://restapi.wcaquino.me/basicauth")
	.then()
		.log().all()
		.statusCode(200)
		.body("status", is("logado"))
	;
	}
	@Test
	public void deveFazerAutenticacaoBasicaChallenge() {
		given()
		.log().all()
		.auth().preemptive().basic("admin", "senha")
	.when()
		.get("https://restapi.wcaquino.me/basicauth2")
	.then()
		.log().all()
		.statusCode(200)
		.body("status", is("logado"))
	;
	}
	
	@Test
	public void deveFazerAutentticacaoComTokemJWT() {
		Map<String, String> login = new HashMap<String, String>();
		login.put("email", "wcaquino@aquino");//e-mail referente a conta criada para acessar  a api
		login.put("senha", "123456");
		//login na api recebe o tokem
	String token = given()
		.log().all()
		.body(login)
		.contentType(ContentType.JSON)
	.when()
		.post("https://barrigarest.wcaquino.me/signin")
	.then()
		.log().all()
		.statusCode(200)
		.extract().path("token")
	;
	//obter as contas
	given()
		.log().all()
		.header("Autorizadetion", "JWT " + token)
	.when()
		.get("https://barrigarest.wcaquino.me/contas")
	.then()
		.log().all()
		.statusCode(200)
		.body("name", hasItem("Conta de teste"))
	;
	}
	
	@Test
	public void deveAcessarAplicacaoWeb() {
		String cookie = given()
		.log().all()
		.formParam("email", "wagner@aquino")
		.formParam("senha", "123456")
		.contentType(ContentType.URLENC.withCharset("UTF-8"))
	.when()
		.post("https://seubarrigare.wcaquino.me/logar")
	.then()
		.log().all()
		.statusCode(200)
		.extract().header("set-cookie")
	;
		cookie = cookie.split("=")[1].split(";")[0];
		System.out.println(cookie);
		
		//obter conta
	String body = given()
		.log().all()
		.cookie("connect.sid", cookie)
	.when()
		.post("https://seubarriga.wcaquino.me/logar")
	.then()
		.log().all()
		.statusCode(200)
		.extract().header("set-cookie")
	;
	}
}
