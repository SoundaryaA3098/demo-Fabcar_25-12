package org.example.demoFabcar.service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.PrivateKey;
import java.util.Properties;
import java.util.Set;

import org.example.demoFabcar.dto.DTOData;
import org.example.demoFabcar.dto.DTOResponseModel;
import org.hyperledger.fabric.gateway.Contract;
import org.hyperledger.fabric.gateway.Gateway;
import org.hyperledger.fabric.gateway.Network;
import org.hyperledger.fabric.gateway.Wallet;
import org.hyperledger.fabric.gateway.Wallet.Identity;
import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.NetworkConfig;
import org.hyperledger.fabric.sdk.User;
import org.hyperledger.fabric.sdk.NetworkConfig.CAInfo;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric.sdk.security.CryptoSuiteFactory;
import org.hyperledger.fabric_ca.sdk.EnrollmentRequest;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.hyperledger.fabric_ca.sdk.RegistrationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Service
public class ExecuteChaincodeService {

	Logger logger = LoggerFactory.getLogger(ExecuteChaincodeService.class);
	//String connectionProfilePath = "/home/akila/fabric-projects/fabric-samples/first-network/contemplate_org1&2/connection-org1.yaml";
    String connectionProfilePath = "/home/akila/Documents/Fabric-java-sdk-webApp/demo-Fabcar/src/main/resources/connection-org_azure.yaml";
   //  String connectionProfilePath = "/home/akila/fabric-projects/fabric-samples/first-network/connection-org1.yaml";
	Path walletPath = Paths.get("wallet");
	Wallet wallet;
	Path networkConfigPath = Paths.get(connectionProfilePath);

