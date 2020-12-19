import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.css.*
import react.RProps
import react.functionalComponent
import react.useEffect
import react.useState
import styled.css
import styled.styledDiv
import styled.styledH1

private val scope = MainScope()

val App = functionalComponent<RProps> { _ ->
    val (weekNoteList, setWeekNoteList) = useState(emptyList<WeekNote>())

    useEffect(dependencies = listOf()) {
        scope.launch {
            setWeekNoteList(getWeekNoteList())
        }
    }
    styledDiv {
        css {
            display = Display.flex
            flexDirection = FlexDirection.column
            width = LinearDimension("100%")
            height = LinearDimension("100%")
        }
        styledH1 {
            css {
                flex(0.0, 1.0, 30.px)
            }
            +"Life Calendar"
        }
        styledDiv {
            css {
                flex(1.0, 1.0)
            }
            table {
                list = weekNoteList
            }
        }
    }
}