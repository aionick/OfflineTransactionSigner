package main;

import main.crypto.Blake2b;
import net.i2p.crypto.eddsa.EdDSAPrivateKey;
import net.i2p.crypto.eddsa.KeyPairGenerator;
import net.i2p.crypto.eddsa.Utils;
import org.aion.util.conversions.Hex;
import java.nio.ByteBuffer;
import java.security.KeyPair;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Arrays;


public final class PrivateKey {
    public static final int SIZE = 32;

    private final byte[] key;
    private final byte[] address;

    /**
     * Constructs a new private key consisting of the provided bytes.
     *
     * @param privateKeyBytes The bytes of the private key.
     */
    private PrivateKey(byte[] privateKeyBytes) throws InvalidKeySpecException {
        if (privateKeyBytes == null) {
            throw new NullPointerException("private key bytes cannot be null");
        }
        if (privateKeyBytes.length != SIZE) {
            throw new IllegalArgumentException("bytes of a private key must have a length of " + SIZE);
        }
        this.key = privateKeyBytes.clone();
        this.address = deriveAddress(this.key);
    }

    public static PrivateKey fromBytes(byte[] privateKeyBytes) throws InvalidKeySpecException {
        return new PrivateKey(privateKeyBytes);
    }

    public static PrivateKey random() {
        try {
            return new PrivateKey(generatePrivateKey());
        } catch (InvalidKeySpecException e) {
            // Hiding the checked exception because this should never actually happen here. We have
            // complete control over these bytes and know they are generated in a sound way.
            throw new RuntimeException(e.getMessage());
        }
    }

    public byte[] copyOfUnderlyingBytes() {
        return this.key.clone();
    }

    public  byte[] getPublicAionAddress() {
        return this.address;
    }

    @Override
    public String toString() {
        return "com.theoan.transactionbuilder.main.PrivateKey { 0x" + Hex.toHexString(this.key) + " }";
    }

    /**
     * Returns true only if other is a com.theoan.transactionbuilder.main.PrivateKey with the same underlying bytes.
     *
     * @param other The other whose equality is to be tested.
     * @return whether this is equal to other.
     */
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof PrivateKey)) {
            return false;
        } else if (other == this) {
            return true;
        }
        return Arrays.equals(this.key, ((PrivateKey) other).key);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(this.key);
    }

    public static byte[] generatePrivateKey() {
        KeyPairGenerator keyPairGenerator = new KeyPairGenerator();
        KeyPair pair = keyPairGenerator.generateKeyPair();
        EdDSAPrivateKey privateKey = (EdDSAPrivateKey) pair.getPrivate();
        return Utils.hexToBytes(Utils.bytesToHex(privateKey.getEncoded()).substring(32, 96));
    }

    private static byte[] deriveAddress(byte[] privateKeyBytes) throws InvalidKeySpecException {
        if (privateKeyBytes == null) {
            throw new NullPointerException("private key cannot be null");
        }

        if (privateKeyBytes.length != 32){
            throw new IllegalArgumentException("private key mute be 32 bytes");
        }

        EdDSAPrivateKey privateKey = new EdDSAPrivateKey(new PKCS8EncodedKeySpec(addSkPrefix(Utils.bytesToHex(privateKeyBytes))));
        byte[] publicKeyBytes = privateKey.getAbyte();

        return computeA0Address(publicKeyBytes);
    }

    private static byte[] addSkPrefix(String skString){
        String skEncoded = "302e020100300506032b657004220420" + skString;
        return Utils.hexToBytes(skEncoded);
    }

    private static byte[] computeA0Address(byte[] publicKey) {
        byte A0_IDENTIFIER = (byte) 0xa0;
        ByteBuffer buf = ByteBuffer.allocate(32);
        buf.put(A0_IDENTIFIER);
        buf.put(blake256(publicKey), 1, 31);
        return buf.array();
    }

    private static byte[] blake256(byte[] input) {
        Blake2b digest = Blake2b.Digest.newInstance(32);
        digest.update(input);
        return digest.digest();
    }
}
