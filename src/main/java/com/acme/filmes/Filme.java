package com.acme.filmes;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;

/*
 * A classe Filme representa um filme.
 * A anota��o @Data � usada para criar automaticamente os m�todos get e set
 * para cara atributo da classe.
 * A anota��o @Entity � usada para dizer ao Spring Boot que esta classe
 * representa uma tabela no banco de dados.
 */
@Data
@Entity
public class Filme {

	@Id // o atributo id abaixo � a chave-prim�ria da tabela
	@GeneratedValue  // o valor do atributo id � gerado automaticamente pelo banco de dados
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
