package org.rudogma.bytownite.utils;


import java.io.*;
import java.util.Arrays;


public class BitSet {

    protected static void wordsToBytes(long[] words, byte[] bytes, int offset){

        int maxOffset = bytes.length - offset - 8;
        int i = 0;

        while( offset <= maxOffset){
            Bits.putLong(bytes, offset, words[i]);

            offset = offset + 8;
            i++;
        }

        int bytesLeft = maxOffset - offset;

        if(bytesLeft < 0){
            bytesLeft = bytes.length;
        }

        if(bytesLeft > 0){
            byte[] tail=new byte[8];

            Bits.putLong(tail, 0, words[i]);

            System.arraycopy(tail, tail.length - bytesLeft, bytes, offset, bytesLeft);
        }
    }

    /**
     * @param bitSet
     * @return
     * @throws IOException
     */
    public static byte[] toByteArray(BitSet bitSet)
            throws IOException
    {
        byte[] bytes = new byte[ bitSet.getNumWords() * 8 ];

        wordsToBytes(bitSet.getBits(), bytes, 0);

        return bytes;
    }

    /**
     * Unnecessary bytes of last word(long) trimmed
     *
     * @param bitSet
     * @return
     * @throws IOException
     */
    public static byte[] toByteArrayTrimmed(BitSet bitSet)
            throws IOException
    {
        int bytesLen = (int)(bitSet.getNumBits() / 8);

        if( bitSet.getNumBits() % 8 > 0){
            bytesLen = bytesLen + 1;
        }

        byte[] bytes = new byte[ bytesLen ];

        wordsToBytes(bitSet.getBits(), bytes, 0);

        return bytes;
    }

    /**
     * Including the header with numBits
     * @param bitSet
     * @return
     * @throws IOException
     */
    public static byte[] toBytesWithHeader(BitSet bitSet)
            throws IOException
    {
        long numBits = bitSet.getNumBits();
        int numWords = bitSet.getNumWords();

        byte[] bytes = new byte[ 8 + numWords * 8 ];

        Bits.putLong(bytes, 0, numBits);

        wordsToBytes(bitSet.getBits(), bytes, 8);

        return bytes;
    }

    public static BitSet fromBytesWithHeader(byte[] bytes){
        long numBits = Bits.getLong(bytes, 0);

        return fromBytes(bytes, numBits, 8);
    }

    public static BitSet fromBytes(byte[] bytes, long numBits, int offset){
        int numWords = (bytes.length - offset)/ 8;
        int i = 0;

        long[] bits = new long[numWords];

        while( i < numWords){
            bits[i] = Bits.getLong(bytes, offset);

            i = i + 1;
            offset = offset + 8;
        }

        return new BitSet(bits, numBits);
    }


    /***
     * COPY PASTED FROM APACHE FOUNDATION
     */

    protected long[] bits;
    protected int wlen;   // number of words (elements) used in the array

    // Used only for assert:
    private long numBits;

    public long getNumBits(){
        return numBits;
    }

    /** Constructs an OpenBitSet large enough to hold {@code numBits}. */
    public BitSet(long numBits) {
        this.numBits = numBits;
        bits = new long[bits2words(numBits)];
        wlen = bits.length;
    }

    public BitSet(long[] bits, long numBits){
        this.numBits = numBits;
        this.bits = bits;
        wlen = bits.length;
    }

