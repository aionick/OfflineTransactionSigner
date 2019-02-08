# Offline Transaction Signer

A tool for signing an Aion transaction offline.

## Build
```shell
ant
```
You can find the jar file to embed in your project in the newly created `dist` directory.

## Example Usage

```java
byte[] signedTransaction = new SignedTransactionBuilder()
    .privateKey(...)
    .destination(...)
    .senderNonce(...)
    .data(...)
    .energyLimit(...)
    .energyPrice(...)
    .buildSignedTransaction();
```

These signed bytes can be sent to the blockchain via the RLP sendRawTransaction method.
