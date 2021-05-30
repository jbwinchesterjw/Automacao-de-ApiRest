package br.teste.api;
import java.util.ArrayList;
import java.util.Arrays;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class UserJsonTest {
	
	@Test
	public void verificaPrimeiroNivel() {
		RestAssured.given()
		.when()
			.get("http://restapi.wcaquino.me/users/1")
		.then()
			.statusCode(200)
			.body("id", Matchers.is(1))
			.body("name", Matchers.containsString("Silva"))
			.body("age", Matchers.greaterThan(18))
		;
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void verificaPrimeiroNivelDeOutraForma() {
		Response response = RestAssured.request(Method.GET,"http://restapi.wcaquino.me/users/1");
		//path primeira maneira
		Assert.assertEquals(new Integer(1), response.path("id"));
		Assert.assertEquals(new Integer(1), response.path("%s","id"));//passando o id por parametro, primeiro uma strig depois o id
		
		//Jsompath
		JsonPath jpath = new JsonPath(response.asString());
		Assert.assertEquals(1, jpath.getInt("id"));
		
		//from
		int id = JsonPath.from(response.asString()).getInt("id");
		Assert.assertEquals(1, id);
	}
	
	@Test
	public void verificaSegundoNilvel() {
		RestAssured.given()
		.when()
			.get("http://restapi.wcaquino.me/users/2")
		.then()
			.statusCode(200)
			.body("id", Matchers.is(1))
			.body("name", Matchers.containsString("Joaquina"))
			.body("endereco.rua", Matchers.is("Rua dos bobos"))//para navegar de nivel em nivel basta colocar, ponto e escreve o nome do prximo intem
		;
	}
	
	@Test
	public void verifivcarLista() {
		RestAssured.given()
		.when()
			.get("http://restapi.wcaquino.me/users/3")
		.then()
			.statusCode(200)
			.body("name", Matchers.containsString("Ana"))//verifica se tem String = ana
			.body("filhos", Matchers.hasSize(2))// verifica a quantidade de filhos
			.body("filhos[0].name", Matchers.is("Zezinho"))//verifica se na primeira posição tem a string zezinho
			.body("filhos[1].name", Matchers.is("Luizinho"))//verifica se na segund posição tem a string luizinho
			.body("filhos.name", Matchers.hasItem("Zezinho"))//verifica se tem o filho com o nome zezinho
			.body("filhos.name", Matchers.hasItems("Luizinho", "Zezinho"))//verifica se tem os dois
		;
	}
	
	@Test
	public void retornaErroUserInexistente() {
		RestAssured.given()
		.when()
			.get("http://restapi.wcaquino.me/users/4")
		.then()
			.statusCode(404)
			.body("error", Matchers.is("Usúario inexistente"))
		;
	}
	
	@Test
	public void verificaListaNaRaiz() {
		RestAssured.given()
		.when()
			.get("http://restapi.wcaquino.me/users")
		.then()
			.statusCode(200)
			.body("$", Matchers.hasSize(3))//verifica o tamanho da lista na raiz, o $ não e obrigatorio apenas uma conversão !
			.body("name", Matchers.hasItems("Joõa da silva","Maria Joaquina", "Ana Júlia"))//verifica todos os nome presentes na lista
			.body("age[1]", Matchers.is(25))//verifica a idade do user 2
			.body("filhos.name", Matchers.hasItem(Arrays.asList("Zezinho","Luizinho")))//procure uma lista dentro de outra lista
			.body("salary", Matchers.contains(1234.5678f, 2500, null))//contains só da certo se for paasdo todos os itens e de forma ordenada
		;
	}
	
	@Test
	public void fazerVerificaçoesAvancada() {
		RestAssured.given()
		.when()
			.get("http://restapi.wcaquino.me/users")
		.then()
			.statusCode(200)
			.body("$", Matchers.hasSize(3))
			.body("age.findAll{it <= 25}.size()", Matchers.is(2))//objeto it faz a istancia da idade atual pecorrendo todas as idades verificando-as
			.body("age.findAll{it <= 25 && it > 20}.size()", Matchers.is(1))
			.body("findAll{it.age <= 25 && it.age > 20}.name()", Matchers.hasItem("Maria Joaquina"))//findAll busca todos que forem entcontrados com essas caracteristicas
			.body("findAll{it.age <= 25}[0].name()", Matchers.is("Maria Joaquina"))//transforma a lista em um objeto
			.body("findAll{it.age <= 25}[-1].name()", Matchers.is("Ana Júlia"))//pega o utimo registro
			.body("find{it.age <= 25}.name()", Matchers.is("Maria Joaquina"))//apesar de ter mas de um intem find so retorna o que foi buscado
			.body("findAll{it.name.contains('n')}.name", Matchers.hasItems("Maria Joaquina","Ana Júlia"))//busca todos que contem n no nome
			.body("findAll{it.name.length() > 10}.name", Matchers.hasItems("Joao da Silva","Maria Joaquina"))//busca todos que tem o nome maior que 10
			.body("nome.collect{it.toUpperCase()}", Matchers.hasItem("MARIA JOAQUINA"))//passa o nome para UpeerCaase
			.body("nome.findAll{it.startsWith('Maria')}.collect{it.toUpperCase()}", Matchers.hasItem("MARIA JOAQUINA"))//antes de passar para maiusculo procure todos os nome que comece com maria
			//.body("nome.findAll{it.startsWith('Maria')}.collect{it.toUpperCase()}.toArray()", Matchers.allOf(Matchers.arrayContaining("MARIA JOAQUINA"),Matchers.arrayWithSize(1)))//antes de passar para maiusculo procure todos os nome que comece com maria
			.body("age.collect{it * 2}", Matchers.hasItems(60,50,40))//mutltiplica todos as idades por 2
			.body("id.max", Matchers.is(3))//verifica qual e o maior id
			.body("salary.min()", Matchers.is(1234.5678f))//verifica qual menor salario
			.body("salary.findAll{it != null}.sum()", Matchers.is(Matchers.closeTo(3734.5678f,0.001)))//soma todos os salarios
			//.body("salary.findAll{it != null}.sum()", Matchers.allOf(Matchers.greaterThan(3000d), Matchers.lessThan(5000)))//maior ou menor
			;
	}
	public void unirJsonPathComJava() {
		ArrayList<String> names = 
		RestAssured.given()
		.when()
			.get("http://restapi.wcaquino.me/users")
		.then()
			.statusCode(200)
			.extract().path("nome.findAll{it.startsWith('Maria')}")
			;
		Assert.assertEquals(1, names.size());
		Assert.assertTrue(names.get(0).equalsIgnoreCase("mAria joaquiNa"));
		Assert.assertEquals(names.get(0).toUpperCase(), "maria joaquina".toUpperCase());
	}
}
