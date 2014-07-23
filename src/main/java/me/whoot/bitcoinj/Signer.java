package me.whoot.bitcoinj;

import com.google.bitcoin.core.ECKey;
import com.google.bitcoin.core.Sha256Hash;
import com.google.bitcoin.crypto.DeterministicKey;
import com.google.bitcoin.wallet.DeterministicKeyChain;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.security.SecureRandom;

@Path("/signer")
public class Signer {

    private static DeterministicKey key = new DeterministicKeyChain(new SecureRandom()).getWatchingKey();

    @GET
    @Path("/xpub")
    @Produces(MediaType.TEXT_PLAIN)
    public String getXpub() {
        return key.serializePubB58();
    }

    @GET
    @Path("/sign")
    @Produces(MediaType.TEXT_PLAIN)
    public byte[] getIt(String hexSighash) {
        Sha256Hash sighash = new Sha256Hash(hexSighash);
        ECKey.ECDSASignature signature = key.sign(sighash);
        return signature.encodeToDER();
    }
}
