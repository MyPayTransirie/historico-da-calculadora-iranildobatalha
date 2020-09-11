package com.example.calculadora

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    var expressao: String = "0" // Expressão atual
    val historico = arrayListOf<String>() //Lista de expressões calculadas
    val json = JSONObject("{ 'historico': [] }") //Variável que empacota como JSON
    var arrayHistorico: Array<String> = arrayOf() // Array<String> que guarda o histórico

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Limpa a expressão atual na tela e na variável
        buttonLimpar.setOnClickListener {
            expressao = "0"
            textView.text = expressao
        }

        // Calcula a expressão e salva no histórico
        buttonResult.setOnClickListener {
            if(expressao.last().isDigit()){
                textView.text = eval(expressao).toString() //Recebendo o resultado da expressão
                val resposta = "$expressao = ${textView.text}" //Colocando a resposta em uma variável

                historico.add(resposta) // Salvando a reposta num ArrayList<String>

                json.accumulate("historico", resposta) //Salvando a resposta num array de JSON

                arrayHistorico = arrayHistorico.plusElement(resposta) // Salvando a resposta num Array<String>

                expressao = "0" //Zerando a expressão
            }
            else {
                Toast.makeText(applicationContext, "Operação inválida!", Toast.LENGTH_LONG).show()
            }
        }

        // Direciona o usuário para uma tela de Histórico e carrega o histórico dos cálculos
        buttonHistorico.setOnClickListener {
            val hist = Historico(historico) // Variável que implementa Parcelable

            val intent = Intent(applicationContext,TelaHistorico::class.java)

            //Convertendo uma lista em uma string separada por ";" e enviando para a outra tela
            intent.putExtra("historico", historico.joinToString(";"))

            //Enviando para a outra tela o JSON como uma string
            intent.putExtra("json",json.toString())

            //Enviando um Array<String> para a próxima tela
            intent.putExtra("array", arrayHistorico)

            //Enviando um Parcelable para a próxima tela
            intent.putExtra("parcelable", hist)

            startActivity(intent)
        }
    }

    // Função comum a todos os botões de operações e que adiciona a operação selecionada na expressão
    fun onClickOperacoes(v: View){
        if(expressao.last().isDigit()){
            var beta: Button = findViewById(v.id)
            expressao += beta.text
            textView.text = expressao
        }
        else{
            Toast.makeText(applicationContext,"Operação inválida!",Toast.LENGTH_LONG).show()
        }
    }

    // Função comum a todos os botões que são dígitos, adicionando o digíto no final da expressão
    fun onClick(v: View) {
        val beta: Button = this.findViewById(v.id)
        if(expressao == "0"){
            expressao = beta.text.toString()
            textView.text = expressao
        }
        else{
            expressao += beta.text
            textView.text = expressao
        }
    }

    // Função que calcula o resultado das expressões
    fun eval(str: String): Double {
        return object : Any() {
            var pos = -1
            var ch = 0
            fun nextChar() {
                ch = if (++pos < str.length) str[pos].toInt() else -1
            }

            fun eat(charToEat: Int): Boolean {
                while (ch == ' '.toInt()) nextChar()
                if (ch == charToEat) {
                    nextChar()
                    return true
                }
                return false
            }

            fun parse(): Double {
                nextChar()
                val x = parseExpression()
                if (pos < str.length) throw RuntimeException("Unexpected: " + ch.toChar())
                return x
            }

            // Grammar:
            // expression = term | expression `+` term | expression `-` term
            // term = factor | term `*` factor | term `/` factor
            // factor = `+` factor | `-` factor | `(` expression `)`
            //        | number | functionName factor | factor `^` factor
            fun parseExpression(): Double {
                var x = parseTerm()
                while (true) {
                    if (eat('+'.toInt())) x += parseTerm() // addition
                    else if (eat('-'.toInt())) x -= parseTerm() // subtraction
                    else return x
                }
            }

            fun parseTerm(): Double {
                var x = parseFactor()
                while (true) {
                    if (eat('*'.toInt())) x *= parseFactor() // multiplication
                    else if (eat('/'.toInt())) x /= parseFactor() // division
                    else return x
                }
            }

            fun parseFactor(): Double {
                if (eat('+'.toInt())) return parseFactor() // unary plus
                if (eat('-'.toInt())) return -parseFactor() // unary minus
                var x: Double
                val startPos = pos
                if (eat('('.toInt())) { // parentheses
                    x = parseExpression()
                    eat(')'.toInt())
                } else if (ch >= '0'.toInt() && ch <= '9'.toInt() || ch == '.'.toInt()) { // numbers
                    while (ch >= '0'.toInt() && ch <= '9'.toInt() || ch == '.'.toInt()) nextChar()
                    x = str.substring(startPos, pos).toDouble()
                } else if (ch >= 'a'.toInt() && ch <= 'z'.toInt()) { // functions
                    while (ch >= 'a'.toInt() && ch <= 'z'.toInt()) nextChar()
                    val func = str.substring(startPos, pos)
                    x = parseFactor()
                    x =
                        if (func == "sqrt") Math.sqrt(x) else if (func == "sin") Math.sin(
                            Math.toRadians(
                                x
                            )
                        ) else if (func == "cos") Math.cos(
                            Math.toRadians(x)
                        ) else if (func == "tan") Math.tan(Math.toRadians(x)) else throw RuntimeException(
                            "Unknown function: $func"
                        )
                } else {
                    throw RuntimeException("Unexpected: " + ch.toChar())
                }
                if (eat('^'.toInt())) x = Math.pow(x, parseFactor()) // exponentiation
                return x
            }
        }.parse()
    }
}