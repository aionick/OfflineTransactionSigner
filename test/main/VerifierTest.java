package main;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigInteger;

public class VerifierTest {
    @Test
    public void testMatchedSignerRegular() throws Exception{
        PrivateKey random = PrivateKey.random();
        byte[] signed = getSignedTransaction(random);
        Assert.assertTrue(SignedTransactionVerifier.isSignerForRegularTransaction(signed, random.getPublicAionAddress()));

    }

    @Test
    public void testUnMatchedSignerRegular() throws Exception{
        PrivateKey expectedSigner = PrivateKey.random();
        PrivateKey otherSigner = PrivateKey.random();
        byte[] signed = getSignedTransaction(expectedSigner);
        Assert.assertFalse(SignedTransactionVerifier.isSignerForRegularTransaction(signed, otherSigner.copyOfUnderlyingBytes()));

    }

    @Test
    public void testMatchedSignerInvokable() throws Exception{
        PrivateKey random = PrivateKey.random();
        byte[] signed = getSignedInvokable(random);
        Assert.assertTrue(SignedTransactionVerifier.isSignerForInvokableTransaction(signed, random.getPublicAionAddress()));

    }

    @Test
    public void testUnMatchedSignerInvokable() throws Exception{
        PrivateKey expectedSigner = PrivateKey.random();
        PrivateKey otherSigner = PrivateKey.random();
        byte[] signed = getSignedInvokable(expectedSigner);
        Assert.assertFalse(SignedTransactionVerifier.isSignerForInvokableTransaction(signed, otherSigner.copyOfUnderlyingBytes()));

    }

    private byte[] getSignedInvokable(PrivateKey privateKey) throws Exception {
        return new SignedInvokableTransactionBuilder().privateKey(privateKey.copyOfUnderlyingBytes())
                        .executor(PrivateKey.random().getPublicAionAddress())
                        .data(new byte[32])
                        .destination(PrivateKey.random().getPublicAionAddress())
                        .senderNonce(BigInteger.ZERO)
                        .buildSignedInvokableTransaction();
    }

    private byte[] getSignedTransaction(PrivateKey privateKey) throws Exception {
        return new SignedTransactionBuilder().privateKey(privateKey.copyOfUnderlyingBytes())
                .data(new byte[0])
                .energyLimit(200000)
                .energyPrice(10000000000L)
                .senderNonce(BigInteger.ZERO)
                .buildSignedTransaction();
    }
}
