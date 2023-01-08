import java.util.ArrayList;

public class Test extends Question {
    private String[] options; // ["Yes", "No"]
    private int numOfOptions; // number of answers (answers.size())
    private ArrayList<Character> labels;

    Test() {

    }

    public String[] getOptions() {
        return options;
    }

    public void setOptions(String[] options) {
        this.options = options;
    }

    public int getNumOfOptions() {
        return numOfOptions;
    }

    public void setNumOfOptions(int numOfOptions) {
        this.numOfOptions = numOfOptions;
    }

    public ArrayList<Character> getLabels() {
        return labels;
    }

    public void setLabels(ArrayList<Character> labels) {
        this.labels = labels;
    }
}
