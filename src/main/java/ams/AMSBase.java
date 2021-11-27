package ams;

import org.apache.commons.codec.digest.MurmurHash3;
import util.HarryPotter;
import util.SeedGenerator;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.Callable;

public class AMSBase implements AMS {
    private final HashFunction hashFunction;

    public AMSBase() {
        this.hashFunction = generate(SeedGenerator.getSeed());
    }

    @Override
    public long process() throws Exception{
        long z = HarryPotter.getWordsStream()
                .mapToLong(hashFunction::on)
                .sum();
        return (long) Math.pow(z, 2);
    }

    @FunctionalInterface
    private static interface HashFunction {
        public long on(String s);
    }

    private static HashFunction generate(int seed) {
        return s -> {
            byte[] value = s.getBytes(StandardCharsets.UTF_8);
            long res = MurmurHash3.hash128x64(value, 0, value.length, seed)[0];
            return res > 0 ? 1 : -1;
        };
    }
}
