package com.acme.filmes;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;

/*
 * A classe Filme representa um filme.
 * A anotação @Data é usada para criar automaticamente os métodos get e set
 * para cara atributo da classe.
 * A anotação @Entity é usada para dizer ao Spring Boot que esta classe
 * representa uma tabela no banco de dados.
 */
@Data
@Entity
public class Filme {

	@Id // o atributo id abaixo é a chave-primária da tabela
	@GeneratedValue  // o valor do atributo id é gerado automaticamente pelo banco de dados
	private Integer id;

	private String titulo;
	private String tituloOriginal;
	private String direcao;
	private String roteiro;
	private String elenco;
	private String sinopse;
	private String posterUrl;
	private Integer estrelas;
	private Integer ano;
}
