package model;

import jakarta.persistence.*;
import java.util.List;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
@Table(name = "curriculumn")
public class Curriculum extends PanacheEntity {

  private String academicYear;

  @ManyToOne
  @JoinColumn(name = "course_id", nullable = false)
  private Course course;

  @OneToMany(mappedBy = "curriculum", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Semester> semesters;

  public Curriculum() {
  }

  public Curriculum(String academicYear, Course course) {
    this.academicYear = academicYear;
    this.course = course;
  }

  public Long getId() {
    return id;
  }

  public String getAcademicYear() {
    return academicYear;
  }

  public void setAcademicYear(String academicYear) {
    this.academicYear = academicYear;
  }

  public Course getCourse() {
    return course;
  }

  public void setCourse(Course course) {
    this.course = course;
  }

  public List<Semester> getSemesters() {
    return semesters;
  }

  public void setSubjects(List<Semester> semesters) {
    this.semesters = semesters;
  }
}
