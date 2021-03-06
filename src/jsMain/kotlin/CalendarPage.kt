import kotlinx.css.*
import react.RBuilder
import react.RProps
import react.child
import react.functionalComponent
import react.router.dom.redirect
import styled.css
import styled.styledDiv

interface CalendarProps : RProps {
    var id: String?
}

val calendarPage = functionalComponent<CalendarProps> { calendarProps ->
    if (calendarProps.id == null) {
        redirect(to = CommonRoutes.API + CommonRoutes.INVALID)
    } else {
        styledDiv {
            css {
                display = Display.flex
                flexDirection = FlexDirection.column
                width = LinearDimension("100%")
                height = LinearDimension("100%")
            }
            styledDiv {
                css {
                    flex(1.0, 1.0)
                }
                myTable {
                    id = calendarProps.id!!
                }
            }
        }
    }
}

fun RBuilder.calendarPage(handler: CalendarProps.() -> Unit) = child(calendarPage) {
    attrs.handler()
}
