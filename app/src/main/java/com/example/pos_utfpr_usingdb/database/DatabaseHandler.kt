package com.example.pos_utfpr_usingdb.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

abstract class DatabaseHandler(context: Context) :
    SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    abstract override fun onUpgrade(db: SQLiteDatabase?, oldDBVersion: Int, newDBVersion: Int)

    fun dbInsert(tableName: String, values: ContentValues): Long {
        val db = this.writableDatabase
        return db.insert(tableName, null, values)
    }

    fun dbUpdate(
        tableName: String, values: ContentValues, whereClause: String, whereArgs: Array<String>
    ): Int {
        val db = this.writableDatabase
        return db.update(tableName, values, whereClause, whereArgs)
    }

    fun dbDelete(tableName: String, whereClause: String, whereArgs: Array<String>): Int {
        val db = this.writableDatabase
        return db.delete(tableName, whereClause, whereArgs)
    }

    //Sempre que for inserir uma nova coluna numa tabela, é necessário atualizar o DB_VERSION
    // para que o onUpgrade seja chamado e a tabela, seja atualizada
    companion object {
        const val DB_NAME = "banco.db"
        const val DB_VERSION = 1
    }
}
