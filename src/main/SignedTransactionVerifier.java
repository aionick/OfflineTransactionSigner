package main;

import main.crypto.Ed25519PublicKey;
import org.aion.rlp.RLP;
import org.aion.rlp.RLPList;
import java.util.Arrays;

public final class SignedTransactionVerifier {

    public static boolean isSignerForRegularTransaction(byte[] signedTransaction, byte[] expectedSigner) {
        if (signedTransaction == null) {
            throw new NullPointerException("Cannot verify the signer with null signed transaction!");
        }

        if (expectedSigner == null) {
            throw new NullPointerException("Cannot verify the signer with null expected signer!");
        }
        return Arrays.equals(getSigner(signedTransaction, 8), expectedSigner);
    }

    public static boolean isSignerForInvokableTransaction(byte[] signedTransaction, byte[] expectedSigner) {
        if (signedTransaction == null) {
            throw new NullPointerException("Cannot verify the signer with null signed transaction!");
        }

        if (expectedSigner == null) {
            throw new NullPointerException("Cannot verify the signer with null expected signer!");
        }
        return Arrays.equals(getSigner(Arrays.copyOfRange(signedTransaction, 1, signedTransaction.length), 5), expectedSigner);
    }

    private static byte[] getSigner(byte[] signedTransaction, int signatureOffset) {
        RLPList decodedTxList;
        decodedTxList = RLP.decode2(signedTransaction);

        RLPList transaction = (RLPList) decodedTxList.get(0);

        byte[] signature = transaction.get(signatureOffset).getRLPData();
        if (signature != null) {
            Ed25519PublicKey from = Ed25519PublicKey.fromBytes(signature);
            return from.getAionAddressBytes();
        } else {
            return null;
        }
    }
}