    public void replace(byte[] bytes, long numBits, int offset)
            throws Exception
    {
        if(numBits != getNumBits()){
            throw new Exception("[BitSet.replace] numBits must be equal to be replaced");
        }

        int i = 0;

        for( ; numBits >= 32; numBits -= 32, i++, offset += 8){
            bits[i] = Bits.getLong(bytes, offset);
        }

        if(numBits > 0){
            byte[] tail = new byte[8];
            int bytesLeft = (int) numBits / 8 + 1;

            System.arraycopy(bytes, offset, tail, 8 - bytesLeft, bytesLeft);

            bits[i] = Bits.getLong(tail, 0);
        }




//
//        int maxOffset = offset + ((int)numBits / 8 + 1); //bytes.length - offset - 8;
//        int maxI = (maxOffset - offset) / 8;
//        int i = 0;
//
//        while( offset < maxI){
//            bits[i] = Bits.getLong(bytes, offset);
//
//            offset = offset + 8;
//            i++;
//        }
//
//        int bytesLeft = (maxOffset - offset);// - (int)numBits % 8; //maxOffset - offset + 8;
//
//        if(bytesLeft < 0){
//            bytesLeft = bytes.length;
//        }
//
//        if(bytesLeft > 0){
//            byte[] tail=new byte[8];
//
//            System.arraycopy(bytes, offset, tail, tail.length - bytesLeft, bytesLeft);
//
//            bits[i] = Bits.getLong(tail, 0);
//        }

//        System.out.println("REPLACED: "+StringUtils.join(ArrayUtils.toObject(bits), ",") );
    }


    /** Returns the current capacity in bits (1 greater than the index of the last bit) */
    public long capacity() { return bits.length << 6; }

    /**
     * Returns the current capacity of this set.  Included for
     * compatibility.  This is *not* equal to {@link #cardinality}
     */
    public long size() {
        return capacity();
    }

    public int length() {
        return bits.length << 6;
    }

    /** Returns true if there are no set bits */
    public boolean isEmpty() { return cardinality()==0; }

    /** Expert: returns the long[] storing the bits */
    public long[] getBits() { return bits; }

    /** Expert: gets the number of longs in the array that are in use */
    public int getNumWords() { return wlen; }

    /** Returns true or false for the specified bit index. */
    public boolean get(int index) {
        int i = index >> 6;               // div 64
        // signed shift will keep a negative index and force an
        // array-index-out-of-bounds-exception, removing the need for an explicit check.
        if (i>=bits.length) return false;

        int bit = index & 0x3f;           // mod 64
        long bitmask = 1L << bit;
        return (bits[i] & bitmask) != 0;
    }


    /** Returns true or false for the specified bit index.
     * The index should be less than the OpenBitSet size
     */
    public boolean fastGet(int index) {
        assert index >= 0 && index < numBits;
        int i = index >> 6;               // div 64
        // signed shift will keep a negative index and force an
        // array-index-out-of-bounds-exception, removing the need for an explicit check.
        int bit = index & 0x3f;           // mod 64
        long bitmask = 1L << bit;
        return (bits[i] & bitmask) != 0;
    }



    /** Returns true or false for the specified bit index
     */
    public boolean get(long index) {
        int i = (int)(index >> 6);             // div 64
        if (i>=bits.length) return false;
        int bit = (int)index & 0x3f;           // mod 64
        long bitmask = 1L << bit;
        return (bits[i] & bitmask) != 0;
    }

    /** Returns true or false for the specified bit index.
     * The index should be less than the OpenBitSet size.
     */
    public boolean fastGet(long index) {
        //assert index >= 0 && index < numBits;
        int i = (int)(index >> 6);               // div 64
        int bit = (int)index & 0x3f;           // mod 64
        long bitmask = 1L << bit;
        return (bits[i] & bitmask) != 0;
    }

  /*
  // alternate implementation of get()
  public boolean get1(int index) {
    int i = index >> 6;                // div 64
    int bit = index & 0x3f;            // mod 64
    return ((bits[i]>>>bit) & 0x01) != 0;
    // this does a long shift and a bittest (on x86) vs
    // a long shift, and a long AND, (the test for zero is prob a no-op)
    // testing on a P4 indicates this is slower than (bits[i] & bitmask) != 0;
  }
  */


    /** returns 1 if the bit is set, 0 if not.
     * The index should be less than the OpenBitSet size
     */
    public int getBit(int index) {
        assert index >= 0 && index < numBits;
        int i = index >> 6;                // div 64
        int bit = index & 0x3f;            // mod 64
        return ((int)(bits[i]>>>bit)) & 0x01;
    }


  /*
  public boolean get2(int index) {
    int word = index >> 6;            // div 64
    int bit = index & 0x0000003f;     // mod 64
    return (bits[word] << bit) < 0;   // hmmm, this would work if bit order were reversed
    // we could right shift and check for parity bit, if it was available to us.
  }
  */