	@RequestMapping(value = "/invoke", method = RequestMethod.POST)
	public DTOResponseModel invokeChaincode(DTOData dtoInvoke) throws Exception {
		try {
			wallet = Wallet.createFileSystemWallet(walletPath);

			System.out.println("user name:::" + dtoInvoke.getAdmin());
			
			Gateway.Builder builder = Gateway.createBuilder();
			builder.identity(wallet,"admin").networkConfig(networkConfigPath).discovery(true);

			try (Gateway gateway = builder.connect()) {
				Network network = gateway.getNetwork("composerchannelrest");
				Contract contract = network.getContract("CreatePoolDemoCC");
				//Contract contract = network.getContract(dtoInvoke.getChaincodeName());
				//System.out.println("chaincode name:::" + dtoInvoke.getChaincodeName());
				System.out.println("chaincode name:::" + contract);
				// Contract contract = network.getContract("fabcar");

				byte[] result;

//				contract.submitTransaction(dtoInvoke.getChaincodeMtd(), "CAR16", "VW", "Polo", "Grey", "thara");
//				System.out.println("chaincode mtd  name:::" + dtoInvoke.getChaincodeMtd());
//				System.out.println("chaincode mtd args name:::" + dtoInvoke.getArgs());

				//contract.submitTransaction(dtoInvoke.getChaincodeMtd(), dtoInvoke.getArgs());
				System.out.println("chaincode mtd  name:::" + dtoInvoke.getChaincodeMtd());
				System.out.println("chaincode mtd args name:::::::" + dtoInvoke.getArgs());

				
			

			contract.submitTransaction("Createpool","598", "KBI", "loans of 1000", "intain","23-2-20","30-8-20","8","Pending","30-12-20","36456","3000","#12553","#45666","25-12-19");
				
				
				//contract.submitTransaction("SaveTranche","123", "145", "TrancheA", "10","100","3","2","fixed","12","366","100","2","6","12","4");
//				result = contract.evaluateTransaction("queryCar", "CAR16");
//				System.out.println(new String(result));
//				result = contract.evaluateTransaction("queryCar", "CAR17");
//				System.out.println(new String(result));

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void queryChaincode(String username, String chaincodeName, String chaincodeMtd) throws Exception {
		try {

			wallet = Wallet.createFileSystemWallet(walletPath);
		//	System.out.println("wallet:::"+wallet.get("user7"));
			Gateway.Builder builder = Gateway.createBuilder();
			builder.identity(wallet,"admin").networkConfig(networkConfigPath).discovery(true);
			System.out.println("builder::::"+builder.toString());
			try (Gateway gateway = builder.connect()) {

				System.out.println("gateway log msg :::" + gateway.getIdentity());
				// get the network and contract
				Network network = gateway.getNetwork("composerchannelrest");
				//Network network = gateway.getNetwork("mychannel");
				//Contract contract = network.getContract(chaincodeName); //TrancheCC 
			// Contract contract = network.getContract("fabcar");
			//Contract contract = network.getContract("CreatePoolCC");
			Contract contract = network.getContract("CreatePoolDemoCC");

				byte[] result;
			//	result = contract.evaluateTransaction(chaincodeMtd);
				// result = contract.evaluateTransaction("queryAllCars"); //GetAllTranches
				result = contract.evaluateTransaction("GetAllpools"); //GetAllTranches
				System.out.println("QUERY ALL Tranche.....");
				System.out.println(new String(result));

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
			
	
	public void queryByArgs(String username, String chaincodeName, String chaincodeMtd, String args[])
			throws Exception {
		try {

			wallet = Wallet.createFileSystemWallet(walletPath);
			Gateway.Builder builder = Gateway.createBuilder();
			// /home/emulya_api path/  work/src/github.com/hyperledger/fabric-ca/clients/admin/msp/keystore  ///client
			// work/src/github.com/hyperledger/fabric-ca/server/msp/keystore                                 //server   
			builder.identity(wallet,"user1").networkConfig(networkConfigPath).discovery(true);

			try (Gateway gateway = builder.connect()) {

				System.out.println("gateway log msg :::" + gateway);
				// get the network and contract
				//Network network = gateway.getNetwork("mychannel");
				Network network = gateway.getNetwork("composerchannelrest");
				
				Contract contract = network.getContract(chaincodeName);
				// Contract contract = network.getContract("fabcar");
				byte[] result;
				System.out.println("QUERY ALL BY ARGS[].....");
				
				result = contract.evaluateTransaction(chaincodeMtd, args);
				// result = contract.evaluateTransaction("queryCar", "CAR12");
				System.out.println(new String(result));

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public DTOResponseModel enrollAdmin(DTOData dtoadmin) {

		try {
			System.out.println("orgName:::::"+dtoadmin.getOrgName());
     		System.out.println("Admin Name::::"+dtoadmin.getAdmin());
			// Create a CA client for interacting with the CA.
			Properties props = new Properties();
			props.put("pemFile",
					"crypto-config/peerOrganizations/"+dtoadmin.getOrgName().toLowerCase()+"/ca/ca."+dtoadmin.getOrgName().toLowerCase()+"-cert.pem");
				props.put("allowAllHostNames", "true");
				props.put("clientCertFile","crypto-config/peerOrganizations/org0/users/User1@org0/tls/client.crt");
				props.put("clientKeyFile","crypto-config/peerOrganizations/org0/users/User1@org0/tls/client.key");
//				props.put("pemFile",
//						"crypto-config/peerOrganizations/"+dtoadmin.getOrgName().toLowerCase()+"/users/Admin@"+dtoadmin.getOrgName().toLowerCase()+"/msp/admincerts/Admin@"+dtoadmin.getOrgName().toLowerCase()+"-cert.pem");
//					props.put("allowAllHostNames", "true");
				
					
//				props.put("pemFile",
//						"crypto-config/peerOrganizations/"+dtoadmin.getOrgName().toLowerCase()+"/peers/peer0."+dtoadmin.getOrgName().toLowerCase()+"/tls/ca.crt");
//					props.put("allowAllHostNames", "true");
//			
			
			
//			props.put("pemFile",
//				"crypto-config/peerOrganizations/"+dtoadmin.getOrgName().toLowerCase()+".example.com/ca/ca."+dtoadmin.getOrgName().toLowerCase()+".example.com-cert.pem");
//			props.put("allowAllHostNames", "true");
			
			//path azure ///home/intain/work/src/github.com/hyperledger/fabric-ca/server/ca-cert.pem
			//props.put("pemFile","work/src/github.com/hyperledger/fabric-ca/server/ca-cert.pem");
//			props.put("pemFile","Azure-cert/ca-cert.pem");
//			props.put("allowAllHostNames", "true");
			//HFCAClient caClient = HFCAClient.createNewInstance("https://35.209.87.55:7054", props);//CA Org1 port
			//azure vm
			HFCAClient caClient = HFCAClient.createNewInstance("https://104.215.59.188:7054", props);// CA Org2 8054 port
			CryptoSuite cryptoSuite = CryptoSuiteFactory.getDefault().getCryptoSuite();
			caClient.setCryptoSuite(cryptoSuite);

			// Create a wallet for managing identities
			//Wallet wallet = Wallet.createFileSystemWallet(Paths.get("wallet"));
			
			Wallet wallet = Wallet.createFileSystemWallet(Paths.get("wallet"));
			// Check to see if we've already enrolled the admin user.
			boolean adminExists = wallet.exists("admin");
	        if (adminExists) {
	            System.out.println("An identity for the admin user \"admin\" already exists in the wallet");
	            //return;
	        }

	        // Enroll the admin user, and import the new identity into the wallet.
	        final EnrollmentRequest enrollmentRequestTLS = new EnrollmentRequest();
	        
	      //  enrollmentRequestTLS.addHost("localhost");
	       enrollmentRequestTLS.addHost("ca.org0");
	        enrollmentRequestTLS.setProfile("tls");
//	        enrollmentRequestTLS.setCsr("-----BEGIN CERTIFICATE-----\n" + 
//	        		"MIIB9TCCAZygAwIBAgIRAN/fB3ZTeWgVyjpcc9PBMGAwCgYIKoZIzj0EAwIwWzEL\n" + 
//	        		"MAkGA1UEBhMCVVMxEzARBgNVBAgTCkNhbGlmb3JuaWExFjAUBgNVBAcTDVNhbiBG\n" + 
//	        		"cmFuY2lzY28xDTALBgNVBAoTBG9yZzAxEDAOBgNVBAMTB2NhLm9yZzAwHhcNMTkw\n" + 
//	        		"OTEyMjMxMzAwWhcNMjkwOTA5MjMxMzAwWjBPMQswCQYDVQQGEwJVUzETMBEGA1UE\n" + 
//	        		"CBMKQ2FsaWZvcm5pYTEWMBQGA1UEBxMNU2FuIEZyYW5jaXNjbzETMBEGA1UEAwwK\n" + 
//	        		"QWRtaW5Ab3JnMDBZMBMGByqGSM49AgEGCCqGSM49AwEHA0IABJuiakoUvMBQaNeV\n" + 
//	        		"ZRXDfcOeQ7zFnCSd6fzp2F9mLzBEFsna7uIecrMVz7ItfR3sBFr8K8ewVmwmz9Je\n" + 
//	        		"ONnfPOCjTTBLMA4GA1UdDwEB/wQEAwIHgDAMBgNVHRMBAf8EAjAAMCsGA1UdIwQk\n" + 
//	        		"MCKAIByjRkxG1X0UjC5sjC1SKGg2F9NSOQfPmgcTFOnXgZdsMAoGCCqGSM49BAMC\n" + 
//	        		"A0cAMEQCIDCgXf05laqiO2nS5NN3Ygv4EzRDKhaobvoHcMw9eEgBAiACoZnZcv/t\n" + 
//	        		"I/a/u3mvKsU7a8sTpLwh3d5B9/siPS4/Hg==\n" + 
//	        		"-----END CERTIFICATE-----");
//	        enrollmentRequestTLS.setKeyPair(new KeyPa);
	        Enrollment enrollment = caClient.enroll(dtoadmin.getAdmin(), "adminpw", enrollmentRequestTLS);
	       	        
	        Identity user = Identity.createIdentity("Org0MSP", enrollment.getCert(), enrollment.getKey());
	        wallet.put("admin", user);
			System.out.println("Successfully enrolled user \"admin\" and imported it into the wallet");
		}
	
			catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public DTOResponseModel registerUser(DTOData dtoregister) {
		try {

			// Create a CA client for interacting with the CA.
			Properties props = new Properties();
			
			props.put("pemFile",
					"crypto-config/peerOrganizations/"+dtoregister.getOrgName().toLowerCase()+"/ca/ca."+dtoregister.getOrgName().toLowerCase()+"-cert.pem");
				props.put("allowAllHostNames", "true");
				
//				props.put("pemFile",
//						"crypto-config/peerOrganizations/"+dtoregister.getOrgName().toLowerCase()+"/users/Admin@"+dtoregister.getOrgName().toLowerCase()+"/msp/admincerts/Admin@"+dtoregister.getOrgName().toLowerCase()+"-cert.pem");
//					props.put("allowAllHostNames", "true");
					props.put("clientCertFile","crypto-config/peerOrganizations/org0/users/User1@org0/tls/client.crt");
					props.put("clientKeyFile","crypto-config/peerOrganizations/org0/users/User1@org0/tls/client.key");
					
					
			//root certificat ...CA tls enabled
//				props.put("pemFile",
//						"crypto-config/peerOrganizations/"+dtoregister.getOrgName().toLowerCase()+"/peers/peer0."+dtoregister.getOrgName().toLowerCase()+"/tls/ca.crt");
//					props.put("allowAllHostNames", "true");
				
//			props.put("pemFile", "crypto-config/peerOrganizations/" + dtoregister.getOrgName().toLowerCase()
//					+ ".example.com/ca/ca." + dtoregister.getOrgName().toLowerCase() + ".example.com-cert.pem");
//			props.put("allowAllHostNames", "true");
			
			// azure
//			props.put("pemFile","Azure-cert/ca-cert.pem");
//			props.put("allowAllHostNames", "true");
			
			//HFCAClient caClient = HFCAClient.createNewInstance("https://35.209.87.55:7054", props);
			//HFCAClient caClient = HFCAClient.createNewInstance("https://localhost:7054", props);
			
			HFCAClient caClient = HFCAClient.createNewInstance("https://104.215.59.188:7054", props); //azure webclient
			CryptoSuite cryptoSuite = CryptoSuiteFactory.getDefault().getCryptoSuite();
			caClient.setCryptoSuite(cryptoSuite);

			// Create a wallet for managing identities
			Wallet wallet = Wallet.createFileSystemWallet(Paths.get("wallet"));

			// Check to see if we've already enrolled the user.
			boolean userExists = wallet.exists(dtoregister.getUsername());
			if (userExists) {
				System.out.println(
						"An identity for the user  " + dtoregister.getUsername() + "already exists in the wallet");
				// return;
			}

			userExists = wallet.exists("admin");
			if (!userExists) {
				System.out.println("\"admin\" needs to be enrolled and added to the wallet first");
				// return;
			}

			Identity adminIdentity = wallet.get("admin");

			System.out.println("Affilication" + adminIdentity.getMspId());

			User admin = new User() {

				@Override
				public String getName() {
					return "admin";
				}

				@Override
				public Set<String> getRoles() {
					return null;
				}

				@Override
				public String getAccount() {
					return null;
				}

				@Override
				public String getAffiliation() { //check  ...IMP					
					return dtoregister.getOrgName().toLowerCase() + ".department1";
				}

				@Override
				public Enrollment getEnrollment() {
					return new Enrollment() {

						@Override
						public PrivateKey getKey() {
							return adminIdentity.getPrivateKey();
						}

						@Override
						public String getCert() {
							return adminIdentity.getCertificate();
						}
					};
				}

				@Override
				public String getMspId() {
					return adminIdentity.getMspId();
				}

			};
			System.out.println("orgMSP:::::::::"+admin.getMspId());
			// Register the user, enroll the user, and import the new identity into the
			// wallet.
			RegistrationRequest registrationRequest = new RegistrationRequest(dtoregister.getUsername());
			registrationRequest.setAffiliation(admin.getAffiliation());
			String enrollmentSecret = caClient.register(registrationRequest, admin);
			Enrollment enrollment = caClient.enroll(dtoregister.getUsername(), enrollmentSecret);
			Identity user = Identity.createIdentity(admin.getMspId(), enrollment.getCert(), enrollment.getKey());
			wallet.put(dtoregister.getUsername(), user);
			System.out.println(
					"Successfully enrolled user " + dtoregister.getUsername() + " and imported it into the wallet");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

}

