import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

@Entity
public class Person {
    private String id;
    private String name;
    private Date birthday;
//    private Person spouse;
//    private ArrayList<Person> kids = new ArrayList();
//    private Gender gender;


//    public Person(String id, String name, Date birthday, Person spouse, ArrayList<Person> kids) {
//        this.id = id;
//        this.name = name;
//        this.birthday = birthday;
//        this.spouse = spouse;
//        this.kids = kids;
//
//    }
public Person(String id, String name, Date birthday) {
    this.id = id;
    this.name = name;
    this.birthday = birthday;

}

    public Person() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    @Id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

//    public Person getSpouse() {
//        return spouse;
//    }
//
//    public void setSpouse(Person spouse) {
//        this.spouse = spouse;
//    }
//
//    public ArrayList<Person> getKids() {
//        return kids;
//    }
//
//    public void setKids(ArrayList<Person> kids) {
//        this.kids = kids;
//    }
//
//    public Gender getGender() {
//        return gender;
//    }
//
//    public void setGender(Gender gender) {
//        this.gender = gender;
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(id, person.id) &&
                Objects.equals(name, person.name) &&
                Objects.equals(birthday, person.birthday);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, birthday);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Person{");
        sb.append("id='").append(id).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", birthday=").append(birthday);
        sb.append('}');
        return sb.toString();
    }
}
