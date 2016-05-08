package hu.greencode.nike2tcx.model.nike;

public class Tag {
    private String tagType;
    private String tagValue;

    public String getTagType() {
        return tagType;
    }

    public void setTagType(String tagType) {
        this.tagType = tagType;
    }

    public String getTagValue() {
        return tagValue;
    }

    public void setTagValue(String tagValue) {
        this.tagValue = tagValue;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Tag{");
        sb.append("tagType='").append(tagType).append('\'');
        sb.append(", tagValue='").append(tagValue).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
