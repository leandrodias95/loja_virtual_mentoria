CREATE TABLE tabela_acesso_end_point(
nome_end_point CHARACTER VARYING,
qtd_acesso_end_point integer
);

INSERT INTO tabela_acesso_end_point(nome_end_point, qtd_acesso_end_point) VALUES ('END-POINT-NOME-PESSOA-FISICA', 0);

ALTER TABLE tabela_acesso_end_point ADD CONSTRAINT nome_end_point_unique UNIQUE(nome_end_point); 
