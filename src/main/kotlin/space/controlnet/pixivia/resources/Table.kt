package space.controlnet.pixivia.resources

import space.controlnet.pixivia.utils.readCsv
import space.controlnet.pixivia.utils.readCsvHeader
import java.io.File

interface Table {
    val file: File
    fun getData(): List<Map<String, String>> = file.readCsv()
    fun getHeaders(): List<String> = file.readCsvHeader()


    fun getColumnByHeader(header: String): List<String> = getData().map { row: Map<String, String> ->
        row[header] ?: ""
    }

    fun getColumnByIndex(colIndex: Int): List<String> = getColumnByHeader(getHeaders()[colIndex])
    fun getRow(rowIndex: Int): Map<String, String> = getData()[rowIndex]
    fun getElement(header: String, rowIndex: Int): String = getColumnByHeader(header)[rowIndex]
}


