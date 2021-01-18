import kotlinx.html.InputType
import kotlinx.html.js.onChangeFunction
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import react.*
import react.dom.*

val homePage = functionalComponent<RProps> {
    val (name, setName) = useState("")
    val (dateOfBirth, setDateOfBirth) = useState("")
    val getChangeFunction: (RSetState<String>) -> ((Event) -> Unit) = { setText ->
        {
            setText((it.target as HTMLInputElement).value)
        }
    }
    div {
        h1 {
            +"Create your life calendar"
        }
        form {
            input(type = InputType.text) {
                attrs {
                    placeholder = "Enter your name"
                    onChangeFunction = getChangeFunction(setName)
                    value = name
                }
            }
            br {}
            input(type = InputType.date) {
                attrs {
                    attrs.onChangeFunction = getChangeFunction(setDateOfBirth)
                    value = dateOfBirth
                }
            }
            br {}
            input(type = InputType.submit) {
                attrs.value = "Get my life calendar!"
            }
        }
    }

}

fun RBuilder.homePage(handler: RProps.() -> Unit) = child(homePage) {
    attrs.handler()
}
