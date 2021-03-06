package com.example.projetoprogramacaoavancada.database

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.provider.BaseColumns

class ContentProviderGym : ContentProvider() {
    var dbOpenHelper : GymDbOpenHelper? = null

    override fun onCreate(): Boolean {
        dbOpenHelper = GymDbOpenHelper(context)

        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        val db = dbOpenHelper!!.readableDatabase

        requireNotNull(projection)
        val colunas = projection as Array<String>

        val argsSeleccao = selectionArgs as Array<String>?

        val id = uri.lastPathSegment

        val cursor = when (getUriMatcher().match(uri)) {
            URI_UTILIZADORES -> TabelaDButilizador(db).query(colunas,selection,argsSeleccao,null, null, sortOrder)
            URI_UTILIZADOR_ESPECIFICO -> TabelaDButilizador(db).query(colunas, "${BaseColumns._ID}=?", arrayOf("$id"), null, null,null)
            URI_DIETAS -> TabelaDBdieta(db).query(colunas,selection,argsSeleccao,null, null, sortOrder)
            URI_DIETA_ESPECIFA -> TabelaDBdieta(db).query(colunas, "${BaseColumns._ID}=?", arrayOf("$id"), null, null,null)
            URI_TREINOS -> TabelaDBtreino(db).query(colunas,selection,argsSeleccao,null, null, sortOrder)
            URI_TREINO_ESPECIFICO -> TabelaDBtreino(db).query(colunas, "${BaseColumns._ID}=?", arrayOf("$id"), null, null,null)
            URI_ALIMENTOS -> TabelaDBalimento(db).query(colunas,selection,argsSeleccao,null, null, sortOrder)
            URI_ALIMENTO_ESPECIFICO -> TabelaDBalimento(db).query(colunas, "${BaseColumns._ID}=?", arrayOf("$id"), null, null,null)
            URI_EXERCICIOS -> TabelaDBexercicio(db).query(colunas,selection,argsSeleccao,null, null, sortOrder)
            URI_EXERCICIOS_ESPECIFICO -> TabelaDBexercicio(db).query(colunas, "${BaseColumns._ID}=?", arrayOf("$id"), null, null,null)
            URI_MAQUINAS -> TabelaDBmaquina(db).query(colunas,selection,argsSeleccao,null, null, sortOrder)
            URI_MAQUINA_ESPECIFICA -> TabelaDBmaquina(db).query(colunas, "${BaseColumns._ID}=?", arrayOf("$id"), null, null,null)

            else -> null
        }


        return cursor
    }

    override fun getType(uri: Uri): String? =
        when(getUriMatcher().match(uri)){

            URI_UTILIZADORES -> "$MULTIPLOS_REGISTOS/${TabelaDButilizador.NOME}"
            URI_UTILIZADOR_ESPECIFICO -> "$UNICO_REGISTO/${TabelaDButilizador.NOME}"
            URI_DIETAS -> "$MULTIPLOS_REGISTOS/${TabelaDBdieta.NOME}"
            URI_DIETA_ESPECIFA -> "$UNICO_REGISTO/${TabelaDBdieta.NOME}"
            URI_TREINOS -> "$MULTIPLOS_REGISTOS/${TabelaDBtreino.NOME}"
            URI_TREINO_ESPECIFICO -> "$UNICO_REGISTO/${TabelaDBtreino.NOME}"
            URI_ALIMENTOS -> "$MULTIPLOS_REGISTOS/${TabelaDBalimento.NOME}"
            URI_ALIMENTO_ESPECIFICO -> "$UNICO_REGISTO/${TabelaDBalimento.NOME}"
            URI_EXERCICIOS -> "$MULTIPLOS_REGISTOS/${TabelaDBexercicio.NOME}"
            URI_EXERCICIOS_ESPECIFICO -> "$UNICO_REGISTO/${TabelaDBexercicio.NOME}"
            URI_MAQUINAS -> "$MULTIPLOS_REGISTOS/${TabelaDBmaquina.NOME}"
            URI_MAQUINA_ESPECIFICA -> "$UNICO_REGISTO/${TabelaDBmaquina.NOME}"
            else -> null
        }


    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val db = dbOpenHelper!!.writableDatabase

        requireNotNull(values)

        val id = when (getUriMatcher().match(uri)) {
            URI_UTILIZADORES -> TabelaDButilizador(db).insert(values)
            URI_DIETAS -> TabelaDBdieta(db).insert(values)
            URI_ALIMENTOS -> TabelaDBalimento(db).insert(values)
            URI_TREINOS -> TabelaDBtreino(db).insert(values)
            URI_EXERCICIOS -> TabelaDBexercicio(db).insert(values)
            URI_MAQUINAS -> TabelaDBmaquina(db).insert(values)

            else -> -1
        }

        db.close()

        if(id==-1L) return null

        return Uri.withAppendedPath(uri, "$id")

    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        val db = dbOpenHelper!!.writableDatabase

        val id = uri.lastPathSegment

