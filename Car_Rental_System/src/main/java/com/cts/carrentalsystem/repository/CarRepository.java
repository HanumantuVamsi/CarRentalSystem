package com.cts.carrentalsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cts.carrentalsystem.model.Car;

public interface CarRepository extends JpaRepository<Car, Long> {

}
