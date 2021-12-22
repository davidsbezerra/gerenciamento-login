A aplicação deverá manter as propriedades do banco de dados e fazer a criação de um DataSource customizado. Ex.: test.ia.datasource.url. 
A aplicação deve ser um Rest WebService que terá um endpoint de login, a autenticação deve ser realizada com Spring Security e a sessão do usuário autenticado deverá ser mantida em uma estrutura de cache externa Redis.
Deverá ser implementada uma outra aplicação que tratará mensagens assíncronas através de um serviço externo de mensageria (RabbitMQ).

Funcionalidades: 

  O WebService deverá conter uma API para cadastro de usuários e um endpoint para enviar uma mensagem de e-mail que será processada pelo módulo assíncrono. 
  1) O CRUD de users deverá conter todas as operações básicas de CRUD e o usuário deverá ter o seguinte modelo: 
    Id 
    name: <obrigatorio> 
    login: <obrigatorio> 
    password: <obrigatorio> 
    createdDate: <obrigatorio> 
    updatedDate 
    e-mail : <obrigatorio> 
    admin (Flag): <obrigatorio> 
    Obs.: Somente usuário admin pode atualizar senhas de outros usuários, ou o próprio usuário.

  2) O segundo endpoint “/email” deverá receber uma mensagem que será direcionada para o RabbitMQ, o módulo assíncrono consumirá a fila de mensagens e poderá enviar a mensagem para os seguintes casos: 
    Todos os usuários Administradores 
    Usuário específico que será recebido com parâmetro na requisição 
  
Requisitos:
  As configurações de conexão com o banco de dados, construção do DataSource, EntityManager NÃO devem utilizar a configuração automática do Spring Boot. 
  A sessão do usuário não poderá estar contida no contexto da aplicação, deve ser armazenada em um serviço externo como solicitado. 
  
Bônus 
  Desenvolver uma versão WebService SOAP para a funcionalidade do envio de e-mail. Documentação da API REST. 
  Construir um container Docker para cada módulo. 
  Utilização de Profiles para configuração de ambientes da aplicação (DEV e Production). 
  
Considerações finais 
  O front end é opcional para a análise do projeto. 
  O projeto deve ser publicado no GitHub em formato privado e enviado o código para análise. 
  Documentação para a execução do projeto deverá ser disponibilizado no README com pontos que o candidato ache importante a serem considerados.
