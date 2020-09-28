package space.controlnet.pixivia.data.table

import space.controlnet.pixivia.utils.readCsv
import space.controlnet.pixivia.utils.readCsvHeader
import java.io.File

abstract class Table {
    protected abstract val file: File
    fun getData(): List<Map<String, String>> = file.readCsv()
    fun getHeaders(): List<String> = file.readCsvHeader()

    open val select: Selector = Selector()

    open inner class Selector {
        open val column = ColumnSelector()
        open val row = RowSelector()

        open inner class ColumnSelector {
            fun byHeader(header: String): List<String> = getData().map { row: Map<String, String> ->
                row[header] ?: ""
            }
            fun byIndex(colIndex: Int): List<String> = byHeader(getHeaders()[colIndex])
        }

        open inner class RowSelector {
            fun byIndex(rowIndex: Int): Map<String, String> = getData()[rowIndex]
        }

        open fun element(header: String, rowIndex: Int): String = column.byHeader(header)[rowIndex]
    }

}



