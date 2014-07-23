Test signing server possesing single private key.

To start use `mvn jetty:run`. Once started the following resources are available:

- ##### GET /signer/xpub
exports server key for marriage.

- ##### POST /signer/sign
signs given sighash with server key. Sighash should be
provided in hex encoded form


