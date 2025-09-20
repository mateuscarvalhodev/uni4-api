package model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "subjects")
public class Subject extends PanacheEntity {

  private String name;
  private Integer hours;

  @ManyToOne
  @JoinColumn(name = "curriculum_id", nullable = false)
  private Curriculum curriculum;

  public Subject() {
  }

  public Subject(String name, Integer hours, Curriculum curriculum) {
    this.name = name;
    this.hours = hours;
    this.curriculum = curriculum;
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

  public Curriculum getCurriculum() {
    return curriculum;
  }

  public void setCurriculum(Curriculum curriculum) {
    this.curriculum = curriculum;
  }
}
