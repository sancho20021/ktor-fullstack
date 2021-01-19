import kotlinx.css.*
import kotlinx.css.properties.border
import styled.StyleSheet

object AppStyles : StyleSheet("AppStyles", isStatic = true) {
    val tableTdTh by css {
        border = "1px solid black"
    }
    val table by css {
        borderCollapse = BorderCollapse.collapse
        width = LinearDimension("100%")
        height = LinearDimension("100%")
    }
    val whiteRedText by css {
        color = Color.white
        border(3.px, BorderStyle.solid, Color.white)
    }
} 