    /** sets a bit, expanding the set size if necessary */
    public void set(long index) {
        int wordNum = wordNum(index);
        int bit = (int)index & 0x3f;
        long bitmask = 1L << bit;
        bits[wordNum] |= bitmask;
    }


    /** Sets the bit at the specified index.
     * The index should be less than the OpenBitSet size.
     */
    public void fastSet(int index) {
        assert index >= 0 && index < numBits;
        int wordNum = index >> 6;      // div 64
        int bit = index & 0x3f;     // mod 64
        long bitmask = 1L << bit;
        bits[wordNum] |= bitmask;
    }

    /** Sets the bit at the specified index.
     * The index should be less than the OpenBitSet size.
     */
    public void fastSet(long index) {
        assert index >= 0 && index < numBits;
        int wordNum = (int)(index >> 6);
        int bit = (int)index & 0x3f;
        long bitmask = 1L << bit;
        bits[wordNum] |= bitmask;
    }

    /** Sets a range of bits, expanding the set size if necessary
     *
     * @param startIndex lower index
     * @param endIndex one-past the last bit to set
     */
    public void set(long startIndex, long endIndex) {
        if (endIndex <= startIndex) return;

        int startWord = (int)(startIndex>>6);

        // since endIndex is one past the end, this is index of the last
        // word to be changed.
        int endWord = wordNum(endIndex - 1);

        long startmask = -1L << startIndex;
        long endmask = -1L >>> -endIndex;  // 64-(endIndex&0x3f) is the same as -endIndex due to wrap

        if (startWord == endWord) {
            bits[startWord] |= (startmask & endmask);
            return;
        }

        bits[startWord] |= startmask;
        Arrays.fill(bits, startWord + 1, endWord, -1L);
        bits[endWord] |= endmask;
    }

    protected int wordNum(long index) {
        return (int)(index >> 6);
    }

    /** clears a bit.
     * The index should be less than the OpenBitSet size.
     */
    public void fastClear(int index) {
        assert index >= 0 && index < numBits;
        int wordNum = index >> 6;
        int bit = index & 0x03f;
        long bitmask = 1L << bit;
        bits[wordNum] &= ~bitmask;
        // hmmm, it takes one more instruction to clear than it does to set... any
        // way to work around this?  If there were only 63 bits per word, we could
        // use a right shift of 10111111...111 in binary to position the 0 in the
        // correct place (using sign extension).
        // Could also use Long.rotateRight() or rotateLeft() *if* they were converted
        // by the JVM into a native instruction.
        // bits[word] &= Long.rotateLeft(0xfffffffe,bit);
    }

    /** clears a bit.
     * The index should be less than the OpenBitSet size.
     */
    public void fastClear(long index) {
        assert index >= 0 && index < numBits;
        int wordNum = (int)(index >> 6); // div 64
        int bit = (int)index & 0x3f;     // mod 64
        long bitmask = 1L << bit;
        bits[wordNum] &= ~bitmask;
    }

    /** clears a bit, allowing access beyond the current set size without changing the size.*/
    public void clear(long index) {
        int wordNum = (int)(index >> 6); // div 64
        if (wordNum>=wlen) return;
        int bit = (int)index & 0x3f;     // mod 64
        long bitmask = 1L << bit;
        bits[wordNum] &= ~bitmask;
    }

    /** Clears a range of bits.  Clearing past the end does not change the size of the set.
     *
     * @param startIndex lower index
     * @param endIndex one-past the last bit to clear
     */
    public void clear(int startIndex, int endIndex) {
        if (endIndex <= startIndex) return;

        int startWord = (startIndex>>6);
        if (startWord >= wlen) return;

        // since endIndex is one past the end, this is index of the last
        // word to be changed.
        int endWord   = ((endIndex-1)>>6);

        long startmask = -1L << startIndex;
        long endmask = -1L >>> -endIndex;  // 64-(endIndex&0x3f) is the same as -endIndex due to wrap

        // invert masks since we are clearing
        startmask = ~startmask;
        endmask = ~endmask;

        if (startWord == endWord) {
            bits[startWord] &= (startmask | endmask);
            return;
        }

        bits[startWord] &= startmask;

        int middle = Math.min(wlen, endWord);
        Arrays.fill(bits, startWord+1, middle, 0L);
        if (endWord < wlen) {
            bits[endWord] &= endmask;
        }
    }

