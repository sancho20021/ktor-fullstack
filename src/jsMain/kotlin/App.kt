import kotlinx.coroutines.MainScope
import kotlinx.css.*
import react.RProps
import react.functionalComponent
import styled.css
import styled.styledDiv

private val scope = MainScope()

val App = functionalComponent<RProps> { _ ->
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
            myTable {}
        }
    }
}