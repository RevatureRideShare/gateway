package com.revature.bean;

public class HouseLocationDto {
  private String address1;
  private String address2;
  private String city;
  private String state;
  private String zipCode;
  private String housingLocationName;
  private TrainingLocationDto trainingLocationDto;

  public String getAddress1() {
    return address1;
  }

  public void setAddress1(String address1) {
    this.address1 = address1;
  }

  public String getAddress2() {
    return address2;
  }

  public void setAddress2(String address2) {
    this.address2 = address2;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public String getZipCode() {
    return zipCode;
  }

  public void setZipCode(String zipCode) {
    this.zipCode = zipCode;
  }

  public String getHousingLocationName() {
    return housingLocationName;
  }

  public void setHousingLocationName(String housingLocationName) {
    this.housingLocationName = housingLocationName;
  }

  public TrainingLocationDto getTrainingLocationDto() {
    return trainingLocationDto;
  }

  public void setTrainingLocationDto(TrainingLocationDto trainingLocationDto) {
    this.trainingLocationDto = trainingLocationDto;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((address1 == null) ? 0 : address1.hashCode());
    result = prime * result + ((address2 == null) ? 0 : address2.hashCode());
    result = prime * result + ((city == null) ? 0 : city.hashCode());
    result = prime * result + ((housingLocationName == null) ? 0 : housingLocationName.hashCode());
    result = prime * result + ((state == null) ? 0 : state.hashCode());
    result = prime * result + ((trainingLocationDto == null) ? 0 : trainingLocationDto.hashCode());
    result = prime * result + ((zipCode == null) ? 0 : zipCode.hashCode());
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
    HouseLocationDto other = (HouseLocationDto) obj;
    if (address1 == null) {
      if (other.address1 != null) {
        return false;
      }
    } else if (!address1.equals(other.address1)) {
      return false;
    }
    if (address2 == null) {
      if (other.address2 != null) {
        return false;
      }
    } else if (!address2.equals(other.address2)) {
      return false;
    }
    if (city == null) {
      if (other.city != null) {
        return false;
      }
    } else if (!city.equals(other.city)) {
      return false;
    }
    if (housingLocationName == null) {
      if (other.housingLocationName != null) {
        return false;
      }
    } else if (!housingLocationName.equals(other.housingLocationName)) {
      return false;
    }
    if (state == null) {
      if (other.state != null) {
        return false;
      }
    } else if (!state.equals(other.state)) {
      return false;
    }
    if (trainingLocationDto == null) {
      if (other.trainingLocationDto != null) {
        return false;
      }
    } else if (!trainingLocationDto.equals(other.trainingLocationDto)) {
      return false;
    }
    if (zipCode == null) {
      if (other.zipCode != null) {
        return false;
      }
    } else if (!zipCode.equals(other.zipCode)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "HouseLocationDto [address1=" + address1 + ", address2=" + address2 + ", city=" + city
        + ", state=" + state + ", zipCode=" + zipCode + ", housingLocationName="
        + housingLocationName + ", trainingLocationDto=" + trainingLocationDto + "]";
  }

  /**
   * HouseLocationDto is a Data Transfer Object that takes in a housing location object from angular
   * and translates it to Java.
   * 
   * @param address1 Housing location address line one (Street, etc.).
   * @param address2 Housing location address line two (building number, etc.).
   * @param city Housing location's city.
   * @param state Housing location's state.
   * @param zipCode Housing location's zip code.
   * @param housingLocationName The name of the housing location.
   * @param trainingLocationDto A data transfer object that represents a training location.
   */

  public HouseLocationDto(String address1, String address2, String city, String state,
      String zipCode, String housingLocationName, TrainingLocationDto trainingLocationDto) {
    super();
    this.address1 = address1;
    this.address2 = address2;
    this.city = city;
    this.state = state;
    this.zipCode = zipCode;
    this.housingLocationName = housingLocationName;
    this.trainingLocationDto = trainingLocationDto;
  }

  public HouseLocationDto() {
    super();
    // TODO Auto-generated constructor stub
  }

}
