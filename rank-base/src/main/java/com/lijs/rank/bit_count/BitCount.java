package com.lijs.rank.bit_count;

/**
 * @author author
 * @date 2024-12-27
 * @description
 */
public class BitCount {

    public static void main(String[] args) {
        System.out.println(Integer.bitCount(13));

        System.out.println(countOnes(13));
    }

    public static int countOnes(int number) {
        int count = 0;
        while (number != 0) {
            int c = number & 1;
            System.out.println(c);
            count += c;
            //count += number & 1; // 检查最低位是否为 1
            number >>>= 1;       // 无符号右移
            System.out.println("右移之后：" + number);
        }
        return count;
    }



}