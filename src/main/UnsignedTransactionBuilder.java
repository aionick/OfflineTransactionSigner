package main;

import org.aion.rlp.RLP;

import java.math.BigInteger;

public final class UnsignedTransactionBuilder {
    // Required fields.
    private BigInteger nonce = null;
    private long energyLimit = -1;

    // Fields we provide default values for.
    private BigInteger value = null;
    private byte[] destination = null;
    private byte[] data = null;
    private long energyPrice = -1;
    private byte type = 0x1;
    private byte[] beaconHash = null;

    /**
     * The destination address of the transaction.
     *
     * <b>This field must be set.</b>
     *
     * @param destination The destination.
     * @return this builder.
     */
    public UnsignedTransactionBuilder destination(byte[] destination) {
        this.destination = destination;
        return this;
    }

    /**
     * The amount of value to transfer from the sender to the destination.
     *
     * @param value The amount of value to transfer.
     * @return this builder.
     */
    public UnsignedTransactionBuilder value(BigInteger value) {
        this.value = value;
        return this;
    }

    /**
     * The nonce of the sender.
     *
     * <b>This field must be set.</b>
     *
     * @param nonce The sender nonce.
     * @return this builder.
     */
    public UnsignedTransactionBuilder senderNonce(BigInteger nonce) {
        this.nonce = nonce;
        return this;
    }

    /**
     * The transaction data.
     *
     * @param data The data.
     * @return this builder.
     */
    public UnsignedTransactionBuilder data(byte[] data) {
        this.data = data;
        return this;
    }

    /**
     * The energy limit of the transaction.
     *
     * <b>This field must be set.</b>
     *
     * @param limit The energy limit.
     * @return this builder.
     */
    public UnsignedTransactionBuilder energyLimit(long limit) {
        this.energyLimit = limit;
        return this;
    }

    /**
     * The energy price of the transaction.
     *
     * @param price The energy price.
     * @return this builder.
     */
    public UnsignedTransactionBuilder energyPrice(long price) {
        this.energyPrice = price;
        return this;
    }

    /**
     * Sets the transaction type to be the type used by the AVM.
     *
     * @return this builder.
     */
    public UnsignedTransactionBuilder useAvmTransactionType() {
        this.type = 0x2;
        return this;
    }

    public UnsignedTransactionBuilder beaconHash(byte[] hash) {
        this.beaconHash = hash;
        return this;
    }

    public byte[] buildSignedTransaction() {
        if (this.nonce == null) {
            throw new IllegalStateException("No nonce specified.");
        }
        if (this.energyLimit == -1) {
            throw new IllegalStateException("No energy limit specified.");
        }

        byte[] to = (this.destination == null) ? new byte[0] : this.destination;
        byte[] value = (this.value == null) ? BigInteger.ZERO.toByteArray() : this.value.toByteArray();
        byte[] nonce = this.nonce.toByteArray();
        byte[] timestamp = BigInteger.valueOf(System.currentTimeMillis() * 1000).toByteArray();
        byte[] encodedNonce = RLP.encodeElement(nonce);
        byte[] encodedTo = RLP.encodeElement(to);
        byte[] encodedValue = RLP.encodeElement(value);
        byte[] encodedData = RLP.encodeElement((this.data == null) ? new byte[0] : this.data);
        byte[] encodedTimestamp = RLP.encodeElement(timestamp);
        byte[] encodedEnergy = RLP.encodeLong(this.energyLimit);
        byte[] encodedEnergyPrice = RLP.encodeLong((this.energyPrice == -1) ? 10_000_000_000L : this.energyPrice);
        byte[] encodedType = RLP.encodeByte(this.type);

        final byte[] fullEncoding;
        if(this.beaconHash == null) {
            fullEncoding = RLP.encodeList(encodedNonce, encodedTo, encodedValue, encodedData, encodedTimestamp, encodedEnergy, encodedEnergyPrice, encodedType);
        } else {
            // For details, see https://aionnetwork.atlassian.net/wiki/spaces/TE/pages/292389035/Transaction+RLP+Encoding
            byte[] beaconHashEncoded = RLP.encodeElement(beaconHash);
            byte[] shapeIdEncoded = RLP.encodeByte((byte) 1);
            fullEncoding = RLP.encodeList(encodedNonce, encodedTo, encodedValue, encodedData, encodedTimestamp, encodedEnergy, encodedEnergyPrice, encodedType, shapeIdEncoded, beaconHashEncoded);
        }

        return fullEncoding;
    }

    /**
     * Resets the builder so that it is in its initial state.
     *
     * The state of the builder after a call to this method is the same as the state of a newly constructed builder.
     */
    public void reset() {
        this.nonce = null;
        this.energyLimit = -1;
        this.value = null;
        this.destination = null;
        this.data = null;
        this.energyPrice = -1;
        this.type = 0x1;
        this.beaconHash = null;
    }
}
