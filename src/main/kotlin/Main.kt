import kotlinx.browser.document
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.HTMLElement

fun main() {
        val body = document.body ?: return
        // Створюємо простий UI через innerHTML для наочності
        body.innerHTML = """
            <style>
                .game{font-family:Arial,Helvetica,sans-serif;background:#fff;padding:20px;border-radius:8px;width:520px;max-width:94%;margin:40px auto;box-shadow:0 6px 24px rgba(22,27,48,.08)}
                .word{font-size:32px;letter-spacing:8px;margin:18px 0}
            </style>
            <div class="game">
                <h1>Гра в слова (Kotlin)</h1>
                <div>Спроби: <span id="attempts">6</span> — Використані літери: <span id="used">—</span></div>
                <div class="word" id="masked">_ _ _ _ _</div>
                <div style="margin-top:8px">
                    <input id="letter" type="text" maxlength="1" placeholder="літера" />
                    <button id="guessLetter">Вгадати літеру</button>
                    <input id="wordGuess" type="text" placeholder="вгадати слово" style="margin-left:8px;width:180px" />
                    <button id="guessWord">Вгадати слово</button>
                </div>
                <div id="message" style="margin-top:12px;font-weight:600">Готово до старту.</div>
                <div style="margin-top:12px"><button id="newGame">Нова гра</button></div>
            </div>
        """.trimIndent()

        val words = arrayOf("програмування", "котлін", "компютер", "веб", "розробка", "інтернет", "алгоритм", "дані")

        var secret = ""
        var masked = mutableListOf<Char>()
        var attempts = 6
        val used = mutableSetOf<Char>()

        val elAttempts = document.getElementById("attempts") as HTMLElement
        val elUsed = document.getElementById("used") as HTMLElement
        val elMasked = document.getElementById("masked") as HTMLElement
        val elMessage = document.getElementById("message") as HTMLElement
        val inputLetter = document.getElementById("letter") as HTMLInputElement
        val inputWord = document.getElementById("wordGuess") as HTMLInputElement
        val btnGuessLetter = document.getElementById("guessLetter") as HTMLButtonElement
        val btnGuessWord = document.getElementById("guessWord") as HTMLButtonElement
        val btnNew = document.getElementById("newGame") as HTMLButtonElement

        fun updateDisplay(){
            elMasked.textContent = masked.joinToString(" ") { if(it == '\u0000') " " else it.toString() }
            elAttempts.textContent = attempts.toString()
            elUsed.textContent = if(used.isEmpty()) "—" else used.joinToString(", ")
        }

        fun finish(win: Boolean){
            if(win) elMessage.textContent = "Вітаю! Ви вгадали слово: $secret"
            else elMessage.textContent = "Поразка — слово було: $secret"
            btnGuessLetter.disabled = true
            btnGuessWord.disabled = true
        }

        fun checkState(){
            if(!masked.contains('_')) finish(true)
            else if(attempts <= 0) finish(false)
        }

        fun newGame(){
            secret = words.random()
            masked = secret.map { if(it == ' ') '\u0000' else '_' }.toMutableList()
            attempts = 6
            used.clear()
            elMessage.textContent = "Гра почалась — вгадайте слово."
            inputLetter.value = ""
            inputWord.value = ""
            btnGuessLetter.disabled = false
            btnGuessWord.disabled = false
            updateDisplay()
            inputLetter.focus()
        }

        fun guessLetter(){
            val v = inputLetter.value.trim().lowercase()
            if(v.isEmpty()){ elMessage.textContent = "Введіть літеру."; return }
            val ch = v[0]
            if(used.contains(ch)){ elMessage.textContent = "Цю літеру вже використовували."; inputLetter.value = ""; return }
            used.add(ch)
            var found = false
            for(i in secret.indices){
                if(secret[i].lowercaseChar() == ch){ masked[i] = secret[i]; found = true }
            }
            if(!found){ attempts--; elMessage.textContent = "Ні — такої літери немає." }
            else elMessage.textContent = "Є! Продовжуйте."
            inputLetter.value = ""
            updateDisplay()
            checkState()
        }

        fun guessWord(){
            val v = inputWord.value.trim().lowercase()
            if(v.isEmpty()){ elMessage.textContent = "Введіть слово для вгадування."; return }
            if(v == secret.lowercase()){
                masked = secret.toMutableList()
                updateDisplay()
                finish(true)
            } else {
                attempts = (attempts - 2).coerceAtLeast(0)
                elMessage.textContent = "Невірно — втрачаєте 2 спроби."
                updateDisplay()
                checkState()
            }
            inputWord.value = ""
        }

        btnGuessLetter.addEventListener("click", { guessLetter() })
        btnGuessWord.addEventListener("click", { guessWord() })
        btnNew.addEventListener("click", { newGame() })
        inputLetter.addEventListener("keyup", { ev -> if((ev as org.w3c.dom.events.KeyboardEvent).key == "Enter") guessLetter() })
        inputWord.addEventListener("keyup", { ev -> if((ev as org.w3c.dom.events.KeyboardEvent).key == "Enter") guessWord() })

        newGame()
}

