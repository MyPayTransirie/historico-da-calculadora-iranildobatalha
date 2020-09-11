package com.example.calculadora

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_historico.*
import org.json.JSONObject

class Historico : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historico)

        //Usando uma String concatenada com ";" para receber o histórico
        val historico = intent.getStringExtra("historico")
        val expressoes = historico?.replace(";","\n\n")
        //txtHistorico.text = expressoes

        //Usando um objeto JSON para obter o histórico vindo da tela anterior
        val jsonString =  intent.getStringExtra("json").toString() // Obtendo o JSON como uma String
        val json = JSONObject(jsonString) // Convertendo a String para o formato JSON
        val lista = json.getJSONArray("historico") // Obtendo o array com as expressões dentro do objeto JSON
        val resposta = arrayListOf<String>()
        for(i in 0 until lista.length()){
            //Pegando item por item e os guardando em uma lista de resposta para ser passado ao TextView
            resposta.add(lista.getString(i))
        }
        //Passando a lista como uma string para a TextView
        //txtHistorico.text = resposta.joinToString("\n\n")
    }
}