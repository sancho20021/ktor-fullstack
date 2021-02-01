import kotlinx.css.*
import kotlinx.html.InputType
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onClickFunction
import kotlinx.html.js.onSubmitFunction
import org.w3c.dom.HTMLTextAreaElement
import org.w3c.dom.events.Event
import react.*
import react.dom.*
import styled.css
import styled.styledButton
import styled.styledDiv
import styled.styledTextArea

external interface TextPopUpProps : RProps {
    var toggle: () -> Unit
    var initText: String
    var handleSubmit: (String) -> Unit
    var readOnly: Boolean
    var additionalDesc: String
}

val textPopUp = functionalComponent<TextPopUpProps> { props ->
    val (text, setText) = useState(props.initText)
    val handleChange: (Event) -> Unit = {
        if (!props.readOnly)
            setText((it.target as HTMLTextAreaElement).value)
    }

    styledDiv {
        css {
            position = Position.absolute
            backgroundColor = Color.white
            width = 500.px
            height = 400.px
        }
        styledButton {
            css {
                position = Position.absolute
                right = 0.px
                backgroundColor = Color.pink
            }
            attrs.onClickFunction = { props.toggle() }
            +"X"
        }
        form {
            attrs.onSubmitFunction = {
                props.handleSubmit(text)
                props.toggle()
            }
            h3 {
                +"Week description:"
            }
            h5 {
                +props.additionalDesc
            }
            styledTextArea {
                css {
                    width = LinearDimension("98%")
                }
                attrs {
                    placeholder =
                        if (props.readOnly) "“Yesterday is gone. Tomorrow has not yet come. We have only today. Let us begin.”\n" +
                                "― Mother Theresa"
                        else "Tell me what you have done this week"
                    value = text
                    onChangeFunction = handleChange
                }
            }
            br {}
            input(InputType.submit) {
                attrs.value = "Save"
            }
        }
    }
}

fun RBuilder.textPopUp(handler: TextPopUpProps.() -> Unit) = child(textPopUp) {
    attrs {
        handler()
    }
}