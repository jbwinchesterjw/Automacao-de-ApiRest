package br.teste.api;

import static org.hamcrest.Matchers.*;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class Html {

	@Test
	public void deveFazerBuscasComHtml() {
		RestAssured.
		given()
			.log().all()
		.when()
			.get("https//restapi.wcaquino.me/v2/users")
		.then()
		.log().all()
		.statusCode(200)
		.contentType(ContentType.HTML)
		.body("html.body.div.table.tbody.tr.size", is(3))
		.body("html.body.div.table.tbody.tr[1].td[2]",is("25"))
		.appendRootPath("html.body.div.table.tbody")//
		.body("tr.find{it.toString().startsWith('2')}.td[1]",is("Maria Joaquina"))
		
		;
	}
	
	@Test//rever aula 40
	public void deveFazerBuscasComXpathComHtml() {
		RestAssured.
		given()
			.log().all()
		.when()
			.get("https//restapi.wcaquino.me/v2/users?format=clean")//URL modificado devido erro 
		.then()
		.log().all()
		.statusCode(200)
		.contentType(ContentType.HTML)
		.body(hasXPath("count(//table/tr)"), is(4))
		
		;
	}
}
