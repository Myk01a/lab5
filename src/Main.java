import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        String[] strings = {"apple", "apple", "apple", "apple", "guava", "guava", "guava", "plum", "plum",
                "orange", "banana", "kiwi"};

        String [] strings1 = RandomUtils.randomStrings(4592, 1000);
        String [] strings2 = RandomUtils.randomStrings(1690, 10000);
        String [] strings3 = RandomUtils.randomStrings(9121, 100000);

        System.out.println();
        sortAndPrint(strings);
        System.out.println();
        sortAndPrint(strings1);
        System.out.println();
        sortAndPrint(strings2);
        System.out.println();
        sortAndPrint(strings3);

        System. exit(0);
    }

    private static void sortAndPrint(String[] strings) {
        List<WordInfo> wordInfoList = Arrays.stream(strings)
                .map(word -> new WordInfo(word, calculateWeight(strings, word)))
                .distinct()
                .sorted(Comparator.comparing(WordInfo::getWeight, Comparator.reverseOrder())
                        .thenComparing(WordInfo::getWord))
                .toList();

        Map<Integer, WordInfo> wordInfoMap = IntStream.range(0, wordInfoList.size())
                .boxed()
                .collect(Collectors.toMap(
                        i -> i + 1,
                        i -> {
                            WordInfo wordInfo = wordInfoList.get(i);
                            wordInfo.setId(i + 1);
                            return wordInfo;
                        }
                ));
        Collection<WordInfo> values = wordInfoMap.values();
        List<WordInfo> wordInfoList2 = new ArrayList<>(values);
        int medianIndex = wordInfoList.size() / 2-1;
        Stream.of(wordInfoList2.get(0), wordInfoList2.get(medianIndex), wordInfoList2.get(wordInfoList.size() - 1))
                .forEach(wordInfo -> System.out.println(wordInfo.getId() + " " + wordInfo.getWeight() + " " + wordInfo.getWord()));
    }

    private static long calculateWeight(String[] words, String targetWord) {
        int calculateLetterValue = calculateLetterValue(targetWord);
        long wordCount = Arrays.stream(words)
                .filter(word -> word.equals(targetWord))
                .count();
        return calculateLetterValue * wordCount;
    }

    private static int calculateLetterValue(String word) {
        Set<Character> uniqueLetters = new HashSet<>();
        int sum = 0;

        for (char letter : word.toCharArray()) {
            if (uniqueLetters.add(letter)) {
                int letterCount = (int) word.chars().filter(c -> c == letter).count();
                sum += letterCount * letterCount;
            }
        }
        return sum;
    }
    static class WordInfo {
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        private  int id;
        private String word;
        private long weight;

        public WordInfo(String word, long weight) {
            this.word = word;
            this.weight = weight;
        }

        public WordInfo(int id, String word, long weight) {
            this.id = id;
            this.word = word;
            this.weight = weight;
        }

        public String getWord() {
            return word;
        }

        public long getWeight() {
            return weight;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof WordInfo wordInfo)) return false;
            return weight == wordInfo.weight && Objects.equals(word, wordInfo.word);
        }

        @Override
        public int hashCode() {
            return Objects.hash(word, weight);
        }
    }
}
