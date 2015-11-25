package com.paulytee.olaf.data;

/**
 * Created by Pauly T on 11/19/2015.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

public class Flashcard {

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
    private Boolean active;
    @SerializedName("forward")
    @Expose
    private Boolean forward;
    @SerializedName("label")
    @Expose
    private String label;
    @SerializedName("text")
    @Expose
    private String text;

    public Integer getItemID() {
        return itemID;
    }
    public void setItemID(Integer itemID) {
        this.itemID = itemID;
    }

    public Integer getLevel() {
        return level;
    }
    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public Boolean getActive() {
        return active;
    }
    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getForward() {
        return forward;
    }
    public void setForward(Boolean forward) {
        this.forward = forward;
    }

    public String getLabel() {
        return label;
    }
    public void setLabel(String label) {
        this.label = label;
    }

    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(itemID).append(level).append(type).append(active).append(forward).append(label).append(text).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Flashcard) == false) {
            return false;
        }
        Flashcard rhs = ((Flashcard) other);
        return new EqualsBuilder().append(itemID, rhs.itemID).append(level, rhs.level).append(type, rhs.type).append(active, rhs.active).append(forward, rhs.forward).append(label, rhs.label).append(text, rhs.text).isEquals();
    }

}