        val registosApagados = when (getUriMatcher().match(uri)) {
            URI_UTILIZADOR_ESPECIFICO -> TabelaDButilizador(db).delete("${BaseColumns._ID}=?", arrayOf("$id"))
            URI_DIETA_ESPECIFA -> TabelaDBdieta(db).delete("${BaseColumns._ID}=?", arrayOf("$id"))
            URI_TREINO_ESPECIFICO -> TabelaDBtreino(db).delete("${BaseColumns._ID}=?", arrayOf("$id"))
            URI_ALIMENTO_ESPECIFICO -> TabelaDBalimento(db).delete("${BaseColumns._ID}=?", arrayOf("$id"))
            URI_EXERCICIOS_ESPECIFICO -> TabelaDBexercicio(db).delete("${BaseColumns._ID}=?", arrayOf("$id"))
            URI_MAQUINA_ESPECIFICA -> TabelaDBmaquina(db).delete("${BaseColumns._ID}=?", arrayOf("$id"))

            else -> 0
        }

        db.close()

        return registosApagados
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        requireNotNull(values)

        val db = dbOpenHelper!!.writableDatabase

        val id = uri.lastPathSegment

        val registosAlterados = when (getUriMatcher().match(uri)){
            URI_UTILIZADOR_ESPECIFICO -> TabelaDButilizador(db).update(values, "${BaseColumns._ID}=?", arrayOf("$id"))
            URI_DIETA_ESPECIFA -> TabelaDBdieta(db).update(values, "${BaseColumns._ID}=?", arrayOf("$id"))
            URI_TREINO_ESPECIFICO -> TabelaDBtreino(db).update(values, "${BaseColumns._ID}=?", arrayOf("$id"))
            URI_ALIMENTO_ESPECIFICO -> TabelaDBalimento(db).update(values, "${BaseColumns._ID}=?", arrayOf("$id"))
            URI_EXERCICIOS_ESPECIFICO -> TabelaDBexercicio(db).update(values, "${BaseColumns._ID}=?", arrayOf("$id"))
            URI_MAQUINA_ESPECIFICA -> TabelaDBmaquina(db).update(values,"${BaseColumns._ID}=?", arrayOf("$id"))
            else -> 0
        }

        db.close()

        return registosAlterados
    }

    companion object{
        private const val AUTHORITY = "com.example.projetoprogramacaoavancada"

        private const val URI_UTILIZADORES = 100
        private const val URI_UTILIZADOR_ESPECIFICO = 101
        private const val URI_DIETAS = 200
        private const val URI_DIETA_ESPECIFA = 201
        private const val URI_TREINOS = 300
        private const val URI_TREINO_ESPECIFICO = 301
        private const val URI_ALIMENTOS = 400
        private const val URI_ALIMENTO_ESPECIFICO = 401
        private const val URI_EXERCICIOS = 500
        private const val URI_EXERCICIOS_ESPECIFICO = 501
        private const val URI_MAQUINAS = 600
        private const val URI_MAQUINA_ESPECIFICA = 601

        private const val UNICO_REGISTO = "vnd.android.cursor.item"
        private const val MULTIPLOS_REGISTOS = "vnd.android.cursor.dir"

        private val ENDERECO_BASE = Uri.parse("content://$AUTHORITY")
        val ENDERECO_UTILIZADORES = Uri.withAppendedPath(ENDERECO_BASE, TabelaDButilizador.NOME)
        val ENDERECO_DIETAS = Uri.withAppendedPath(ENDERECO_BASE, TabelaDBdieta.NOME)
        val ENDERECO_TREINOS = Uri.withAppendedPath(ENDERECO_BASE, TabelaDBtreino.NOME)
        val ENDERECO_EXERCICIOS = Uri.withAppendedPath(ENDERECO_BASE, TabelaDBexercicio.NOME)
        val ENDERECO_ALIMENTOS = Uri.withAppendedPath(ENDERECO_BASE, TabelaDBalimento.NOME)
        val ENDERECO_MAQUINAS = Uri.withAppendedPath(ENDERECO_BASE, TabelaDBmaquina.NOME)

        fun getUriMatcher() : UriMatcher {
            var uriMatcher = UriMatcher(UriMatcher.NO_MATCH)

            uriMatcher.addURI(AUTHORITY, TabelaDButilizador.NOME, URI_UTILIZADORES)
            uriMatcher.addURI(AUTHORITY, "${TabelaDButilizador.NOME}/#", URI_UTILIZADOR_ESPECIFICO)
            uriMatcher.addURI(AUTHORITY, TabelaDBdieta.NOME, URI_DIETAS)
            uriMatcher.addURI(AUTHORITY,"${TabelaDBdieta.NOME}/#", URI_DIETA_ESPECIFA)
            uriMatcher.addURI(AUTHORITY, TabelaDBtreino.NOME, URI_TREINOS)
            uriMatcher.addURI(AUTHORITY,"${TabelaDBtreino.NOME}/#", URI_TREINO_ESPECIFICO)
            uriMatcher.addURI(AUTHORITY, TabelaDBalimento.NOME, URI_ALIMENTOS)
            uriMatcher.addURI(AUTHORITY,"${TabelaDBalimento.NOME}/#", URI_ALIMENTO_ESPECIFICO)
            uriMatcher.addURI(AUTHORITY, TabelaDBexercicio.NOME, URI_EXERCICIOS)
            uriMatcher.addURI(AUTHORITY,"${TabelaDBexercicio.NOME}/#", URI_EXERCICIOS_ESPECIFICO)
            uriMatcher.addURI(AUTHORITY,TabelaDBmaquina.NOME, URI_MAQUINAS)
            uriMatcher.addURI(AUTHORITY,"${TabelaDBmaquina.NOME}/#", URI_MAQUINA_ESPECIFICA)

            return uriMatcher
        }
    }
}