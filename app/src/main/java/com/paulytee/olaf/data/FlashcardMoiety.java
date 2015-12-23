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

public class FlashcardMoiety
{
    @SerializedName("_id")
    @Expose
    private String _id;
    @SerializedName("_rev")
    @Expose
    private String _rev;
    @SerializedName("level")
    @Expose
    private Integer level;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("script")
    @Expose
    private String script;
    @SerializedName("roman")
    @Expose
    private String roman;

    public String get_id() { return _id; }
    public void set_id(String s) { _id = s; }

    public String get_rev() { return _rev; }
    public void set_rev(String s) { _rev = s; }

    public Integer getLevel() { return level; }
    public void setLevel(Integer i) { level = i; }

    public String getType() { return type; }
    public void setType(String s) { type = s; }

    public String getScript() { return script; }
    public void setScript(String s) { script = s; }

    public String getRoman() { return roman; }
    public void setRoman(String s) { roman = s; }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(_id).append(_rev).append(level).append(type).append(script).append(roman).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof FlashcardMoiety)) {
            return false;
        }
        FlashcardMoiety rhs = ((FlashcardMoiety) other);
        return new EqualsBuilder().append(_id, rhs._id).append(_rev, rhs._rev).append(level, rhs.level)
                .append(type, rhs.type).append(script, rhs.script).append(roman, rhs.roman).isEquals();
    }

}
