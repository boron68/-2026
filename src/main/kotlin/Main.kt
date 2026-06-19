import kotlinx.browser.document
import kotlinx.html.dom.append
import kotlinx.html.js.button
import kotlinx.html.js.h1
import kotlinx.html.js.onClickFunction

fun main() {
    document.body?.append {
        h1 { +"Kotlin/JS веб-додаток" }
        button {
            +"Натисни мене"
            onClickFunction = {
                document.body?.append { +"Кнопку натиснуто!" }
            }
        }
    }
}