    public void clearAll(){
        Arrays.fill(bits, 0, bits.length, 0L);
    }

    /** Clears a range of bits.  Clearing past the end does not change the size of the set.
     *
     * @param startIndex lower index
     * @param endIndex one-past the last bit to clear
     */
    public void clear(long startIndex, long endIndex) {
        if (endIndex <= startIndex) return;

        int startWord = (int)(startIndex>>6);
        if (startWord >= wlen) return;

        // since endIndex is one past the end, this is index of the last
        // word to be changed.
        int endWord   = (int)((endIndex-1)>>6);

        long startmask = -1L << startIndex;
        long endmask = -1L >>> -endIndex;  // 64-(endIndex&0x3f) is the same as -endIndex due to wrap

        // invert masks since we are clearing
        startmask = ~startmask;
        endmask = ~endmask;

        if (startWord == endWord) {
            bits[startWord] &= (startmask | endmask);
            return;
        }

        bits[startWord] &= startmask;

        int middle = Math.min(wlen, endWord);
        Arrays.fill(bits, startWord+1, middle, 0L);
        if (endWord < wlen) {
            bits[endWord] &= endmask;
        }
    }



    /** Sets a bit and returns the previous value.
     * The index should be less than the OpenBitSet size.
     */
    public boolean getAndSet(int index) {
        assert index >= 0 && index < numBits;
        int wordNum = index >> 6;      // div 64
        int bit = index & 0x3f;     // mod 64
        long bitmask = 1L << bit;
        boolean val = (bits[wordNum] & bitmask) != 0;
        bits[wordNum] |= bitmask;
        return val;
    }

    /** Sets a bit and returns the previous value.
     * The index should be less than the OpenBitSet size.
     */
    public boolean getAndSet(long index) {
        assert index >= 0 && index < numBits;
        int wordNum = (int)(index >> 6);      // div 64
        int bit = (int)index & 0x3f;     // mod 64
        long bitmask = 1L << bit;
        boolean val = (bits[wordNum] & bitmask) != 0;
        bits[wordNum] |= bitmask;
        return val;
    }

    /** flips a bit.
     * The index should be less than the OpenBitSet size.
     */
    public void fastFlip(int index) {
        assert index >= 0 && index < numBits;
        int wordNum = index >> 6;      // div 64
        int bit = index & 0x3f;     // mod 64
        long bitmask = 1L << bit;
        bits[wordNum] ^= bitmask;
    }

    /** flips a bit.
     * The index should be less than the OpenBitSet size.
     */
    public void fastFlip(long index) {
        assert index >= 0 && index < numBits;
        int wordNum = (int)(index >> 6);   // div 64
        int bit = (int)index & 0x3f;       // mod 64
        long bitmask = 1L << bit;
        bits[wordNum] ^= bitmask;
    }

    /** flips a bit, expanding the set size if necessary */
    public void flip(long index) {
        int wordNum = wordNum(index);
        int bit = (int)index & 0x3f;       // mod 64
        long bitmask = 1L << bit;
        bits[wordNum] ^= bitmask;
    }

    /** flips a bit and returns the resulting bit value.
     * The index should be less than the OpenBitSet size.
     */
    public boolean flipAndGet(int index) {
        assert index >= 0 && index < numBits;
        int wordNum = index >> 6;      // div 64
        int bit = index & 0x3f;     // mod 64
        long bitmask = 1L << bit;
        bits[wordNum] ^= bitmask;
        return (bits[wordNum] & bitmask) != 0;
    }

