package model.da.aerospike;
import com.aerospike.client.AerospikeClient;
import com.aerospike.client.Record;
import com.aerospike.client.AerospikeException;
import com.aerospike.client.Key;
import com.aerospike.client.Host;

import com.aerospike.client.Bin;
import com.aerospike.client.policy.ClientPolicy;
import com.aerospike.client.policy.RecordExistsAction;
import com.aerospike.client.policy.WritePolicy;


import java.util.Scanner;

public class UserDA {


    private AerospikeClient client;

    private String username,password,agency;

    public UserDA(AerospikeClient c) {
        this.client = c;
    }

    public Scanner scanner;

    public static void main(String[] args) {
        Host[] hosts = new com.aerospike.client.Host[] {
                new Host("192.168.150.201", 3000),
                new Host("192.168.150.201", 3001),
                new Host("192.168.150.201", 3002)


        };
        AerospikeClient client = new AerospikeClient(new ClientPolicy(), hosts);
        //AerospikeClient client = new AerospikeClient("192.168.150.201", 3000);

        //new UserDA(client).createUser("farshad","noravesh","safarPeima");
        new UserDA(client).getUser("farshad");
    }


    public void createUser(String username,String password,String agency) throws AerospikeException {

                System.out.println("creating user:"+username);
                this.username=username;
                this.password=password;
                this.agency=agency;


                // Write record
                WritePolicy wPolicy = new WritePolicy();
                wPolicy.recordExistsAction = RecordExistsAction.UPDATE;

                Key key = new Key("test", "users", username);
                Bin bin1 = new Bin("username", username);
                Bin bin2 = new Bin("password", password);
                Bin bin3 = new Bin("agency", agency);


               client.put(wPolicy, key, bin1, bin2, bin3);
               // client.put(null, key, bin1, bin2, bin3);


                Record record = client.get(null, key);

                System.out.println("username in database is:"+record.getValue("username"));

                client.close();

    }



    public void getUser(String username) throws AerospikeException {
        this.username=username;
        Record userRecord = null;
        Key userKey = null;

        userKey = new Key("test", "users", this.username);
        userRecord = client.get(null, userKey);
        System.out.println("username="+userRecord.getValue("username"));
        System.out.println("password="+userRecord.getValue("password"));
        System.out.println("agency="+userRecord.getValue("agency"));

    }

}
