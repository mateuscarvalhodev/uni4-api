package model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "subjects")
public class Subject extends PanacheEntity {

  private String name;
  private Integer hours;

  @ManyToOne
  @JoinColumn(name = "semester_id", nullable = false)
  private Semester semester;

  public Subject() {
  }

  public Subject(String name, Integer hours, Semester semester) {
    this.name = name;
    this.hours = hours;
    this.semester = semester;
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getHours() {
    return hours;
  }

  public void setHours(Integer hours) {
    this.hours = hours;
  }

  public Semester getSemester() {
    return semester;
  }

  public void setSemester(Semester semester) {
    this.semester = semester;
  }
}
