package onepiece.whitebeard.movieappkotlin

class TestClass {


}

fun main(arg: Array<String>) {
    val listOfChars = listOf("AAA", "CD", "BB", "AB", "AAA", "BB", "AB")
    removeDuplicatesAndSort(listOfChars)
}

private fun removeDuplicatesAndSort(list: List<String>?=null) {
    val result = list?.distinct()?.sorted()
    println(result)
}