package net.thucydides.maven.plugin.test.example;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * This class needed for test that
 * given steps generation works with
 * inherited fields for Person child class
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class PersonParent {
    @XmlElement
    private int personParentId;

    public int getPersonParentId() {
        return personParentId;
    }

    public void setPersonParentId(int personParentId) {
        this.personParentId = personParentId;
    }
}