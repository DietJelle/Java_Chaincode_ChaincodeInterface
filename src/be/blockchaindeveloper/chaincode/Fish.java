/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.blockchaindeveloper.chaincode;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

/**
 *
 * @author jellediet
 */
@DataType()
public class Fish implements Serializable {

    private static final long serialVersionUID = 1L;
    @Property()
    private UUID id;
    @Property()
    private String type;
    @Property()
    private double weight;
    @Property()
    private BigDecimal price;
    @Property()
    private final String docType = "Fish";

    public String getDocType() {
        return docType;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String toJSONString() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(Fish.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static Fish fromJSONString(String json) {
        ObjectMapper mapper = new ObjectMapper();
        Fish fish = null;
        try {
            fish = mapper.readValue(json, Fish.class);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(Fish.class.getName()).log(Level.SEVERE, null, ex);
        }
        return fish;
    }

}
