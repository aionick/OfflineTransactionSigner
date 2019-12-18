package main.crypto;

import java.nio.ByteBuffer;
import java.util.Arrays;

public final class Ed25519PublicKey {
    public static final byte A0_IDENTIFIER = (byte) 0xA0;
    private static final int SIG_BYTES = 96;
    private byte[] publicKey;

    private Ed25519PublicKey(byte[] publicKey) {
        this.publicKey = publicKey;
    }

    public static Ed25519PublicKey fromBytes(byte[] signature) {
        if (signature == null) {
            throw new NullPointerException("Cannot get public key from null signature!");
        }

        if(signature.length != SIG_BYTES) {
            throw new IllegalArgumentException("Cannot generate public key from signature: signature length should be 96 bytes, but it is " + signature.length + ".");
        }

        byte[] publicKey = Arrays.copyOfRange(signature, 0, 32);
        return new Ed25519PublicKey(publicKey);

    }

    public byte[] getAionAddressBytes() {
        return this.publicKey == null ? null : computeA0Address(this.publicKey);
    }

    private static byte[] computeA0Address(byte[] publicKey) {
        ByteBuffer buf = ByteBuffer.allocate(32);
        buf.put(A0_IDENTIFIER);
        buf.put(blake2b(publicKey), 1, 31);
        return buf.array();
    }

    private static byte[] blake2b(byte[] bytes) {
        Blake2b digest = Blake2b.Digest.newInstance(32);
        digest.update(bytes);
        return digest.digest();
    }
}
