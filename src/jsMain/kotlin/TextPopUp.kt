import kotlinx.css.*
import kotlinx.html.js.onClickFunction
import react.*
import react.dom.div
import react.dom.p
import styled.css
import styled.styledDiv
import styled.styledSpan

external interface TextPopUpProps : RProps {
    var toggle: () -> Unit
}

class TextPopUp : RComponent<TextPopUpProps, RState>() {

    override fun RBuilder.render() {
        styledDiv {
            styledDiv {
                css {
                    position = Position.absolute
                }
                styledSpan {
                    css {
                        color = Color.red
                    }
                    attrs.onClickFunction = { props.toggle() }
                    +"Close"
                }
                p {
                    +"I'm a PopUp!!!"
                }
            }
        }
    }
}

fun RBuilder.textPopUp(handler: TextPopUpProps.() -> Unit): ReactElement {
    return child(TextPopUp::class) {
        this.attrs(handler)
    }
}