    /** flips a bit and returns the resulting bit value.
     * The index should be less than the OpenBitSet size.
     */
    public boolean flipAndGet(long index) {
        assert index >= 0 && index < numBits;
        int wordNum = (int)(index >> 6);   // div 64
        int bit = (int)index & 0x3f;       // mod 64
        long bitmask = 1L << bit;
        bits[wordNum] ^= bitmask;
        return (bits[wordNum] & bitmask) != 0;
    }

    /** Flips a range of bits, expanding the set size if necessary
     *
     * @param startIndex lower index
     * @param endIndex one-past the last bit to flip
     */
    public void flip(long startIndex, long endIndex) {
        if (endIndex <= startIndex) return;
        int startWord = (int)(startIndex>>6);

        // since endIndex is one past the end, this is index of the last
        // word to be changed.
        int endWord = wordNum(endIndex - 1);

        /*** Grrr, java shifting wraps around so -1L>>>64 == -1
         * for that reason, make sure not to use endmask if the bits to flip will
         * be zero in the last word (redefine endWord to be the last changed...)
         long startmask = -1L << (startIndex & 0x3f);     // example: 11111...111000
         long endmask = -1L >>> (64-(endIndex & 0x3f));   // example: 00111...111111
         ***/

        long startmask = -1L << startIndex;
        long endmask = -1L >>> -endIndex;  // 64-(endIndex&0x3f) is the same as -endIndex due to wrap

        if (startWord == endWord) {
            bits[startWord] ^= (startmask & endmask);
            return;
        }

        bits[startWord] ^= startmask;

        for (int i=startWord+1; i<endWord; i++) {
            bits[i] = ~bits[i];
        }

        bits[endWord] ^= endmask;
    }


  /*
  public static int pop(long v0, long v1, long v2, long v3) {
    // derived from pop_array by setting last four elems to 0.
    // exchanges one pop() call for 10 elementary operations
    // saving about 7 instructions... is there a better way?
      long twosA=v0 & v1;
      long ones=v0^v1;

      long u2=ones^v2;
      long twosB =(ones&v2)|(u2&v3);
      ones=u2^v3;

      long fours=(twosA&twosB);
      long twos=twosA^twosB;

      return (pop(fours)<<2)
             + (pop(twos)<<1)
             + pop(ones);

  }
  */


    /** @return the number of set bits */
    public long cardinality() {
        return BitUtil.pop_array(bits, 0, wlen);
    }

    /** Returns the popcount or cardinality of the intersection of the two sets.
     * Neither set is modified.
     */
    public static long intersectionCount(BitSet a, BitSet b) {
        return BitUtil.pop_intersect(a.bits, b.bits, 0, Math.min(a.wlen, b.wlen));
    }

    /** Returns the popcount or cardinality of the union of the two sets.
     * Neither set is modified.
     */
//        public static long unionCount(BitSet a, BitSet b) {
//            long tot = BitUtil.pop_union(a.bits, b.bits, 0, Math.min(a.wlen, b.wlen));
//            if (a.wlen < b.wlen) {
//                tot += BitUtil.pop_array(b.bits, a.wlen, b.wlen-a.wlen);
//            } else if (a.wlen > b.wlen) {
//                tot += BitUtil.pop_array(a.bits, b.wlen, a.wlen-b.wlen);
//            }
//            return tot;
//        }

    /** Returns the popcount or cardinality of "a and not b"
     * or "intersection(a, not(b))".
     * Neither set is modified.
     */
//        public static long andNotCount(OpenBitSet a, OpenBitSet b) {
//            long tot = BitUtil.pop_andnot(a.bits, b.bits, 0, Math.min(a.wlen, b.wlen));
//            if (a.wlen > b.wlen) {
//                tot += BitUtil.pop_array(a.bits, b.wlen, a.wlen-b.wlen);
//            }
//            return tot;
//        }

    /** Returns the popcount or cardinality of the exclusive-or of the two sets.
     * Neither set is modified.
     */
//        public static long xorCount(OpenBitSet a, OpenBitSet b) {
//            long tot = BitUtil.pop_xor(a.bits, b.bits, 0, Math.min(a.wlen, b.wlen));
//            if (a.wlen < b.wlen) {
//                tot += BitUtil.pop_array(b.bits, a.wlen, b.wlen-a.wlen);
//            } else if (a.wlen > b.wlen) {
//                tot += BitUtil.pop_array(a.bits, b.wlen, a.wlen-b.wlen);
//            }
//            return tot;
//        }


