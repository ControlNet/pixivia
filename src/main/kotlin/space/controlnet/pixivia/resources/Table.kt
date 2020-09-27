package space.controlnet.pixivia.resources

interface Table {
    val data: List<Map<String, String>>
    val headers: List<String>

    fun getColumnByHeader(header: String): List<String> = data.map { row: Map<String, String> ->
        row[header] ?: ""
    }

    fun getColumnByIndex(colIndex: Int): List<String> = getColumnByHeader(headers[colIndex])
    fun getRow(rowIndex: Int): Map<String, String> = data[rowIndex]
    fun getElement(header: String, rowIndex: Int): String = getColumnByHeader(header)[rowIndex]
}


