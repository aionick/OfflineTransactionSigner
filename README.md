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

These signed bytes can be sent to the blockchain via the RPC sendRawTransaction method.

## Credit

The majority of this code was taken from another Aion offline signing tool by arajasek, available at [this github repository](https://github.com/arajasek/TxTool). The primary difference between the two tools is that arajasek's tool both creates the signed transaction __and__ sends it to a node, whereas this tool only creates the transaction itself and nothing more.
