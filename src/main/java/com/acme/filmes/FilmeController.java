package com.acme.filmes;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/*
 * Classe respons�vel por responder as requisi��es do usu�rio
 * A anota��o @RestController � usada para definir que esta
 * classe retorna dados e n�o p�ginas HTML.
 */
@RestController
public class FilmeController {

	// filmeRepository � usada para acessar o banco de dados
	// A anota��o @Autowired diz ao Spring Boot que ele deve criar
	// esta vari�vel automaticamente.
	@Autowired
	private FilmeRepository filmeRepository;

	// m�todo que atende as requi��es GET em http://localhost:8080/filmes
	// retorna todos os filmes
	@GetMapping("/filmes")
	public List<Filme> todos() {
		return filmeRepository.findAll(); // retorna todos os filmes
	}

	// m�todo que atende as requi��es GET em http://localhost:8080/filmes/{id}
	// exemplo: http://localhost:8080/filmes/1
	// retorna o filme de id igual ao {id} passado como argumento.
	@GetMapping("/filmes/{id}")
	public Optional<Filme> get(@PathVariable Integer id) {
		return filmeRepository.findById(id);  // encontra o filme de id = {id}
	}

	// m�todo que atende as requi��es POST em http://localhost:8080/filmes
	// salva um filme no banco.
	// os dados do filme a ser salvo s�o passados no corpo (body) da requisi��o.
	// retorna o filme inserido no banco com o id preenchido.
	@PostMapping("/filmes")
	public Filme inserir(@RequestBody Filme filme) {
		// se o id do filme for passado, retorna um erro.
		// o usu�rio n�o deve informar o id pois ele � gerado pelo banco de dados
		if (filme.getId() != null) { throw new RuntimeException("N�o informe o ID");}
		return filmeRepository.save(filme); // salva o filme
	}

	// m�todo que atende as requi��es PUT em http://localhost:8080/filmes/{id}
	// exemplo: http://localhost:8080/filmes/1
	// altera o filme de id = {id} no banco.
	// os dados do filme a ser alterado s�o passados no corpo (body) da requisi��o.
	// retorna o filme alterado no banco de dados.
	@PutMapping("/filmes/{id}")
	public Filme atualizar(@PathVariable Integer id, @RequestBody Filme filme) {
		// procura o filme no banco de dados
		Optional<Filme> filmeOptional = filmeRepository.findById(id);
		// se o filme n�o foi encontrado, apresenta uma mensagem de erro.
		if (filmeOptional.isEmpty()) { throw new RuntimeException("Filme com ID " + id + " n�o encontrado"); }
		// se o id do filme for passado, retorna um erro.
		// o usu�rio n�o deve informar o id pois ele � gerado pelo banco de dados
		if (filme.getId() != null) { throw new RuntimeException("N�o informe o ID no corpo da requisi��o"); }

		Filme f = filmeOptional.get();  // obt�m o filme pesquisado

		// altera apenas os atributos passados no corpo (body) da requisi��o
		// atributos n�o passdos n�o s�o alterados
		if (filme.getTitulo() != null) f.setTitulo(filme.getTitulo());
		if (filme.getTituloOriginal() != null) f.setTituloOriginal(filme.getTituloOriginal());
		if (filme.getDirecao() != null) f.setDirecao(filme.getDirecao());
		if (filme.getRoteiro() != null) f.setRoteiro(filme.getRoteiro());
		if (filme.getElenco() != null) f.setElenco(filme.getElenco());
		if (filme.getSinopse() != null) f.setSinopse(filme.getSinopse());
		if (filme.getPosterUrl() != null) f.setPosterUrl(filme.getPosterUrl());
		if (filme.getEstrelas() != null) f.setEstrelas(filme.getEstrelas());
		if (filme.getAno() != null) f.setAno(filme.getAno());
		f.setId(id); // seta o id do filme
		return filmeRepository.save(f); // salva o filme
	}

	// m�todo que atende as requi��es DELETE em http://localhost:8080/filmes/{id}
	// exemplo: http://localhost:8080/filmes/1
	// exclui o filme de id = {id} no banco.
	@DeleteMapping("/filmes/{id}")
	public void excluir(@PathVariable Integer id) {
		filmeRepository.deleteById(id); // exclui o filme de id = {id}
	}

	// m�todo que atende as requi��es GET em http://localhost:8080/filmes/filtrar
	// os par�metros da pesquisa s�o passados como vari�veis da requisi��o;
	// exemplo: http://localhost:8080/filmes/filtrar?estrelas=4&ano=2008
	@GetMapping("/filmes/filtrar")
	public List<Filme> filtrar(@RequestParam(defaultValue = "") String texto, @RequestParam(defaultValue = "") Integer estrelas, @RequestParam(defaultValue = "") Integer ano) {

		// temTexto � true se o texto foi passado, false caso contr�rio
		boolean temTexto = texto != null && !texto.trim().equals("");
		// temEstrelas � true se o estrelas foi passado, false caso contr�rio
		boolean temEstrelas = estrelas != null;
		// temAno � true se o ano foi passado, false caso contr�rio
		boolean temAno = ano != null;

		if (temTexto && temEstrelas && temAno) {
			// se foram passados texto, estrelas e ano
			// chama o m�todo apropriado
			return filmeRepository.filtrarTextoEstrelasAno("%"+texto.toUpperCase()+"%", estrelas, ano);
		}
		if (temTexto && temEstrelas) {
			// se foram passados texto e estrelas, mas n�o ano
			// chama o m�todo apropriado
			return filmeRepository.filtrarTextoEstrelas("%"+texto.toUpperCase()+"%", estrelas);
		}
		if (temTexto && temAno) {
			// se foram passados texto e ano, mas n�o estrelas
			// chama o m�todo apropriado
			return filmeRepository.filtrarTextoAno("%"+texto.toUpperCase()+"%", ano);
		}
		if (temEstrelas && temAno) {
			// se foram passados estrelas e ano, mas n�o texto
			// chama o m�todo apropriado
			return filmeRepository.filtrarEstrelasAno(estrelas, ano);
		}
		if (temTexto) {
			// se foi passado apenas texto,
			// chama o m�todo apropriado
			return filmeRepository.filtrarTexto("%"+texto.toUpperCase()+"%");
		}
		if (temEstrelas) {
			// se foi passado apenas estrelas,
			// chama o m�todo apropriado
			return filmeRepository.filtrarEstrelas(estrelas);
		}
		// se foi passado apenas ano,
		// chama o m�todo apropriado
		return filmeRepository.filtrarAno(ano);
	}

}
