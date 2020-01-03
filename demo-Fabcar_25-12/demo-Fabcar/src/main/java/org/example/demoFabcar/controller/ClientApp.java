package org.example.demoFabcar.controller;


import org.example.demoFabcar.dto.DTOResponseModel;
import org.example.demoFabcar.service.ExecuteChaincodeService;
import org.example.demoFabcar.dto.DTOData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/v1")

public class ClientApp {
	Logger logger = LoggerFactory.getLogger(ClientApp.class);
	
	@Autowired
	private ExecuteChaincodeService chaincodeService;
	DTOResponseModel response;
	
	@PostMapping(value = "/invoke")
	public ResponseEntity<?> invoke(@RequestBody DTOData dtoInvoke ) throws Exception {
		
		 response =chaincodeService.invokeChaincode(dtoInvoke);
		return ResponseEntity.ok("Chaincode invocation successful");
	}
	
	@RequestMapping(value = "/query", method = RequestMethod.GET)
	public ResponseEntity<?> query(@RequestParam String username,@RequestParam String chaincodeName,@RequestParam String chaincodeMtd) throws Exception {
		chaincodeService.queryChaincode(username,chaincodeName,chaincodeMtd);
		
		return ResponseEntity.ok("Querying chaincode successful");
	}
	
	@RequestMapping(value = "/queryByArgs", method = RequestMethod.GET)
	public ResponseEntity<?> query(@RequestParam String username,@RequestParam String chaincodeName,@RequestParam String chaincodeMtd,@RequestParam String args[]) throws Exception {
		chaincodeService.queryByArgs(username,chaincodeName,chaincodeMtd,args);
		
		return ResponseEntity.ok("Querying chaincode successful");
	}
	@PostMapping(value="/enrollAdmin")
	public ResponseEntity<?>  enrollAdmin(@RequestBody DTOData dtoadmin)
	{
		 response=chaincodeService.enrollAdmin(dtoadmin);
		 
		return ResponseEntity.ok("Enroll Admin successful");
	}
	@PostMapping(value="/registerUser")
	public ResponseEntity<?>  registerUser(@RequestBody DTOData dtoregister)
	{
		 response=chaincodeService.registerUser(dtoregister);
		 
		return ResponseEntity.ok("Register User successful");
	}

	}