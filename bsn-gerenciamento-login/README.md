# bsn-gerenciamento-login

> Aplicação responsável por fornecer operações que são comuns à diversos contextos de negócio.

## Referências

Projetos que dependem dos serviços desta API podem ser vistos na página [Stakeholders](https://github.com/davidsbezerra/gerenciamento-login/wikis/Stakeholders).
MRs neste projeto devem passar por aprovação de pelo menos um membro de cada equipe.

## Documentação


### Listeners

#### - Inserir Login
_Serviço para nserir de dados dde login._

* Rota: `gerenciamento-login.inserir.message` _(request)_
* Mensagem: _InserirLoginMessage_

```json
{
    "login": {
        "login": "String",
        "password": "String"
    }
}
```

* Resposta: _InserirLoginResponse_

```json
{ 
  "login": {
      "name": "String",
      "login": "String",
      "password": "String",
      "createdDate": "OffsetDateTime",
      "updatedDate": "OffsetDateTime",
      "email": "String",
      "admin": "Boolean"
  }
}
```

### Diagrama(s)

*  Diagrama do fluxo X: `POST /x`

![convite](Diagramas/post-x.png "Fluxo de X.")

## Execução

### Maven

Executando via Maven no ambiente local:

```sh
$ mvn clean package spring-boot:run
```

### Docker

Executando via Docker no ambiente local:

```sh
$ mvn clean package
$ cd target
$ docker build -t bsn-gerenciamento-login .
$ docker run -d -p 8082:8082 --name bsn-gerenciamento-login bsn-gerenciamento-login
```


