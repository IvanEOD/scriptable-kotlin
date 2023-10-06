/**
 * show-table-example
 */
            
fun main() {
    val table = buildTable {
        row {
            isHeader()
            text("Example Title", "example subtitle")
        }
    }

    table.present()
}
