package hu.greencode.nike2tcx.model.nike;

public class Paging {
    private String next;
    private String previous;

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Paging{");
        sb.append("next='").append(next).append('\'');
        sb.append(", previous='").append(previous).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
