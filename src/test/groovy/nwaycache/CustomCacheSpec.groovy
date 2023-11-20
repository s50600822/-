package nwaycache;

import spock.lang.Specification;

class CustomCacheSpec extends Specification {

    def "test one read and one write happy flow - generified"() {
        given:
        Cache<String, String, Long> cache = new NWaySetAssociativeCache<>(8, 2, new LruReplacementAlgorithm<>());
        when:
        cache.put("Something", "Good");
        cache.put("Always", "Bad");
        then:
        cache.get("Something") == "Good";
        cache.get("Always") == "Bad";
    }

    def "test correct placement in the same set"() {
        given:
        Cache<Integer, String, Long> cache = new NWaySetAssociativeCache<>(8, 2, new LruReplacementAlgorithm<>());
        when:
        cache.put(16, "Good");
        cache.put(32, "Bad");
        then:
        cache.getCacheContents()[0][0].getData() == ("Good");
        cache.getCacheContents()[0][1].getData() == ("Bad");
    }

    def "test that the update by the same key - works"() {
        given:
        Cache<Integer, String, Long> cache = new NWaySetAssociativeCache<>(8, 2, new LruReplacementAlgorithm<>());
        when:
        cache.put(16, "Good");
        cache.put(16, "Bad");
        then:
        cache.get(16) == ("Bad");
    }

    def "test that least recently used algorithm works"() throws InterruptedException {
        given:
        Cache<Integer, String, Long> cache = new NWaySetAssociativeCache<>(8, 2, new LruReplacementAlgorithm<>());
        when:
        cache.put(16, "Good");
        Thread.sleep(10);
        cache.put(32, "Bad");
        Thread.sleep(10);
        cache.put(48, "Ugly");
        then:
        cache.get(16) == null;
        cache.get(32) == "Bad";
        cache.get(48) == "Ugly";
    }

    def "test that most recently used algorithm works"() throws InterruptedException {
        given:
        Cache<Integer, String, Long> cache = new NWaySetAssociativeCache<>(8, 2, new MruReplacementAlgorithm<>());
        when:
        cache.put(16, "Good");
        Thread.sleep(50);
        cache.put(32, "Bad");
        Thread.sleep(50);
        cache.put(48, "Ugly");
        then:
        cache.get(16) == "Good";
        cache.get(32) == null;
        cache.get(48) == "Ugly";
    }

    def "test that our custom implemented cache that chooses the first one to evict - works"() throws InterruptedException {
        given:
        Cache<Integer, String, Long> cache = new NWaySetAssociativeCache<>(8, 4, new LruReplacementAlgorithm<>());
        when:
        cache.put(16, "Good");
        Thread.sleep(50);
        cache.put(32, "Bad");
        Thread.sleep(50);
        cache.put(48, "Ugly");
        Thread.sleep(50);
        cache.put(64, "Something");
        Thread.sleep(50);
        cache.put(80, "Wrong");
        then:
        cache.get(16) == null;
        cache.get(32) == ("Bad");
        cache.get(48) == ("Ugly");
        cache.get(64) == ("Something");
        cache.get(80) == ("Wrong");
    }

    def "test that frequency based algorithm works"() throws InterruptedException {
        given:
        Cache<Integer, String, Long> cache = new NWaySetAssociativeCache<>(1, 2, new LfuReplacementAlgorithm<>());
        when:
        cache.put(16, "Good");
        cache.put(32, "Bad");
        cache.get(16);
        cache.get(32);
        cache.get(32);
        cache.put(24, "New Stuff");
        then:
        cache.get(32) == ("Bad");
        cache.get(16) == null;
    }

}
