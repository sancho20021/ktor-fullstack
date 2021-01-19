import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.css.*
import kotlinx.html.InputType
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onClickFunction
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import react.*
import styled.*

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
    styledImg(src = "https://sun9-23.userapi.com/impg/j5ndC85x7EDkJDfFNabmO1MfzcK0ZD76W5rK6w/7Y_P-DcHHFE.jpg?size=2560x1440&quality=96&proxy=1&sign=651330aa007a00c145d98865713c6de9&type=album") {
        css {
            position = Position.fixed
            width = LinearDimension("100%")
            zIndex = -1
        }
    }
    styledDiv {
        css {
            display = Display.flex
            alignItems = Align.center
            justifyContent = JustifyContent.center
            flexDirection = FlexDirection.column
        }
        styledH1 {
            css {
                color = Color.white
            }
            +"Create your life calendar"
        }
        styledInput(type = InputType.text) {
            css {
                width = 250.px
            }
            attrs {
                placeholder = "Enter your name"
                onChangeFunction = getChangeFunction(setName)
                value = name
            }
        }
        styledInput(type = InputType.date) {
            css {
                width = 250.px
            }
            attrs {
                attrs.onChangeFunction = getChangeFunction(setDateOfBirth)
                value = dateOfBirth
            }
        }
        styledInput(type = InputType.button) {
            css {
                width = 250.px
            }
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
                    +AppStyles.whiteRedText
                    backgroundColor = Color.skyBlue
                }
                +"Here is your link: "
                styledA(href = "http://$link") {
                    css {
                        +AppStyles.whiteRedText
                        backgroundColor = Color.skyBlue
                    }
                    +"http://$link"
                }
            }
        }
        if (inputError.isNotEmpty()) {
            styledP {
                css {
                    +AppStyles.whiteRedText
                    backgroundColor = Color.hotPink
                }
                +inputError
            }
        }
    }
}

fun RBuilder.homePage(handler: RProps.() -> Unit) = child(homePage) {
    attrs.handler()
}
