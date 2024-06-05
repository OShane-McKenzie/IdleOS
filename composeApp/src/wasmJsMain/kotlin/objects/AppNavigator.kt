package objects

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf


/**
 * AppNavigator is a utility class for managing navigation within a Compose application.
 *
 * @param initialScreen The initial screen to be displayed, defaulting to "splash".
 * @param permanentIgnoreList A list of screens to permanently ignore when updating navigation history.
 * @param terminationActions An array of lambda functions to be executed on termination.
 */
class AppNavigator(
    initialScreen: String = "splash",
    permanentIgnoreList: List<String> = listOf(),
    terminationActions: Array<() -> Unit> = arrayOf()
) {
    // Private properties to store navigation history and current view state.

    private val screenArray = mutableStateListOf<String>()
    private val setView = mutableStateOf(initialScreen)
    private val firstScreen = initialScreen
    private var getTerminationActions: Array<() -> Unit> = terminationActions
    private val globalPermanentIgnoreList = permanentIgnoreList
    //private val viewsCoroutineScope = mutableStateOf(CoroutineScope(Dispatchers.IO))

    /**
     * Sets the view state to the specified screen, optionally executing a task and updating navigation history.
     *
     * @param view The target screen to navigate to.
     * @param execTask Indicates whether to execute runTask() after the screen change.
     * @param ignoreList A list of screens to ignore when updating navigation history for the current transition.
     * @param updateHistory Indicates whether to update the navigation history.
     * @param clearHistory Indicates whether to clear the navigation history.
     * @param runTask A lambda function to execute after the screen transition.
     */
    fun setViewState(
        view: String,
        execTask: Boolean = false,
        ignoreList: List<String> = listOf(),
        updateHistory: Boolean = true,
        clearHistory: Boolean = false,
        runTask: () -> Unit = {}
    ) {

        if (setView.value != view &&
            setView.value != firstScreen &&
            setView.value != "loading" &&
            view.trim() != "" && updateHistory
        ) {
            if (setView.value !in ignoreList && setView.value !in globalPermanentIgnoreList) {
                screenArray.add(setView.value)
            }
        }

        setView.value = view

        if (execTask) {
            runTask()
        }

        if (clearHistory) {
            screenArray.clear()
        }
    }

    /**
     * Initializes the view state to the specified screen.
     *
     * @param view The screen to initialize as the current view.
     */
    fun initView(view: String = firstScreen) {
        setView.value = view
    }

    /**
     * Retrieves the current view state.
     *
     * @return The current screen being displayed.
     */
    fun getView(): String {
        return setView.value
    }

    fun getLastIndex(): Int {
        return screenArray.size - 1
    }

    val screenTerminationActionsList = mutableStateMapOf<String, () -> Unit>()
    fun goBack(lastIndex: Int) {
        // Set the current view state to the last item in the navigation history array.
        val currentScreen = setView.value
        if(currentScreen in screenTerminationActionsList){
            try{
                screenTerminationActionsList[currentScreen]?.let { it() }
            }catch (e: Exception){
                println("goBack: ${e.message} ${e.cause}")
            }
        }
        setView.value = screenArray[lastIndex]
        // Remove the last item from the navigation history array.
        screenArray.removeAt(lastIndex)
    }

}