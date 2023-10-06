import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 *  Table Builder
 *
 * @author IvanEOD ( 10/5/2023 at 4:07 PM EST )
 */
@DslMarker
annotation class UITableMarker

@UITableMarker
sealed interface UIBuilderScope {
    val component: Any
    fun rgb(red: Int, blue: Int, green: Int) = Color("#${hexComponent(red)}${hexComponent(blue)}${hexComponent(green)}")
    private fun hexComponent(value: Int) = value.toString(16).padStart(2, '0').takeLast(2)
}

class TableBuilder(
    override val component: UITable
): UIBuilderScope {


    fun showSeparators(value: Boolean = true) {
        component.showSeparators = value
    }

    fun row(block: RowBuilder.() -> Unit): UITableRow {
        val instance = buildRow(block = block)
        component.addRow(instance)
        return instance
    }

    class RowBuilder(override val component: UITableRow) : UIBuilderScope {

        fun cellSpacing(value: Double) {
            component.cellSpacing = value
        }

        fun height(value: Double) {
            component.height = value
        }

        fun isHeader(value: Boolean = true) {
            component.isHeader = value
        }

        fun dismissOnSelect(value: Boolean = true) {
            component.dismissOnSelect = value
        }

        fun cell(
            cell: UITableCell = UITableCell(),
            block: CellBuilder.() -> Unit
        ): UITableCell {
            val instance = buildCell(cell, block)
            component.addCell(instance)
            return instance
        }

        fun text(
            title: String,
            subtitle: String? = null,
            block: CellBuilder.() -> Unit = {}
        ): UITableCell = cell(UITableCell.text(title, subtitle ?: ""), block)

        fun image(image: Image, block: CellBuilder.() -> Unit = {}): UITableCell = cell(UITableCell.image(image), block)

        fun imageAtUrl(url: String, block: CellBuilder.() -> Unit = {}): UITableCell = cell(UITableCell.imageAtURL(url), block)

        fun button(text: String, block: CellBuilder.() -> Unit = {}): UITableCell = cell(UITableCell.button(text), block)

        fun onSelect(block: () -> Unit) {
            component.onSelect = block
        }

    }

    class CellBuilder(override val component: UITableCell = UITableCell()): UIBuilderScope {

        fun widthWeight(value: Double) {
            component.widthWeight = value
        }

        fun dismissOnTap(value: Boolean = true) {
            component.dismissOnTap = value
        }

        @JsName("titleColorRGB")
        fun titleColor(red: Int, green: Int, blue: Int) = titleColor(rgb(red, green, blue))

        fun titleColor(value: Color) {
            component.titleColor = value
        }

        @JsName("subtitleColorRGB")
        fun subtitleColor(red: Int, green: Int, blue: Int) = subtitleColor(rgb(red, green, blue))

        fun subtitleColor(value: Color) {
            component.subtitleColor = value
        }

        @JsName("titleFontNameSize")
        fun titleFont(name: String, size: Int) = titleFont(Font(name, size))

        fun titleFont(value: Font) {
            component.titleFont = value
        }

        @JsName("subtitleFontNameSize")
        fun subtitleFont(name: String, size: Int) = subtitleFont(Font(name, size))

        fun subtitleFont(value: Font) {
            component.subtitleFont = value
        }

        fun leftAligned() {
            component.leftAligned()
        }

        fun centerAligned() {
            component.centerAligned()
        }

        fun rightAligned() {
            component.rightAligned()
        }

        @JsName("onTapDismiss")
        fun onTap(dismiss: Boolean, block: () -> Unit) {
            component.dismissOnTap = dismiss
            onTap(block)
        }

        fun onTap(value: () -> Unit) {
            component.onTap = value
        }

    }

}

fun buildTable(table: UITable = UITable(), block: TableBuilder.() -> Unit): UITable {
    contract { callsInPlace(block, InvocationKind.EXACTLY_ONCE) }
    val builder = TableBuilder(table)
    builder.block()
    return builder.component
}

fun buildRow(row: UITableRow = UITableRow(), block: TableBuilder.RowBuilder.() -> Unit): UITableRow {
    contract { callsInPlace(block, InvocationKind.EXACTLY_ONCE) }
    val builder = TableBuilder.RowBuilder(row)
    builder.block()
    return builder.component
}

fun buildCell(cell: UITableCell = UITableCell(), block: TableBuilder.CellBuilder.() -> Unit): UITableCell {
    contract { callsInPlace(block, InvocationKind.EXACTLY_ONCE) }
    val builder = TableBuilder.CellBuilder(cell)
    builder.block()
    return builder.component
}