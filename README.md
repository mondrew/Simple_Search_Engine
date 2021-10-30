# Simple_Search_Engine
Phone book search engine
To run the program you need to specify the --data flag with the value of filepath.

How to compile and run program:
1. Go to the project root directory 
2. To compile the program run "javac .\src\ru\hyperskill\search\Main.java"
3. To run program go to src folder and run "java ru.hyperskill.search.Main --data "PATH_TO_FILE"

Where PATH_TO_FILE is the path to file where you phonebook is stored in the following format:
Dwight Joseph djo@gmail.com
Rene Webb webb@gmail.com
Katie Jacobs
Erick Harrington harrington@gmail.com
Myrtle Medina
Erick Burgess

You can search by the following strategies (by one or more words):
1) ALL - finds only writings where all words are included
2) ANY - searches all writings where at least one word is included
3) NONE - searches all writings that don't include any word.
