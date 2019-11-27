package com.revature.bean;

import org.springframework.stereotype.Component;

@Component
public class TrainingLocationDto {
  private String trainingLocationName;

  public String getTrainingLocationName() {
    return trainingLocationName;
  }

  public void setTrainingLocationName(String trainingLocationName) {
    this.trainingLocationName = trainingLocationName;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result =
        prime * result + ((trainingLocationName == null) ? 0 : trainingLocationName.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    TrainingLocationDto other = (TrainingLocationDto) obj;
    if (trainingLocationName == null) {
      if (other.trainingLocationName != null) {
        return false;
      }
    } else if (!trainingLocationName.equals(other.trainingLocationName)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "TrainingLocationDto [trainingLocationName=" + trainingLocationName + "]";
  }

  public TrainingLocationDto(String trainingLocationName) {
    super();
    this.trainingLocationName = trainingLocationName;
  }

  public TrainingLocationDto() {
    super();
  }

}
