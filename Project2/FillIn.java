public class FillIn extends Question{
    @Override
    public String toString() {
        return getAnswer();
    }

    @Override
    public String[] option() {return new String[0];
    }
}

