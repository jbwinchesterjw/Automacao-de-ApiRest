package br.teste.api;

import org.hamcrest.Matchers;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class EnvioDadosTest {
	@Test
	public void deveEnviarValorViaQuery() {
	RestAssured
	.given()
		.log().all()
	.when()
		.get("https//restapi.wcaquino.me/v2/users?format=json")
	.then()
		.log().all()
		.statusCode(200)
		.contentType(ContentType.JSON)
		;
	}
	@Test
	public void deveEnviarValorViaQueryViaParametro() {
	RestAssured
	.given()
		.log().all()
		.queryParam("format", "xml")
		.queryParam("outra", "coisa")//os parametros podem ser aleatorios não irar dar problema
	.when()
		.get("https//restapi.wcaquino.me/v2/users")
	.then()
		.log().all()
		.statusCode(200)
		.contentType(ContentType.XML)
		.contentType(Matchers.containsString("utf-8"))
		;
	}
	
	@Test
	public void deveEnviarValorViaHeader() {
	RestAssured
	.given()
		.log().all()
		.accept(ContentType.XML)//atenção: quando eu quero dizer o que venha da resposta eu não uso o contentType e sim o accept
	.when()
		.get("https//restapi.wcaquino.me/v2/users")
	.then()
		.log().all()
		.statusCode(200)
		.contentType(ContentType.JSON)
		;
	}
	
}
