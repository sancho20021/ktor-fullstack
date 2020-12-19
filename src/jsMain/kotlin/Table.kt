import kotlinx.css.Color
import kotlinx.css.backgroundColor
import react.*
import react.dom.tr
import styled.css
import styled.styledTable
import styled.styledTd

external interface TableProps : RProps {
    var list: List<WeekNote>
}

class Table : RComponent<TableProps, RState>() {
    val rows = 80
    val columns = 52
    override fun RBuilder.render() {
        styledTable {
            css {
                +AppStyles.table
                +AppStyles.tableTdTh
            }
            for (row in 0 until rows) {
                tr {
                    for (col in 0 until columns) {
                        val index = row * columns + col
                        styledTd {
                            css {
                                +AppStyles.tableTdTh
                                backgroundColor =
                                    if (index < props.list.size)
                                        if (props.list[index].desc.isEmpty()) Color.red
                                        else Color.yellow
                                    else Color.wheat
                            }
                        }
                    }
                }
            }
        }
    }
}

fun RBuilder.table(handler: TableProps.() -> Unit): ReactElement {
    return child(Table::class) {
        this.attrs(handler)
    }
}