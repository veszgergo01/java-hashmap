import src.definitions.OurMapInterface;
import src.implementations.LinearProbingHashMap;

class Main {
    public static void main(String[] args) {
        OurMapInterface<String, Integer> map = new LinearProbingHashMap<>();
    }
}