import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class WordleSolver{

    private Set<Character> bonusLetters = new HashSet<>(Arrays.asList('r', 'l', 's', 't', 'n', 'e'));
    public static void main (String [] args) throws IOException{
        new WordleSolver().run(5);
}

// All words go in a "dictionary" and the words variable updates depending on how many words are able to be created.
// It gives the user 6 tryWords to in a structured format where tryWord is a different function.

        private void run(int wordLength) throws IOException{

            Set<String> dictionary = getWords("index.json", wordLength);
            Set<String> words = new HashSet<>(dictionary);

            try(Scanner scanner = new Scanner(System.in)){
                for(int i = 0; i < 6 ; i++){
                    String tryWord = getTryWord(words);
                    String result;

                    do{
                        System.out.println();
                        System.out.println("#"+(i+1)+" Try this: "+tryWord+" (out of "+ words.size()+ " words)");
                        System.out.println("(g=green, y=yellow, r=red)");
                        System.out.println();
                        result = scanner.nextLine().trim();

                        if(result.startsWith("get ")){
                            printWordsWith(dictionary, result.substring(4).toCharArray());
                        }
                        else{
                            words = getNewWords(words, tryWord, result);
    }
}
                    while(result.startsWith("get "));
        }
    }
}

// Puts all 5 letter words from the "dictionary" into the variable "words".

    private Set<String> getWords (String path, int wordLength) throws IOException{
        Set<String> words = new HashSet<>();

        for(String w : Files.readAllLines(Paths.get(path)).get(0).split(",")){
            String word = w.replaceAll("[\\[\\],\\\"]","");

            if(wordLength == word.length()){
                words.add(word);
    }
}
        return words;
}

// Function to display the best word to use by setting new variables and using the method to implement a point system with the word such as the Wheel or Fortune. 
// There is a set of bonus letter so every possible word gets checked and gets added points if contains the bonus letters. 
// The word with the "maxScore" will be chosen to be the best "tryWord".
    
    private String getTryWord(Set<String> words){
        String bestWord = null;
        int maxUniqueLetters = 0;
        int maxScore = words.iterator().next().length()*2;
        Set<Character> letters = new HashSet<>();

        for(String word : words){
            for(int i = 0; i < word.length(); i++){
                letters.add(word.charAt(i));
}
            int score = letters.size();

            for(char c : letters){
                if(bonusLetters.contains(c)){
                    score++;
    }
}
            if(score == maxScore){
                return word;
}
            else if(letters.size () > maxUniqueLetters ){
                maxUniqueLetters = letters.size();
                bestWord = word;
}
            letters.clear();
}
        return bestWord;
}

// Creates a new word list by only including words that are able to be the answer
// Goes through every word and every letter to decide which letter is g(correct letter and position), y(correct letter but wrong position), or r(wrong letter).
// Updates the list depending on the whether the letters are g, y, or r using a boolean to evaluate if the word is valid or not.
 
    private Set<String> getNewWords(Set<String> words, String tryWord, String result){
        Set<String> newWords = new HashSet<>();

        for(String word : words){
            boolean isWordValid = true;
            char[] wordChars = word.toCharArray();

            for(int i = 0; i < tryWord.length(); i++){
                char r = result.charAt(i);

                if((r == 'g'&& wordChars[i] != tryWord.charAt(i)) 
                        || (r=='y' && (wordChars[i] == tryWord.charAt(i) ||  !contains (wordChars,tryWord.charAt(i))))
                        || (r=='r' && contains(wordChars,tryWord.charAt(i)))){
                    isWordValid = false;
                    break;
}
                else if (r == 'g'){
                    wordChars[i] = '-';
    }
}
            if(isWordValid){
                System.out.println(word);
                newWords.add(word);
    }
}
        return newWords;
}

// Function which checks if the word contains the letter that is being looked for.

    private boolean contains(char[] wordChars, char targetChar){
        for(char c : wordChars){
            if(c == targetChar){
                return true; 
    }
}
        return false;
}

// Function that creates a smarter way of figuring out the "bestWord".
// The best word is determined depending on which word will give the most knowledge.
// EX) There are 7 possible words that can be the possible answer with the same first three letters. This function analzyes the word and figures out which letters are repetitively included in the 7 words and determines the bestWord which has the most repetitive letters.

    private void printWordsWith (Set<String> dictionary, char[] targetChars){
        int bestScore = 2;

        for(String word : dictionary){
            Set<Character> letters = new HashSet<>();

            for(int i = 0; i < word.length(); i++){
                letters.add(word.charAt(i));
}
            int score = 0;

            for(char letter : letters){
                if(contains(targetChars, letter)){
                    score++;
    }
}
            if(score >= bestScore){
                System.out.print(word);

                for(int i = 2; i <= score; i++){
                    System.out.print("*");
}
                System.out.println();

                if(score > bestScore){
                    bestScore = score;
                }
            }
        }
    }
} 