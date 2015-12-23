package com.paulytee.olaf.data;

/**
 * Created by Pauly T on 11/19/2015.
 * This is the basic flashcard object.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Flashcard
{
    @SerializedName("itemID")
    @Expose
    private Integer itemID;
    @SerializedName("level")
    @Expose
    private Integer level;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("active")
    @Expose
    private Boolean active = true;
    @SerializedName("nativeFirst")
    @Expose
    private Boolean nativeFirst = true;
    @SerializedName("label")
    @Expose
    private String label;
    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("foreign")
    @Expose
    private String foreign;
    @SerializedName("foreignRoman")
    @Expose
    private String foreignRoman;

    public Flashcard(String ntv, String frn, String frnRoman, Integer lvl) {
        text = ntv;
        foreign = frn;
        foreignRoman = frnRoman;
        level = lvl;
    }

    public Integer getItemID() { return itemID; }
    public void setItemID(Integer i) { itemID = i; }

    public Integer getLevel() { return level; }
    public void setLevel(Integer i) { level = i; }

    public String getType() { return type; }
    public void setType(String s) { type = s; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean b) { active = b; }

    public Boolean getNativeFirst() { return nativeFirst; }
    public void setNativeFirst(Boolean b) { nativeFirst = b; }

    public String getLabel() { return label; }
    public void setLabel(String s) { label = s; }

    public String getText() { return text; }
    public void setText(String s) { text = s; }

    public String getForeign() { return foreign; }
    public void setForeign(String s) { foreign = s; }

    public String getForeignRoman() { return foreignRoman; }
    public void setForeignRoman(String s) { foreignRoman = s; }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(itemID).append(level).append(type).append(active)
              .append(nativeFirst).append(label).append(text).append(foreign).append(foreignRoman).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Flashcard)) {
            return false;
        }
        Flashcard rhs = ((Flashcard) other);
        return new EqualsBuilder().append(itemID, rhs.itemID).append(level, rhs.level)
            .append(type, rhs.type).append(active, rhs.active).append(nativeFirst, rhs.nativeFirst)
            .append(label, rhs.label).append(text, rhs.text).append(foreign, rhs.foreign)
            .append(foreignRoman, rhs.foreignRoman).isEquals();
    }

}
