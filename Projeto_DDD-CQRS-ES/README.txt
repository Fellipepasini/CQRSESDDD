Esta aplica��o foi constru�da usando o padr�o de projeto CQRS (Command Query Responsability Segregation) e da t�cnica ES (Event Sourcing).
Al�m disso utilizamos da metodologia DDD (Domain Driven Design) para tratar das quest�es relacionadas ao dom�nio do sistema.
Em rela��o a este dom�nio, a aplica��o tem por finalidade ser um aplicativo de m�sicas, aonde o usu�rio poder� gerenciar suas playlists,
selecionar m�sicas favoritas, escutar m�sicas, etc.

Em rela��o ao armazenamento das informa��es, utilizamos um banco de dados MySQL com dois SCHEMAS diferentes. Um para a base de escrita
e outro para a base de leitura. Para o correto funcionamento da aplica��o, al�m de arrumar as configura��es de acesso (url, usuario, senha) 
da base de dados na classe "projeto.tcc.infraestrutura.Conexao", s�o necess�rios rodas os seguinte scripts nesta base MySQL
:

------------Scripts para a base de escrita-----------------------


CREATE DATABASE `eventsource` /*!40100 DEFAULT CHARACTER SET latin1 */;


CREATE TABLE eventsource.aggregates (
  `aggregate_id` varchar(40) DEFAULT NULL,
  `type` varchar(45) DEFAULT NULL,
  `version` bigint(20) DEFAULT NULL,
  KEY `aggregate_id_idx` (`aggregate_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE eventsource.eventstore (
  `ideventstore` int(11) NOT NULL AUTO_INCREMENT,
  `aggregate_id` varchar(40) DEFAULT NULL,
  `events` blob NOT NULL,
  `version` bigint(20) DEFAULT NULL,
  `groupVersion` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ideventstore`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8;


-------------------------------------------------------------------------------------------

-----------------------Scripts para a base de leitura----------------

CREATE DATABASE `baseleitura` /*!40100 DEFAULT CHARACTER SET utf8 */;

CREATE TABLE baseleitura.dadosusuario (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `login` varchar(20) NOT NULL,
  `senha` varchar(100) NOT NULL,
  `nome` varchar(50) NOT NULL,
  `CPF` varchar(11) NOT NULL,
  `email` varchar(40) NOT NULL,
  `dataNascimento` date DEFAULT NULL,
  `sexo` varchar(1) DEFAULT NULL,
  `aggregateID` varchar(40) DEFAULT NULL,
  `dataUltimoLogin` date DEFAULT NULL,
  `cdPerfil` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

CREATE TABLE baseleitura.musicasusuario (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nome` varchar(45) NOT NULL,
  `duracao` varchar(45) DEFAULT NULL,
  `aggregateId` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

CREATE TABLE baseleitura.playlistsusuario (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `aggregateIDUsuario` varchar(45) NOT NULL,
  `aggregateIDPlayList` varchar(45) NOT NULL,
  `nome` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

