package com.example.calculadora

import android.content.Intent
import android.os.Bundle
import android.util.JsonWriter
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import org.json.JSONStringer

class MainActivity : AppCompatActivity() {

    var expressao: String = "0"
    val historico = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonLimpar.setOnClickListener {
            expressao = "0"
            textView.text = expressao
        }

        buttonResult.setOnClickListener {
            if(expressao.last().isDigit()){
                textView.text = eval(expressao).toString()
                historico.add("$expressao = ${textView.text}")
                expressao = "0"
            }
            else {
                Toast.makeText(applicationContext, "Operação inválida!", Toast.LENGTH_LONG).show()
            }
        }

        buttonHistorico.setOnClickListener {
            val intent = Intent(applicationContext,Historico::class.java)
            intent.putExtra("historico", historico.joinToString(";"))
            startActivity(intent)
        }
    }

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