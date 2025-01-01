package com.lijs.rank.bloom;

import java.nio.charset.StandardCharsets;

/**
 * 保护 Redis 和数据库
 * 保护 Redis：
 * <p>
 * 布隆过滤器可以在 Redis 层面过滤掉不存在的key，减少缓存穿透。
 * 布隆过滤器的大小和精度可根据 Redis 的数据量设计。
 * 保护数据库：
 * <p>
 * 如果布隆过滤器判定数据不存在，则无需查询数据库。
 * 可避免大量无效查询对数据库造成压力。
 *
 * @author ljs
 * @date 2024-12-27
 * @description
 */

public class ByteBloomFilter {

    public static void main(String[] args) {
        // 创建布隆过滤器，位数组大小为 1,000 位，哈希函数数量为 3
        ByteBloomFilter bloomFilter = new ByteBloomFilter(1000, 3);

        // 添加元素
        bloomFilter.add("hello");
        bloomFilter.add("world");

        // 检查元素是否存在
        System.out.println(bloomFilter.mightContain("hello")); // true
        System.out.println(bloomFilter.mightContain("java"));  // false
    }

    private final byte[] bitArray;  // 位数组
    private final int bitArraySize; // 位数组大小（单位：bit）
    private final int numHashFunctions; // 哈希函数数量

    public ByteBloomFilter(int bitArraySize, int numHashFunctions) {
        this.bitArraySize = bitArraySize;
        this.numHashFunctions = numHashFunctions;
        this.bitArray = new byte[(bitArraySize + 7) / 8]; // 按字节对齐
    }

    // 添加元素到布隆过滤器
    public void add(String element) {
        int[] hashes = hash(element);
        for (int hash : hashes) {
            setBit(hash);
        }
    }

    // 检查元素是否可能存在
    public boolean mightContain(String element) {
        int[] hashes = hash(element);
        for (int hash : hashes) {
            if (!getBit(hash)) {
                return false;
            }
        }
        return true; // 所有位置为1，可能存在
    }

    // 计算多个哈希值
    private int[] hash(String element) {
        int[] result = new int[numHashFunctions];
        byte[] bytes = element.getBytes(StandardCharsets.UTF_8);
        int hash1 = murmurHash(bytes, 0); // 哈希1
        int hash2 = murmurHash(bytes, hash1); // 哈希2，基于前一次结果

        for (int i = 0; i < numHashFunctions; i++) {
            result[i] = Math.abs((hash1 + i * hash2) % bitArraySize); // 组合哈希
        }
        return result;
    }

    // 设置位数组中的某一位为1
    private void setBit(int position) {
        int byteIndex = position / 8; // 字节索引
        int bitIndex = position % 8; // 位索引
        bitArray[byteIndex] |= (1 << bitIndex);
    }

    // 检查位数组中的某一位是否为1
    private boolean getBit(int position) {
        int byteIndex = position / 8;
        int bitIndex = position % 8;
        return (bitArray[byteIndex] & (1 << bitIndex)) != 0;
    }

    // MurmurHash实现
    private int murmurHash(byte[] data, int seed) {
        int m = 0x5bd1e995;
        int r = 24;
        int len = data.length;

        int h = seed ^ len;
        int i = 0;

        while (len >= 4) {
            int k = (data[i] & 0xFF) |
                    ((data[i + 1] & 0xFF) << 8) |
                    ((data[i + 2] & 0xFF) << 16) |
                    ((data[i + 3] & 0xFF) << 24);
            k *= m;
            k ^= k >>> r;
            k *= m;

            h *= m;
            h ^= k;

            i += 4;
            len -= 4;
        }

        switch (len) {
            case 3:
                h ^= (data[i + 2] & 0xFF) << 16;
            case 2:
                h ^= (data[i + 1] & 0xFF) << 8;
            case 1:
                h ^= (data[i] & 0xFF);
                h *= m;
        }

        h ^= h >>> 13;
        h *= m;
        h ^= h >>> 15;

        return h;
    }

}
