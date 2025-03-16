import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

class CacheBlock {
    int tag;    // Memory block address
    int[] data; // Block data (multiple words)
    boolean valid;

    public CacheBlock(int tag, int blockSize) {
        this.tag = tag;
        this.data = new int[blockSize];
        this.valid = true;
    }

    public int getData(int offset) {
        return data[offset];
    }

    public void setData(int offset, int value) {
        data[offset] = value;
    }
}

public class Memory {
    private static final int MEM_SIZE = 2048;
    private static final int CACHE_SIZE = 16; // Fully associative cache size
    private static final int BLOCK_SIZE = 4;  // 4 words per cache block
    private static final int OFFSET_MASK = 0b11;  // Extracts offset (last 2 bits)
    private static final int TAG_MASK = ~0b11;    // Extracts tag (removes offset)

    public int[] memory;
    public int[] IX;
    public int[] GPR;
    public int PC, MAR, MBR, IR, CC, MFR;

    private Map<Integer, CacheBlock> cache;  // Fully associative cache (tag -> block)
    private Queue<Integer> cacheQueue;  // FIFO replacement

    public Memory() {
        memory = new int[MEM_SIZE];
        IX = new int[3];
        GPR = new int[4];

        cache = new LinkedHashMap<>();
        cacheQueue = new LinkedList<>();

        for (int i = 0; i < MEM_SIZE; i++) memory[i] = 0;
        for (int i = 0; i < 3; i++) IX[i] = 0;
        for (int i = 0; i < 4; i++) GPR[i] = 0;

        PC = 0; MAR = 0; MBR = 0; IR = 0; CC = 0; MFR = 0;
    }

    /** Reads from cache if present, else fetches from memory */
    public int readWord(int address) {
        int tag = address & TAG_MASK;
        int offset = address & OFFSET_MASK;

        if (cache.containsKey(tag)) {
            System.out.println("[CACHE HIT] Address: " + address);
            return cache.get(tag).getData(offset);
        }

        System.out.println("[CACHE MISS] Fetching block for Address: " + address);
        loadBlockToCache(tag);
        return cache.get(tag).getData(offset);
    }

    /** Writes data to memory and updates cache if present */
    public void writeWord(int address, int data) {
        int tag = address & TAG_MASK;
        int offset = address & OFFSET_MASK;

        memory[address] = data;

        if (cache.containsKey(tag)) {
            cache.get(tag).setData(offset, data);
            System.out.println("[CACHE UPDATE] Address: " + address + " -> " + data);
        } else {
            loadBlockToCache(tag);
            cache.get(tag).setData(offset, data);
        }
    }

    /** Loads a block of memory into cache (FIFO Replacement) */
    private void loadBlockToCache(int tag) {
        if (cacheQueue.size() >= CACHE_SIZE) {
            int oldestTag = cacheQueue.poll();
            cache.remove(oldestTag);
        }

        CacheBlock newBlock = new CacheBlock(tag, BLOCK_SIZE);
        for (int i = 0; i < BLOCK_SIZE; i++) {
            newBlock.setData(i, memory[tag + i]);
        }

        cache.put(tag, newBlock);
        cacheQueue.add(tag);
    }

    public Map<Integer, CacheBlock> getCache() {
        return cache;
    }
}
