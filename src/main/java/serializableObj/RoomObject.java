package serializableObj;

import core.accountService.UserImpl;

import java.util.UUID;

/**
 * Created by Zver on 20.12.2015.
 */
public class RoomObject {
    UUID id;
    String clientName1;
    String clientName2;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getClientName1() {
        return clientName1;
    }

    public void setClientName1(String clientName1) {
        this.clientName1 = clientName1;
    }

    public String getClientName2() {
        return clientName2;
    }

    public void setClientName2(String clientName2) {
        this.clientName2 = clientName2;
    }
}
