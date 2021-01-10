import kotlinx.browser.window
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
import styled.styledDiv
import styled.styledSpan

external interface TextPopUpProps : RProps {
    var toggle: () -> Unit
    var text: String
}

val textPopUp = functionalComponent<TextPopUpProps> { props ->
    val (text, setText) = useState(props.text)
    val handleChange: (Event) -> Unit = {
        setText((it.target as HTMLTextAreaElement).value)
    }

    fun handleSubmit(text: String): (Event) -> Unit {
        return {
            window.alert("отправлено '${text}', никуда")
            it.preventDefault()
        }
    }

    styledDiv {
        styledDiv {
            css {
                position = Position.absolute
                backgroundColor = Color.white
            }
            styledSpan {
                css {
                    color = Color.red
                }
                attrs.onClickFunction = { props.toggle() }
                +"Close"
            }
            form {
                attrs.onSubmitFunction = handleSubmit(text)
                label {
                    +"Описание недели:"
                    textArea {
                        attrs {
                            value = text
                            onChangeFunction = handleChange
                        }
                    }
                }
                input(InputType.submit) {
                    attrs.value = "Сохранить"
                }
            }
        }
    }
}

fun RBuilder.textPopUp(handler: TextPopUpProps.() -> Unit) = child(textPopUp) {
    attrs {
        handler()
    }
}