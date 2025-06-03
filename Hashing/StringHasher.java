package Hashing;

public class StringHasher<T> {
    public enum HashStrategy {
        JAVA_DEFAULT,
        STRING_FOLDING,
        SIMPLE_STRATEGY;
        // TODO multi-hash-function
    }

    public final HashStrategy DEFAULT_HASH_FUNCTION = HashStrategy.JAVA_DEFAULT;
    

    public int hash(T s, int mod) {
        return hash(s, DEFAULT_HASH_FUNCTION, mod);
    }

    /**
     * Computes the hash of string {@code s} using the strategy {@code hashFunction} modulo {@code mod}.
     * 
     * @param s the String the hash of which will be computed
     * @param hashFunction a value from {@link HashStrategy}, according to which the hashing will be done
     * @param mod integer the hash value will be modulo'd by
     * 
     * @throws IllegalArgumentException if {@code hashFunction} is not a value from {@link HashStrategy}.
     */
    public int hash(T s, HashStrategy hashFunction, int mod) {
        int result;

        switch (hashFunction) {
            case JAVA_DEFAULT:
                result = javaDefaultHashing(s);
                break;
            case STRING_FOLDING:
                result = stringFolding(s.toString());
                break;
            case SIMPLE_STRATEGY:
                result = simpleStrategy(s.toString());
                break;
            default:
                throw new IllegalArgumentException(String.format("%s is not a valid strategy; see method contract", hashFunction.toString()));
        }

        result = Math.abs(result) % mod;
        if (result < 0) result += mod;

        return result;
    }

    private int stringFolding(String s) {
        long sum = 0, mul = 1;
        for (int i = 0; i < s.length(); i++) {
            mul = (i % 4 == 0) ? 1 : mul * 256;
            sum += s.charAt(i) * mul;
        }

        // Overflow is not a problem due to uniqueness of hashing, and
        // we're taking modulo anyway.
        return (int) sum;
    }

    private int javaDefaultHashing(T s) {
        return s.hashCode();
    }

    private int simpleStrategy(String s) {
        char[] c = s.toCharArray();
        int sum = 0;

        for (int i = 0; i < c.length; i++) {
            sum += c[i];
        }

        return sum;
    }
}
