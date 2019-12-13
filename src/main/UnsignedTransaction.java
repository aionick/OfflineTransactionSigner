package main;

import main.util.ByteUtil;
import org.aion.rlp.RLP;
import org.aion.rlp.RLPList;

import java.math.BigInteger;
import java.util.Arrays;

/**
 * A user-friendly representation of an unsigned transaction. This class is intended specifically as a means of decoding
 * the bytes of an unsigned transaction into an object that is meaningful to humans.
 */
public final class UnsignedTransaction {
    private final BigInteger nonce;
    private final BigInteger value;
    private final byte[] destination;
    private final byte[] data;
    private final byte[] beaconHash;
    private final long energyLimit;
    private final long energyPrice;
    private final byte type;

    private UnsignedTransaction(BigInteger nonce, BigInteger value, byte[] destination, byte[] data, byte[] beaconHash, long energyLimit, long energyPrice, byte type) {
        this.nonce = nonce;
        this.value = value;
        this.destination = (destination == null) ? null : destination.clone();
        this.data = (data == null) ? null : data.clone();
        this.beaconHash = (beaconHash == null) ? null : beaconHash.clone();
        this.energyLimit = energyLimit;
        this.energyPrice = energyPrice;
        this.type = type;
    }

    public static UnsignedTransaction parseFrom(byte[] unsignedTransaction) {
        if (unsignedTransaction == null) {
            throw new NullPointerException("Cannot parse null unsigned transaction!");
        }

        RLPList components = RLP.decode2(unsignedTransaction);

        System.err.println(components.get(0).getRLPData().length);

        //if (this.nonce == null) {
        //            throw new IllegalStateException("No nonce specified.");
        //        }
        //        if (this.energyLimit == -1) {
        //            throw new IllegalStateException("No energy limit specified.");
        //        }
        //
        //        byte[] to = (this.destination == null) ? new byte[0] : this.destination;
        //        byte[] value = (this.value == null) ? BigInteger.ZERO.toByteArray() : this.value.toByteArray();
        //        byte[] nonce = this.nonce.toByteArray();
        //        byte[] timestamp = BigInteger.valueOf(System.currentTimeMillis() * 1000).toByteArray();
        //        byte[] encodedNonce = RLP.encodeElement(nonce);
        //        byte[] encodedTo = RLP.encodeElement(to);
        //        byte[] encodedValue = RLP.encodeElement(value);
        //        byte[] encodedData = RLP.encodeElement((this.data == null) ? new byte[0] : this.data);
        //        byte[] encodedTimestamp = RLP.encodeElement(timestamp);
        //        byte[] encodedEnergy = RLP.encodeLong(this.energyLimit);
        //        byte[] encodedEnergyPrice = RLP.encodeLong((this.energyPrice == -1) ? 10_000_000_000L : this.energyPrice);
        //        byte[] encodedType = RLP.encodeByte(this.type);
        //
        //        final byte[] fullEncoding;
        //        if(this.beaconHash == null) {
        //            fullEncoding = RLP.encodeList(encodedNonce, encodedTo, encodedValue, encodedData, encodedTimestamp, encodedEnergy, encodedEnergyPrice, encodedType);
        //        } else {
        //            // For details, see https://aionnetwork.atlassian.net/wiki/spaces/TE/pages/292389035/Transaction+RLP+Encoding
        //            byte[] beaconHashEncoded = RLP.encodeElement(beaconHash);
        //            byte[] shapeIdEncoded = RLP.encodeByte((byte) 1);
        //            fullEncoding = RLP.encodeList(encodedNonce, encodedTo, encodedValue, encodedData, encodedTimestamp, encodedEnergy, encodedEnergyPrice, encodedType, shapeIdEncoded, beaconHashEncoded);
        //        }
        //
        //        return fullEncoding;
        return null;
    }

    public BigInteger getNonce() {
        return this.nonce;
    }

    public BigInteger getValue() {
        return this.value;
    }

    public byte[] getDestination() {
        return (this.destination == null) ? null : this.destination.clone();
    }

    public byte[] getData() {
        return (this.data == null) ? null : this.data.clone();
    }

    public byte[] getBeaconHash() {
        return (this.beaconHash == null) ? null : this.beaconHash.clone();
    }

    public long getEnergyLimit() {
        return this.energyLimit;
    }

    public long getEnergyPrice() {
        return this.energyPrice;
    }

    public byte getType() {
        return this.type;
    }

    @Override
    public String toString() {
        return "Unsigned Transaction { nonce = " + this.nonce
                + ", value = " + this.value
                + ", destination = " + ((this.destination == null) ? "[none]" : ByteUtil.bytesToHexString(this.destination))
                + ", data = " + ((this.data == null) ? "[none]" : ByteUtil.bytesToHexString(this.data))
                + ", beacon hash = " + ((this.beaconHash == null) ? "[none]" : ByteUtil.bytesToHexString(this.beaconHash))
                + ", energy limit = " + this.energyLimit
                + ", energy price = " + this.energyPrice
                + ", type = " + this.type + " }";
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        } else if (other.getClass() != UnsignedTransaction.class) {
            return false;
        }

        UnsignedTransaction otherTransaction = (UnsignedTransaction) other;

        return this.nonce.equals(otherTransaction.nonce)
                && this.value.equals(otherTransaction.value)
                && Arrays.equals(this.destination, otherTransaction.destination)
                && Arrays.equals(this.data, otherTransaction.data)
                && Arrays.equals(this.beaconHash, otherTransaction.beaconHash)
                && this.energyLimit == otherTransaction.energyLimit
                && this.energyPrice == otherTransaction.energyPrice
                && this.type == otherTransaction.type;
    }

    @Override
    public int hashCode() {
        return this.nonce.hashCode()
                * this.value.hashCode()
                * ((this.destination == null) ? 1 : Arrays.hashCode(this.destination))
                * ((this.data == null) ? 1 : Arrays.hashCode(this.data))
                * ((this.beaconHash == null) ? 1 : Arrays.hashCode(this.beaconHash))
                * Math.max(1, (int) this.energyLimit)
                * Math.max(1, (int) this.energyPrice)
                * Math.max(1, (int) this.type);
    }
}
