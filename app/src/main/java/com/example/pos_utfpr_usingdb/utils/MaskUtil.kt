package com.example.pos_utfpr_usingdb.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

object MaskUtil {
    // Função responsável por aplicar a máscara de celular no EditText
    fun maskCell(editText: EditText): TextWatcher {
        return object : TextWatcher {

            // Controla se o texto está sendo alterado pela própria máscara
            // Evita loop infinito ao chamar editText.setText()
            private var isUpdating: Boolean = false

            // Executa antes do texto ser alterado
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            // Executa enquanto o texto está sendo alterado
            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                // Se a alteração foi feita pelo próprio setText da máscara,
                // cancela a execução para não entrar em ‘loop’
                if (isUpdating) {
                    isUpdating = false
                    return
                }

                // Remove todos os caracteres que não são números
                // Exemplo: "(44) 99999-9999" vira "44999999999"
                val numbers = unmaskCell(s.toString())

                // Define qual máscara será usada:
                // 10 dígitos: telefone fixo -> (##) ####-####
                // 11 dígitos: celular -> (##) #####-####
                val mask = if (numbers.length > 10) {
                    "(##) #####-####"
                } else {
                    "(##) ####-####"
                }

                val masked = StringBuilder()
                var index = 0

                // Percorre cada character da máscara
                for (char in mask) {

                    // Se o caractere da máscara não for '#',
                    // significa que é um caractere fixo: (, ), espaço ou -
                    if (char != '#') {
                        if (index < numbers.length) {
                            masked.append(char)
                        }
                        continue
                    }

                    // Se o caractere for '#', substitui pelo próximo número digitado
                    if (index >= numbers.length) {
                        break
                    }

                    masked.append(numbers[index])
                    index++
                }

                // Marca que o próximo setText será feito pela própria máscara
                isUpdating = true

                // Atualiza o texto do EditText com a máscara aplicada
                editText.setText(masked.toString())

                // Coloca o cursor no final do texto
                editText.setSelection(masked.length)
            }

            // Executa depois que o texto foi alterado
            // Neste caso, não precisa fazer nada aqui
            override fun afterTextChanged(s: Editable?) {}
        }
    }

    // Remove todos os caracteres que não são números
    // Exemplo: "(44) 99999-9999" vira "44999999999"
    fun unmaskCell(value: String): String {
        return value.replace(Regex("[^0-9]"), "")
    }
}