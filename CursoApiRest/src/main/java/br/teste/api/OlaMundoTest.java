package br.teste.api;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.startsWith;
import java.util.Arrays;
import java.util.List;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

public class OlaMundoTest {

	@Test
	public void testOlaMundo() {
		Response response = RestAssured.request(Method.GET, "http://restapi.wcaquino.me/ola");
		Assert.assertTrue(response.getBody().asString().equals("Ola Mundo!"));
		Assert.assertTrue(response.statusCode() == 200);// código do status que serar retornado
		Assert.assertTrue("O status deveria ser 200", response.getStatusCode() == 200);
		Assert.assertEquals(200, response.statusCode());// primerio parametro e o esperado e o segundo e o atual o que
														// esta sendo pego da aplicação

		ValidatableResponse validacao = response.then();
		validacao.statusCode(200);
	}

	@Test
	public void conhecendoOutraFormaRestAssured() {
		Response response = RestAssured.request(Method.GET, "http://restapi.wcaquino.me/ola");// fazendo a requesição
		ValidatableResponse validacao = response.then();// o then pega a validação que retorna um ValidatableResponse
		validacao.statusCode(200);

		/*
		 * forma resumida de fazer o mesmo das linhas a cima e ainda da pra fazer o
		 * import statico do RestAssured resumindo ainda mas
		 */
		RestAssured.get("http://restapi.wcaquino.me/ola").then().statusCode(200);
		/*
		 * dado quando então Given = condição que queira ser aplicada, when = a ação de
		 * fato Ex: quando for feito a requisição, Them = seria as assertivas
		 */
		RestAssured.given()// pre condicoes
				.when()// get, post, put
				.get("http://restapi.wcaquino.me/ola").then()// verificacões Assertivas
				.statusCode(200);
	}

	@Test
	public void conhecendoMatchersHamcrest() {
		/* caso queria da pra inportar o Matchers de forma statica */
		Assert.assertThat("joao", Matchers.is("joao"));// aqui o atual e o primeiro
		Assert.assertThat(123, Matchers.is(123));
		Assert.assertThat(123, Matchers.isA(Integer.class));
		Assert.assertThat(123d, Matchers.isA(Double.class));
		Assert.assertThat(123d, Matchers.greaterThan(120d));// verifica se 123 e maior que 120
		Assert.assertThat(123d, Matchers.lessThan(125d));// verifica se 123 e menor que 125

		List<Integer> impares = Arrays.asList(1, 3, 5, 7, 9);
		Assert.assertThat(impares, Matchers.hasSize(5));// verifica o tamanho da lista
		Assert.assertThat(impares, Matchers.contains(1, 3, 5, 7, 9));// verifica os itens da lista e suas respequitavas
																		// ordem
		Assert.assertThat(impares, Matchers.containsInAnyOrder(1, 3, 5, 9, 7));// verifica os itens da lista
																				// independente da ordem
		Assert.assertThat(impares, Matchers.hasItem(1));// verifica se esse elemento esta presente na lista
		Assert.assertThat(impares, Matchers.hasItems(1, 5));// verifica se os dois elementos estão presente na lista

		Assert.assertThat("Maria", Matchers.is(not("joao")));
		Assert.assertThat("Maria", Matchers.anyOf(is("joao"), is("Maria")));// o valor pode ser maria ou joao
		Assert.assertThat("joaoBatista", Matchers.allOf(startsWith("joa"), endsWith("sta"), containsString("Bati")));
	}

	public void deveValidarBody() {
		RestAssured.given().when().get("http://restapi.wcaquino.me/ola").then().statusCode(200)
				.body(Matchers.is("Ola Mundo!"))// forma restrita
		// .body(containsString(("Mundo"))//restrição media
		;
		/* body(Matchers.is(not(nullValue()))) verifica se o corpo veio vazio */
	}
}
