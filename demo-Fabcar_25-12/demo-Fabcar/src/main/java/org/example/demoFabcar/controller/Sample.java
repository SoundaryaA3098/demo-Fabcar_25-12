//package org.example.demoFabcar.controller;
//
//
////
////
////import java.io.IOException;
////import java.nio.charset.StandardCharsets;
////import java.nio.file.Path;
////import java.nio.file.Paths;
////import java.util.concurrent.TimeoutException;
////
////import org.hyperledger.fabric.gateway.Contract;
////import org.hyperledger.fabric.gateway.ContractException;
////import org.hyperledger.fabric.gateway.Gateway;
////import org.hyperledger.fabric.gateway.Network;
////import org.hyperledger.fabric.gateway.Wallet;
////
////public final class Sample {
////    public static void main(String[] args) throws IOException, InterruptedException {
////
////        // Load an existing wallet holding identities used to access the network.
////        Path walletDirectory = Paths.get("wallet");
////        System.out.println("wallet:::"+walletDirectory);
////        System.out.println("wallet:::"+walletDirectory.toString());
////        
////
////        Wallet wallet = Wallet.createFileSystemWallet(walletDirectory);
////
////		Path networkConfigFile = Paths.get("src","main","resources","connection-org1.yaml");
////        // Path to a common connection profile describing the network.
////       // Path networkConfigFile = Paths.get("connection.json");
////		System.out.println("networkConfigFile:::::::"+networkConfigFile);
////        // Configure the gateway connection used to access the network.
////        Gateway.Builder builder = Gateway.createBuilder()
////                .identity(wallet, "user1")
////                .networkConfig(networkConfigFile);
////
////        // Create a gateway connection
////        try (Gateway gateway = builder.connect()) {
////
////            // Obtain a smart contract deployed on the network.
////            Network network = gateway.getNetwork("mychannel");
////            Contract contract = network.getContract("fabcar");
////
////            // Submit transactions that store state to the ledger.
////            byte[] createCarResult = contract.submitTransaction("createCar1", "CAR10", "VW", "Polo", "Grey", "Mary");
////            System.out.println(new String(createCarResult, StandardCharsets.UTF_8));
////
////            // Evaluate transactions that query state from the ledger.
////            byte[] queryAllCarsResult = contract.evaluateTransaction("queryAllCars");
////            System.out.println(new String(queryAllCarsResult, StandardCharsets.UTF_8));
////
////        } catch (ContractException | TimeoutException e) {
////            e.printStackTrace();
////        }
////    }
////}
//
//
//
//import java.io.BufferedReader;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.security.GeneralSecurityException;
//import java.security.KeyFactory;
//import java.security.PrivateKey;
//import java.security.spec.PKCS8EncodedKeySpec;
//import java.util.Collection;
//import java.util.Date;
//import java.util.HashSet;
//import java.util.Random;
//import java.util.Set;
//
//import javax.xml.bind.DatatypeConverter;
//
//import org.hyperledger.fabric.sdk.ChaincodeID;
//import org.hyperledger.fabric.sdk.Channel;
//import org.hyperledger.fabric.sdk.Enrollment;
//import org.hyperledger.fabric.sdk.HFClient;
//import org.hyperledger.fabric.sdk.ProposalResponse;
//import org.hyperledger.fabric.sdk.QueryByChaincodeRequest;
//import org.hyperledger.fabric.sdk.TransactionProposalRequest;
//import org.hyperledger.fabric.sdk.User;
//import org.hyperledger.fabric.sdk.security.CryptoSuite;
//
//public class Sample {
//
//    private static HFClient client = null;
//
//    public static void main(String[] args) throws Throwable {
//        /*
//         * wallet_path: path.join(__dirname, './creds'), user_id: 'PeerAdmin',
//         * channel_id: 'mychannel', chaincode_id: 'fabcar', network_url:
//         * 'grpc://192.168.99.100:7051', orderer: grpc://192.168.99.100:7050
//         * 
//         */
//
//        // just new objects, without any payload inside
//        client = HFClient.createNewInstance();
//        CryptoSuite cs = CryptoSuite.Factory.getCryptoSuite();
//        client.setCryptoSuite(cs);
//
//        // We implement User interface below in code
//        // folder c:\tmp\creds should contain PeerAdmin.cert (extracted from HF's fabcar
//        // example's PeerAdmin json file)
//        // and PeerAdmin.priv (copy from
//        // cd96d5260ad4757551ed4a5a991e62130f8008a0bf996e4e4b84cd097a747fec-priv)
//        User user = new SampleUser("/home/akila/fabric-projects/fabric-samples/first-network/crypto-config/peerOrganizations/org1.example.com/users/User1@org1.example.com", "user");
//        // "Log in"
//        client.setUserContext(user);
//
//        // Instantiate channel
//        Channel channel = client.newChannel("mychannel");
//        channel.addPeer(client.newPeer("peer", "grpcs://localhost:7051"));
//        // It always wants orderer, otherwise even query does not work
//        channel.addOrderer(client.newOrderer("orderer", "grpcs://localhost:7050"));
//        channel.initialize();
//
//        // below is querying and setting new owner
//
//        String newOwner = "New Owner #" + new Random(new Date().getTime()).nextInt(999);
//        System.out.println("New owner is '" + newOwner + "'\n");
//
//        queryFabcar(channel, "CAR1");
//        updateCarOwner(channel, "CAR1", newOwner, false);
//
//        System.out.println("after request for transaction without commit");
//        queryFabcar(channel, "CAR1");
//        updateCarOwner(channel, "CAR1", newOwner, true);
//
//        System.out.println("after request for transaction WITH commit");
//        queryFabcar(channel, "CAR1");
//
//        System.out.println("Sleeping 5s");
//        Thread.sleep(5000); // 5secs
//        queryFabcar(channel, "CAR1");
//        System.out.println("all done");
//    }
//
//    private static void queryFabcar(Channel channel, String key) throws Exception {
//        QueryByChaincodeRequest req = client.newQueryProposalRequest();
//        ChaincodeID cid = ChaincodeID.newBuilder().setName("fabcar").build();
//        req.setChaincodeID(cid);
//        req.setFcn("queryCar");
//        req.setArgs(new String[] { key });
//        System.out.println("Querying for " + key);
//        Collection<ProposalResponse> resps = channel.queryByChaincode(req);
//        for (ProposalResponse resp : resps) {
//            String payload = new String(resp.getChaincodeActionResponsePayload());
//            System.out.println("response: " + payload);
//        }
//
//    }
//
//    private static void updateCarOwner(Channel channel, String key, String newOwner, Boolean doCommit)
//            throws Exception {
//        TransactionProposalRequest req = client.newTransactionProposalRequest();
//        ChaincodeID cid = ChaincodeID.newBuilder().setName("fabcar").build();
//        req.setChaincodeID(cid);
//        req.setFcn("changeCarOwner");
//        req.setArgs(new String[] { key, newOwner });
//        System.out.println("Executing for " + key);
//        Collection<ProposalResponse> resps = channel.sendTransactionProposal(req);
//        if (doCommit) {
//            channel.sendTransaction(resps);
//        }
//    }
//
//}
//
///***
// * Implementation of user. main business logic (as for fabcar example) is in
// * getEnrollment - get user's private key and cert
// * 
// */
//class SampleUser implements User {
//    private final String certFolder;
//    private final String userName;
//
//    public SampleUser(String certFolder, String userName) {
//        this.certFolder = certFolder;
//        this.userName = userName;
//    }
//
//    @Override
//    public String getName() {
//        return userName;
//    }
//
//    @Override
//    public Set<String> getRoles() {
//        return new HashSet<String>();
//    }
//
//    @Override
//    public String getAccount() {
//        return "";
//    }
//
//    @Override
//    public String getAffiliation() {
//        return "";
//    }
//
//    @Override
//    public Enrollment getEnrollment() {
//        return new Enrollment() {
//
//            @Override
//            public PrivateKey getKey() {
//                try {
//                    return loadPrivateKey(Paths.get(certFolder, userName + ".priv"));
//                } catch (Exception e) {
//                    return null;
//                }
//            }
//
//            @Override
//            public String getCert() {
//                try {
//                    return new String(Files.readAllBytes(Paths.get(certFolder, userName + ".cert")));
//                } catch (Exception e) {
//                    return "";
//                }
//            }
//
//        };
//    }
//
//    @Override
//    public String getMspId() {
//        return "Org1MSP";
//    }
//    /***
//     * loading private key from .pem-formatted file, ECDSA algorithm
//     * (from some example on StackOverflow, slightly changed)
//     * @param fileName - file with the key
//     * @return Private Key usable
//     * @throws IOException
//     * @throws GeneralSecurityException
//     */
//    public static PrivateKey loadPrivateKey(Path fileName) throws IOException, GeneralSecurityException {
//        PrivateKey key = null;
//        InputStream is = null;
//        try {
//            is = new FileInputStream(fileName.toString());
//            BufferedReader br = new BufferedReader(new InputStreamReader(is));
//            StringBuilder builder = new StringBuilder();
//            boolean inKey = false;
//            for (String line = br.readLine(); line != null; line = br.readLine()) {
//                if (!inKey) {
//                    if (line.startsWith("-----BEGIN ") && line.endsWith(" PRIVATE KEY-----")) {
//                        inKey = true;
//                    }
//                    continue;
//                } else {
//                    if (line.startsWith("-----END ") && line.endsWith(" PRIVATE KEY-----")) {
//                        inKey = false;
//                        break;
//                    }
//                    builder.append(line);
//                }
//            }
//            //
//            byte[] encoded = DatatypeConverter.parseBase64Binary(builder.toString());
//            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
//            KeyFactory kf = KeyFactory.getInstance("ECDSA");
//            key = kf.generatePrivate(keySpec);
//        } finally {
//            is.close();
//        }
//        return key;
//    }
//}