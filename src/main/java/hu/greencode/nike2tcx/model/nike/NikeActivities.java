package hu.greencode.nike2tcx.model.nike;

import java.util.List;

public class NikeActivities {
    private List<NikeActivity> data;
    private Paging paging;

    public List<NikeActivity> getData() {
        return data;
    }

    public void setData(List<NikeActivity> data) {
        this.data = data;
    }

    public Paging getPaging() {
        return paging;
    }

    public void setPaging(Paging paging) {
        this.paging = paging;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("NikeActivities{");
        sb.append("data=").append(data);
        sb.append(", paging=").append(paging);
        sb.append('}');
        return sb.toString();
    }
}
