# bsn-gerenciamento-login

> Aplicação responsável por fornecer operações que são comuns à diversos contextos de negócio.

## Referências

Projetos que dependem dos serviços desta API podem ser vistos na página [Stakeholders](https://github.com/davidsbezerra/gerenciamento-login/wikis/Stakeholders).
MRs neste projeto devem passar por aprovação de pelo menos um membro de cada equipe.

## Documentação


### Listeners

#### - Inserir Login
_Serviço para inserir dados de login._

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

* Rota de sucesso: `gerenciamento-login.inserir.success.event`
* Rota de erro: `gerenciamento-login.inserir.error.event`
* Evento: `InserirLoginAmqpEvent`
* Resposta: _InserirLoginResponse_

```json
{
    "requisicao": InserirLoginAmqpEvent,
    "erro": ErrorResponse,
    "resultado": {
        "login": {
            "Id": "Long",
            "name": "String",
            "login": "String",
            "password": "String",
            "createdDate": "OffsetDateTime",
            "updatedDate": "OffsetDateTime",
            "email": "String",
            "admin": "Boolean"
        }
    }
}
```

#### - Atualizar Login
_Serviço para atualizar dados de login._

* Rota: `gerenciamento-login.atualizar.message` _(request)_
* Mensagem: _AtualizarLoginMessage_

```json
{
    "login": {
        "id": "Long",
        "login": "String",
        "password": "String"
    }
}
```

* Rota de sucesso: `gerenciamento-login.atualizar.success.event`
* Rota de erro: `gerenciamento-login.atualizar.error.event`
* Evento: `InserirLoginAmqpEvent`
* Resposta: _AtualizarLoginResponse_

```json
{
    "requisicao": AtualizarLoginAmqpEvent,
    "erro": ErrorResponse,
    "resultado": {
        "login": {
            "id": "Long",
            "name": "String",
            "login": "String",
            "password": "String",
            "createdDate": "OffsetDateTime",
            "updatedDate": "OffsetDateTime",
            "email": "String",
            "admin": "Boolean"
        }
    }
}
```

#### - Remover Login
_Serviço para remover de dados de login._

* Rota: `gerenciamento-login.remover.message` _(request)_
* Mensagem: _RemoverLoginMessage_

```json
{
    "id" : "Long"
}
```

*
* Rota de sucesso: `gerenciamento-login.remover.success.event`
* Rota de erro: `gerenciamento-login.remover.error.event`
* Evento: `RemoverLoginAmqpEvent`
* Resposta: _RemoverLoginResponse_

```json
{
    "requisicao": RemoverLoginAmqpEvent,
    "erro": ErrorResponse,
    "resultado": {
        "sucesso": boolean
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


