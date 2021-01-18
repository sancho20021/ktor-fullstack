import kotlinx.browser.window
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.css.*
import kotlinx.html.js.onClickFunction
import react.*
import react.dom.tr
import react.router.dom.redirect
import styled.css
import styled.styledDiv
import styled.styledTable
import styled.styledTd

private val scope = MainScope()

interface MyTableProps : RProps {
    var id: Int
}

val myTable = functionalComponent<MyTableProps> { props ->
    val (textPopUpSeen, setPopUpSeen) = useState(false)
    val (popUpIndex, setPopUpIndex) = useState(-1)
    val rows = 90
    val columns = 52

    val (weekNoteList, setWeekNoteList) = useState(emptyList<WeekNote>())

    useEffect(dependencies = listOf()) {
        scope.launch {
            setWeekNoteList(getWeekNoteList(props.id))
        }
    }
//    if (weekNoteList.isEmpty()) {
//        redirect(to = CommonRoutes.API + CommonRoutes.INVALID)
//    } else {
        fun togglePop() {
            setPopUpSeen(!textPopUpSeen)
        }

        if (textPopUpSeen) {
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

                textPopUp {
                    toggle = {
                        togglePop()
                    }
                    initText =
                        if (popUpIndex < weekNoteList.size)
                            weekNoteList[popUpIndex].desc
                        else "Это будущая неделя!"
                    handleSubmit = {
                        if (popUpIndex < weekNoteList.size) {
                            weekNoteList[popUpIndex].desc = it
                            setWeekNoteList(weekNoteList)
                            scope.launch {
                                setWeekNote(props.id, weekNoteList[popUpIndex])
                            }
                        }
                        setPopUpSeen(false)
                    }
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
                                    if (index < weekNoteList.size)
                                        if (weekNoteList[index].desc.isEmpty()) Color.red
                                        else Color.yellow
                                    else Color.wheat
                            }
                            attrs.onClickFunction = {
                                setPopUpSeen(true)
                                setPopUpIndex(index)
                            }
                        }
                    }
                }
            //}
        }
    }
}

fun RBuilder.myTable(handler: MyTableProps.() -> Unit) = child(myTable) {
    attrs {
        handler()
    }
}
