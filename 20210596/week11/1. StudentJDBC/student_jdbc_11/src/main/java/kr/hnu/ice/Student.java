package kr.hnu.ice;

public class Student {
    private int id;
    private String name;
    private String univ;
    private String birthdate;
    private String email;

    public Student() {
    }

    public Student(int id, String name, String univ, String birthdate, String email) {
        this.id = id;
        this.name = name;
        this.univ = univ;
        this.birthdate = birthdate;
        this.email = email;
    }

    public Student(String name, String univ, String birthdate, String email) {
        this.name = name;
        this.univ = univ;
        this.birthdate = birthdate;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    
    public String getUniv() {
        return univ;
    }

    public void setUniv(String univ) {
        this.univ = univ;
    }


    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}