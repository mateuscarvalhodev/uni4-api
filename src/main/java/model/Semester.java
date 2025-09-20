package model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "semesters")
public class Semester extends PanacheEntity {

  @Column(nullable = false)
  private Integer number;

  @ManyToOne
  @JoinColumn(name = "curriculum_id", nullable = false)
  private Curriculum curriculum;

  @OneToMany(mappedBy = "semester", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Subject> subjects = new ArrayList<>();

  public Semester() {
  }

  public Semester(Integer number, Curriculum curriculum) {
    this.number = number;
    this.curriculum = curriculum;
  }

  public Long getId() {
    return id;
  }

  public Integer getNumber() {
    return number;
  }

  public void setNumber(Integer number) {
    this.number = number;
  }

  public Curriculum getCurriculum() {
    return curriculum;
  }

  public void setCurriculum(Curriculum curriculum) {
    this.curriculum = curriculum;
  }

  public List<Subject> getSubjects() {
    return subjects;
  }

  public void setSubjects(List<Subject> subjects) {
    this.subjects = subjects;
  }
}
