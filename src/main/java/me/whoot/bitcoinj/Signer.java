package me.whoot.bitcoinj;

import com.google.bitcoin.core.ECKey;
import com.google.bitcoin.core.Sha256Hash;
import com.google.bitcoin.core.Utils;
import com.google.bitcoin.crypto.ChildNumber;
import com.google.bitcoin.crypto.DeterministicKey;
import com.google.bitcoin.crypto.HDUtils;
import com.google.bitcoin.wallet.DeterministicKeyChain;
import com.google.common.collect.ImmutableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/signer")
public class Signer {

    public static final Logger log = LoggerFactory.getLogger(Signer.class);

    private static final byte[] ENTROPY = Sha256Hash.create("don't use a string seed like this in real life".getBytes()).getBytes();
    private static DeterministicKeyChain keyChain;

    static {
        keyChain = new DeterministicKeyChain(ENTROPY, "", 1389353062L);
        keyChain.setLookaheadSize(2);
    }

    @GET
    @Path("/xpub")
    @Produces(MediaType.TEXT_PLAIN)
    public String getXpub() {
        return keyChain.getWatchingKey().serializePubB58();
    }

    @POST
    @Path("/sign")
    @Produces(MediaType.TEXT_PLAIN)
    public byte[] sign(@FormParam("sighash") String hexSighash, @FormParam("keypath") String keyPath) {
        log.info("Signing sighash={} with key by path {}", hexSighash, keyPath);
        Sha256Hash sighash = new Sha256Hash(hexSighash);
        ImmutableList<ChildNumber> path = ImmutableList.copyOf(HDUtils.parsePath(keyPath));

        DeterministicKey key = keyChain.getKeyByPath(path, true);
        log.info("Key: {}", key);

        ECKey.ECDSASignature signature = key.sign(sighash);
        byte[] sig = signature.encodeToDER();
        log.info("Done. Signature: {}", Utils.HEX.encode(sig));

        return sig;
    }
}
