Esta aplica��o foi constru�da usando o padr�o de projeto CQRS (Command Query Responsability Segregation) e da t�cnica ES (Event Sourcing).
Al�m disso utilizamos da metodologia DDD (Domain Driven Design) para tratar das quest�es relacionadas ao dom�nio do sistema.
Em rela��o a este dom�nio, a aplica��o tem por finalidade ser um aplicativo de m�sicas, aonde o usu�rio poder� gerenciar suas playlists,
selecionar m�sicas favoritas, escutar m�sicas, etc.

Em rela��o ao armazenamento das informa��es, utilizamos um banco de dados MySQL com dois SCHEMAS diferentes. Um para a base de escrita
e outro para a base de leitura. Para o correto funcionamento da aplica��o s�o necess�rioas rodas os seguinte scripts numa base de dados MySQL
:

Scripts para a base de escrita:



Scriptas para a base de leitura:

