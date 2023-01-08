import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class QuizMaker {
    public static void main(String[] args) throws InvalidQuizFormatException, FileNotFoundException {

        try {
            String textFile = args[0]; // "JavaQuiz.txt"
            Quiz quiz = new Quiz();
            quiz.loadFromFile(textFile);
            quiz.start();

        }catch(InvalidQuizFormatException e) {
            System.out.println("Such a file doesn't exit");
        }
    }
}
