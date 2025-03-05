aoop

cli version

weaver game in java

1. The starting word and target word will be fixed at four letters.
   2. Players will have unlimited attempts to reach the target word.
   3. Each attempt must produce a valid intermediate word.
   4. If the player successfully reaches the target word, they win.
   5. Feedback is provided for each word entered

For the CLI version, a confirmatory message indicating the player has won is required.

The behaviour of the program shall be controlled by three flags: · One flag should, if set to true, cause an error message to be displayed if the word is not a valid intermediate word. If set to false, no error message should be displayed. · Another flag should, if set to true, display the path (not necessarily the best path) from the start word to the end word for testing purposes. · A third flag should, if set to true, cause the starting and target words to be randomly selected from a dictionary (dictionary.txt). If set to false, these words will be fixed

CLI versions of the program should allow players to input their intermediate words

The Model should load a list of valid words from a fixed location (from one provided file, dictionary.txt). This list will serve as the potential valid intermediate words for the player

The CLI should indicate the start word, the end word, and any intermediate words

must be constructed according to the principles of MVC

Model. This should have an interface designed to be convenient for the Controller, View and JUnit class to use with no superfluous public methods, no references to two classes and contain no GUI code. It may consist of several classes but there must be a class called Model or similar that provides the interface and this class should extend Observable. File reading should also be done in the Model. A high mark will be earned for a Model that implements all the required functionality and respects all these constraints. A pass mark will be earned for a Model that implements only some of the required functionality or fails to respect these constraints.



gui version

1. The starting word and target word will be fixed at four letters.
2. Players will have unlimited attempts to reach the target word. 
3. Each attempt must produce a valid intermediate word.
4. If the player successfully reaches the target word, they win.
5. Feedback is provided for each word entered (see figure 3):

·     Letters in the correct position are highlighted in green.

·     Letters not in the target word are shown in grey.



For the GUI version, a confirmatory message or a message box should be displayed to indicate whether the player has won (transformed the starting word into the target word), even though the game status is clear from the tile colouring on the last filled row.

The behaviour of the program shall be controlled by three flags:

·      One flag should, if set to true, cause an error message to be displayed if the word is not a valid intermediate word. If set to false, no error message should be displayed.

·      Another flag should, if set to true, display the path (not necessarily the best path) from the start word to the end word for testing purposes.

A third flag should, if set to true, cause the starting and target words to be randomly selected from a dictionary (dictionary.txt). If set to false, these words will be fixed, as shown in figure 2.

GUI versions of the program should allow players to input their intermediate words

The GUI version should have a button to request a reset of the game (return to the original state), which will be enabled only after the first valid input has been made. The GUI version should also have a button to request a new game, regardless of whether input has been made. This is not required for the CLI version.

  The GUI version must be constructed  according to the principles of MVC, as restated below. Code that belongs in  the View but is placed in the Model will usually not be counted towards the  marks for the View. Similar rules will apply for other misplaced code.  

The code must be documented with assert statements, unit testing, class diagrams, docstrings and comments as described below.

The flags mentioned in FR3 should be in the Model. It is necessary for them to be changeable at runtime. (e.g., using three buttons or other controls

| Model. This should have an interface designed to be  convenient for the Controller, View and JUnit class to use with no  superfluous public methods, no references to two classes and contain no GUI  code. It may consist of several classes but there must be a class called  Model or similar that provides the interface and this class should extend  Observable. File reading should also be done in the Model. A high mark will  be earned for a Model that implements all the required functionality and  respects all these constraints. A pass mark will be earned for a Model that  implements only some of the required functionality or fails to respect these  constraints. |
| ------------------------------------------------------------ |
| Controller. This should forward only  valid requests to the Model, querying the Model if necessary to find out if  the request is valid, and must also enable / disable buttons as described  above in the functional requirements. It must have no GUI code, though it may  send messages to the View. A high mark will be given to a controller that  respects all these constraints and a pass mark will be given to a controller  that respects only some of them |
| View of GUI version using the Swing framework. It  should implement Observer and therefore have an update method that is called  when the Model changes. This will be marked according to how many of the  functional requirements have been met. A high mark will be given to a view  that implements all the requirements and a pass mark will be given to a view  that implements only some of them. |

Specification of Model with asserts. This should include invariants for the class as well as pre and post conditions for each public method in the model. This will be marked according to how many of the relevant conditions are included and whether the ones that are included are correct. Partial credit will be available for describing them in English. A high mark will be given to a specification that includes all the relevant constraints. A pass mark will be given to a specification that includes only a few of them.

@startuml
top to bottom direction

class WeaverGameView {
    -controller: WeaverGameController
    -inputField: JTextField
    -submitButton: JButton
    -feedbackArea: JTextArea
    -resetButton: JButton
    -newGameButton: JButton
    -startWordLabel: JLabel
    -targetWordLabel: JLabel
    -enteredWordsArea: JTextArea
    -enteredWords: List<String>
    -isValidInputMade: boolean
    -enteredWordsPanel: JPanel
    -startWordPanel: JPanel
    -targetWordPanel: JPanel
    -virtualKeyboardPanel: JPanel
    +initialize(): void
    +updateWordsDisplay(startWord: String, targetWord: String): void
}

class WeaverGameModel {
    -startWord: String
    -targetWord: String
    -validWords: List<String>
    -errorFlag: boolean
    -showPathFlag: boolean
    -randomWordsFlag: boolean
    +loadDictionary(filename: String): List<String>
    +getStartWord(): String
    +getTargetWord(): String
    +setStartWord(startWord: String): void
    +setTargetWord(targetWord: String): void
    +isValidWord(word: String): boolean
    +getFeedback(word: String): String
    +findPath(): List<String>
}

class WeaverGameController {
    -model: WeaverGameModel
    +processInput(word: String): String
    +startNewGame(validWords: List<String>): void
    +getModel(): WeaverGameModel
    +showPath(): String
}

WeaverGameView --> WeaverGameController : uses
WeaverGameController --> WeaverGameModel : controls
WeaverGameModel --> WeaverGameView : updates

' 加入类之间的关系基数
WeaverGameView "1" --> "1" WeaverGameController : uses
WeaverGameController "1" --> "1" WeaverGameModel : controls
WeaverGameModel "1" --> "1..*" WeaverGameView : updates
@enduml