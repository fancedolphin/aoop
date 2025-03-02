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