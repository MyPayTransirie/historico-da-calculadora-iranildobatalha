# Exercicio 2 - Calculadora com Histórico

Esta tarefa foi pensada para exercitar a passagem de informação entre `Activities` diferentes e exercitar a programação em Kotlin.

Melhore a calculadora do exercício 1 acrescentando um botão para mostrar o histórico de cálculos em uma segunda tela (`Activity`). Utilizaremos diversas formas de passar a informação para fins de treino do uso da linguagem Kotlin.

Indique quais partes completou e descreva as dificuldades que teve.

## Etapas

Abaixo segue alguns passos sugeridos para criar a aplicação:

1. Acrescente o botão para abrir mostrar o histórico
2. Crie uma segunda `Activity` com um `TextView` de id `txtHistorico`
3. Associe o listener para que, ao apertar o botão, a segunda `Activity` seja iniciada
4. Sempre que "=" for pressionado, salve o resultado da operação em uma string com ";". Ex. "10+10=20;5*5=25"
5. Envie a string para a segunda tela utilizando `putExtra('historico', string...)`
6. No `onCreate` da tela 2, obtenha o histórico e "decodifique", separando as expressões e exibindo uma por linha no `TextView`
7. Faça o mesmo do passo 5 e 6, porém armazene no formato JSON, ex. {"expr": "10+10", "result": "20"}
8. Agora não mais utilize uma única string, mas sim um `Array<String>`
9. Implemente uma nova classe "Historico" contendo o histórico de expressões como você achar melhor
10. Por fim, envie um **objeto** Histórico para a próxima tela utilize o padrão de projeto _Serialization Proxy Pattern_ com a interface `Parcelable` para encapsular ele.

## Orientações

- Comente o código desenvolvido explicando o que fez em cada passo
- Caso não tenha dificuldades, procure incrementar a calculadora com mais operações, botão de apagar, entre outros

## Status

| Passo | Descrição  | Completou? |
| ----- | ---------- | ---------  |
|   1  | Botão Histórico                       | sim |
|   2  | Tela Histórico                        | sim |
|   3  | Listener iniciar tela                 | sim |
|   4  | Salvar histórico string ";"           | sim |
|   5  | Enviar histórico com string           | sim |
|   6  | Exibir histórico na tela 2            | sim |
|   7  | Enviar histórico como JSON            | sim |
|   8  | Enviar histórico como `Array<String>` | sim |
|   9  | Salvar histórico como `Historico`     | sim |
|   10 | Enviar histórico com `Parcelable`    | sim |

----------
## Dificuldades

Explique de forma resumida as dificuldades que teve e como resolveu.

> Escreva: "não tive dificuldades" se for o caso e apague o resto

1. **Enviar meu projeto para o github da turma**
   - Tive muita dificuldade em enviar meu código para o github
   - Conversei então com um colega que me ajudou e me mostrou onde eu estava errando nos comandos git.
2. **Dificuldade em utilizar o JSON**
   - Não conseguia encontrar em sites exemplos usando JSON em Kotlin e os que apareciam em Java eram meio complicados
   - Apelei para a documentação do Kotlin que é bem densa, mas para a minha surpresa consegui entender como utilizar e consegui fazer a atividade com o auxilio de um video da internet.
3. **Utilizar o Parcelable**
   - Minha ideia para o Parcelable era criar uma classe com um atributo List<String> em que eu podia ir adicionando as expressoes e no final enviar a classe como um pacote.
   - No entanto não funcionava isso, eu não queria ter que passar uma variável no construtor mas só funcionou meu código assim.
   - Para utilizar uma lista no Parcelable apelei para o StackOverflow, no entanto as soluções que tinham usavam construtor, então também resolvi usar.
   - No fim consegui realizar a atividade, mas seria bom se tivesse uma explicação um pouco mais detalhada do Parcelable e como utilizá-o.
