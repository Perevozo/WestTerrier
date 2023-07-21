package items;

import java.time.LocalDate;
import java.util.Date;

public class Dog {
    private String name;
    private LocalDate birthday;
    private String sex;
    private String breeder;
    private String owner;
    private Integer chipNumber;
    private Integer motherID;
    private Integer fatherID;
    private String westURL;



    public String getWestURL() {
        return westURL;
    }

    public void setWestURL(String westURL) {
        this.westURL = westURL;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBreeder() {
        return breeder;
    }

    public void setBreeder(String breeder) {
        this.breeder = breeder;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Integer getChipNumber() {
        return chipNumber;
    }

    public void setChipNumber(Integer chipNumber) {
        this.chipNumber = chipNumber;
    }

    public Integer getMotherID() {
        return motherID;
    }

    public void setMotherID(Integer motherID) {
        this.motherID = motherID;
    }

    public Integer getFatherID() {
        return fatherID;
    }

    public void setFatherID(Integer fatherID) {
        this.fatherID = fatherID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
