package com.example.projetoprogramacaoavancada

import android.content.ContentValues

data class Alimento(
    var nome : String,
    var quantidade: Int,
    var calorias: Int,
    var iddieta: Long,
    var id: Long = -1
)
{
    fun toContentValues() : ContentValues {
        val valores = ContentValues()

        valores.put(TabelaDBalimento.CAMPO_NOME_ALIMENTO, nome)
        valores.put(TabelaDBalimento.CAMPO_QUANTIDADE_ALIMENTO, quantidade)
        valores.put(TabelaDBalimento.CAMPO_CALORIA_ALIMENTO, calorias)
        valores.put(TabelaDBalimento.CAMPO_DIETA_ID, iddieta)
        return valores
    }
}
