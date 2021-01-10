import kotlinx.browser.window
import kotlinx.css.*
import kotlinx.html.js.onClickFunction
import react.*
import react.dom.tr
import styled.css
import styled.styledDiv
import styled.styledTable
import styled.styledTd

external interface TableProps : RProps {
    var list: List<WeekNote>
}

external interface TableState : RState {
    var textPopUpSeen: Boolean
    var descIndex: Int?
}

class Table : RComponent<TableProps, TableState>() {
    val rows = 90
    val columns = 52

    fun togglePop() {
        setState {
            textPopUpSeen = !textPopUpSeen
        }
    }

    override fun TableState.init() {
        textPopUpSeen = false
    }

    override fun RBuilder.render() {
        styledDiv {
            css {
                zIndex = 100
                position = Position.relative
                top = LinearDimension("30%")
                display = Display.flex
                flexDirection = FlexDirection.column
                justifyContent = JustifyContent.center
                alignItems = Align.center
            }
            if (state.textPopUpSeen) {
                textPopUp {
                    toggle = { setState {
                        textPopUpSeen = false
                        descIndex = null
                    } }
                    text =
                        if (state.descIndex != null)
                            props.list[state.descIndex!!].desc
                        else "Это будущая неделя!"
                }
            }
        }
        styledTable {
            css {
                +AppStyles.table
                +AppStyles.tableTdTh
                zIndex = 1
                position = Position.absolute
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
                            attrs.onClickFunction = {
                                setState {
                                    textPopUpSeen = true
                                    descIndex =
                                        if (index < props.list.size)
                                            index
                                        else null
                                }
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