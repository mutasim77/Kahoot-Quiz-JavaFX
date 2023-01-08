import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.*;

public class Quiz {
    private String name;
    private ArrayList<Question> questions;

    Quiz(){
        questions = new ArrayList<>();
    }

    public Quiz loadFromFile(String filePath) throws FileNotFoundException, InvalidQuizFormatException {
        setName(filePath.split("\\.")[0]); //JavaQuiz.text -> JavaQuiz
        File file = new File(filePath); //JavaQuiz.txt
        Scanner in = new Scanner(file); //FileReader

        Test test;
        FillIn fillIn;
        while (in.hasNext()) {
            String line = in.nextLine();

            if(line.contains("{blank}")){
                line = line.replace("{blank}", "_____");
            }
            if (isLineTest(line)) { //What's your name
                test = new Test();
                test.setDescription(line);

                ArrayList<String> answers = new ArrayList<>();

                while (in.hasNext()) {
                    String temp = in.nextLine();//
                    if (temp.equals("")) {
                        break;
                    } else {
                        answers.add(temp); // No , Yes
                    }
                }//while
                test.setAnswer(answers.get(0)); //we set our correct answer in answers -> Question;
                test.setNumOfOptions(answers.size()); //set number of answers in numberOfOptions -> Test

                ArrayList<Character> labels = new ArrayList<>(); //for labels (variants)
                for (int i = 0; i < answers.size(); i++) {
                    labels.add((char) (65 + i));
                }

                Collections.shuffle(answers); //By this method we shuffle our array's elements;

                test.setLabels(labels);
                test.setOptions(answers.toArray(new String[0]));
                addQuestion(test);
            } else {
                fillIn = new FillIn();
                fillIn.setDescription(line);
                fillIn.setAnswer(in.nextLine());
                addQuestion(fillIn);
                if (in.hasNext())
                    in.nextLine();
            }//else

        }//while
        return this;

    }//end of method (loadFromFile)

    public void start() {
        Collections.shuffle(questions);
        Scanner in = new Scanner(System.in);
        System.out.println("===========================================");
        System.out.println("\tWELCOME TO \"" + getName() + "\" QUIZ!");
        System.out.println("-------------------------------------------");
        int currentQuestion = 0; //Question
        int totalCorrectAnswer = 0; // total correct answers
        String correctAnswer; //save our correct answers from ArrayList

        while (currentQuestion < questions.size()) {
            System.out.println((currentQuestion + 1) + ". " + questions.get(currentQuestion).getDescription());// get question and write it
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            correctAnswer = questions.get(currentQuestion).getAnswer();//No

            if (isTest(questions.get(currentQuestion))) {
                Test test = ((Test) questions.get(currentQuestion));
                for (int i = 0; i < test.getNumOfOptions(); i++) {
                    System.out.println(test.getLabels().get(i) + ") " + test.getOptions()[i]); //A) Yes / B)No
                    if (test.getOptions()[i].equals(test.getAnswer())) { // getOptions ("Yes") == "Yes"
                        test.setAnswer(test.getAnswer().concat(Character.toString(test.getLabels().get(i)))); //NoA  a b c d
                    }// "YesB"                                                                                          //      No
                }
                correctAnswer = test.getAnswer();//"YesB"
                System.out.println("-------------------------------------------");

            System.out.print("Enter the correct choice: "); //
            String input = in.nextLine();//A
            if (String.valueOf(correctAnswer.charAt(correctAnswer.length() - 1)).equalsIgnoreCase(input)) { // NoA.
                System.out.println("Correct!");
                System.out.println("-------------------------------------------");
                totalCorrectAnswer++;
            } else {

                int numberOfUncorrectAnswers = 2;
                while (numberOfUncorrectAnswers > 0) {
                    System.out.print("Invalid choice! Try again(Ex: A, B, ...) : ");
                    input = in.nextLine();
                    if (String.valueOf(correctAnswer.charAt(correctAnswer.length() - 1)).equalsIgnoreCase(input) ) { //No A.equals(A)
                        System.out.println("Correct!");
                        System.out.println("-------------------------------------------");
                        totalCorrectAnswer++;
                        break;
                    } else {
                        numberOfUncorrectAnswers--;
                    }
                }
                if (numberOfUncorrectAnswers == 0) {
                    System.out.println("Incorrect!");
                }
                System.out.println("-------------------------------------------");

            }
            currentQuestion++;

         } else {
                Test test = new Test();
                System.out.print("Type your answer: ");
                String fill = in.nextLine(); //
                correctAnswer = questions.get(currentQuestion).getAnswer();

                if(fill.equalsIgnoreCase(correctAnswer)){
                    System.out.println("Correct!");
                    totalCorrectAnswer++;
                    currentQuestion++;
                }
                else{
                    System.out.println("Incorrect!");
                    currentQuestion++;
                }

            }//else

        }//while

        System.out.print("Correct answers: " + totalCorrectAnswer + "/" + questions.size());
        System.out.printf(" (%.2f", (double)(totalCorrectAnswer * 100) / questions.size());
        System.out.println("%)\n");
        printResult((double)(totalCorrectAnswer * 100) / questions.size());
    
    }// end of method start();

    private boolean isLineTest(String line) {
        return !line.contains("_____");
    }

    private boolean isTest(Question question) {
        return question instanceof Test;
    }

    public void addQuestion(Question question) {
        this.questions.add(question);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) throws InvalidQuizFormatException{
        if(!name.equals("JavaQuiz")){
            throw new InvalidQuizFormatException();
        }
        else
            this.name = name;
    }
    public void printResult(double total){
        if(total >= 75 ) System.out.println("\tExcellent\n");
        else if(total >= 50 && total < 75) System.out.println("\tNormal!\n");
        else System.out.println("\tDrop class! Retake necessary!\n");
    }
}
