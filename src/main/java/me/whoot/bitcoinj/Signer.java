package me.whoot.bitcoinj;

import com.google.bitcoin.core.ECKey;
import com.google.bitcoin.core.Sha256Hash;
import com.google.bitcoin.crypto.DeterministicKey;
import com.google.bitcoin.crypto.HDKeyDerivation;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/signer")
public class Signer {

    public static final String SEED = "legal winner thank year wave sausage worth useful legal winner thank year wave sausage worth useful legal winner thank year wave sausage worth title";
    private static DeterministicKey key = HDKeyDerivation.createMasterPrivateKey(SEED.getBytes());

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
