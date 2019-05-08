/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mentoringsystems.chaincode;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.hyperledger.fabric.shim.ChaincodeBase;
import org.hyperledger.fabric.shim.ChaincodeStub;

public class Chaincode extends ChaincodeBase {

    /**
     * Init is called when initializing or updating chaincode. Use this to set
     * initial world state
     *
     * @param stub {@link ChaincodeStub} to operate proposal and ledger
     * @return Response with message and payload
     */
    @Override
    public Response init(ChaincodeStub stub) {
        String fcn = stub.getFunction();
        List<String> params = stub.getParameters();
        return newSuccessResponse("response message");
    }

    /**
     * Invoke is called to read from or write to the ledger
     *
     * @param stub {@link ChaincodeStub} to operate proposal and ledger
     * @return Response
     */
    @Override
    public Response invoke(ChaincodeStub stub) {
        try {
            // Extract the function and args from the transaction proposal
            String func = stub.getFunction();
            List<byte[]> params = stub.getArgs();
            switch (func) {
                case "set":
                    // Return result as success payload
                    return set(stub, params);
                case "get":
                    // Return result as success payload
                    return get(stub, params);
                case "delete":
                    // Return result as success payload
                    return delete(stub, params);
                default:
                    break;
            }
            //Error if unknown method
            return ChaincodeBase.newErrorResponse("Invalid invoke function name. Expecting one of: [\"set\", \"get\", \"delete\"");
        } catch (Throwable e) {
            return ChaincodeBase.newErrorResponse(e.getMessage());
        }
    }

    /**
     * Get receives the value of a key from the ledger
     *
     * @param stub {@link ChaincodeStub} to operate proposal and ledger
     * @param args key
     * @return Response with message and payload
     */
    private Response get(ChaincodeStub stub, List<byte[]> args) {
        if (args.size() != 1) {
            throw new RuntimeException("Incorrect arguments. Expecting a key");
        }

        String key = getKey(args.get(0));
        byte[] value = stub.getState(key);
        if (value == null) {
            throw new RuntimeException("Asset not found: " + key);
        }
        Response response = newSuccessResponse("Returned value for key : " + key, value);
        return response;
    }

    /**
     * Set stores the asset (both key and value) on the ledger. If the key
     * exists, it will override the value with the new one
     *
     * @param stub {@link ChaincodeStub} to operate proposal and ledger
     * @param args key and value
     * @return value
     */
    private Response set(ChaincodeStub stub, List<byte[]> args) {
        if (args.size() != 2) {
            throw new RuntimeException("Incorrect arguments. Expecting a key and a value");
        }
        String key = getKey(args.get(0));
        stub.putState(key, args.get(1));
        return newSuccessResponse("Succesfully set key : " + key, args.get(1));
    }

    /**
     * Delete the key from the world state
     *
     * @param stub {@link ChaincodeStub} to operate proposal and ledger
     * @param args key
     * @return Response with message and payload
     */
    private Response delete(ChaincodeStub stub, List<byte[]> args) {
        if (args.size() != 1) {
            return newErrorResponse("Incorrect number of arguments. Expecting a key");
        }
        String key = getKey(args.get(0));
        // Delete the key from the state in ledger
        stub.delState(key);
        return newSuccessResponse("Succesfully deleted key : " + key + "from the ledger", "success".getBytes(StandardCharsets.UTF_8));
    }

    //Convert key to String
    private String getKey(final byte[] keyBytes) {
        String key;
        try {
            key = new String(keyBytes, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException("UnsupportedEncodingException");
        }
        return key;
    }

    //Needs to be included in your main class defined in pom.xml
    public static void main(String[] args) {

        new Chaincode().start(args);
    }

}
