package com.rodrigo.soares.lista.models

import java.io.*
import java.util.*

class Gerenciador : Serializable {
    val listas: ArrayList<Lista>

    init {
        this.listas = ArrayList()
    }

    fun adicionarLista(lista: Lista) {
        listas.add(lista)
    }

    fun removerLista(lista: Lista) {
        listas.remove(lista)
    }

    /**
     * Recupera os dados do gerenciador através de um arquivo .lst
     * @param caminho - String contendo o caminho do arquivo.
     */
    fun getGerenciador(caminho: String) {
        try {
            val arquivoLeitura = FileInputStream(caminho)
            val gerenciador = ObjectInputStream(arquivoLeitura)

            gerenciador.close()
            arquivoLeitura.close()

            salvarGerenciadorBD(gerenciador.readObject() as Gerenciador)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     * Salva os dados do gerenciador no banco de dados.
     * @param gerenciador - Objeto Gerenciador a ser salvo.
     */
    private fun salvarGerenciadorBD(gerenciador: Gerenciador) {
        //IMPLEMENTAR
    }

    /**
     * Salva o objeto Gerenciador em um arquivo lista.lst.
     * @return boolean - Resposta se a ação dei certo ou não.
     */
    fun salvarGerenciadorArquivo(): Boolean {
        try {
            // SALVAR AS INFORMAÇÕES DO BANCO DE DADOS
            val gerenciador = Gerenciador()

            val arquivoGrav = FileOutputStream("lists.lst")

            val objGravar = ObjectOutputStream(arquivoGrav)

            objGravar.writeObject(gerenciador)

            objGravar.flush()

            objGravar.close()

            arquivoGrav.flush()

            arquivoGrav.close()

            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }

    }

}