    /** Returns the index of the first set bit starting at the index specified.
     *  -1 is returned if there are no more set bits.
     */
    public int nextSetBit(int index) {
        int i = index>>6;
        if (i>=wlen) return -1;
        int subIndex = index & 0x3f;      // index within the word
        long word = bits[i] >> subIndex;  // skip all the bits to the right of index

        if (word!=0) {
            return (i<<6) + subIndex + Long.numberOfTrailingZeros(word);
        }

        while(++i < wlen) {
            word = bits[i];
            if (word!=0) return (i<<6) + Long.numberOfTrailingZeros(word);
        }

        return -1;
    }

    /** Returns the index of the first set bit starting at the index specified.
     *  -1 is returned if there are no more set bits.
     */
    public long nextSetBit(long index) {
        int i = (int)(index>>>6);
        if (i>=wlen) return -1;
        int subIndex = (int)index & 0x3f; // index within the word
        long word = bits[i] >>> subIndex;  // skip all the bits to the right of index

        if (word!=0) {
            return (((long)i)<<6) + (subIndex + Long.numberOfTrailingZeros(word));
        }

        while(++i < wlen) {
            word = bits[i];
            if (word!=0) return (((long)i)<<6) + Long.numberOfTrailingZeros(word);
        }

        return -1;
    }


    /** Returns the index of the first set bit starting downwards at
     *  the index specified.
     *  -1 is returned if there are no more set bits.
     */
    public int prevSetBit(int index) {
        int i = index >> 6;
        final int subIndex;
        long word;
        if (i >= wlen) {
            i = wlen - 1;
            if (i < 0) return -1;
            subIndex = 63;  // last possible bit
            word = bits[i];
        } else {
            if (i < 0) return -1;
            subIndex = index & 0x3f;  // index within the word
            word = (bits[i] << (63-subIndex));  // skip all the bits to the left of index
        }

        if (word != 0) {
            return (i << 6) + subIndex - Long.numberOfLeadingZeros(word); // See LUCENE-3197
        }

        while (--i >= 0) {
            word = bits[i];
            if (word !=0 ) {
                return (i << 6) + 63 - Long.numberOfLeadingZeros(word);
            }
        }

        return -1;
    }

    /** Returns the index of the first set bit starting downwards at
     *  the index specified.
     *  -1 is returned if there are no more set bits.
     */
    public long prevSetBit(long index) {
        int i = (int) (index >> 6);
        final int subIndex;
        long word;
        if (i >= wlen) {
            i = wlen - 1;
            if (i < 0) return -1;
            subIndex = 63;  // last possible bit
            word = bits[i];
        } else {
            if (i < 0) return -1;
            subIndex = (int)index & 0x3f;  // index within the word
            word = (bits[i] << (63-subIndex));  // skip all the bits to the left of index
        }

        if (word != 0) {
            return (((long)i)<<6) + subIndex - Long.numberOfLeadingZeros(word); // See LUCENE-3197
        }

        while (--i >= 0) {
            word = bits[i];
            if (word !=0 ) {
                return (((long)i)<<6) + 63 - Long.numberOfLeadingZeros(word);
            }
        }

        return -1;
    }

    /** returns the number of 64 bit words it would take to hold numBits */
    public static int bits2words(long numBits) {
        return (int)(((numBits-1)>>>6)+1);
    }


    public BitSet setOR(BitSet bitSet){
        int bitsLen = (int) bitSet.numBits;
        System.out.println("beforeOR: self="+cardinality()+", donor="+bitSet.cardinality());

        for(int i=0; i<bitsLen; i++){
            if( bitSet.fastGet(i) && !fastGet(i) ){
                set(i);
            }
        }

        return this;
    }

    public void reverseBits(){
        for(int i=0; i<numBits; i++){
            if( fastGet(i)){
                clear(i);
            }else{
                fastSet(i);
            }
        }
    }
}


