### N-way Set Associative 缓存
![CI](https://github.com/s50600822/-/actions/workflows/ci.yml/badge.svg)


Basic implementation of a generic [N-way Set Associative Cache](https://en.wikipedia.org/wiki/Cache_placement_policies#Set-associative_cache) using Java

Comes with 3 basic [Cache Replacement policy implementations](https://en.wikipedia.org/wiki/Cache_replacement_policies):
1. [Least Recently Used (LRU)](https://en.wikipedia.org/wiki/Cache_replacement_policies#Least_recently_used_(LRU))
2. [Most Recently Used(MRU)](https://en.wikipedia.org/wiki/Cache_replacement_policies#Most_recently_used_(MRU))
3. [Least Frequently Used (LFU)](https://en.wikipedia.org/wiki/Cache_replacement_policies#Least-frequently_used_(LFU))


To use in your code, `NWaySetAssociativeCache` is supplied with Set Size, Entry Size and a Replacement Algorithm:
```
NWaySetAssociativeCache(int setSize,  int entrySize,  ReplacementAlgorithm<K, V, M> replacementAlgorithm)
```

Example: 
```
Cache<Integer, String, Long> cache =  new NWaySetAssociativeCache<>(8, 2, new LruReplacementAlgorithm<>());
cache.put(16, "One");
cache.put(16, "Two");// cache.get(16) == "Two"
```
To use a custom Cache Replacement Algorithm, implement the `ReplacementAlgorithm` interface and supply it into the `NWaySetAssociativeCache`:
```
public interface ReplacementAlgorithm<K,V,M>  extends Comparator<CacheElement<K,V,M>>
```

To integrate with other typical caches, `NWaySetAssociativeCache` implements a basic `Cache` interface:
```
public interface Cache<K, V, M> {
       V get(final K key);
       void put(final K key, final V value);
}
```