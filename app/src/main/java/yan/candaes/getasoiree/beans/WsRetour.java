package yan.candaes.getasoiree.beans;

public class WsRetour {

    String request;
    boolean success;
    boolean response;

    public WsRetour(String request, boolean success, boolean response) {
        this.request = request;
        this.success = success;
        this.response = response;
    }

    public boolean isSuccess() {
            return this.success;
    }
}
