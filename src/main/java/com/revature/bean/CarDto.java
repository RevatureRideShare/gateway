package com.revature.bean;

public class CarDto {
  private int seatNumber;

  public int getSeatNumber() {
    return seatNumber;
  }

  public void setSeatNumber(int seatNumber) {
    this.seatNumber = seatNumber;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + seatNumber;
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
    CarDto other = (CarDto) obj;
    if (seatNumber != other.seatNumber) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "CarDto [seatNumber=" + seatNumber + "]";
  }

  public CarDto(int seatNumber) {
    super();
    this.seatNumber = seatNumber;
  }

  public CarDto() {
    super();
  }

}
