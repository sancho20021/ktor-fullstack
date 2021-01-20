import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.css.*
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDate
import kotlinx.html.js.onClickFunction
import react.*
import react.dom.h1
import react.dom.p
import react.dom.tr
import react.router.dom.redirect
import styled.*

private val scope = MainScope()

interface MyTableProps : RProps {
    var id: Int
}

val myTable = functionalComponent<MyTableProps> { props ->
    val (textPopUpSeen, setPopUpSeen) = useState(false)
    val (popUpIndex, setPopUpIndex) = useState(-1)
    val rows = 90
    val columns = 52

    val (userInfo, setUserInfo) = useState(UserInfo("", ""))
    val (weekNoteList, setWeekNoteList) = useState(emptyList<WeekNote>())

    useEffect(dependencies = listOf()) {
        scope.launch {
            val testUser = getTestUser(props.id)
            setUserInfo(testUser.userInfo)
            setWeekNoteList(testUser.weekNoteList)
        }
    }
    if (userInfo.name == "no" && userInfo.dateOfBirth == "no") {
        redirect(to = CommonRoutes.API + CommonRoutes.INVALID)
    } else if (userInfo.name.isNotEmpty() && userInfo.dateOfBirth.isNotEmpty()) {
        styledH1 {
            +"${userInfo.name}, родился: ${userInfo.dateOfBirth.toLocalDate()}"
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
                        setPopUpSeen(!textPopUpSeen)
                        setPopUpIndex(-1)
                    }
                    initText =
                        if (popUpIndex < weekNoteList.size)
                            weekNoteList[popUpIndex].desc
                        else ""
                    handleSubmit = {
                        if (popUpIndex < weekNoteList.size) {
                            weekNoteList[popUpIndex].desc = it
                            setWeekNoteList(weekNoteList)
                            scope.launch {
                                setWeekNote(props.id, weekNoteList[popUpIndex])
                            }
                        }
                    }
                    readOnly = popUpIndex >= weekNoteList.size
                    val startOfTheWeek = userInfo.dateOfBirth
                        .toLocalDate() + DatePeriod(days = popUpIndex * 7)
                    val endOfTheWeek = startOfTheWeek + DatePeriod(days = 6)
                    additionalDesc = "Неделя $popUpIndex ($startOfTheWeek - $endOfTheWeek)"
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
                                        else Color.lightGreen
                                    else Color.wheat
                                if (index == popUpIndex) {
                                    backgroundColor = Color.white
                                    border = "2px solid #00f"
                                }
                            }
                            attrs.onClickFunction = {
                                setPopUpSeen(true)
                                setPopUpIndex(index)
                            }
                        }
                    }
                }
            }
        }
    }
}

fun RBuilder.myTable(handler: MyTableProps.() -> Unit) = child(myTable) {
    attrs {
        handler()
    }
}
