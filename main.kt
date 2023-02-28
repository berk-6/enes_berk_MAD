import kotlin.random.Random

fun main() {
    val answer = generateAnswer()
    println("Welcome to the number guessing game!")
    var guess: String?
    do {
        print("Please guess a 4-digit number with NO repeating digits: ")
        guess = readLine()
        if (guess != null && guess.length == 4 && guess.toSet().size == 4) {
            val (n, m) = evaluateGuess(guess, answer)
            println("$n:$m")
        } else {
            println("Invalid guess. Please enter a 4-digit number with no repeating digits.")
        }
    } while (guess != answer)
    println("Congratulations, you guessed the correct number!")
}

fun generateAnswer(): String {
    val digits = mutableListOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9')
    digits.shuffle()
    return digits.subList(0, 4).joinToString("")
}

fun evaluateGuess(guess: String, answer: String): Pair<Int, Int> {
    var n = 0
    var m = 0
    for (i in 0 until 4) {
        if (guess[i] == answer[i]) {
            m++
        }
        if (guess[i] in answer) {
            n++
        }
    }
    n -= m
    return Pair(n, m)
}
