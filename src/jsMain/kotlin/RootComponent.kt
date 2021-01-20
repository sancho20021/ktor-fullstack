import org.w3c.dom.url.URLSearchParams
import react.RProps
import react.functionalComponent
import react.router.dom.browserRouter
import react.router.dom.redirect
import react.router.dom.route
import react.router.dom.switch

val RootComponent = functionalComponent<RProps> {
    browserRouter {
        switch {
            route(CommonRoutes.HOME, exact = true) {
                homePage {}
            }
            route<RProps>(CommonRoutes.CALENDARS) {
                calendarPage {
                    link = URLSearchParams(it.location.search).get("link")
                }
            }
            redirect(from = CommonRoutes.API + CommonRoutes.INVALID, to = CommonRoutes.HOME)
        }
    }
}