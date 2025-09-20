package model;

import jakarta.persistence.*;
import java.util.List;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
@Table(name = "courses")
public class Course extends PanacheEntity {

  private String name;
  private String description;
  private Integer totalHours;

  @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Curriculum> curriculumn;

  public Course() {
  }

  public Course(String name, String description, Integer totalHours) {
    this.name = name;
    this.description = description;
    this.totalHours = totalHours;
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

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Integer getTotalHours() {
    return totalHours;
  }

  public void setTotalHours(Integer totalHours) {
    this.totalHours = totalHours;
  }

  public List<Curriculum> getCurriculumn() {
    return curriculumn;
  }

  public void setCurriculumn(List<Curriculum> curriculumn) {
    this.curriculumn = curriculumn;
  }
}
