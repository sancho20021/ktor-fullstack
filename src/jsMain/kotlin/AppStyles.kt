import kotlinx.css.*
import styled.StyleSheet

object AppStyles : StyleSheet("AppStyles", isStatic = true) {
    val textContainer by css {
        padding(5.px)

        backgroundColor = rgb(8, 97, 22)
        color = rgb(56, 246, 137)
    }

    val textInput by css {
        margin(vertical = 5.px)

        fontSize = 14.px
    }
    val tableTdTh by css {
        border = "1px solid black"
    }
    val table by css {
        borderCollapse = BorderCollapse.collapse
        width = LinearDimension("100%")
        height = LinearDimension("100%")
    }
} 