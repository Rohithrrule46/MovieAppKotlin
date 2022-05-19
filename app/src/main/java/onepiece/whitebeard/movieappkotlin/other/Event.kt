package onepiece.whitebeard.movieappkotlin.other

open class Event<T>(private val content: T) {

    var hasBeenHandled = false
        private set           // Allows external read but not write

    /*
    * returns the content and prevents its use again
    */
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }


    /*
     * returns the content even if its already handled
     */
    fun getContentEvenIfHandled(): T {
        return content
    }

}