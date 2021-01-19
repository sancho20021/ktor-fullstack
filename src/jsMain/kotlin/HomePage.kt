import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.css.Color
import kotlinx.css.color
import kotlinx.html.InputType
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onClickFunction
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import react.*
import react.dom.*
import styled.css
import styled.styledP

private val scope = MainScope()

val homePage = functionalComponent<RProps> {
    val (name, setName) = useState("")
    val (dateOfBirth, setDateOfBirth) = useState("")
    val getChangeFunction: (RSetState<String>) -> ((Event) -> Unit) = { setText ->
        {
            setText((it.target as HTMLInputElement).value)
        }
    }
    val (link, setLink) = useState("")
    val (inputError, setInputError) = useState("")
    div {
        h1 {
            +"Create your life calendar"
        }
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
        input(type = InputType.button) {
            attrs.value = "Get my life calendar!"
            attrs.onClickFunction = {
                if (inputError.isNotEmpty()) {
                    setInputError("")
                }
                if (link.isEmpty()) {
                    if (name.isEmpty() || dateOfBirth.isEmpty()) {
                        setInputError("Please, fill all the information")
                    } else {
                        scope.launch {
                            setLink(getNewUserLink(UserInfo(name, dateOfBirth)))
                        }
                    }
                } else {
                    setInputError("You have already received your link :)")
                }
            }
        }
        if (link == "invalid date") {
            setInputError(link)
            setLink("")
        }
        if (link.isNotEmpty()) {
            styledP {
                css {
                    color = Color.green
                }
                +"Here is your link: "
                a(href = "http://$link") {
                    +"http://$link"
                }
            }
        }
        if (inputError.isNotEmpty()) {
            styledP {
                css {
                    color = Color.red
                }
                +inputError
            }
        }
    }
}

fun RBuilder.homePage(handler: RProps.() -> Unit) = child(homePage) {
    attrs.handler()
}
