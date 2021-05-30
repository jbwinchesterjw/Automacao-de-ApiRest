package br.teste.api;

import static io.restassured.RestAssured.given;


import org.junit.Test;

import io.restassured.matcher.RestAssuredMatchers;
import org.xml.sax.SAXParseException;

public class SchemaTest {
	
	@Test
	public void deveValidarSchemaXML() {
		given()
	.when()
		.get("http://restapi.wcaquino.me/usersXML")
	.then()
		.log().all()
		.statusCode(200)
		.body(RestAssuredMatchers.matchesXsdInClasspath("users.xsd"))
	;
	}

	@Test(expected = SAXParseException.class)
	public void naoDeveValidarSchemaXMLInvalido() {
		given()
	.when()
		.get("http://restapi.wcaquino.me/invalidUsersXML")
	.then()
		.log().all()
		.statusCode(200)
		.body(RestAssuredMatchers.matchesXsdInClasspath("users.xsd"))
	;
	}
	//aula 45 https://swapi.dev
	@Test
	public void deveValidarSchemaJson() {
		given()
	.when()
		.get("http://restapi.wcaquino.me/users")
	.then()
		.log().all()
		.statusCode(200)
		.body(RestAssuredMatchers.matchesXsdInClasspath("users.json"))
	;
	}
	
}
