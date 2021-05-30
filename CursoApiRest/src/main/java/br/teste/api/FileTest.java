package br.teste.api;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;


public class FileTest {
	@Test
	public void deveObrigarEnviodoArquivo() {
		given()
			.log().all()
		.when()
			.post("http://restapi.wcaquino.me/upload")
		.then()
			.log().all()
			.statusCode(404)//deveria ser 400
			.body("error", Matchers.is("Arquivo não enviado"))
		;
	}
	
	@Test
	public void deveFazerUploadArquivo() {
		given()
			.log().all()//o caminho do arquivo não foi definido 
			.multiPart("arquivo", new File(""))//primeira string e o nome do parametro que sera enviado segunda e o caminho do arquivo 
		.when()
			.post("http://restapi.wcaquino.me/upload")
		.then()
			.log().all()
			.statusCode(200)
			.body("nome", is("users.pdf"))
		;
	}
	
	@Test
	public void naoDeveFazerUploadArquivoGrande() {
		given()
			.log().all()//o caminho do arquivo não foi definido 
			.multiPart("arquivo", new File(""))//primeira string e o nome do parametro que sera enviado segunda e o caminho do arquivo 
		.when()
			.post("http://restapi.wcaquino.me/upload")
		.then()
			.log().all()
			.time(lessThan(5000l))//determina o tempo limie de espera para o upload de um arquivo
			.statusCode(413)
		;
	}
	
	@Test
	public void deveBaixarArquivo() throws IOException {
		byte[] image = given()
			.log().all()
		.when()
			.get("http://restapi.wcaquino.me/upload")
		.then()
			.statusCode(200)
			.extract().asByteArray();
		;
		File imagen = new File("src/main/resources/file.jpg");//caminho em que a imagem sera salva
		OutputStream out = new FileOutputStream(imagen);
		out.write(image);
		out.close();
		
		Assert.assertThat(imagen.length(), lessThan(100000L));
	}
	
	
}















