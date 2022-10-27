package com.acme.filmes;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/*
 * A interface FilmeRepository é usada para acessar o banco de dados.
 * Por extender JpaRepository, a interface FilmeRepostory possui
 * métodos para ler, gravar e excluir dados elacionados a entidade Filme.
 * Alguns métodos herdados de JpaRepository:
 *  - findAll(): retorna todos os filmes
 *  - save(): salva um filme
 *  - deleteById(): deleta um filme pelo id
 */
public interface FilmeRepository extends JpaRepository<Filme, Integer>{

	// método para selecionar do banco os filmes filtrados pelo texto, número de
	// estrelas e ano do filme, passados como parãmetros
	@Query("select f from Filme f where ((upper(f.titulo) like :texto) or (upper(f.tituloOriginal) like :texto) or (upper(f.direcao) like :texto) or (upper(f.roteiro) like :texto) or (upper(f.elenco) like :texto) or (upper(f.sinopse) like :texto)) and (f.estrelas = :estrelas) and (f.ano = :ano)")
	List<Filme> filtrarTextoEstrelasAno(@Param("texto") String texto, @Param("estrelas") Integer estrelas, @Param("ano") Integer ano);

	// método para selecionar do banco os filmes filtrados pelo texto e número de
	// estrelas, passados como parãmetros
	@Query("select f from Filme f where ((upper(f.titulo) like :texto) or (upper(f.tituloOriginal) like :texto) or (upper(f.direcao) like :texto) or (upper(f.roteiro) like :texto) or (upper(f.elenco) like :texto) or (upper(f.sinopse) like :texto)) and (f.estrelas = :estrelas)")
	List<Filme> filtrarTextoEstrelas(@Param("texto") String texto, @Param("estrelas") Integer estrelas);

	// método para selecionar do banco os filmes filtrados pelo texto e ano do
	// filme, passados como parãmetros
	@Query("select f from Filme f where ((upper(f.titulo) like :texto) or (upper(f.tituloOriginal) like :texto) or (upper(f.direcao) like :texto) or (upper(f.roteiro) like :texto) or (upper(f.elenco) like :texto) or (upper(f.sinopse) like :texto)) and (f.ano = :ano)")
	List<Filme> filtrarTextoAno(@Param("texto") String texto, @Param("ano") Integer ano);

	// método para selecionar do banco os filmes filtrados pelo número de
	// estrelas e ano do filme, passados como parãmetros
	@Query("select f from Filme f where (f.estrelas = :estrelas) and (f.ano = :ano)")
	List<Filme> filtrarEstrelasAno(@Param("estrelas") Integer estrelas, @Param("ano") Integer ano);

	// método para selecionar do banco os filmes filtrados pelo texto,
	// passado como parãmetro
	@Query("select f from Filme f where(upper(f.titulo) like :texto) or (upper(f.tituloOriginal) like :texto) or (upper(f.direcao) like :texto) or (upper(f.roteiro) like :texto) or (upper(f.elenco) like :texto) or (upper(f.sinopse) like :texto)")
	List<Filme> filtrarTexto(@Param("texto") String texto);

	// método para selecionar do banco os filmes filtrados pelo número de estrelas,
	// passado como parãmetro
	@Query("select f from Filme f where f.estrelas = :estrelas")
	List<Filme> filtrarEstrelas(@Param("estrelas") Integer estrelas);

	// método para selecionar do banco os filmes filtrados pelo ano do filme,
	// passado como parãmetro
	@Query("select f from Filme f where f.ano = :ano")
	List<Filme> filtrarAno(@Param("ano") Integer ano);

}
