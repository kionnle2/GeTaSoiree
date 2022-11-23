package yan.candaes.getasoiree.beans;

public class WsRetour {

    String request;
    boolean success;
    Object response;

    public WsRetour(String request, boolean success, String response) {
        this.request = request;
        this.success = success;
        this.response = response;
    }

    public WsRetour(String request, boolean success, boolean response) {
        this.request = request;
        this.success = success;
        this.response = response;
    }

    public boolean isresponseOk() {
        if ((boolean) response) {
            return true;
        } else {
            return false;
        }
    }
}
