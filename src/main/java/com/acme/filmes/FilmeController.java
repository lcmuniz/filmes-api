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
 * Classe responsável por responder as requisições do usuário
 * A anotação @RestController é usada para definir que esta
 * classe retorna dados e não páginas HTML.
 */
@RestController
public class FilmeController {

	// filmeRepository é usada para acessar o banco de dados
	// A anotação @Autowired diz ao Spring Boot que ele deve criar
	// esta variável automaticamente.
	@Autowired
	private FilmeRepository filmeRepository;

	// método que atende as requições GET em http://localhost:8080/filmes
	// retorna todos os filmes
	@GetMapping("/filmes")
	public List<Filme> todos() {
		return filmeRepository.findAll(); // retorna todos os filmes
	}

	// método que atende as requições GET em http://localhost:8080/filmes/{id}
	// exemplo: http://localhost:8080/filmes/1
	// retorna o filme de id igual ao {id} passado como argumento.
	@GetMapping("/filmes/{id}")
	public Optional<Filme> get(@PathVariable Integer id) {
		return filmeRepository.findById(id);  // encontra o filme de id = {id}
	}

	// método que atende as requições POST em http://localhost:8080/filmes
	// salva um filme no banco.
	// os dados do filme a ser salvo são passados no corpo (body) da requisição.
	// retorna o filme inserido no banco com o id preenchido.
	@PostMapping("/filmes")
	public Filme inserir(@RequestBody Filme filme) {
		// se o id do filme for passado, retorna um erro.
		// o usuário não deve informar o id pois ele é gerado pelo banco de dados
		if (filme.getId() != null) { throw new RuntimeException("Não informe o ID");}
		return filmeRepository.save(filme); // salva o filme
	}

	// método que atende as requições PUT em http://localhost:8080/filmes/{id}
	// exemplo: http://localhost:8080/filmes/1
	// altera o filme de id = {id} no banco.
	// os dados do filme a ser alterado são passados no corpo (body) da requisição.
	// retorna o filme alterado no banco de dados.
	@PutMapping("/filmes/{id}")
	public Filme atualizar(@PathVariable Integer id, @RequestBody Filme filme) {
		// procura o filme no banco de dados
		Optional<Filme> filmeOptional = filmeRepository.findById(id);
		// se o filme não foi encontrado, apresenta uma mensagem de erro.
		if (filmeOptional.isEmpty()) { throw new RuntimeException("Filme com ID " + id + " não encontrado"); }
		// se o id do filme for passado, retorna um erro.
		// o usuário não deve informar o id pois ele é gerado pelo banco de dados
		if (filme.getId() != null) { throw new RuntimeException("Não informe o ID no corpo da requisição"); }

		Filme f = filmeOptional.get();  // obtém o filme pesquisado

		// altera apenas os atributos passados no corpo (body) da requisição
		// atributos não passdos não são alterados
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

	// método que atende as requições DELETE em http://localhost:8080/filmes/{id}
	// exemplo: http://localhost:8080/filmes/1
	// exclui o filme de id = {id} no banco.
	@DeleteMapping("/filmes/{id}")
	public void excluir(@PathVariable Integer id) {
		filmeRepository.deleteById(id); // exclui o filme de id = {id}
	}

	// método que atende as requições GET em http://localhost:8080/filmes/filtrar
	// os parâmetros da pesquisa são passados como variáveis da requisição;
	// exemplo: http://localhost:8080/filmes/filtrar?estrelas=4&ano=2008
	@GetMapping("/filmes/filtrar")
	public List<Filme> filtrar(@RequestParam(defaultValue = "") String texto, @RequestParam(defaultValue = "") Integer estrelas, @RequestParam(defaultValue = "") Integer ano) {

		// temTexto é true se o texto foi passado, false caso contrário
		boolean temTexto = texto != null && !texto.trim().equals("");
		// temEstrelas é true se o estrelas foi passado, false caso contrário
		boolean temEstrelas = estrelas != null;
		// temAno é true se o ano foi passado, false caso contrário
		boolean temAno = ano != null;

		if (temTexto && temEstrelas && temAno) {
			// se foram passados texto, estrelas e ano
			// chama o método apropriado
			return filmeRepository.filtrarTextoEstrelasAno("%"+texto.toUpperCase()+"%", estrelas, ano);
		}
		if (temTexto && temEstrelas) {
			// se foram passados texto e estrelas, mas não ano
			// chama o método apropriado
			return filmeRepository.filtrarTextoEstrelas("%"+texto.toUpperCase()+"%", estrelas);
		}
		if (temTexto && temAno) {
			// se foram passados texto e ano, mas não estrelas
			// chama o método apropriado
			return filmeRepository.filtrarTextoAno("%"+texto.toUpperCase()+"%", ano);
		}
		if (temEstrelas && temAno) {
			// se foram passados estrelas e ano, mas não texto
			// chama o método apropriado
			return filmeRepository.filtrarEstrelasAno(estrelas, ano);
		}
		if (temTexto) {
			// se foi passado apenas texto,
			// chama o método apropriado
			return filmeRepository.filtrarTexto("%"+texto.toUpperCase()+"%");
		}
		if (temEstrelas) {
			// se foi passado apenas estrelas,
			// chama o método apropriado
			return filmeRepository.filtrarEstrelas(estrelas);
		}
		// se foi passado apenas ano,
		// chama o método apropriado
		return filmeRepository.filtrarAno(ano);
	}

}
