package br.teste.api;

import static io.restassured.RestAssured.given;

import static org.hamcrest.Matchers.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import io.restassured.http.ContentType;

public class VerbosTest {

	@Test
	public void deveSalvarUsuario() {
		given()
			.log().all()
			.contentType("application/json")
			.body("{\"name\" : \"Jose\", \"age\":50}")
		.when()
			.post("http://restapi.wcaquino.me/users")
		.then()
			.log().all()
			.statusCode(201)
			.body("id", is(notNullValue()))
			.body("name", is("Jose"))
			.body("age", is(50))
		;
	}
	
	@Test
	public void devesSalvarUsuarioUsandoMap() {//Map e como se fosse uma lista porem ele organiza pares
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("name", "Usauario via map");
		params.put("age", 25);
		given()
			.log().all()
			.contentType("application/json")
			.body(params)
		.when()
			.post("http://restapi.wcaquino.me/users")
		.then()
			.log().all()
			.statusCode(201)
			.body("id", is(notNullValue()))
			.body("name", is("Usauario via map"))
			.body("age", is(25))
		;
	}
	
	@Test
	public void devesSalvarUsuarioUsandoObjeto() {
		User user = new User("Usuario via objeto", 35);
		given()
			.log().all()
			.contentType("application/json")
			.body(user)
		.when()
			.post("http://restapi.wcaquino.me/users")
		.then()
			.log().all()
			.statusCode(201)
			.body("id", is(notNullValue()))
			.body("name", is("Usuario via objeto"))
			.body("age", is(35))
		;
	}
	
	@Test
	public void deveDeserializarObjetoSalvarUsuario() {
		User user = new User("Usuario deserializado", 35);
		User usuarioInserido = given()
			.log().all()
			.contentType("application/json")
			.body(user)
		.when()
			.post("http://restapi.wcaquino.me/users")
		.then()
			.log().all()
			.statusCode(201)
			.extract().body().as(User.class)
		;
		System.out.println(usuarioInserido);
		Assert.assertThat(usuarioInserido.getId(), notNullValue());
		Assert.assertEquals("Usuario deserializado", usuarioInserido.getName());
		Assert.assertThat(usuarioInserido.getAge(), is(35));
	}
	
	@Test
	public void naoSalavarUserSemNome () {
		given()
		.log().all()
		.contentType("application/json")
		.body("{\"age\":50}")
	.when()
		.post("http://restapi.wcaquino.me/users")
	.then()
		.log().all()
		.statusCode(400)
		.body("id", is(nullValue()))
		.body("error", is("Name é um atributo obrigatóri"))
	;
	}
	
	@Test
	public void deveSalavarUserViaXml () {
		given()
		.log().all()
		.contentType(ContentType.XML)
		//.contentType("application/xml")
		.body("<user><name>Jose</name><age>50</age></user>")
	.when()
		.post("http://restapi.wcaquino.me/usersXML")
	.then()
		.log().all()
		.statusCode(201)
		.body("user.@id", is(notNullValue()))
		.body("user.name", is("Jose"))
		.body("user.age", is("50"))
	;
	}
	
	@Test
	public void deveSalavarUserViaXmlUsandoObjeto() {
		User user = new User("UsuarioXML", 40);
		given()
		.log().all()
		.contentType(ContentType.XML)
		//.contentType("application/xml")
		.body(user)
	.when()
		.post("http://restapi.wcaquino.me/usersXML")
	.then()
		.log().all()
		.statusCode(201)
		.body("user.@id", is(notNullValue()))
		.body("user.name", is("UsuarioXML"))
		.body("user.age", is("40"))
	;
	}
	
	@Test
	public void deveDeserializarXMLaoSalavarUser() {
		User user = new User("UsuarioXML", 40);
		 User ususarioInserido = given()
		.log().all()
		.contentType(ContentType.XML)
		//.contentType("application/xml")
		.body(user)
	.when()
		.post("http://restapi.wcaquino.me/usersXML")
	.then()
		.log().all()
		.statusCode(201)
		.extract().body().as(User.class)
	;
		 Assert.assertThat(ususarioInserido.getId(), notNullValue());
		 Assert.assertThat(ususarioInserido.getName(), is("Usuario Xml"));
		 Assert.assertThat(ususarioInserido.getAge(), is(40));
	}
	
	@Test
	public void deveAlterarUsuario() {
		given()
			.log().all()
			.contentType("application/json")
			.body("{\"name\" : \"Usuari alterado\", \"age\":80}")
		.when()
			.put("http://restapi.wcaquino.me/users/1")
		.then()
			.log().all()
			.statusCode(200)
			.body("id", is(1))
			.body("name", is("Usuari alterado"))
			.body("age", is(80))
			.body("salary", is(1234.5678f))
		;
	}
	
	@Test
	public void deveCustomizarURL() {
		given()
			.log().all()
			.contentType("application/json")
			.body("{\"name\" : \"Usuari alterado\", \"age\":80}")
		.when()
			.put("http://restapi.wcaquino.me/{entidade}/{userId}","users", "1")
		.then()
			.log().all()
			.statusCode(200)
			.body("id", is(1))
			.body("name", is("Usuari alterado"))
			.body("age", is(80))
			.body("salary", is(1234.5678f))
		;
	}
	
	@Test
	public void deveCustomizarURLParte2() {
		given()
			.log().all()
			.contentType("application/json")
			.body("{\"name\" : \"Usuari alterado\", \"age\":80}")
			.pathParam("entidade", "users")
			.pathParam("userId", 1)
		.when()
			.put("http://restapi.wcaquino.me/{entidade}/{userId}")
		.then()
			.log().all()
			.statusCode(200)
			.body("id", is(1))
			.body("name", is("Usuari alterado"))
			.body("age", is(80))
			.body("salary", is(1234.5678f))
		;
	}
	
	@Test
	public void deveRemoverUsusario() {
		given()
			.log().all()
		.when()
			.delete("http://restapi.wcaquino.me/users/1")
		.then()
			.log().all()
			.statusCode(204)
			;
	}
	
	@Test
	public void naoDeveRemoverUsusarioInexistente() {
		given()
			.log().all()
		.when()
			.delete("http://restapi.wcaquino.me/users/100")
		.then()
			.log().all()
			.statusCode(400)
			.body("error", is("Registro enexistente"))
			;
	}
}
